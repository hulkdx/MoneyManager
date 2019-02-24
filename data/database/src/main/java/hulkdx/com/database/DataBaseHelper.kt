package hulkdx.com.database

import hulkdx.com.database.tables.user.UserTable
import hulkdx.com.domain.models.User
import hulkdx.com.repository.datasource.IDataBase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
@Singleton
class DatabaseHelper @Inject constructor(private val mUserDataBaseHelper: UserTable): IDataBase {
    override fun setUser(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUser(): User? {
        return mUserDataBaseHelper.getUser()
    }

}