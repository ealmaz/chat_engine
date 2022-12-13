package kg.nurtelecom.chat_engine.model

import java.io.Serializable

data class FormItem(
    val formItemType: FormItemType? = null,
    val formItem: Any? = null
): Serializable

enum class FormItemType {
    INPUT_FIELD, GROUP_BUTTON_FORM_ITEM, DROP_DOWN_FORM_ITEM,
}
