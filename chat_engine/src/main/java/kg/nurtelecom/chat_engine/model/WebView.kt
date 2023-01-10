package kg.nurtelecom.chat_engine.model

import java.io.Serializable

data class WebView(
    val id: String,
    val url: String? = null,
    val properties: WebViewProperties? = null,
): Serializable

data class WebViewProperties(
    val fileType: WebViewFileTypes? = null
): Serializable

enum class WebViewFileTypes {
    PDF
}
