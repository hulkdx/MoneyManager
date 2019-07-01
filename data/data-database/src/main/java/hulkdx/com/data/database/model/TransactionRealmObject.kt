package hulkdx.com.data.database.model

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
}