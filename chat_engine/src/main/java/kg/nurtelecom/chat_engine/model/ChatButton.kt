package kg.nurtelecom.chat_engine.model

data class ChatButton(
    val buttonId: String,
    val text: String? = null,
    val style: ButtonStyle? = null,
) : MessageAdapterItem  {

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

