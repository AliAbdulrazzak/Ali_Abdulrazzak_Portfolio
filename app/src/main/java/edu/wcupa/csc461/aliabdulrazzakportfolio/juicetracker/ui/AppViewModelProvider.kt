package edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.wcupa.csc461.aliabdulrazzakportfolio.PortfolioApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            EntryViewModel(portfolioApplication().container.trackerRepository)
        }
        initializer {
            TrackerViewModel(portfolioApplication().container.trackerRepository)
        }
    }
}

fun CreationExtras.portfolioApplication(): PortfolioApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PortfolioApplication)
