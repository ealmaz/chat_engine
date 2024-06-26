package kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.view.Gravity
import android.widget.LinearLayout
import com.design2.chili2.view.input.MaskedInputView
import kg.nurtelecom.chat_engine.model.InputField
import kg.nurtelecom.chat_engine.model.InputFieldInputType
import kg.nurtelecom.chat_engine.model.Validation

object InputFieldCreator : ValidatableItem() {

    fun create(
        context: Context,
        fieldInfo: InputField,
        onFiledChanged: (result: List<String>, isValid: Boolean) -> Unit
    ): MaskedInputView {
        return MaskedInputView(context).apply {
            tag = fieldInfo.fieldId
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
            setGravity(Gravity.START)
            fieldInfo.label?.let { setupLabelBehavior(it, fieldInfo.hint) }
            fieldInfo.placeholder?.let { setHint(it) }
            fieldInfo.mask?.let { setupNewMask(it) }
            fieldInfo.maskSymbols?.let { setupNewMaskSymbols(it.map { it.first() }) }

            when (fieldInfo.inputType) {
                InputFieldInputType.NUMBER -> setInputType(InputType.TYPE_CLASS_NUMBER)
                InputFieldInputType.TEXT_ALL_CAPS -> {
                    setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    clearAndSetFilters(arrayOf(InputFilter.AllCaps()))
                }

                else -> setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
            }
            setupClearTextButton()
            setSimpleTextChangedListener {
                val input = listOf(getInputText())

                val isValid = validInputFieldValue(this, fieldInfo.inputType, input, fieldInfo.validations)
                onFiledChanged(input, isValid)
            }
            setText(fieldInfo.value ?: "")
        }
    }

    private fun validInputFieldValue(
        view: MaskedInputView,
        inputType: InputFieldInputType?,
        input: List<String>,
        validations: List<Validation>?
    ): Boolean {
        return if (inputType == InputFieldInputType.NUMBER) {
            validateItem(validations, input) && view.isInputMaskFilled()
        } else {
            validateItem(validations, input)
        }
    }

}

fun MaskedInputView.setupLabelBehavior(label: String, message: String?) {
    setHint(label)
    setFocusChangeListener({
        setMessage(if (message.isNullOrBlank()) label else message)
    }, {
        hideMessage()
    })
}