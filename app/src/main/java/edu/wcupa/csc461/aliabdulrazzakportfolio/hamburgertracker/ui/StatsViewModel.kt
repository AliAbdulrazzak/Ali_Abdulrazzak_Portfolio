package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.HamburgerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class StatsUiState(
    val totalCount: Int = 0,
    val averageRating: Double = 0.0,
    val topRestaurant: String = "",
    val mostCommonType: String = "",
    val maxPrice: Double = 0.0,
    val minPrice: Double = 0.0,
    val ratingDistribution: Map<Int, Int> = emptyMap()
)

class StatsViewModel(private val repository: HamburgerRepository) : ViewModel() {

    val statsState: StateFlow<StatsUiState> = combine(
        repository.getCount(),
        repository.getAverageRating(),
        repository.getTopRestaurantName(),
        repository.getMostCommonType(),
        repository.getMaxPrice()
    ) { count, avg, topRest, topType, maxPrice ->
        StatsUiState(
            totalCount = count,
            averageRating = avg ?: 0.0,
            topRestaurant = topRest ?: "—",
            mostCommonType = topType ?: "—",
            maxPrice = maxPrice ?: 0.0
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StatsUiState()
    )

    val ratingCounts: StateFlow<List<Int>> = combine(
        repository.getCountByRating(1),
        repository.getCountByRating(2),
        repository.getCountByRating(3),
        repository.getCountByRating(4),
        repository.getCountByRating(5)
    ) { r1, r2, r3, r4, r5 ->
        listOf(r1, r2, r3, r4, r5)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = listOf(0, 0, 0, 0, 0)
    )
}
