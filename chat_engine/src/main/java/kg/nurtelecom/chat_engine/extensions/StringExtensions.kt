package kg.nurtelecom.chat_engine.extensions

import android.content.Context
import android.content.res.Configuration

fun String.replaceByTheme(
    context: Context,
    lightTheme: String,
    darkTheme: String,
): String {
    return when (context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
        Configuration.UI_MODE_NIGHT_NO -> this
        Configuration.UI_MODE_NIGHT_YES -> this.replace(lightTheme, darkTheme)
        else -> this
    }
}

fun String.getImgUrlByTheme(context: Context): String {
    return this.replaceByTheme(context, "light", "dark")
}