package kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators

import android.content.Context
import android.widget.LinearLayout
import com.design.chili.view.modals.bottom_sheet.serach_bottom_sheet.Option
import com.design.chili.view.modals.bottom_sheet.serach_bottom_sheet.SearchSelectorBottomSheet
import kg.nurtelecom.chat_engine.custom_views.DropDownInputField
import kg.nurtelecom.chat_engine.model.ChooseType
import kg.nurtelecom.chat_engine.model.DropDownFieldInfo

object DropDownFieldCreator : ItemCreator() {

    fun create(context: Context, dropDownFieldInfo: DropDownFieldInfo, onSelectionChanged: (selected: List<String>, isValid: Boolean) -> Unit): DropDownInputField {
        return DropDownInputField(context).apply {
            tag = dropDownFieldInfo.formItemId
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(
                    0,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_4dp),
                    0,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_4dp)
                )
            }
            setOnClickListener {
                val options = mutableListOf<Option>()
                dropDownFieldInfo.options.forEach {
                    options.add(Option(it.id, it.value, it.isSelected))
                }
                val bs = createSearchBottomSheet(context, options, dropDownFieldInfo.chooseType == ChooseType.SINGLE)
                bs.setOnDismissListener {
                    setHint(dropDownFieldInfo.label)
                    val selectedValues = mutableListOf<String>()
                    dropDownFieldInfo.options.forEach { dropDownOption ->
                        options.find { dropDownOption.id == it.id }?.let {
                            dropDownOption.isSelected = it.isSelected
                            if (it.isSelected) selectedValues.add(it.value)
                        }
                    }
                    setText(selectedValues.joinToString { it })
                    val selectedOptions = options.mapNotNull { if (it.isSelected) it.id else null }
                    val isValid = validateItem(dropDownFieldInfo.validations, selectedOptions)
                    onSelectionChanged(selectedOptions, isValid)
                }
                bs.show()
            }
            setHint(dropDownFieldInfo.label)
        }
    }

    private fun createSearchBottomSheet(context: Context, options: List<Option>, isSingleSelector: Boolean): SearchSelectorBottomSheet {
        return SearchSelectorBottomSheet.Builder()
            .setIsHeaderVisible(true)
            .setIsSearchAvailable(true)
            .setIsSingleSelection(isSingleSelector)
            .build(context, options)
    }

}