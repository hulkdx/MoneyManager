package hulkdx.com.data.datasource

import hulkdx.com.domain.models.User

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
interface IDataBase {
    fun getUser(): User?
}