package kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators

import android.content.Context
import kg.nurtelecom.chat_engine.custom_views.ChatButtonsGroup
import kg.nurtelecom.chat_engine.model.GroupButtonFormItem

object GroupButtonsCreator : ItemCreator() {

    fun create(context: Context, groupInfo: GroupButtonFormItem, onSelectedChanged: (selected: List<String>, isValid: Boolean) -> Unit): ChatButtonsGroup {
        return ChatButtonsGroup(context).apply {
            tag = groupInfo.fieldId
            setSelectedItemChangedListener {
                val isValid = validateItem(groupInfo.validations, it)
                onSelectedChanged(it, isValid)
            }
            groupInfo.buttonType?.let { setButtonType(it) }
            groupInfo.chooseType?.let { setChooseType(it) }
            groupInfo.options?.let { setAllButtons(it) }
            renderButtons()
        }
    }
}