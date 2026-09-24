package edu.wcupa.csc461.aliabdulrazzakportfolio.unscramble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import edu.wcupa.csc461.aliabdulrazzakportfolio.unscramble.ui.GameScreen
import edu.wcupa.csc461.aliabdulrazzakportfolio.unscramble.ui.theme.UnscrambleTheme

class UnscrambleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            UnscrambleTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GameScreen()
                }
            }
        }
    }
}
