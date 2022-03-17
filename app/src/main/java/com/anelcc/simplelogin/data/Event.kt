package com.anelcc.simplelogin.data

open class Event<out T> (private val content: T) {
    var hasBenHandled = false
        private set

    fun  getContentOrNull(): T? {
        return if (hasBenHandled) {
            null
        } else {
            hasBenHandled = true
            content
        }
    }
}