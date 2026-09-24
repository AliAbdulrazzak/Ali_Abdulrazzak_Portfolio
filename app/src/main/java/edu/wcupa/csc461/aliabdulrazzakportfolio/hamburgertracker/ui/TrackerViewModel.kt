package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.BurgerType
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.Hamburger
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.HamburgerRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SortOrder(val label: String) {
    DATE_DESC("Newest First"),
    DATE_ASC("Oldest First"),
    RATING_HIGH("Highest Rated"),
    RATING_LOW("Lowest Rated"),
    NAME_ASC("Name A-Z"),
    NAME_DESC("Name Z-A"),
    PRICE_LOW("Cheapest First"),
    PRICE_HIGH("Most Expensive")
}

data class TrackerUiState(
    val searchQuery: String = "",
    val selectedType: BurgerType? = null,
    val minRating: Int = 0,
    val sortOrder: SortOrder = SortOrder.DATE_DESC,
    val showFavoritesOnly: Boolean = false
)

class TrackerViewModel(private val repository: HamburgerRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackerUiState())
    val uiState: StateFlow<TrackerUiState> = _uiState

    @OptIn(ExperimentalCoroutinesApi::class)
    val hamburgers: StateFlow<List<Hamburger>> = combine(
        _uiState,
        repository.hamburgersStream
    ) { state, all ->
        var list = all
        if (state.searchQuery.isNotBlank()) {
            val q = state.searchQuery.lowercase()
            list = list.filter {
                it.name.lowercase().contains(q) || it.restaurantName.lowercase().contains(q)
            }
        }
        if (state.selectedType != null) {
            list = list.filter { it.burgerType == state.selectedType.name }
        }
        if (state.minRating > 0) {
            list = list.filter { it.rating >= state.minRating }
        }
        if (state.showFavoritesOnly) {
            list = list.filter { it.isFavorite }
        }
        when (state.sortOrder) {
            SortOrder.DATE_DESC -> list.sortedByDescending { it.dateAdded }
            SortOrder.DATE_ASC -> list.sortedBy { it.dateAdded }
            SortOrder.RATING_HIGH -> list.sortedByDescending { it.rating }
            SortOrder.RATING_LOW -> list.sortedBy { it.rating }
            SortOrder.NAME_ASC -> list.sortedBy { it.name.lowercase() }
            SortOrder.NAME_DESC -> list.sortedByDescending { it.name.lowercase() }
            SortOrder.PRICE_LOW -> list.sortedBy { it.price }
            SortOrder.PRICE_HIGH -> list.sortedByDescending { it.price }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun selectType(type: BurgerType?) {
        _uiState.value = _uiState.value.copy(selectedType = type)
    }

    fun setMinRating(rating: Int) {
        _uiState.value = _uiState.value.copy(minRating = rating)
    }

    fun setSortOrder(order: SortOrder) {
        _uiState.value = _uiState.value.copy(sortOrder = order)
    }

    fun toggleFavoritesOnly() {
        _uiState.value = _uiState.value.copy(showFavoritesOnly = !_uiState.value.showFavoritesOnly)
    }

    fun toggleFavorite(hamburger: Hamburger) {
        viewModelScope.launch {
            repository.updateHamburger(hamburger.copy(isFavorite = !hamburger.isFavorite))
        }
    }

    fun deleteHamburger(hamburger: Hamburger) {
        viewModelScope.launch {
            repository.deleteHamburger(hamburger)
        }
    }
}
