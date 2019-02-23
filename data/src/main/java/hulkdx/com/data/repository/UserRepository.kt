package hulkdx.com.data.repository

import hulkdx.com.data.datasource.IDataBase
import hulkdx.com.domain.models.User
import hulkdx.com.domain.repository.IUserRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
@Singleton
class UserRepository @Inject constructor(private val mDataBase: IDataBase): IUserRepository {

    override fun getUser(): User? {
        return mDataBase.getUser()
    }
}
