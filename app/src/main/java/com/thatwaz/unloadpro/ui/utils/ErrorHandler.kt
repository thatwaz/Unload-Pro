package com.thatwaz.unloadpro.ui.utils



import android.util.Log

object ErrorHandler {
    fun handleError(error: Throwable) {
        // Log the error
        Log.e("ErrorHandler", "An error occurred", error)

        // Optionally, show a toast or update a UI element
        // This depends on your architecture and requirements
    }
}
