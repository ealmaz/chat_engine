package kg.nurtelecom.chat_engine.model

data class Validation(val type: ValidationType?, val value: String?)

enum class ValidationType {
    REQUIRED, REGEX
}