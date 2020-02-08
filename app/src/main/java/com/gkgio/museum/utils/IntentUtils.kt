package com.gkgio.museum.utils

import android.content.Intent
import android.net.Uri

object IntentUtils {

    fun createPhoneIntent(phone: String) = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("tel:$phone")
    )

    fun createEmailIntent(email: String) = Intent(
        Intent.ACTION_SENDTO
    ).apply {
        data = Uri.parse("mailto:$email")
        putExtra(Intent.EXTRA_EMAIL, email)
    }
}
