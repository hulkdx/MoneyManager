package hulkdx.com.data.database.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

internal open class CategoryRealmObject constructor(): RealmObject() {

    @PrimaryKey
    private var id: Long = 0
    private var name: String = ""
    private var hexColor: String = ""

    constructor(id: Long, name: String, hexColor: String) : this() {
        this.id = id
        this.name = name
        this.hexColor = hexColor
    }
}