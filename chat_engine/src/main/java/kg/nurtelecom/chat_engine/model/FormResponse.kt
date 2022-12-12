package kg.nurtelecom.chat_engine.model

import java.io.Serializable

data class FormResponse(val values: List<EnteredValue>): Serializable

data class EnteredValue(val fieldId: String, val enteredValue: List<String>?): Serializable