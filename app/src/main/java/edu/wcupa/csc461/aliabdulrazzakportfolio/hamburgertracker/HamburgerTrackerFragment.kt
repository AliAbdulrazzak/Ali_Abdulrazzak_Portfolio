package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.navigation.HamburgerNavGraph
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.HamburgertrackerTheme

class HamburgerTrackerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                HamburgertrackerTheme(dynamicColor = false) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        HamburgerNavGraph()
                    }
                }
            }
        }
    }
}
