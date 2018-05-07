package com.test.epm.locationmap.ui.locationlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.epm.locationmap.R
import com.test.epm.locationmap.ui.locationlist.entity.LocationDistance
import kotlinx.android.synthetic.main.item_location.view.*

class LocationRecyclerViewAdapter(
        private val clickListener: (LocationDistance) -> Unit)
    : RecyclerView.Adapter<LocationRecyclerViewAdapter.ViewHolder>() {

    private val items: MutableList<LocationDistance> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_location, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, clickListener)
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(items: List<LocationDistance>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.name
        private val distance = itemView.distance

        fun bind(item: LocationDistance, clickListener: (LocationDistance) -> Unit) {
            name.text = item.location.name
            distance.text = distance.context.getString(R.string.distance_placeholder, item.distance)
            itemView.setOnClickListener { clickListener(item) }
        }
    }
}
