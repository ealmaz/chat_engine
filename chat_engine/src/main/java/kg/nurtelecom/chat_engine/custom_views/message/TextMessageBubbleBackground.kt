package kg.nurtelecom.chat_engine.custom_views.message

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.model.MessageType

class TextMessageBubbleBackground : LinearLayout {

    private var messageType: MessageType? = null
    private var messageRoundedCorners: MessageRoundedCorners? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        return when (messageType) {
            MessageType.REQUEST -> mergeDrawableForRequest(drawableState)
            MessageType.RESPONSE -> mergeDrawableForResponse(drawableState)
            else -> drawableState
        }
    }

    private fun mergeDrawableForRequest(drawableState: IntArray): IntArray {
        val state: IntArray = when (messageRoundedCorners) {
            MessageRoundedCorners.ALL -> intArrayOf(R.attr.state_is_start_single)
            MessageRoundedCorners.TOP -> intArrayOf(R.attr.state_is_start_top)
            MessageRoundedCorners.MIDDLE -> intArrayOf(R.attr.state_is_start_middle)
            MessageRoundedCorners.BOTTOM -> intArrayOf(R.attr.state_is_start_bottom)
            else -> return drawableState
        }
        return mergeDrawableStates(drawableState, state)
    }

    private fun mergeDrawableForResponse(drawableState: IntArray): IntArray {
        val state: IntArray = when (messageRoundedCorners) {
            MessageRoundedCorners.ALL -> intArrayOf(R.attr.state_is_end_single)
            MessageRoundedCorners.TOP -> intArrayOf(R.attr.state_is_end_top)
            MessageRoundedCorners.MIDDLE -> intArrayOf(R.attr.state_is_end_middle)
            MessageRoundedCorners.BOTTOM -> intArrayOf(R.attr.state_is_end_bottom)
            else -> return drawableState
        }
        return mergeDrawableStates(drawableState, state)
    }

    fun setupMessageRoundedCorners(messageType: MessageType?, messageRoundedCorners: MessageRoundedCorners?) {
        this.messageType = messageType
        this.messageRoundedCorners = messageRoundedCorners
        refreshDrawableState()
    }
}