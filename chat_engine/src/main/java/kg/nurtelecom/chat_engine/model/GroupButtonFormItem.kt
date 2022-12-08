package kg.nurtelecom.chat_engine.model

import java.io.Serializable

data class GroupButtonFormItem(
    val formItemId: String,
    val options: List<Option>,
    val chooseType: ChooseType,
    val buttonType: ButtonType,
    val validations: List<Validation>?
): Serializable

enum class ChooseType {
    MULTIPLE, SINGLE
}

enum class ButtonType {
    CHEK_BOX, TOGGLE, RADIO_BUTTON
}

data class Option(val id: String, val value: String, var isSelected: Boolean): Serializable
