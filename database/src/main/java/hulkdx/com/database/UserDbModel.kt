package hulkdx.com.database

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 *
 * Since we only have one user the id is hardcoded to 0
 */
internal open class UserRealmObject: RealmObject() {
    @PrimaryKey var id: Int = 0
    var userName: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var token: String? = null
    var currency: String? = null
}
