package kg.nurtelecom.chat_engine.base.chat.adapter.chat_button_vh

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import com.design.chili.extensions.setOnSingleClickListener
import com.facebook.shimmer.ShimmerFrameLayout
import kg.nurtelecom.chat_engine.databinding.ChatEngineItemAccentChatButtonBinding
import kg.nurtelecom.chat_engine.model.ButtonProperties

class AccentChatButtonVH(private val vb: ChatEngineItemAccentChatButtonBinding): BaseChatButtonVH(vb.root) {

    override val btn: Button = vb.btn
    override val progress: ProgressBar = vb.progress
    override val shimmer: ShimmerFrameLayout = vb.shimmer

    companion object {
        fun create(parent: ViewGroup, onClick: (tag: String, buttonProperties: ButtonProperties?) -> Unit): AccentChatButtonVH {
            val view = ChatEngineItemAccentChatButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return AccentChatButtonVH(view).apply {
                vb.btn.setOnSingleClickListener {
                    onClick.invoke(vb.btn.tag.toString(), additionalButtonProperties)
                    stopShimmer()
                }
            }
        }
    }
}

