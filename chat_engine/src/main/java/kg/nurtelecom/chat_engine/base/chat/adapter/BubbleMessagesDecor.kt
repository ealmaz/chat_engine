package kg.nurtelecom.chat_engine.base.chat.adapter

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import kg.nurtelecom.chat_engine.base.chat.adapter.message_vh.TextMessageViewHolder

class BubbleMessagesDecor(
    private val defaultOffsetPx: Int,
    private val oppositeOffsetPx: Int,
    private val topBottomOffsetPx: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val adapter = parent.adapter ?: return
        val currentPosition = parent.getChildAdapterPosition(view).takeIf { it != RecyclerView.NO_POSITION } ?: return
        when (adapter.getItemViewType(currentPosition)) {
            MessageAdapterViewTypes.RESPONSE_TEXT.ordinal -> outRect.set(oppositeOffsetPx,  topBottomOffsetPx, defaultOffsetPx, topBottomOffsetPx)
            MessageAdapterViewTypes.RESPONSE_IMAGE.ordinal -> outRect.set(oppositeOffsetPx,  topBottomOffsetPx, defaultOffsetPx, topBottomOffsetPx)
            MessageAdapterViewTypes.REQUEST_TEXT.ordinal -> outRect.set(defaultOffsetPx,  topBottomOffsetPx, oppositeOffsetPx, topBottomOffsetPx)
            MessageAdapterViewTypes.REQUEST_IMAGE.ordinal -> outRect.set(defaultOffsetPx,  topBottomOffsetPx, oppositeOffsetPx, topBottomOffsetPx)
            else -> outRect.set(0,  5, 0, 5)
        }

    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val adapter = parent.adapter ?: return
        parent.children.forEach { view ->
            (parent.getChildViewHolder(view) as? TextMessageViewHolder)?.let {
                val currentPostion = parent.getChildAdapterPosition(view).takeIf { it != RecyclerView.NO_POSITION } ?: return
                val currentViewType = adapter.getItemViewType(currentPostion)
                val hasPrev = if (currentPostion == 0) {
                    false
                } else {
                    adapter.getItemViewType(currentPostion - 1) == currentViewType
                }
                val hasNext = if (currentPostion == state.itemCount - 1) {
                    false
                } else {
                    adapter.getItemViewType(currentPostion + 1) == currentViewType
                }
                it.renderCorners(hasPrev, hasNext)
            }
        }
    }
}