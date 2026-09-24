package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.AppViewModelProvider
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.EntryViewModel
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.StatsViewModel
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.TrackerViewModel
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.screens.StatsScreen
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.screens.TrackerScreen

sealed class Screen(val route: String) {
    data object Tracker : Screen("tracker")
    data object Stats : Screen("stats")
}

@Composable
fun HamburgerNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val trackerViewModel: TrackerViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val entryViewModel: EntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val statsViewModel: StatsViewModel = viewModel(factory = AppViewModelProvider.Factory)

    NavHost(
        navController = navController,
        startDestination = Screen.Tracker.route
    ) {
        composable(Screen.Tracker.route) {
            TrackerScreen(
                trackerViewModel = trackerViewModel,
                entryViewModel = entryViewModel,
                onNavigateToStats = { navController.navigate(Screen.Stats.route) }
            )
        }
        composable(Screen.Stats.route) {
            StatsScreen(
                viewModel = statsViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
