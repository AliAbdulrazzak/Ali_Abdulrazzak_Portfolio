package edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JuiceDao {
    @Query("SELECT * FROM juice")
    fun getAll(): Flow<List<Juice>>

    @Query("SELECT * FROM juice WHERE id = :id")
    fun get(id: Long): Flow<Juice>

    @Insert
    suspend fun insert(juice: Juice)

    @Delete
    suspend fun delete(juice: Juice)

    @Update
    suspend fun update(juice: Juice)
}
