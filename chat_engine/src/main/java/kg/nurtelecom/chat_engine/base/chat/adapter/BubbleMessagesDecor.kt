package kg.nurtelecom.chat_engine.base.chat.adapter

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import kg.nurtelecom.chat_engine.base.chat.adapter.message_vh.TextMessageViewHolder

class BubbleMessagesDecor(
    private val topInOffsetPx: Int,
    private val topOutOffsetPx: Int,
    private val oppositeOffset: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val adapter = parent.adapter ?: return
        val currentPosition = parent.getChildAdapterPosition(view).takeIf { it != RecyclerView.NO_POSITION } ?: return

        val prevViewType = adapter.getItemViewType(currentPosition - 1)

        when (val currentViewType = adapter.getItemViewType(currentPosition)) {
            MessageAdapterViewTypes.RESPONSE_TEXT.ordinal, MessageAdapterViewTypes.REQUEST_TEXT.ordinal -> {
                if (prevViewType == currentViewType)
                    outRect.set(0,  topInOffsetPx, 0, 0)
                else
                    outRect.set(0,  topOutOffsetPx, 0, 0)
            }
            MessageAdapterViewTypes.RESPONSE_IMAGE.ordinal -> {
                outRect.set(oppositeOffset,  topOutOffsetPx, 0, 0)
            }
            MessageAdapterViewTypes.REQUEST_IMAGE.ordinal -> {
                outRect.set(0,  topOutOffsetPx, oppositeOffset, 0)
            }
            MessageAdapterViewTypes.TYPING.ordinal -> {
                outRect.set(0,  topOutOffsetPx, 0, 0)
            }
            MessageAdapterViewTypes.ACCENT_BUTTON.ordinal, MessageAdapterViewTypes.SECONDARY_BUTTON.ordinal -> {
                outRect.set(0,  0, 0, 0)
            }
            MessageAdapterViewTypes.BOTTOM_ANCHOR_HOLDER.ordinal -> outRect.set(0,  0, 0, 0)
            else -> outRect.set(0,  topOutOffsetPx, 0, 0)
        }

    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val adapter = parent.adapter ?: return
        parent.children.forEach { view ->
            (parent.getChildViewHolder(view) as? TextMessageViewHolder)?.let {
                val currentPosition = parent.getChildAdapterPosition(view).takeIf { it != RecyclerView.NO_POSITION } ?: return
                val currentViewType = adapter.getItemViewType(currentPosition)
                val hasPrev = if (currentPosition == 0) {
                    false
                } else {
                    adapter.getItemViewType(currentPosition - 1) == currentViewType
                }
                val hasNext = if (currentPosition == state.itemCount - 1) {
                    false
                } else {
                    adapter.getItemViewType(currentPosition + 1) == currentViewType
                }
                it.renderCorners(hasPrev, hasNext)
            }
        }
    }
}