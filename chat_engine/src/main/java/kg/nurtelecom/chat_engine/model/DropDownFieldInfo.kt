package kg.nurtelecom.chat_engine.model

data class DropDownFieldInfo(
    val formItemId: String,
    val childDropDownIds: List<String>,
    val chooseType: ChooseType,
    val label: String,
    val validations: List<Validation>?,
    val isNeedToFetchOptions: Boolean,
    val options: List<Option>
)
