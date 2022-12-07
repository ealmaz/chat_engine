package kg.nurtelecom.chat_engine.model

interface MessageAdapterItem {
    fun getItemId(): String
    fun areItemsTheSame(other: Any): Boolean
    fun areContentTheSame(other: Any): Boolean
}