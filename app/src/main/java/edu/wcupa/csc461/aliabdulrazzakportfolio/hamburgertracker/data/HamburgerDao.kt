package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HamburgerDao {

    @Query("SELECT * FROM hamburgers ORDER BY dateAdded DESC")
    fun getAll(): Flow<List<Hamburger>>

    @Query("SELECT * FROM hamburgers WHERE id = :id")
    fun get(id: Long): Flow<Hamburger?>

    @Query(
        "SELECT * FROM hamburgers WHERE " +
        "(name LIKE '%' || :query || '%' OR restaurantName LIKE '%' || :query || '%') " +
        "ORDER BY dateAdded DESC"
    )
    fun search(query: String): Flow<List<Hamburger>>

    @Query("SELECT * FROM hamburgers WHERE isFavorite = 1 ORDER BY dateAdded DESC")
    fun getFavorites(): Flow<List<Hamburger>>

    @Query("SELECT AVG(CAST(rating AS REAL)) FROM hamburgers WHERE rating > 0")
    fun getAverageRating(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM hamburgers")
    fun getCount(): Flow<Int>

    @Query(
        "SELECT restaurantName FROM hamburgers WHERE restaurantName != '' " +
        "GROUP BY restaurantName ORDER BY COUNT(*) DESC LIMIT 1"
    )
    fun getTopRestaurantName(): Flow<String?>

    @Query("SELECT COUNT(*) FROM hamburgers WHERE rating = :stars")
    fun getCountByRating(stars: Int): Flow<Int>

    @Query("SELECT burgerType FROM hamburgers GROUP BY burgerType ORDER BY COUNT(*) DESC LIMIT 1")
    fun getMostCommonType(): Flow<String?>

    @Query("SELECT MAX(price) FROM hamburgers WHERE price > 0")
    fun getMaxPrice(): Flow<Double?>

    @Query("SELECT MIN(price) FROM hamburgers WHERE price > 0")
    fun getMinPrice(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hamburger: Hamburger)

    @Update
    suspend fun update(hamburger: Hamburger)

    @Delete
    suspend fun delete(hamburger: Hamburger)
}
