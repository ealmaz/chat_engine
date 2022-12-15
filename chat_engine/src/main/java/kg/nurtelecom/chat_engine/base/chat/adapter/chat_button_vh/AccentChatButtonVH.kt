package kg.nurtelecom.chat_engine.base.chat.adapter.chat_button_vh

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kg.nurtelecom.chat_engine.databinding.ChatEngineItemAccentChatButtonBinding
import kg.nurtelecom.chat_engine.model.ChatButton

class AccentChatButtonVH(private val vb: ChatEngineItemAccentChatButtonBinding): BaseChatButtonVH(vb.root) {

    override val btn: Button = vb.btn
    override val progress: ProgressBar = vb.progress

    companion object {
        fun create(parent: ViewGroup, onClick: (tag: String) -> Unit): AccentChatButtonVH {
            val view = ChatEngineItemAccentChatButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            view.btn.setOnClickListener { onClick.invoke(it.tag.toString()) }
            return AccentChatButtonVH(view)
        }
    }
}

