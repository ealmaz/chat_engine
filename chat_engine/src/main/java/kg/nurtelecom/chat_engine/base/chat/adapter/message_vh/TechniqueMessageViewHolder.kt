package kg.nurtelecom.chat_engine.base.chat.adapter.message_vh

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import kg.nurtelecom.chat_engine.databinding.ChatEngineItemTechniqueMessageBinding
import kg.nurtelecom.chat_engine.model.Message
import kg.nurtelecom.chat_engine.model.MessageContentType

class TechniqueMessageViewHolder(val vb: ChatEngineItemTechniqueMessageBinding) : RecyclerView.ViewHolder(vb.root) {

    fun onBind(message: Message) {
        val content = when (message.contentType) {
            MessageContentType.TEXT_HTML -> message.content.parseAsHtml().trimEnd()
            else -> message.content
        }
        vb.tvHeader.text = content
    }

    companion object {
        fun create(parent: ViewGroup): TechniqueMessageViewHolder {
            val view = ChatEngineItemTechniqueMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return TechniqueMessageViewHolder(view)
        }
    }
}