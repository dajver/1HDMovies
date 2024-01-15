package com.a1hd.core.etc

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

fun View.openKeyboard() {
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    inputMethodManager.showSoftInput(this, 0)
}

fun View?.hideKeyboard() {
    this?.let {
        val inputMethodManager =
            it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun Fragment.isKeyboardOpen(): Boolean {
    return ViewCompat.getRootWindowInsets(requireView())
        ?.isVisible(WindowInsetsCompat.Type.ime()) == true
}
