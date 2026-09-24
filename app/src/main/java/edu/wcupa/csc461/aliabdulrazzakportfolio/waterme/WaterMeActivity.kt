package edu.wcupa.csc461.aliabdulrazzakportfolio.waterme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.ui.WaterMeApp
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.ui.theme.WaterMeTheme

class WaterMeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            WaterMeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WaterMeApp()
                }
            }
        }
    }
}
