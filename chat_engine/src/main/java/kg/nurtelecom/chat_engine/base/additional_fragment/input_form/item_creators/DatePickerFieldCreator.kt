package kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators

import android.content.Context
import android.widget.LinearLayout
import kg.nurtelecom.chat_engine.custom_views.DatePickerInputField
import kg.nurtelecom.chat_engine.model.DatePickerFieldInfo

object DatePickerFieldCreator : ValidatableItem() {

    fun create(
        context: Context,
        datePickerFieldInfo: DatePickerFieldInfo,
        onSetValue: (List<String> , Boolean) -> Unit
    ): DatePickerInputField {
        return DatePickerInputField(context).apply {
            tag = datePickerFieldInfo.fieldId
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(
                    0,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_4dp),
                    0,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_4dp)
                )
            }
            setupViews(datePickerFieldInfo, onSetValue)
        }
    }
}