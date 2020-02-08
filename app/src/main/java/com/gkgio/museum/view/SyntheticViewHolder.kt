package com.gkgio.museum.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

open class SyntheticViewHolder(
        override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    companion object {
        fun inflateFrom(
                parent: ViewGroup, @LayoutRes layoutId: Int,
                attachToRoot: Boolean = false
        ): SyntheticViewHolder {
            return SyntheticViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            layoutId,
                            parent,
                            attachToRoot
                    )
            )
        }
    }
}