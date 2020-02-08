package com.gkgio.museum.utils

import io.reactivex.subjects.PublishSubject

abstract class BaseEvent {
    private var subject = PublishSubject.create<Int>()

    fun getEventResult() = subject

    open fun onComplete(event: Int) = subject.onNext(event)
}