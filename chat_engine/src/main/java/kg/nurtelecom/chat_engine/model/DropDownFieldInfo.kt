package kg.nurtelecom.chat_engine.model

import java.io.Serializable

data class DropDownFieldInfo(
    val fieldId: String,
    val parentFieldId: String? = null,
    val chooseType: ChooseType? = ChooseType.SINGLE,
    val label: String? = null,
    val validations: List<Validation>? = null,
    val isNeedToFetchOptions: Boolean? = null,
    val options: List<Option>? = null
): Serializable
