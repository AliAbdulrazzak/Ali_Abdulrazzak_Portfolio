package edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import edu.wcupa.csc461.aliabdulrazzakportfolio.databinding.FragmentJuiceTrackerBinding
import edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.ui.AppViewModelProvider
import edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.ui.JuiceListAdapter
import edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.ui.TrackerViewModel
import kotlinx.coroutines.launch

class TrackerFragment : Fragment() {

    private val viewModel by viewModels<TrackerViewModel> { AppViewModelProvider.Factory }

    private val adapter = JuiceListAdapter(
        onEdit = { drink ->
            EntryBottomSheet.newInstance(drink.id)
                .show(parentFragmentManager, EntryBottomSheet.TAG)
        },
        onDelete = { drink ->
            viewModel.deleteJuice(drink)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentJuiceTrackerBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentJuiceTrackerBinding.bind(view)
        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener {
            EntryBottomSheet.newInstance()
                .show(parentFragmentManager, EntryBottomSheet.TAG)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.juicesStream.collect {
                    adapter.submitList(it)
                }
            }
        }
    }
}
