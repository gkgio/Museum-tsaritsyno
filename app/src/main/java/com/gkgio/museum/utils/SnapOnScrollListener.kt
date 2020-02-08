package com.gkgio.museum.utils

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.gkgio.museum.ext.getSnapPosition

class SnapOnScrollListener(
    private val snapHelper: SnapHelper,
    var behavior: Behavior = Behavior.NOTIFY_ON_SCROLL,
    var onSnapPositionChangeListener: OnSnapPositionChangeListener? = null
) : RecyclerView.OnScrollListener() {

    companion object {
        private const val FIRST_VISIBLE_ITEM = 0
    }

    enum class Behavior {
        NOTIFY_ON_SCROLL,
        NOTIFY_ON_SCROLL_STATE_IDLE
    }

    private var snapPosition = RecyclerView.NO_POSITION

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (behavior == Behavior.NOTIFY_ON_SCROLL) {
            maybeNotifySnapPositionChange(recyclerView)
        }
        if (dx > 0 || dx < 0) {
            onSnapPositionChangeListener?.onScroll()
        } else {
            scrollPositionStop(recyclerView)
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            scrollPositionStop(recyclerView)

            if (behavior == Behavior.NOTIFY_ON_SCROLL_STATE_IDLE) {
                maybeNotifySnapPositionChange(recyclerView)
            }
        }
    }

    private fun scrollPositionStop(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val currentPosition = layoutManager.findFirstVisibleItemPosition()
        val view = layoutManager.getChildAt(FIRST_VISIBLE_ITEM)
        onSnapPositionChangeListener?.onScrollStop(currentPosition, view)
    }

    private fun maybeNotifySnapPositionChange(recyclerView: RecyclerView) {
        val snapPosition = snapHelper.getSnapPosition(recyclerView)
        val snapPositionChanged = this.snapPosition != snapPosition
        if (snapPositionChanged) {
            onSnapPositionChangeListener?.onSnapPositionChange(snapPosition)
            this.snapPosition = snapPosition
        }
    }

    interface OnSnapPositionChangeListener {
        fun onScroll() {
            // Empty by default
        }

        fun onScrollStop(position: Int, childView: View?) {
            // Empty by default
        }

        fun onSnapPositionChange(position: Int) {
            // Empty by default
        }
    }
}
