package kg.nurtelecom.chat_engine.model

import java.io.Serializable

data class InputForm(
    val formId: String,
    val title: String,
    val openButton: ChatButton,
    val formItems: List<FormItem>
): Serializable
