package edu.wcupa.csc461.aliabdulrazzakportfolio.diceroller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import edu.wcupa.csc461.aliabdulrazzakportfolio.diceroller.ui.theme.DiceRollerTheme

class DiceRollerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DiceRollerTheme {
                    DiceRollerApp()
                }
            }
        }
    }
}
