package com.example.demo_kotlin.ad_types

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_kotlin.R
import com.intergi.playwiresdk.PWAdMode

class AdTypesAdapter(
    private val onItemClicked: (Pair<PWAdMode, String>) -> Unit
) : ListAdapter<Pair<PWAdMode, String>, AdTypesAdapter.ViewHolder>(DiffCallback) {

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
        private val adUnitModeTextView: TextView = itemView.findViewById(R.id.ad_unit_mode)

        fun bind(adUnit: Pair<PWAdMode, String>) {
            adUnitAliasTextView.text = adUnit.second
            adUnitModeTextView.text = adUnit.first.name
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Pair<PWAdMode, String>>() {
        override fun areItemsTheSame(
            oldItem: Pair<PWAdMode, String>,
            newItem: Pair<PWAdMode, String>
        ): Boolean = oldItem.second == newItem.second

        override fun areContentsTheSame(
            oldItem: Pair<PWAdMode, String>,
            newItem: Pair<PWAdMode, String>
        ): Boolean = oldItem == newItem
    }
}
