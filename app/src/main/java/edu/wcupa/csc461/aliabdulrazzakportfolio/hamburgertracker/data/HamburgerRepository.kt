package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data

import kotlinx.coroutines.flow.Flow

interface HamburgerRepository {
    val hamburgersStream: Flow<List<Hamburger>>
    fun getHamburgerStream(id: Long): Flow<Hamburger?>
    fun searchHamburgers(query: String): Flow<List<Hamburger>>
    fun getFavorites(): Flow<List<Hamburger>>
    fun getAverageRating(): Flow<Double?>
    fun getCount(): Flow<Int>
    fun getTopRestaurantName(): Flow<String?>
    fun getCountByRating(stars: Int): Flow<Int>
    fun getMostCommonType(): Flow<String?>
    fun getMaxPrice(): Flow<Double?>
    fun getMinPrice(): Flow<Double?>
    suspend fun addHamburger(hamburger: Hamburger)
    suspend fun updateHamburger(hamburger: Hamburger)
    suspend fun deleteHamburger(hamburger: Hamburger)
}
