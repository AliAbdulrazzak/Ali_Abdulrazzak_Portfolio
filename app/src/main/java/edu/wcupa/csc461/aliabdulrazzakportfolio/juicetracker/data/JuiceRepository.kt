package edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.data

import kotlinx.coroutines.flow.Flow

interface JuiceRepository {
    val juicesStream: Flow<List<Juice>>
    fun getJuiceStream(id: Long): Flow<Juice?>
    suspend fun addJuice(juice: Juice)
    suspend fun deleteJuice(juice: Juice)
    suspend fun updateJuice(juice: Juice)
}
