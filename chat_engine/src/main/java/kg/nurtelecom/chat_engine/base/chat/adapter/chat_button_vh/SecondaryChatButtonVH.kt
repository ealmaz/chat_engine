package kg.nurtelecom.chat_engine.base.chat.adapter.chat_button_vh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kg.nurtelecom.chat_engine.databinding.ChatEngineItemSecondaryChatButtonBinding
import kg.nurtelecom.chat_engine.model.ChatButton

class SecondaryChatButtonVH(private val vb: ChatEngineItemSecondaryChatButtonBinding): RecyclerView.ViewHolder(vb.root) {

    fun onBind(chatButton: ChatButton) = with(vb.btn) {
        isVisible = true
        vb.progress.isVisible = false
        tag = chatButton.buttonId
        text = chatButton.text
    }

    companion object {
        fun create(parent: ViewGroup, onClick: (tag: String) -> Unit): SecondaryChatButtonVH {
            val view = ChatEngineItemSecondaryChatButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            view.btn.setOnClickListener {
                view.progress.isVisible = true
                view.btn.visibility = View.INVISIBLE
                onClick.invoke(it.tag.toString())
            }
            return SecondaryChatButtonVH(view)
        }
    }
}