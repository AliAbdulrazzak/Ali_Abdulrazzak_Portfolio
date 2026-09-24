package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.BurgerType
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.Hamburger
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.HamburgerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EntryViewModel(private val repository: HamburgerRepository) : ViewModel() {

    fun getHamburgerStream(id: Long): Flow<Hamburger?> = repository.getHamburgerStream(id)

    fun saveHamburger(
        id: Long,
        name: String,
        description: String,
        restaurantName: String,
        burgerType: BurgerType,
        rating: Int,
        price: Double,
        isFavorite: Boolean
    ) {
        viewModelScope.launch {
            val hamburger = Hamburger(
                id = id,
                name = name,
                description = description,
                restaurantName = restaurantName,
                burgerType = burgerType.name,
                rating = rating,
                price = price,
                isFavorite = isFavorite
            )
            if (id == 0L) {
                repository.addHamburger(hamburger)
            } else {
                repository.updateHamburger(hamburger)
            }
        }
    }
}
