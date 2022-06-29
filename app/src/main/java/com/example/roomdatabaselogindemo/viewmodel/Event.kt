package com.example.roomdatabaselogindemo.viewmodel

/**
 * Boilerplate Event Handling class used to return the status of events that occur
 * in room database.
 *
 * Essentially exists for the purpose of updating the user when information has been
 * saved to the database.
 */
    /**
     * Used as a wrapper for data that is exposed via a LiveData that represents an event.
     */
    open class Event<out T>(private val content: T) {

        var hasBeenHandled = false
            private set // Allow external read but not write

        /**
         * Returns the content and prevents its use again.
         */
        fun getContentIfNotHandled(): T? {
            return if (hasBeenHandled) {
                null
            } else {
                hasBeenHandled = true
                content
            }
        }

        /**
         * Returns the content, even if it's already been handled.
         */
        fun peekContent(): T = content
    }
