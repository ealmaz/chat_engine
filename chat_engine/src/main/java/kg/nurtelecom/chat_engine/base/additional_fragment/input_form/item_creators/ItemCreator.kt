package kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators

import kg.nurtelecom.chat_engine.model.Validation
import kg.nurtelecom.chat_engine.model.ValidationType

open class ItemCreator {

    fun validateItem(validations: List<Validation>?, values: List<String>): Boolean {
        validations?.forEach {
            when (it.type) {
                ValidationType.REQUIRED -> {
                    if (it.value == "true" && (values.isEmpty() || values.firstOrNull().isNullOrBlank())) {
                        return false
                    }
                }
                ValidationType.REGEX -> {
                    if (it.value != null && (!(values.firstOrNull() ?: "").matches(it.value.toRegex()))) {
                        return false
                    }
                }
            }
        }
        return true
    }
}