package kg.nurtelecom.chat_engine.base.chat.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.nurtelecom.chat_engine.R

class TypingViewHolder(private val imageView: ImageView): RecyclerView.ViewHolder(imageView) {

    fun onBind() {
        Glide.with(imageView)
            .asGif()
            .load(R.raw.loading)
            .into(imageView)
    }

    companion object {
        fun create(parent: ViewGroup): TypingViewHolder {
            val view = ImageView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.WRAP_CONTENT,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.view_24dp)
                )
            }
            return TypingViewHolder(view)
        }
    }
}