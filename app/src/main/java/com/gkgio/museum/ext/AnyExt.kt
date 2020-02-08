package com.gkgio.museum.ext

fun <T : Any> T.addTo(iterable: MutableList<T>) {
    iterable.add(this)
}