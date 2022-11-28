package kg.nurtelecom.chat_engine.base.chat.adapter.chat_button_vh

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kg.nurtelecom.chat_engine.databinding.ChatEngineItemAccentChatButtonBinding
import kg.nurtelecom.chat_engine.model.ChatButton

class AccentChatButtonVH(private val vb: ChatEngineItemAccentChatButtonBinding): RecyclerView.ViewHolder(vb.root) {

    fun onBind(chatButton: ChatButton) = with(vb.btn) {
        tag = chatButton.buttonId
        text = chatButton.title
    }

    companion object {
        fun create(parent: ViewGroup, onClick: (tag: String) -> Unit): AccentChatButtonVH {
            val view = ChatEngineItemAccentChatButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            view.btn.setOnClickListener { onClick.invoke(it.tag.toString()) }
            return AccentChatButtonVH(view)
        }
    }
}

