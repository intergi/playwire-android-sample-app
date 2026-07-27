package com.example.demo_kotlin.ad_types

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_kotlin.R

class AdTypesAdapter(
    private val onItemClicked: (Pair<String, Class<out Activity>>) -> Unit
) : ListAdapter<Pair<String, Class<out Activity>>, AdTypesAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ad_unit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adUnit = getItem(position)
        holder.bind(adUnit)
        holder.itemView.setOnClickListener {
            onItemClicked(adUnit)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val adUnitAliasTextView: TextView = itemView.findViewById(R.id.ad_unit_name)

        fun bind(adUnit: Pair<String, Class<out Activity>>) {
            adUnitAliasTextView.text = adUnit.first
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Pair<String, Class<out Activity>>>() {
        override fun areItemsTheSame(
            oldItem: Pair<String, Class<out Activity>>,
            newItem: Pair<String, Class<out Activity>>
        ): Boolean = oldItem.second == newItem.second

        override fun areContentsTheSame(
            oldItem: Pair<String, Class<out Activity>>,
            newItem: Pair<String, Class<out Activity>>
        ): Boolean = oldItem == newItem
    }
}
