package hulkdx.com.data.database.model

import hulkdx.com.domain.data.model.User
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 *
 * Since we only have one user the id is hardcoded to 0
 */
internal open class UserRealmObject constructor(): RealmObject() {

    @PrimaryKey
    var id: Int = 0
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

    fun mapToUser(): User {
        return User(userName, firstName, lastName, email, currency, token)
    }
}
