package kg.nurtelecom.chat_engine.base.additional_fragment

import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.design.chili.view.input.MaskedInputView
import com.design.chili.R
import kg.nurtelecom.chat_engine.custom_views.ChatButtonsGroup
import kg.nurtelecom.chat_engine.model.*
import java.lang.IllegalArgumentException

class FormItemsAdapter(private val listener: InputFormResultHandler) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<FormItem> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FormItemType.INPUT_FIELD.ordinal -> InputFieldFormItemVH.create(parent, listener)
            FormItemType.GROUP_BUTTON_FORM_ITEM.ordinal -> GroupButtonFormItemVH.create(parent, listener)
            FormItemType.DROP_DOWN_FORM_ITEM.ordinal -> TODO("Not yet implemented")
            else -> throw IllegalArgumentException("Unknown view type for $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is InputFieldFormItemVH -> holder.onBind(items[position])
            is GroupButtonFormItemVH -> holder.onBind(items[position])
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position].formItemType) {
            FormItemType.INPUT_FIELD -> FormItemType.INPUT_FIELD.ordinal
            FormItemType.DROP_DOWN_FORM_ITEM -> FormItemType.DROP_DOWN_FORM_ITEM.ordinal
            FormItemType.GROUP_BUTTON_FORM_ITEM -> FormItemType.GROUP_BUTTON_FORM_ITEM.ordinal
        }
    }
}

abstract class BaseFormItemVH(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun onBind(item: FormItem)
}

class InputFieldFormItemVH(private val field: MaskedInputView) : BaseFormItemVH(field) {

    private var inputFieldFormItem: InputField? = null

    override fun onBind(item: FormItem) {
        (item.forItem as? InputField)?.let {
            inputFieldFormItem = it
            setupFiled(it)
        }
    }

    private fun setupFiled(fieldData: InputField) = with(field) {
        tag = fieldData.fieldId
        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(
                0,
                resources.getDimensionPixelSize(R.dimen.padding_4dp),
                0,
                resources.getDimensionPixelSize(R.dimen.padding_4dp)
            )
        }
        setGravity(Gravity.START)
        setHint((fieldData.hint ?: ""))
        setText(fieldData.value ?: "")
        setupNewMask(fieldData.mask ?: "*")
        when (fieldData.inputType) {
            InputFieldInputType.NUMBER -> setInputType(InputType.TYPE_CLASS_NUMBER)
            else -> setInputType(InputType.TYPE_CLASS_TEXT)
        }
        setupClearTextButton()
    }

    fun validateItemResult(result: String?): Boolean {
        return inputFieldFormItem?.let {
            when {
                (!(it.mask.isNullOrBlank()) && it.mask != "*") -> field.isInputMaskFilled()
                !(it.regex.isNullOrBlank()) -> result?.matches(it.regex.toRegex()) ?: false
                else -> true
            }
        } ?: false
    }

    companion object {
        fun create(parent: ViewGroup, listener: InputFormResultHandler): InputFieldFormItemVH {
            return InputFieldFormItemVH(MaskedInputView(parent.context)).apply {
                field.setSimpleTextChangedListener {
                    inputFieldFormItem?.value = it
                    listener.onFieldTextChanged(inputFieldFormItem?.fieldId, it, validateItemResult(it))
                }
            }
        }
    }
}

class GroupButtonFormItemVH(private val groupButton: ChatButtonsGroup): BaseFormItemVH(groupButton) {

    private var groupButtonInfo: GroupButtonFormItem? = null

    override fun onBind(item: FormItem) {
        (item.forItem as? GroupButtonFormItem)?.let {
            groupButtonInfo = it
            setupGroupButtons(it)
        }
    }

    private fun setupGroupButtons(groupInfo: GroupButtonFormItem) = with(groupButton) {
        tag = groupInfo.formItemId
        setButtonType(groupInfo.buttonType)
        setChooseType(groupInfo.chooseType)
        setAllButtons(groupInfo.options)
        renderButtons()
    }

    fun validateItemResult(result: List<String>): Boolean {
        return groupButtonInfo?.let {
            if (it.isRequiredItem) result.isNotEmpty()
            else true
        } ?: false
    }

    companion object {
        fun create(parent: ViewGroup, resultHandler: InputFormResultHandler): GroupButtonFormItemVH {
            return GroupButtonFormItemVH(ChatButtonsGroup(parent.context)).apply {
                groupButton.setSelectedItemChangedListener {
                    resultHandler.onGroupButtonSelected(groupButtonInfo?.formItemId, it, validateItemResult(it))
                }
            }
        }
    }
}

interface InputFormResultHandler {
    fun onFieldTextChanged(fieldTag: String?, fieldValue: String?, isValid: Boolean?)
    fun onGroupButtonSelected(groupTag: String?, selectedValuesId: List<String>?, isValid: Boolean?)
}