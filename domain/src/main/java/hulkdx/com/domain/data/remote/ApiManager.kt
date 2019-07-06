package hulkdx.com.domain.data.remote

import hulkdx.com.domain.data.model.Transaction
import hulkdx.com.domain.data.model.User
import io.reactivex.Single
import java.io.IOException

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
interface ApiManager {
    @Throws(IOException::class)
    fun login(username: String, password: String): Single<LoginApiResponse>
    @Throws(IOException::class)
    fun register(firstName: String,
                 lastName: String,
                 username: String,
                 password: String,
                 email:    String,
                 currency: String): Single<RegisterApiResponse>

    @Throws(IOException::class)
    fun getTransactions(token: String): TransactionApiResponse<GetTransactionApiResponse>
    @Throws(IOException::class)
    fun deleteTransactions(token: String, id: List<Long>): TransactionApiResponse<DeleteTransactionApiResponse>

    data class LoginApiResponse(
            val status: RemoteStatus,
            val user: User
    )

    data class RegisterApiResponse(
            val status: RemoteStatus,
            val authError: RegisterAuthErrorStatus? = null
    )

    sealed class TransactionApiResponse<out T> {
        class Success<T>(val data: T): TransactionApiResponse<T>()
        object GeneralError: TransactionApiResponse<Nothing>()
        object AuthWrongToken: TransactionApiResponse<Nothing>()
    }

    data class GetTransactionApiResponse (
        val transactions: List<Transaction>,
        val totalAmount: Float
    )

    data class DeleteTransactionApiResponse (
            val totalAmount: Float
    )
}