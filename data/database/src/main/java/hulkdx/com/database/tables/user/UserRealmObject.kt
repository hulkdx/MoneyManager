package hulkdx.com.database.tables.user

import hulkdx.com.domain.models.User
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 *
 * Since we only have one user the id is hardcoded to 0
 */
internal open class UserRealmObject constructor(): RealmObject() {

    @PrimaryKey var id: Int = 0
    var userName: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var token: String = ""
    var currency: String = ""

    constructor(userName: String, firstName: String, lastName: String, email: String, token: String, currency: String) : this() {
        this.userName = userName
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.token = token
        this.currency = currency
    }


    fun map(): User? {
        return User(userName, firstName, lastName, email, token, currency)
    }
}
