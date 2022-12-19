package kg.nurtelecom.chat_engine.custom_views.message

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.databinding.ChatEngineItemHorizontalTableMessageBinding
import kg.nurtelecom.chat_engine.databinding.ChatEngineItemVerticalTableMessageBinding
import kg.nurtelecom.chat_engine.databinding.ChatEngineViewTableMessageBinding
import kg.nurtelecom.chat_engine.model.TableItem
import kg.nurtelecom.chat_engine.model.TableOrientation
import kg.nurtelecom.chat_engine.model.TableRow

class TableMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.baseMessageViewDefaultStyle,
    defStyle: Int = R.style.chat_engine_BaseMessageViewStyle)
    : BaseMessageView<ChatEngineViewTableMessageBinding>(context, attrs, defStyleAttr, defStyle) {

    private var renderedRows: List<TableRow>? = null

    override fun inflateView(): ChatEngineViewTableMessageBinding {
        return ChatEngineViewTableMessageBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setupMessageTableView(info: List<TableRow>) {
        if (info == renderedRows) return
        invalidate()
        val rootContainer = createContainer(TableOrientation.VERTICAL)
        info.forEach { rowInfo ->
            val container = createContainer(rowInfo.orientation ?: TableOrientation.VERTICAL)
            val lastItemIndex = rowInfo.items?.lastIndex
            rowInfo.items?.forEachIndexed { index, itemInfo ->
                val item = if (rowInfo.orientation == TableOrientation.VERTICAL) {
                    createVerticalItem(itemInfo, index == lastItemIndex)
                } else {
                    createHorizontalItem(itemInfo, index == lastItemIndex)
                }
                container.addView(item)
            }
            rootContainer.addView(container)
        }
        vb.flContainer.addView(rootContainer)
        renderedRows = info
    }

    private fun createContainer(orientation: TableOrientation): LinearLayout {
        return LinearLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setOrientation(if (orientation == TableOrientation.VERTICAL) VERTICAL else HORIZONTAL)
        }
    }

    private fun createHorizontalItem(item: TableItem, isLast: Boolean): View {
        val itemVb = ChatEngineItemHorizontalTableMessageBinding.inflate(LayoutInflater.from(context), this, false)
        itemVb.apply {
            divider.isVisible = !isLast
            tvValue.text = item.title
        }
        Glide.with(itemVb.root)
            .load(item.icon)
            .into(itemVb.ivIcon)
        return itemVb.root
    }

    private fun createVerticalItem(item: TableItem, isLast: Boolean): View {
        val itemVb = ChatEngineItemVerticalTableMessageBinding.inflate(LayoutInflater.from(context), this, false)
        itemVb.apply {
            divider.isVisible = !isLast
            tvTitle.text = item.title
            tvValue.text = item.price
        }
        return  itemVb.root
    }

    override fun setupAsResponseMessage() {
        super.setupAsResponseMessage()
    }

    override fun setupAsRequestMessage() {
        super.setupAsRequestMessage()
    }
}