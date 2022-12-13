package kg.nurtelecom.chat_engine.model

import java.io.Serializable

data class ChatButton(
    val buttonId: String,
    val text: String? = null,
    val style: ButtonStyle? = null,
    val properties: ButtonProperties? = null,
) : MessageAdapterItem, Serializable {

    override fun getItemId(): String {
        return buttonId
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return if (other is ChatButton) other.buttonId == this.buttonId
        else false
    }

    override fun areContentTheSame(other: Any): Boolean {
        return if (other is ChatButton) other == this
        else false
    }
}

data class ButtonProperties(
    val identifier: String? = null,
    val enableAt: String? = null,
    val formIdToOpen: String? = null,
)

