package kg.nurtelecom.chat_engine.base.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.custom_views.message.MessageRoundedCorners
import kg.nurtelecom.chat_engine.databinding.ItemTypingBinding
import kg.nurtelecom.chat_engine.model.MessageType

class TypingViewHolder(private val vb: ItemTypingBinding): RecyclerView.ViewHolder(vb.root) {

    fun onBind() {
        vb.container.setupMessageRoundedCorners(MessageType.SYSTEM, MessageRoundedCorners.ALL)
        Glide.with(vb.ivLoader)
            .asGif()
            .load(R.raw.loading)
            .into(vb.ivLoader)
    }

    companion object {
        fun create(parent: ViewGroup): TypingViewHolder {
            val vb = ItemTypingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TypingViewHolder(vb)
        }
    }
}