package kg.nurtelecom.chat_engine.model

import java.util.*

data class Message(
    val messageId: String,
    val content: String = "",
    val contentType: MessageContentType,
    val type: MessageType,
    val status: MessageStatus = MessageStatus.DONE,
    val date: Date = Date()
): MessageAdapterItem {

    override fun areItemsTheSame(other: Any): Boolean {
        return if (other is Message) other.messageId == this.messageId
        else false
    }

    override fun areContentTheSame(other: Any): Boolean {
        return if (other is Message) other == this
        else false
    }

}