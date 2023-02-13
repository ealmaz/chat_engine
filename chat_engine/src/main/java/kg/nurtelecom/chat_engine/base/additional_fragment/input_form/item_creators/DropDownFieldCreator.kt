package kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators

import android.content.Context
import android.widget.LinearLayout
import com.design.chili.view.modals.bottom_sheet.serach_bottom_sheet.Option
import kg.nurtelecom.chat_engine.custom_views.DropDownInputField
import kg.nurtelecom.chat_engine.model.DropDownFieldInfo

object DropDownFieldCreator : ValidatableItem() {

    fun create(context: Context, dropDownFieldInfo: DropDownFieldInfo, onSelectionChanged: (selected: List<String>, isValid: Boolean) -> Unit): DropDownInputField {
        return DropDownInputField(context).apply {
            tag = dropDownFieldInfo.fieldId
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(
                    0,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_4dp),
                    0,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_4dp)
                )
            }
            setupViews(dropDownFieldInfo, onSelectionChanged)
            dropDownFieldInfo.options?.let { options = it.map { Option(it.id, it.label ?: "", it.isSelected ?: false) } }
            setHint(dropDownFieldInfo.label ?: "")
        }
    }

}