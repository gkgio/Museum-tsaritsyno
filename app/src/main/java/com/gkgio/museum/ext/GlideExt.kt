package com.gkgio.museum.ext

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

fun RequestBuilder<*>.withCenterCropRoundedCorners(
    context: Context,
    radiusDp: Int = 8
) = apply(
    RequestOptions().transform(
        CenterCrop(),
        RoundedCorners(context.dpToPx(radiusDp))
    )
)

fun RequestBuilder<Drawable>.withFade() = apply(
    transition(DrawableTransitionOptions.withCrossFade())
)