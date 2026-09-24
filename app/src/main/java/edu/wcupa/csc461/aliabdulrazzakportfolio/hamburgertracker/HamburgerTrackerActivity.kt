package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.navigation.HamburgerNavGraph
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.HamburgertrackerTheme

class HamburgerTrackerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
