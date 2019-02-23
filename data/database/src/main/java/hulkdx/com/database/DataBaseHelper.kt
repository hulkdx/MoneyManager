package hulkdx.com.database

import hulkdx.com.data.datasource.IDataBase
import hulkdx.com.domain.models.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
@Singleton
class DataBaseHelper @Inject constructor(): IDataBase {
    override fun getUser(): User? {
        return null
    }
}