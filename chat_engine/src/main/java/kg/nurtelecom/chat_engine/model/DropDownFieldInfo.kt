package kg.nurtelecom.chat_engine.model

data class DropDownFieldInfo(
    val formItemId: String,
    val childDropDownIds: List<String>? = null,
    val chooseType: ChooseType = ChooseType.SINGLE,
    val label: String? = null,
    val validations: List<Validation>? = null,
    val isNeedToFetchOptions: Boolean? = null,
    val options: List<Option>? = null
)
