package com.gkgio.museum.feature.museum

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gkgio.museum.R
import com.gkgio.museum.ext.setDebounceOnClickListener
import com.gkgio.museum.ext.withCenterCropRoundedCorners
import com.gkgio.museum.ext.withFade
import com.gkgio.museum.feature.model.Museum
import com.gkgio.museum.view.SyntheticViewHolder
import kotlinx.android.synthetic.main.museum_recycler_item.view.*

class MuseumsAdapter(
    val itemClick: (museum: Museum) -> Unit
) : RecyclerView.Adapter<SyntheticViewHolder>() {

    private val museumsList = mutableListOf<Museum>()

    fun setMuseumTypesList(museumsList: MutableList<Museum>) {
        this.museumsList.clear()
        this.museumsList.addAll(museumsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyntheticViewHolder {
        return SyntheticViewHolder.inflateFrom(parent, R.layout.museum_recycler_item)
    }

    override fun onBindViewHolder(holder: SyntheticViewHolder, position: Int) =
        with(holder.itemView) {

            val museum = museumsList[position]
            Glide.with(context)
                .load(museum.imageUrl)
                .withFade()
                .withCenterCropRoundedCorners(context)
                .into(museumImg)

            museumTitle.text = museum.title

            setDebounceOnClickListener { itemClick(museum) }
        }

    override fun getItemCount(): Int {
        return museumsList.size
    }
}
