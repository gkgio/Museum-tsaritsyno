package com.gkgio.museum.utils

import android.view.View
import androidx.annotation.DrawableRes
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar

object DialogUtils {

    fun showDialog(
        fragmentTag: String,
        fragmentManager: FragmentManager,
        message: String,
        buttonRightText: String,
        @DrawableRes iconRes: Int = 0,
        title: String? = null,
        buttonLeftText: String? = null
    ) {
        InfoDialogFragment.getInstance(
            fragmentTag,
            fragmentManager,
            message,
            buttonRightText,
            iconRes,
            title,
            buttonLeftText
        ).show(fragmentManager, InfoDialogFragment.TAG)
    }

    fun showError(
        contentView: View?,
        message: String,
        actionText: String? = null,
        action: View.OnClickListener? = null
    ) {
        if (contentView != null) {
            val snackBar = Snackbar.make(
                contentView,
                message,
                Snackbar.LENGTH_LONG
            )
            if (action != null) {
                snackBar.setAction(actionText, action)
            }
            snackBar.show()
        }
    }
}