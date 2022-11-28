package kg.nurtelecom.chat_engine.base.chat.adapter.message_vh

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kg.nurtelecom.chat_engine.custom_views.message.MessageRoundedCorners
import kg.nurtelecom.chat_engine.databinding.ChatEngineItemTextMessageBinding
import kg.nurtelecom.chat_engine.model.Message
import kg.nurtelecom.chat_engine.model.MessageContentType

class TextMessageViewHolder(private val vb: ChatEngineItemTextMessageBinding) : RecyclerView.ViewHolder(vb.root) {

    private var message: Message? = null

    fun onBind(message: Message) = with(vb.textMessage) {
        this@TextMessageViewHolder.message = message
        when(message.contentType) {
            MessageContentType.TEXT -> setMessage(message.content)
            MessageContentType.TEXT_HTML -> setMessageHtml(message.content)
            else -> {}
        }
        setupMessageType(message.type)
        setupMessageStatus(message.status)
    }

    fun renderCorners(hasPrev: Boolean, hasNext: Boolean) {
        message?.let {
            val cornerType = when {
                hasPrev && hasNext -> MessageRoundedCorners.MIDDLE
                hasPrev -> MessageRoundedCorners.BOTTOM
                hasNext -> MessageRoundedCorners.TOP
                else ->  MessageRoundedCorners.ALL
            }
            vb.textMessage.setupMessageRoundedCorners(cornerType, it.type)
        }
    }

    companion object {
        fun create(parent: ViewGroup): TextMessageViewHolder {
            val vb  = ChatEngineItemTextMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TextMessageViewHolder(vb)
        }
    }
}