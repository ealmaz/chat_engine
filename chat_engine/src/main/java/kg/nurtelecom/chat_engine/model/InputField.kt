package kg.nurtelecom.chat_engine.model

data class InputField(
    val fieldId: String,
    var value: String?,
    val hint: String?,
    val inputType: InputFieldInputType?,
    val mask: String?,
    val validations: List<Validation>?
)

enum class InputFieldInputType {
    TEXT, NUMBER
}
