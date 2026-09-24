package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data

import kotlinx.coroutines.flow.Flow

class RoomHamburgerRepository(private val dao: HamburgerDao) : HamburgerRepository {

    override val hamburgersStream: Flow<List<Hamburger>> = dao.getAll()

    override fun getHamburgerStream(id: Long): Flow<Hamburger?> = dao.get(id)

    override fun searchHamburgers(query: String): Flow<List<Hamburger>> = dao.search(query)

    override fun getFavorites(): Flow<List<Hamburger>> = dao.getFavorites()

    override fun getAverageRating(): Flow<Double?> = dao.getAverageRating()

    override fun getCount(): Flow<Int> = dao.getCount()

    override fun getTopRestaurantName(): Flow<String?> = dao.getTopRestaurantName()

    override fun getCountByRating(stars: Int): Flow<Int> = dao.getCountByRating(stars)

    override fun getMostCommonType(): Flow<String?> = dao.getMostCommonType()

    override fun getMaxPrice(): Flow<Double?> = dao.getMaxPrice()

    override fun getMinPrice(): Flow<Double?> = dao.getMinPrice()

    override suspend fun addHamburger(hamburger: Hamburger) = dao.insert(hamburger)

    override suspend fun updateHamburger(hamburger: Hamburger) = dao.update(hamburger)

    override suspend fun deleteHamburger(hamburger: Hamburger) = dao.delete(hamburger)
}
