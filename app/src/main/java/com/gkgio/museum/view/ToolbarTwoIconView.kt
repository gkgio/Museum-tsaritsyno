package com.gkgio.museum.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.gkgio.museum.R
import com.gkgio.museum.ext.getDrawableCompat
import com.gkgio.museum.ext.setDebounceOnClickListener
import kotlinx.android.synthetic.main.toolbar_two_icon_view.view.*
import timber.log.Timber

class ToolbarTwoIconView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var leftIconClickListener: (() -> Unit)? = null
    private var firstRightIconClickListener: (() -> Unit)? = null
    private var secondRightIconClickListener: (() -> Unit)? = null

    init {
        init(attrs)
    }

    fun setLeftIcon(icon: Drawable?) {
        if (icon != null) {
            leftIcon.setImageDrawable(icon)
            leftIconContainer.isVisible = true
        } else {
            leftIconContainer.isVisible = false
        }
    }

    fun setFirstRightIcon(icon: Drawable?) {
        if (icon != null) {
            firstRightIcon.setImageDrawable(icon)
            firstRightIconContainer.isVisible = true
        } else {
            firstRightIconContainer.isVisible = false
        }
    }

    fun setSecondRightIcon(icon: Drawable?) {
        if (icon != null) {
            secondRightIcon.setImageDrawable(icon)
            secondRightIcon.isVisible = true
        } else {
            secondRightIcon.isVisible = false
        }
    }

    fun setTitle(title: String?) {
        if (title != null) {
            titleTextView.text = title
            titleTextView.isVisible = true
        } else {
            titleTextView.isVisible = false
        }
    }

    fun setLeftIconClickListener(clickListener: (() -> Unit)?) {
        leftIconClickListener = clickListener
    }

    fun setFirstRightIconClickListener(clickListener: (() -> Unit)?) {
        firstRightIconClickListener = clickListener
    }

    fun setSecondRightIconClickListener(clickListener: (() -> Unit)?) {
        secondRightIconClickListener = clickListener
    }

    fun init(attributes: AttributeSet?) {
        inflate(context, R.layout.toolbar_two_icon_view, this)
        attributes?.let { attrs ->
            val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.ToolbarCustomView,
                0,
                0
            )
            try {
                val titleText = typedArray.getString(R.styleable.ToolbarCustomView_titleText)
                if (!titleText.isNullOrEmpty()) {
                    titleTextView.text = titleText
                    titleTextView.isInvisible = false
                } else {
                    titleTextView.isInvisible = true
                }

                firstRightIconContainer.setDebounceOnClickListener { firstRightIconClickListener?.invoke() }
                val firstRightIconRes = typedArray.getResourceId(
                    R.styleable.ToolbarCustomView_firstRightIcon,
                    -1
                )
                if (firstRightIconRes > 0) {
                    setFirstRightIcon(context.getDrawableCompat(firstRightIconRes))
                    firstRightIconContainer.isVisible = true
                } else {
                    firstRightIconContainer.isVisible = false
                }

                secondRightIconContainer.setDebounceOnClickListener { secondRightIconClickListener?.invoke() }
                val secondRightIconRes = typedArray.getResourceId(
                    R.styleable.ToolbarCustomView_secondRightIcon,
                    -1
                )
                if (secondRightIconRes > 0) {
                    setSecondRightIcon(context.getDrawableCompat(secondRightIconRes))
                    secondRightIconContainer.isVisible = true
                } else {
                    secondRightIconContainer.isVisible = false
                }

                leftIconContainer.setDebounceOnClickListener { leftIconClickListener?.invoke() }
                val iconLeftRes: Int = typedArray.getResourceId(
                    R.styleable.ToolbarCustomView_iconLeft,
                    -1
                )
                if (iconLeftRes > 0) {
                    setLeftIcon(context.getDrawableCompat(iconLeftRes))
                    leftIconContainer.isVisible = true
                } else {
                    leftIconContainer.isVisible = false
                }
            } catch (error: Throwable) {
                error.printStackTrace()
                Timber.e(error, "Can't draw toolbar")
            } finally {
                typedArray.recycle()
            }
        }
    }
}
