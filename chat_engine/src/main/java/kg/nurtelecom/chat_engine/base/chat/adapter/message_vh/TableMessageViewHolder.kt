package kg.nurtelecom.chat_engine.base.chat.adapter.message_vh

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kg.nurtelecom.chat_engine.databinding.ChatEngineItemTableMessageBinding
import kg.nurtelecom.chat_engine.model.TableMessage

class TableMessageViewHolder(private val vb: ChatEngineItemTableMessageBinding) : RecyclerView.ViewHolder(vb.root) {

    fun onBind(message: TableMessage) {
        message.rows?.let { vb.tableMessage.setupMessageTableView(it) }
    }

    companion object {
        fun create(parent: ViewGroup): TableMessageViewHolder {
            val view = ChatEngineItemTableMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return TableMessageViewHolder(view)
        }
    }
}