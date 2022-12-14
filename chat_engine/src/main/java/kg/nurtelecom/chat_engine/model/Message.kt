package kg.nurtelecom.chat_engine.model

import java.io.Serializable

data class Message(
    val id: String,
    val content: String = "",
    val contentType: MessageContentType,
    val messageType: MessageType,
    val status: MessageStatus = MessageStatus.DONE
): MessageAdapterItem, Serializable {

    override fun getItemId(): String {
        return id
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return if (other is Message) other.id == this.id
        else false
    }

    override fun areContentTheSame(other: Any): Boolean {
        return if (other is Message) other == this
        else false
    }

}