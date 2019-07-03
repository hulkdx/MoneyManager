package hulkdx.com.domain.data.remote

import hulkdx.com.domain.data.model.Transaction
import hulkdx.com.domain.data.model.User
import io.reactivex.Single

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface ApiManager {
    fun login(username: String, password: String): Single<LoginApiResponse>
    fun register(firstName: String,
                 lastName: String,
                 username: String,
                 password: String,
                 email:    String,
                 currency: String): Single<RegisterApiResponse>

    fun getTransactions(token: String): Single<TransactionApiResponse>

    data class LoginApiResponse(
            val status: RemoteStatus,
            val user: User
    )

    data class RegisterApiResponse(
            val status: RemoteStatus,
            val authError: RegisterAuthErrorStatus? = null
    )

    sealed class TransactionApiResponse {
        class Success(val transactions: List<Transaction>, val totalAmount: Float): TransactionApiResponse()
        object GeneralError: TransactionApiResponse()
        object AuthWrongToken: TransactionApiResponse()
    }
}