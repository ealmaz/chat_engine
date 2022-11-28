package kg.nurtelecom.chat_engine.extensions

import androidx.fragment.app.FragmentActivity

fun FragmentActivity.closeCurrentFragment() {
    supportFragmentManager.popBackStack()
}