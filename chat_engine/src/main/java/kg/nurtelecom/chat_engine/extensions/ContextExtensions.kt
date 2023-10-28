package kg.nurtelecom.chat_engine.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Context.hideKeyboard() {
    val view = (this as? Activity)?.currentFocus
    if (view != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}