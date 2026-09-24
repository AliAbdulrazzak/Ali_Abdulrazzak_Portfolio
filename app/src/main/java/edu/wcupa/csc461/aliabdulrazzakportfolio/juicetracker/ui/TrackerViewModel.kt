package edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.data.Juice
import edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.data.JuiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TrackerViewModel(private val juiceRepository: JuiceRepository) : ViewModel() {
    val juicesStream: Flow<List<Juice>> = juiceRepository.juicesStream

    fun deleteJuice(juice: Juice) = viewModelScope.launch {
        juiceRepository.deleteJuice(juice)
    }
}
