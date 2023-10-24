package kg.nurtelecom.chat_engine.model

import java.io.Serializable

data class InputField(
    val fieldId: String,
    var value: String? = null,
    val hint: String? = null,
    val placeholder: String? = null,
    val label: String? = null,
    val inputType: InputFieldInputType? = null,
    val mask: String? = null,
    val maskSymbols: List<String>? = null,
    val validations: List<Validation>? = null
): Serializable

enum class InputFieldInputType {
    TEXT, NUMBER, TEXT_ALL_CAPS
}
