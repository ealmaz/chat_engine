package kg.nurtelecom.chat_engine.model

data class ChatButton(
    val buttonId: String,
    val text: String,
    val style: ButtonStyle
) : MessageAdapterItem  {

    override fun getItemId(): String {
        return buttonId
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return false
    }

    override fun areContentTheSame(other: Any): Boolean {
        return false
    }
}

