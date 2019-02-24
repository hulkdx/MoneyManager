package hulkdx.com.database.tables.user

import hulkdx.com.domain.models.User
import io.realm.Realm
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
@Singleton
class UserTable @Inject constructor(private val mRealm: Realm) {

    fun getUser(): User? {
        val userModel = mRealm.where(UserRealmObject::class.java).findFirst()
        return userModel?.map()
    }

}

