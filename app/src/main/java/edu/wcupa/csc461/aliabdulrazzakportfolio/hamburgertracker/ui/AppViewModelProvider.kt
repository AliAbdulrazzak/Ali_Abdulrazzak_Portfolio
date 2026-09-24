package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.wcupa.csc461.aliabdulrazzakportfolio.PortfolioApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TrackerViewModel(portfolioApp().hamburgerContainer.hamburgerRepository)
        }
        initializer {
            EntryViewModel(portfolioApp().hamburgerContainer.hamburgerRepository)
        }
        initializer {
            StatsViewModel(portfolioApp().hamburgerContainer.hamburgerRepository)
        }
    }
}

fun CreationExtras.portfolioApp(): PortfolioApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PortfolioApplication)
