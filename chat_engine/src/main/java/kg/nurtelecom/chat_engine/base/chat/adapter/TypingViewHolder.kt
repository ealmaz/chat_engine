package kg.nurtelecom.chat_engine.base.chat.adapter

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.databinding.ItemTypingBinding

class TypingViewHolder(private val vb: ItemTypingBinding): RecyclerView.ViewHolder(vb.root) {

    fun onBind() {
        Glide.with(vb.ivLoader)
            .asGif()
            .load(resolveTypingGifResource())
            .into(vb.ivLoader)
    }

    private fun resolveTypingGifResource(): Int {
        return when (vb.root.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> R.raw.loading_night
            else -> R.raw.loading
        }
    }

    companion object {
        fun create(parent: ViewGroup): TypingViewHolder {
            val vb = ItemTypingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TypingViewHolder(vb)
        }
    }
}