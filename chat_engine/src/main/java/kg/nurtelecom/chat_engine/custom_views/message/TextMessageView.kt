package kg.nurtelecom.chat_engine.custom_views.message

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Html
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.databinding.ChatEngineViewTextMessageBinding
import kg.nurtelecom.chat_engine.model.MessageStatus
import kg.nurtelecom.chat_engine.model.MessageType

class TextMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.baseMessageViewDefaultStyle,
    defStyle: Int = R.style.chat_engine_BaseMessageViewStyle
) : BaseMessageView<ChatEngineViewTextMessageBinding>(context, attrs, defStyleAttr, defStyle) {

    private var messageRoundedCorner: MessageRoundedCorners? = null

    override fun inflateView(): ChatEngineViewTextMessageBinding {
        return ChatEngineViewTextMessageBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun obtainAttributes(typedArray: TypedArray) {
        super.obtainAttributes(typedArray)
        typedArray.run {
            getString(R.styleable.chat_engine_BaseMessageView_message)?.let {
                setMessage(it)
            }
            getInt(R.styleable.chat_engine_BaseMessageView_messageRoundedCorners, 0).let {
                setMessageRoundedCorners(it)
            }
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var superState: Parcelable? = null
        (state as? Bundle)?.let { bundle ->
            setMessage(bundle.getString(TEXT_STATE))
            setMessageRoundedCorners(bundle.getInt(MESSAGE_CORNERS_STATE))
            superState = bundle.getParcelable(SUPER_STATE)
        }
        super.onRestoreInstanceState(superState)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val supperState = super.onSaveInstanceState()
        return Bundle().apply {
            putString(TEXT_STATE, getMessage())
            messageRoundedCorner?.ordinal?.let { putInt(MESSAGE_CORNERS_STATE, it) }
            putParcelable(SUPER_STATE, supperState)
        }
    }

    override fun setupMessageStatus(status: MessageStatus) {
        when (status) {
            MessageStatus.DONE -> isMessageLoading(false)
            MessageStatus.LOADING -> isMessageLoading(true)
            else -> {}
        }
    }

    fun getMessage(): String {
        return vb.tvMessage.text.toString()
    }

    fun setMessage(text: String?) {
        vb.tvMessage.text = text
    }

    fun setMessage(@StringRes textResId: Int) {
        vb.tvMessage.setText(textResId)
    }

    fun setMessageHtml(html: String) {
        val text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(html)
        }
        vb.tvMessage.text = text
    }

    private fun isMessageLoading(isLoading: Boolean) {
        if (isLoading) {
            when (messageType) {
                MessageType.RESPONSE -> vb.ivLeftIcon.isVisible = true
                MessageType.REQUEST -> vb.ivRightIcon.isVisible = true
                else -> {}
            }
        } else {
            vb.ivRightIcon.isVisible = false
            vb.ivLeftIcon.isVisible = false
        }
    }

    private fun setMessageRoundedCorners(intType: Int) {
        val cornerType = when (intType) {
            MessageRoundedCorners.ALL.ordinal -> MessageRoundedCorners.ALL
            MessageRoundedCorners.TOP.ordinal -> MessageRoundedCorners.TOP
            MessageRoundedCorners.MIDDLE.ordinal -> MessageRoundedCorners.MIDDLE
            MessageRoundedCorners.BOTTOM.ordinal -> MessageRoundedCorners.BOTTOM
            else -> return
        }
        messageRoundedCorner = cornerType
        setupMessageRoundedCorners(cornerType)

    }

    fun setupMessageRoundedCorners(cornerType: MessageRoundedCorners, type: MessageType? = null) {
        messageRoundedCorner = cornerType
        type?.let { messageType = it }
        vb.llTextContainer.setupMessageRoundedCorners(messageType, messageRoundedCorner)
    }

    override fun setupAsResponseMessage() {
        super.setupAsResponseMessage()
        vb.llRoot.gravity = Gravity.END
        refreshDrawableState()
    }

    override fun setupAsRequestMessage() {
        super.setupAsRequestMessage()
        vb.llRoot.gravity = Gravity.START
        refreshDrawableState()
    }

    companion object {
        const val SUPER_STATE = "super_state"
        const val TEXT_STATE = "text_state"
        const val MESSAGE_CORNERS_STATE = "message_corners_state"
    }
}

enum class MessageRoundedCorners {
    ALL, TOP, MIDDLE, BOTTOM
}