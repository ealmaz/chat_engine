package kg.nurtelecom.chat_engine.base.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.base.chat.adapter.chat_button_vh.AccentChatButtonVH
import kg.nurtelecom.chat_engine.base.chat.adapter.chat_button_vh.SecondaryChatButtonVH
import kg.nurtelecom.chat_engine.base.chat.adapter.message_vh.ImageMessageViewHolder
import kg.nurtelecom.chat_engine.base.chat.adapter.message_vh.TextMessageViewHolder
import kg.nurtelecom.chat_engine.databinding.ChatEngineItemTextMessageBinding
import kg.nurtelecom.chat_engine.model.*

class MessagesAdapter(
    private val onButtonClick: (tag: String) -> Unit,
    private val onLinkClick: (String) -> Unit
) : ListAdapter<MessageAdapterItem, RecyclerView.ViewHolder>(AsyncDifferConfig.Builder(MessageItemDiffUtilCallback()).build()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            MessageAdapterViewTypes.REQUEST_IMAGE.ordinal -> ImageMessageViewHolder.create(parent)
            MessageAdapterViewTypes.RESPONSE_IMAGE.ordinal -> ImageMessageViewHolder.create(parent)
            MessageAdapterViewTypes.REQUEST_TEXT.ordinal -> TextMessageViewHolder.create(parent, onLinkClick)
            MessageAdapterViewTypes.RESPONSE_TEXT.ordinal -> TextMessageViewHolder.create(parent, onLinkClick)
            MessageAdapterViewTypes.BOTTOM_ANCHOR_HOLDER.ordinal -> BottomAnchorViewHolder(parent.context)
            MessageAdapterViewTypes.TYPING.ordinal -> TypingViewHolder.create(parent)
            MessageAdapterViewTypes.ACCENT_BUTTON.ordinal -> AccentChatButtonVH.create(parent, onButtonClick)
            MessageAdapterViewTypes.SECONDARY_BUTTON.ordinal -> SecondaryChatButtonVH.create(parent, onButtonClick)
            else -> UnsupportedViewHolder.create(parent)
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
            isRequestTextMessage(item) -> MessageAdapterViewTypes.REQUEST_TEXT.ordinal
            isRequestImageMessage(item) -> MessageAdapterViewTypes.REQUEST_IMAGE.ordinal
            isResponseTextMessage(item) -> MessageAdapterViewTypes.RESPONSE_TEXT.ordinal
            isResponseImageMessage(item) -> MessageAdapterViewTypes.RESPONSE_IMAGE.ordinal
            isAccentButton(item) -> MessageAdapterViewTypes.ACCENT_BUTTON.ordinal
            isSecondaryButton(item) -> MessageAdapterViewTypes.SECONDARY_BUTTON.ordinal
            else -> MessageAdapterViewTypes.UNSUPPORTED_MESSAGE_TYPE.ordinal
        }
    }

    private fun isRequestTextMessage(item: MessageAdapterItem): Boolean {
        return item is Message
                && item.messageType == MessageType.SYSTEM
                && (item.contentType == MessageContentType.TEXT
                || item.contentType == MessageContentType.TEXT_HTML)
    }

    private fun isRequestImageMessage(item: MessageAdapterItem): Boolean {
        return item is Message
                && item.messageType == MessageType.SYSTEM
                && (item.contentType == MessageContentType.IMAGE_URL
                || item.contentType == MessageContentType.IMAGE_FILE_PATH)
    }

    private fun isResponseTextMessage(item: MessageAdapterItem): Boolean {
        return item is Message
                && item.messageType == MessageType.USER
                && (item.contentType == MessageContentType.TEXT
                || item.contentType == MessageContentType.TEXT_HTML)
    }

    private fun isResponseImageMessage(item: MessageAdapterItem): Boolean {
        return item is Message
                && item.messageType == MessageType.USER
                && (item.contentType == MessageContentType.IMAGE_URL
                || item.contentType == MessageContentType.IMAGE_FILE_PATH)
    }

    private fun isAccentButton(item: MessageAdapterItem): Boolean {
        return item is ChatButton && item.style == ButtonStyle.ACCENT
    }

    private fun isSecondaryButton(item: MessageAdapterItem): Boolean {
        return item is ChatButton && item.style == ButtonStyle.SECONDARY
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

enum class MessageAdapterViewTypes {
    BOTTOM_ANCHOR_HOLDER,
    RESPONSE_TEXT,
    RESPONSE_IMAGE,
    REQUEST_TEXT,
    REQUEST_IMAGE,
    TYPING,
    ACCENT_BUTTON,
    SECONDARY_BUTTON,
    UNSUPPORTED_MESSAGE_TYPE
}

class BottomAnchorViewHolder(context: Context) : RecyclerView.ViewHolder(ViewStub(context))

class UnsupportedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun create(parent: ViewGroup): UnsupportedViewHolder {
            val vb  = ChatEngineItemTextMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            vb.textMessage.setMessage(R.string.unsupported_type)
            return UnsupportedViewHolder(vb.root)
        }
    }
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

object UnsupportedMessageItem : MessageAdapterItem {
    override fun getItemId(): String {
        return "Unsupported_message"
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return other == this
    }

    override fun areContentTheSame(other: Any): Boolean {
        return other == this
    }
}