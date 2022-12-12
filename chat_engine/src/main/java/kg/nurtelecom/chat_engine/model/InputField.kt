package kg.nurtelecom.chat_engine.model

data class InputField(
    val fieldId: String,
    var value: String? = null,
    val hint: String? = null,
    val placeholder: String? = null,
    val label: String? = null,
    val inputType: InputFieldInputType? = null,
    val mask: String? = null,
    val validations: List<Validation>? = null
)

enum class InputFieldInputType {
    TEXT, NUMBER
}
