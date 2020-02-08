package com.gkgio.museum.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.roundToInt

open class ScrollOverrideRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {
    private var scrollPointerId = -1
    private var initialTouchX = 0
    private var initialTouchY = 0
    private var touchSlop = 0
    private val offset = 0.5f

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun setScrollingTouchSlop(slopConstant: Int) {
        super.setScrollingTouchSlop(slopConstant)

        val vc = ViewConfiguration.get(context)
        when (slopConstant) {
            TOUCH_SLOP_DEFAULT -> touchSlop = vc.scaledTouchSlop
            TOUCH_SLOP_PAGING -> touchSlop = vc.scaledPagingTouchSlop
        }
    }

    @SuppressLint("Recycle")
    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        if (e == null) {
            return false
        }

        val action = MotionEvent.obtain(e).action
        val actionIndex = MotionEvent.obtain(e).actionIndex

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                scrollPointerId = MotionEvent.obtain(e).getPointerId(0)
                initialTouchX = (e.x + offset).roundToInt()
                initialTouchY = (e.y + offset).roundToInt()
                return super.onInterceptTouchEvent(e)
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                scrollPointerId = MotionEvent.obtain(e).getPointerId(actionIndex)
                initialTouchX = (MotionEvent.obtain(e).getX(actionIndex) + offset).roundToInt()
                initialTouchY = (MotionEvent.obtain(e).getY(actionIndex) + offset).roundToInt()
                return super.onInterceptTouchEvent(e)
            }

            MotionEvent.ACTION_MOVE -> {
                val index = MotionEvent.obtain(e).findPointerIndex(scrollPointerId)
                if (index < 0) {
                    return false
                }

                val x = (MotionEvent.obtain(e).getX(index) + offset).roundToInt()
                val y = (MotionEvent.obtain(e).getY(index) + offset).roundToInt()
                if (scrollState != SCROLL_STATE_DRAGGING) {
                    val dx = x - initialTouchX
                    val dy = y - initialTouchY
                    var startScroll = false
                    if (layoutManager!!.canScrollHorizontally() && abs(dx) > touchSlop &&
                        (layoutManager!!.canScrollVertically() || abs(dx) > abs(dy))
                    ) {
                        startScroll = true
                    }
                    if (layoutManager!!.canScrollVertically() && abs(dy) > touchSlop &&
                        (layoutManager!!.canScrollHorizontally() || abs(dy) > abs(dx))
                    ) {
                        startScroll = true
                    }
                    return startScroll && super.onInterceptTouchEvent(e)
                }

                return super.onInterceptTouchEvent(e)
            }

            else -> {
                return super.onInterceptTouchEvent(e)
            }
        }
    }
}