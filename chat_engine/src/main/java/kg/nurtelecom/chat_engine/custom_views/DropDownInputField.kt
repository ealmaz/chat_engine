package kg.nurtelecom.chat_engine.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import com.design.chili.view.modals.bottom_sheet.serach_bottom_sheet.Option
import com.design.chili.view.modals.bottom_sheet.serach_bottom_sheet.SearchSelectorBottomSheet
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators.DropDownFieldCreator
import kg.nurtelecom.chat_engine.model.ChooseType
import kg.nurtelecom.chat_engine.model.DropDownFieldInfo

class DropDownInputField @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : LinearLayout(context, attributeSet) {

    private val views: DropDownInputFiledData

    var options: List<Option> = listOf()
        set(value) {
            field = value
            onBottomSheetDismiss()
        }

    private var onSelectionChanged: ((values: List<String>, Boolean) -> Unit)? = null

    private var dropDownListInfo: DropDownFieldInfo? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_engine_form_item_drop_down, this, true)
        views = DropDownInputFiledData(view.findViewById(R.id.tv_label))
    }

    fun setupViews(dropDownFieldInfo: DropDownFieldInfo, onSelectionChanged: (values: List<String>, Boolean) -> Unit) {
        this.onSelectionChanged = onSelectionChanged
        this.dropDownListInfo = dropDownFieldInfo
        this.setOnClickListener {
            if (options.isEmpty()) return@setOnClickListener
            val bs = createSearchBottomSheet(context, dropDownFieldInfo.chooseType != ChooseType.MULTIPLE)
            bs.setOnDismissListener { onBottomSheetDismiss() }
            bs.show()
        }
        onBottomSheetDismiss()
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

    private fun createSearchBottomSheet(context: Context, isSingleSelection: Boolean): SearchSelectorBottomSheet {
        return SearchSelectorBottomSheet.Builder()
            .setIsHeaderVisible(true)
            .setIsSearchAvailable(true)
            .setIsSingleSelection(isSingleSelection)
            .build(context, options)
    }

    fun clearSelected() {
        options.forEach { it.isSelected = false }
        onBottomSheetDismiss()
    }

    private fun onBottomSheetDismiss() {
        setHint(dropDownListInfo?.label ?: "")
        val selectedValues = mutableListOf<String>()
        val selectedIds = mutableListOf<String>()
        options.forEach {
            if (it.isSelected) {
                selectedValues.add(it.value)
                selectedIds.add(it.id)
            }
        }
        setText(selectedValues.joinToString { it })
        val isValid = DropDownFieldCreator.validateItem(dropDownListInfo?.validations, selectedIds)
        onSelectionChanged?.invoke(selectedIds, isValid)
    }
}

data class DropDownInputFiledData(
    val tvTitle: TextView
)