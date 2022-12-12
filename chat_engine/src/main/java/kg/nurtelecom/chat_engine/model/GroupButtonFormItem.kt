package kg.nurtelecom.chat_engine.model

import java.io.Serializable

data class GroupButtonFormItem(
    val formItemId: String,
    val options: List<Option>? = null,
    val chooseType: ChooseType? = ChooseType.MULTIPLE,
    val buttonType: ButtonType? = ButtonType.CHECK_BOX,
    val validations: List<Validation>? = null
): Serializable

enum class ChooseType {
    MULTIPLE, SINGLE
}

enum class ButtonType {
    CHECK_BOX, TOGGLE, RADIO_BUTTON
}

data class Option(val id: String, val value: String, var isSelected: Boolean): Serializable
