package edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import edu.wcupa.csc461.aliabdulrazzakportfolio.databinding.FragmentJuiceEntryBottomSheetBinding
import edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.data.Juice
import edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.data.JuiceColor
import edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.ui.AppViewModelProvider
import edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.ui.EntryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class EntryBottomSheet : BottomSheetDialogFragment() {

    private val entryViewModel by viewModels<EntryViewModel> { AppViewModelProvider.Factory }
    private var _binding: FragmentJuiceEntryBottomSheetBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "EntryBottomSheet"
        private const val JUICE_ID = "juiceId"

        fun newInstance(juiceId: Long = 0L) = EntryBottomSheet().apply {
            arguments = Bundle().apply {
                putLong(JUICE_ID, juiceId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJuiceEntryBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val juiceId = arguments?.getLong(JUICE_ID) ?: 0L

        val colorAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            JuiceColor.values().map { getString(it.label) }
        )
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.colorSpinner.adapter = colorAdapter

        if (juiceId > 0L) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    entryViewModel.getJuiceStream(juiceId).collect { juice ->
                        juice?.let { populateFields(it) }
                    }
                }
            }
        }

        binding.saveButton.setOnClickListener {
            entryViewModel.saveJuice(
                id = juiceId,
                name = binding.nameEditText.text.toString(),
                description = binding.descriptionEditText.text.toString(),
                color = JuiceColor.values()[binding.colorSpinner.selectedItemPosition].name,
                rating = binding.ratingBar.rating.toInt()
            )
            dismiss()
        }
    }

    private fun populateFields(juice: Juice) {
        binding.nameEditText.setText(juice.name)
        binding.descriptionEditText.setText(juice.description)
        val colorIndex = JuiceColor.values().indexOfFirst { it.name == juice.color }
        if (colorIndex >= 0) binding.colorSpinner.setSelection(colorIndex)
        binding.ratingBar.rating = juice.rating.toFloat()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
