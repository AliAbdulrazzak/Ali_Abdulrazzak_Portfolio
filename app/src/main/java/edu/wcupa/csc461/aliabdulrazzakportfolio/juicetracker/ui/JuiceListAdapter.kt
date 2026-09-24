package edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.wcupa.csc461.aliabdulrazzakportfolio.databinding.JuiceListItemBinding
import edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.data.Juice
import edu.wcupa.csc461.aliabdulrazzakportfolio.juicetracker.data.JuiceColor

class JuiceListAdapter(
    private var onEdit: (Juice) -> Unit,
    private var onDelete: (Juice) -> Unit
) : ListAdapter<Juice, JuiceListAdapter.JuiceListViewHolder>(JuiceDiffCallback()) {

    class JuiceListViewHolder(
        private val binding: JuiceListItemBinding,
        private val onEdit: (Juice) -> Unit,
        private val onDelete: (Juice) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(juice: Juice) {
            binding.name.text = juice.name
            binding.description.text = juice.description
            binding.drinkColorOverlay.setColorFilter(
                JuiceColor.valueOf(juice.color).color,
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            binding.ratingBar.rating = juice.rating.toFloat()
            binding.deleteButton.setOnClickListener { onDelete(juice) }
            binding.root.setOnClickListener { onEdit(juice) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = JuiceListViewHolder(
        JuiceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onEdit,
        onDelete
    )

    override fun onBindViewHolder(holder: JuiceListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class JuiceDiffCallback : DiffUtil.ItemCallback<Juice>() {
    override fun areItemsTheSame(oldItem: Juice, newItem: Juice) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Juice, newItem: Juice) = oldItem == newItem
}
