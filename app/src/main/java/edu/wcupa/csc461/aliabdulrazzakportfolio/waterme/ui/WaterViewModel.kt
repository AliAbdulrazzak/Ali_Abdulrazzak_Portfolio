package edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.wcupa.csc461.aliabdulrazzakportfolio.PortfolioApplication
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.data.Reminder
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.data.WaterRepository
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.model.Plant

class WaterViewModel(private val waterRepository: WaterRepository) : ViewModel() {

    private val customPlants = mutableStateListOf<Plant>()

    val plants: List<Plant> by derivedStateOf {
        waterRepository.plants + customPlants
    }

    private val lastWateredMap = mutableStateMapOf<String, Long>()
    fun lastWatered(plantId: String): Long? = lastWateredMap[plantId]

    var totalWaterings by mutableStateOf(0)
        private set

    var darkTheme by mutableStateOf(false)

    var selectedFilter by mutableStateOf<Int?>(null)

    val visiblePlants: List<Plant> by derivedStateOf {
        val filter = selectedFilter
        if (filter == null) plants else plants.filter { it.type == filter }
    }

    fun availableTypes(): List<Int> = plants.map { it.type }.distinct()

    fun scheduleReminder(reminder: Reminder) {
        waterRepository.scheduleReminder(reminder.duration, reminder.unit, reminder.plantName)
    }

    fun cancelReminder(plantName: String) {
        waterRepository.cancelReminder(plantName)
    }

    fun markWatered(plant: Plant) {
        lastWateredMap[plant.id] = System.currentTimeMillis()
        totalWaterings += 1
    }

    fun addCustomPlant(plant: Plant) {
        customPlants.add(plant)
    }

    fun toggleTheme() {
        darkTheme = !darkTheme
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PortfolioApplication
                WaterViewModel(waterRepository = app.waterMeContainer.waterRepository)
            }
        }
    }
}
