package kg.nurtelecom.chat_engine.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import com.design.chili.view.modals.bottom_sheet.serach_bottom_sheet.SearchSelectorBottomSheet
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.model.Option

class DropDownInputField @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : LinearLayout(context, attributeSet) {

    private val views: DropDownInputFiledData

    var options: List<Option> = listOf()

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_engine_form_item_drop_down, this, true)
        views = DropDownInputFiledData(
            view.findViewById(R.id.tv_label)
        )

    }

    fun setHint(hint: String) {
        views.tvTitle.apply {
            text = hint
            setTextColor(getColor(context, com.design.chili.R.color.gray_1_alpha_50))
        }
    }

    fun setText(text: String) {
        if (text.isBlank()) return
        views.tvTitle.apply {
            this.text = text
            setTextColor(getColor(context, R.color.chat_engine_drop_down_item_text_color))
        }
    }


    private fun createSearchBottomSheet(context: Context, options: List<com.design.chili.view.modals.bottom_sheet.serach_bottom_sheet.Option>, isSingleSelector: Boolean): SearchSelectorBottomSheet {
        return SearchSelectorBottomSheet.Builder()
            .setIsHeaderVisible(true)
            .setIsSearchAvailable(true)
            .setIsSingleSelection(isSingleSelector)
            .build(context, options)
    }
}

data class DropDownInputFiledData(
    val tvTitle: TextView
)