package kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators

import android.content.Context
import android.widget.LinearLayout
import kg.nurtelecom.chat_engine.custom_views.DatePickerInputField
import kg.nurtelecom.chat_engine.model.DatePickerFieldInfo

object DatePickerFieldCreator : ValidatableItem() {

    fun create(
        context: Context,
        datePickerFieldInfo: DatePickerFieldInfo,
        onSetValue: (List<String>, Boolean) -> Unit
    ): DatePickerInputField {
        return DatePickerInputField(context).apply {
            tag = datePickerFieldInfo.fieldId
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(
                    resources.getDimensionPixelSize(com.design2.chili2.R.dimen.padding_16dp),
                    resources.getDimensionPixelSize(com.design2.chili2.R.dimen.padding_8dp),
                    resources.getDimensionPixelSize(com.design2.chili2.R.dimen.padding_16dp),
                    resources.getDimensionPixelSize(com.design2.chili2.R.dimen.padding_8dp)
                )
            }
            setCornerRadius(resources.getDimension(com.design2.chili2.R.dimen.radius_12dp))
            setupViews(datePickerFieldInfo, onSetValue)
        }
    }
}