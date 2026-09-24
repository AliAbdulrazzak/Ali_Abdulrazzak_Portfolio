package edu.wcupa.csc461.aliabdulrazzakportfolio.unscramble

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
import edu.wcupa.csc461.aliabdulrazzakportfolio.unscramble.ui.GameScreen
import edu.wcupa.csc461.aliabdulrazzakportfolio.unscramble.ui.theme.UnscrambleTheme

class UnscrambleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                UnscrambleTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        GameScreen()
                    }
                }
            }
        }
    }
}
