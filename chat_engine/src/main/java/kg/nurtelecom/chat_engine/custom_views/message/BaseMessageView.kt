package kg.nurtelecom.chat_engine.custom_views.message

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.model.MessageStatus
import kg.nurtelecom.chat_engine.model.MessageType

abstract class BaseMessageView<VB: ViewBinding> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.baseMessageViewDefaultStyle,
    defStyle: Int = R.style.chat_engine_BaseMessageViewStyle
): LinearLayout(context, attrs, defStyleAttr, defStyle)  {

    protected val vb: VB by lazy { inflateView() }

    protected var messageType: MessageType? = null
    protected var messageStatus: MessageStatus? = null

    init {
        context.obtainStyledAttributes(attrs, R.styleable.chat_engine_BaseMessageView, defStyleAttr, defStyle).run {
            obtainAttributes(this)
            recycle()
        }
    }

    abstract fun inflateView(): VB

    @CallSuper
    protected open fun obtainAttributes(typedArray: TypedArray) {
        typedArray.run {
            getInt(R.styleable.chat_engine_BaseMessageView_messageType, 0).let {
                setMessageType(it)
            }
            getInt(R.styleable.chat_engine_BaseMessageView_messageStatus, 0).let {
                setMessageStatus(it)
            }
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var superState: Parcelable? = null
        (state as? Bundle)?.let { bundle ->
            setMessageType(bundle.getInt(MESSAGE_TYPE_STATE))
            setMessageStatus(bundle.getInt(MESSAGE_STATUS_STATE))
            superState = bundle.getParcelable(SUPER_STATE)
        }
        super.onRestoreInstanceState(superState)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val supperState = super.onSaveInstanceState()
        return Bundle().apply {
            messageType?.ordinal?.let { putInt(MESSAGE_TYPE_STATE, it) }
            messageStatus?.ordinal?.let { putInt(MESSAGE_STATUS_STATE, it) }
            putParcelable(TextMessageView.SUPER_STATE, supperState)
        }
    }

    private fun setMessageStatus(statusInt: Int) {
        val status = when (statusInt) {
            0 -> MessageStatus.DONE
            1 -> MessageStatus.LOADING
            2 -> MessageStatus.ERROR
            else -> return
        }
        messageStatus = status
        setupMessageStatus(status)
    }

    open fun setupMessageStatus(status: MessageStatus) {  }

    private fun setMessageType(intType: Int) {
        val type = when (intType) {
            MessageType.RESPONSE.ordinal -> MessageType.RESPONSE
            MessageType.REQUEST.ordinal -> MessageType.REQUEST
            else -> return
        }
        messageType = type
        setupMessageType(type)
    }

    open fun setupMessageType(type: MessageType) {
        when (type) {
            MessageType.RESPONSE -> setupAsResponseMessage()
            MessageType.REQUEST -> setupAsRequestMessage()
        }
    }

    open fun setupAsResponseMessage() {
        messageType = MessageType.RESPONSE
    }

    open fun setupAsRequestMessage() {
        messageType = MessageType.REQUEST
    }

    companion object {
        const val SUPER_STATE = "super_state"
        const val MESSAGE_TYPE_STATE = "message_type_state"
        const val MESSAGE_STATUS_STATE ="message_status_state"
    }
}