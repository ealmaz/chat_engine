package kg.nurtelecom.chat_engine.model

import java.io.Serializable

data class TableMessage(
    val id: String,
    val messageType: MessageType,
    val rows: List<TableRow>? = null
): MessageAdapterItem, Serializable {

    override fun getItemId(): String {
        return hashCode().toString()
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return (other as? TableMessage) == this
    }

    override fun areContentTheSame(other: Any): Boolean {
        return (other as? TableMessage) == this
    }
}

data class TableRow(
    val orientation: TableOrientation? = null,
    val items: List<TableItem>? = null
): Serializable

data class TableItem(
    val price: String? = null,
    val icon: String? = null,
    val title: String? = null
): Serializable

enum class TableOrientation {
    VERTICAL, HORIZONTAL
}
