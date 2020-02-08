package com.gkgio.museum.feature.museum

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gkgio.museum.R
import com.gkgio.museum.ext.setDebounceOnClickListener
import com.gkgio.museum.ext.withFade
import com.gkgio.museum.feature.model.Exhibition
import com.gkgio.museum.view.SyntheticViewHolder
import kotlinx.android.synthetic.main.exhibition_recycler_item.view.*


class ExhibitionsAdapter(
    val itemClick: (museum: Exhibition) -> Unit
) : RecyclerView.Adapter<SyntheticViewHolder>() {

    private val exhibitionsList = mutableListOf<Exhibition>()

    fun setExhibitionsList(exhibitionsList: MutableList<Exhibition>) {
        this.exhibitionsList.clear()
        this.exhibitionsList.addAll(exhibitionsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyntheticViewHolder {
        return SyntheticViewHolder.inflateFrom(parent, R.layout.exhibition_recycler_item)
    }

    override fun onBindViewHolder(holder: SyntheticViewHolder, position: Int) =
        with(holder.itemView) {
            val exhibition = exhibitionsList[position]

            Glide.with(context)
                .load(exhibition.imageUrl)
                .withFade()
                .into(exhibitionImg)

            exhibitionDescription.text = exhibition.description
            exhibitionDate.text = exhibition.time

            setDebounceOnClickListener { itemClick(exhibition) }
        }

    override fun getItemCount(): Int {
        return exhibitionsList.size
    }
}
