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

    override fun getUser(): User? {
        return mUserDataBaseHelper.getUser()
    }

}