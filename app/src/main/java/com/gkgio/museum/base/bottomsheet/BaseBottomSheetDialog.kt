package com.gkgio.museum.base.bottomsheet

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.view.doOnPreDraw
import com.gkgio.museum.R
import com.gkgio.museum.ext.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseBottomSheetDialog : BottomSheetDialogFragment() {

    private companion object {
        private const val BOUNCE_OFFSET_DP = 20
        private const val APPEAR_ANIM_DURATION = 350L
        private const val BOUNCE_ANIM_DURATION = 300L
    }

    protected var disposables = CompositeDisposable()

    protected val bottomSheetContainer: FrameLayout
        get() {
            return dialog?.findViewById(
                com.google.android.material.R.id.design_bottom_sheet
            ) as FrameLayout
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        setupView(view)

        view.doOnPreDraw {
            val bottomSheet = bottomSheetContainer
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = view.measuredHeight
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheet.setBackgroundResource(provideBackground())
            animateBottomSheet(bottomSheet,view.context)
        }

        return view
    }

    protected fun Disposable.addDisposable() {
        disposables.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Метод для совершения операций перед вычислением размеров контейнера BottomSheet.
     * Использовать, когда нужно отрисовать динамические элементы до вычисления размера или когда
     * требуется вычислить рамер View для плавной анимации
     */
    protected open fun setupView(view: View) {
        // Empty
    }

    @DrawableRes
    protected open fun provideBackground() = R.drawable.bg_rounded_bottom_sheet

    // Пока убрал, так как есть микрофриз
    private fun animateBottomSheet(bottomSheet: FrameLayout, context: Context) {
        val bounce = context.dpToPx(BOUNCE_OFFSET_DP).toFloat()

        bottomSheet.y = context.resources.displayMetrics.heightPixels.toFloat() - bounce

        val appearAnim = ObjectAnimator.ofFloat(
            bottomSheet,
            View.TRANSLATION_Y,
            0f
        ).apply {
            duration =
                APPEAR_ANIM_DURATION
        }

        val bounceAnim = ObjectAnimator.ofFloat(
            bottomSheet,
            View.TRANSLATION_Y,
            bounce
        ).apply {
            duration =
                BOUNCE_ANIM_DURATION
            interpolator = DecelerateInterpolator()
        }

        AnimatorSet().apply {
            playSequentially(appearAnim, bounceAnim)
            start()
        }
    }
}