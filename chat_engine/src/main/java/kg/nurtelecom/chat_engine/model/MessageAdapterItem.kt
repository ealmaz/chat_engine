package kg.nurtelecom.chat_engine.model

interface MessageAdapterItem {
    fun areItemsTheSame(other: Any): Boolean
    fun areContentTheSame(other: Any): Boolean
}