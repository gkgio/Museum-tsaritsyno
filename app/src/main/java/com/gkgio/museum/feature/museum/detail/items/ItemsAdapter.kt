package com.gkgio.museum.feature.museum.detail.items

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gkgio.museum.R
import com.gkgio.museum.ext.setDebounceOnClickListener
import com.gkgio.museum.ext.withCenterCropRoundedCorners
import com.gkgio.museum.ext.withFade
import com.gkgio.museum.feature.model.Item
import com.gkgio.museum.view.SyntheticViewHolder
import kotlinx.android.synthetic.main.exhibition_item_recycler_item.view.*

class ItemsAdapter(
    val itemClick: (item: Item) -> Unit
) : RecyclerView.Adapter<SyntheticViewHolder>() {

    private val itemsList = mutableListOf<Item>()

    fun setItemsList(itemsList: MutableList<Item>) {
        this.itemsList.clear()
        this.itemsList.addAll(itemsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyntheticViewHolder {
        return SyntheticViewHolder.inflateFrom(parent, R.layout.exhibition_item_recycler_item)
    }

    override fun onBindViewHolder(holder: SyntheticViewHolder, position: Int) =
        with(holder.itemView) {

            val item = itemsList[position]
            Glide.with(context)
                .load(item.images[0])
                .withFade()
                .withCenterCropRoundedCorners(context)
                .into(image)

            description.text = item.description

            setDebounceOnClickListener { itemClick(item) }
        }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}