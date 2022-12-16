package kg.nurtelecom.chat_engine.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators.DatePickerFieldCreator
import kg.nurtelecom.chat_engine.databinding.ChatEngineFormItemDatePickerBinding
import kg.nurtelecom.chat_engine.model.DatePickerFieldInfo
import java.text.SimpleDateFormat
import java.util.*

class DatePickerInputField @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : LinearLayout(context, attributeSet) {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    private var onNewValueListener: ((List<String>, Boolean) -> Unit)? = null

    private var datePickerFieldInfo: DatePickerFieldInfo? = null

    private val vb: ChatEngineFormItemDatePickerBinding = ChatEngineFormItemDatePickerBinding.inflate(LayoutInflater.from(context), this, true)

    fun setupViews(datePickerFieldInfo: DatePickerFieldInfo, onSetNewValue: (List<String> , Boolean) -> Unit) {
        setDate(datePickerFieldInfo.value)
        datePickerFieldInfo.hint?.let { setHelperText(it) }
        datePickerFieldInfo.placeHolder?.let { setHint(it) }
        datePickerFieldInfo.label?.let { setLabel(it) }
        this.onNewValueListener = onSetNewValue
    }

    fun setHint(hint: String) {
        vb.tvLabel.apply {
            text = hint
            setTextColor(ContextCompat.getColor(context, com.design.chili.R.color.gray_1_alpha_50))
        }
    }

    private fun setText(text: String) {
        if (text.isBlank()) return
        vb.tvLabel.apply {
            this.text = text
            setTextColor(ContextCompat.getColor(context,
                R.color.chat_engine_drop_down_item_text_color))
        }
    }

    fun setDate(dateLong: Long?) {
        val values = dateLong?.toString()?.let { listOf(it) } ?: emptyList()
        val isValid = DatePickerFieldCreator.validateItem(datePickerFieldInfo?.validations, values)
        if (dateLong == null) {
            setText("")
        } else {
            setText(dateFormat.format(Date(dateLong)))
        }
        onNewValueListener?.invoke(values, isValid)
    }

    fun setLabel(label: String) {
        setHint(label)
    }

    fun setHelperText(helperText: String) {
        vb.tvHelper.apply {
            visibility = VISIBLE
            text = helperText
        }
    }
}