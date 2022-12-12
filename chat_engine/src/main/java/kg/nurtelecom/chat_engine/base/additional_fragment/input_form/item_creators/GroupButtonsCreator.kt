package kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators

import android.content.Context
import kg.nurtelecom.chat_engine.custom_views.ChatButtonsGroup
import kg.nurtelecom.chat_engine.model.GroupButtonFormItem

object GroupButtonsCreator : ItemCreator() {

    fun create(context: Context, groupInfo: GroupButtonFormItem, onSelectedChanged: (selected: List<String>, isValid: Boolean) -> Unit): ChatButtonsGroup {
        return ChatButtonsGroup(context).apply {
            tag = groupInfo.formItemId
            setSelectedItemChangedListener {
                val isValid = validateItem(groupInfo.validations, it)
                onSelectedChanged(it, isValid)
            }
            setButtonType(groupInfo.buttonType)
            setChooseType(groupInfo.chooseType)
            setAllButtons(groupInfo.options)
            renderButtons()
        }
    }
}