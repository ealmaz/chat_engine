package kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators

import android.content.Context
import android.text.InputType
import android.view.Gravity
import android.widget.LinearLayout
import com.design.chili.view.input.MaskedInputView
import kg.nurtelecom.chat_engine.model.InputField
import kg.nurtelecom.chat_engine.model.InputFieldInputType

object InputFieldCreator : ValidatableItem() {

    fun create(context: Context, fieldInfo: InputField, onFiledChanged: (result: List<String>, isValid: Boolean) -> Unit): MaskedInputView {
        return MaskedInputView(context).apply {
            tag = fieldInfo.fieldId
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(
                    0,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_4dp),
                    0,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_4dp)
                )
            }
            setGravity(Gravity.START)
            fieldInfo.label?.let { setupLabelBehavior(it, fieldInfo.hint) }
            fieldInfo.hint?.let { setMessage(it) }
            fieldInfo.placeholder?.let { setHint(it) }
            fieldInfo.mask?.let { setupNewMask(it) }
            fieldInfo.maskSymbols?.let { setupNewMaskSymbols(it.map { it.first() }) }
            when (fieldInfo.inputType) {
                InputFieldInputType.NUMBER -> setInputType(InputType.TYPE_CLASS_NUMBER)
                else -> setInputType(InputType.TYPE_CLASS_TEXT)
            }
            setupClearTextButton()
            setSimpleTextChangedListener {
                val input = listOf(getInputText())
                val isValid = validateItem(fieldInfo.validations, input) && isInputMaskFilled()
                onFiledChanged(input, isValid)
            }
            setText(fieldInfo.value ?: "")
        }
    }
}

fun MaskedInputView.setupLabelBehavior(label: String, message: String?) {
    setHint(label)
    setFocusChangeListener({
        setMessage(if (message.isNullOrBlank()) label else message)
    }, {
        if (message.isNullOrBlank()) hideMessage() else setMessage(message)
    })
}