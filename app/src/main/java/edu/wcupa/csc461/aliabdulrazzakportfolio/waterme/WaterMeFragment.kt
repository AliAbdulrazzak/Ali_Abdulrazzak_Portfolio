package edu.wcupa.csc461.aliabdulrazzakportfolio.waterme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.ui.WaterMeApp
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.ui.theme.WaterMeTheme

class WaterMeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WaterMeTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        WaterMeApp()
                    }
                }
            }
        }
    }
}
