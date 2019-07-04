package hulkdx.com.data.database.model

import hulkdx.com.domain.data.model.Category
import hulkdx.com.domain.data.model.Transaction
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

internal open class TransactionRealmObject constructor(): RealmObject() {

    @PrimaryKey
    private var id: Long = 0
    private var date: String = ""
    private var category: CategoryRealmObject? = null
    private var amount: Float = 0F
    private var attachment: String? = null

    constructor(id: Long, date: String, category: CategoryRealmObject?, amount: Float, attachment: String?) : this() {
        this.id = id
        this.date = date
        this.category = category
        this.amount = amount
        this.attachment = attachment
    }

    fun mapToTransaction(): Transaction {
        val category: Category? = this.category?.mapToCategory()
        return Transaction(id, date, category, amount, attachment)
    }
}