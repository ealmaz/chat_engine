package kg.nurtelecom.chat_engine.model

import java.io.Serializable

data class Validation(val type: ValidationType?, val value: String?): Serializable

enum class ValidationType {
    REQUIRED, REGEX
}