package kg.nurtelecom.chat_engine.base.chat.adapter

import android.content.Context
import android.view.ViewGroup
import android.view.ViewStub
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.nurtelecom.chat_engine.base.chat.adapter.chat_button_vh.AccentChatButtonVH
import kg.nurtelecom.chat_engine.base.chat.adapter.chat_button_vh.SecondaryChatButtonVH
import kg.nurtelecom.chat_engine.base.chat.adapter.message_vh.ImageMessageViewHolder
import kg.nurtelecom.chat_engine.base.chat.adapter.message_vh.TextMessageViewHolder
import kg.nurtelecom.chat_engine.model.*
import java.lang.IllegalArgumentException

class MessagesAdapter(private val onButtonClick: (tag: String) -> Unit) : ListAdapter<MessageAdapterItem, RecyclerView.ViewHolder>(AsyncDifferConfig.Builder(MessageItemDiffUtilCallback()).build()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            MessageAdapterViewTypes.REQUEST_IMAGE.ordinal -> ImageMessageViewHolder.create(parent)
            MessageAdapterViewTypes.RESPONSE_IMAGE.ordinal -> ImageMessageViewHolder.create(parent)
            MessageAdapterViewTypes.REQUEST_TEXT.ordinal -> TextMessageViewHolder.create(parent)
            MessageAdapterViewTypes.RESPONSE_TEXT.ordinal -> TextMessageViewHolder.create(parent)
            MessageAdapterViewTypes.BOTTOM_ANCHOR_HOLDER.ordinal -> BottomAnchorViewHolder(parent.context)
            MessageAdapterViewTypes.TYPING.ordinal -> TypingViewHolder.create(parent)
            MessageAdapterViewTypes.ACCENT_BUTTON.ordinal -> AccentChatButtonVH.create(parent, onButtonClick)
            MessageAdapterViewTypes.SECONDARY_BUTTON.ordinal -> SecondaryChatButtonVH.create(parent, onButtonClick)
            else -> throw IllegalArgumentException() // todo empty viewHolder
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TextMessageViewHolder -> holder.onBind(currentList[position] as Message)
            is ImageMessageViewHolder -> holder.onBind(currentList[position] as Message)
            is TypingViewHolder -> holder.onBind()
            is AccentChatButtonVH -> holder.onBind(currentList[position] as ChatButton)
            is SecondaryChatButtonVH -> holder.onBind(currentList[position] as ChatButton)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return when {
            item is ItemAnchor -> MessageAdapterViewTypes.BOTTOM_ANCHOR_HOLDER.ordinal
            item is ItemTyping -> MessageAdapterViewTypes.TYPING.ordinal
            item is Message && item.messageType == MessageType.SYSTEM && (item.contentType == MessageContentType.TEXT || item.contentType == MessageContentType.TEXT_HTML) -> MessageAdapterViewTypes.REQUEST_TEXT.ordinal
            item is Message && item.messageType == MessageType.SYSTEM && (item.contentType == MessageContentType.IMAGE_URL || item.contentType == MessageContentType.IMAGE_FILE_PATH) -> MessageAdapterViewTypes.REQUEST_IMAGE.ordinal
            item is Message && item.messageType == MessageType.USER && (item.contentType == MessageContentType.TEXT || item.contentType == MessageContentType.TEXT_HTML) -> MessageAdapterViewTypes.RESPONSE_TEXT.ordinal
            item is Message && item.messageType == MessageType.USER && (item.contentType == MessageContentType.IMAGE_URL || item.contentType == MessageContentType.IMAGE_FILE_PATH) -> MessageAdapterViewTypes.RESPONSE_IMAGE.ordinal
            item is ChatButton && item.style == ButtonStyle.ACCENT -> MessageAdapterViewTypes.ACCENT_BUTTON.ordinal
            item is ChatButton && item.style == ButtonStyle.SECONDARY -> MessageAdapterViewTypes.SECONDARY_BUTTON.ordinal
            else -> throw IllegalArgumentException("Unknown view type for ${item}")
        }
    }
}

class BottomAnchorViewHolder(context: Context) : RecyclerView.ViewHolder(ViewStub(context))

enum class MessageAdapterViewTypes {
    BOTTOM_ANCHOR_HOLDER,
    RESPONSE_TEXT,
    RESPONSE_IMAGE,
    REQUEST_TEXT,
    REQUEST_IMAGE,
    TYPING,
    ACCENT_BUTTON,
    SECONDARY_BUTTON
}

object ItemTyping : MessageAdapterItem {

    override fun getItemId(): String {
        return "ItemTyping"
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return other == this
    }

    override fun areContentTheSame(other: Any): Boolean {
        return other == this
    }
}

object ItemAnchor : MessageAdapterItem {
    override fun getItemId(): String {
        return "ItemAnchor"
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return other == this
    }

    override fun areContentTheSame(other: Any): Boolean {
        return other == this
    }
}

class MessageItemDiffUtilCallback : DiffUtil.ItemCallback<MessageAdapterItem>() {
    override fun areItemsTheSame(oldItem: MessageAdapterItem, newItem: MessageAdapterItem): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: MessageAdapterItem, newItem: MessageAdapterItem): Boolean {
        return oldItem.areContentTheSame(newItem)
    }
}