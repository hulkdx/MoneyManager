package hulkdx.com.domain.models

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
data class User(
        val userName: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val token: String,
        val currency: String
)