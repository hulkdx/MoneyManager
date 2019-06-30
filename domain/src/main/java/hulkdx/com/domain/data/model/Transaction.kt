package hulkdx.com.domain.data.model

/**
 * Created by Mohammad Jafarzadeh Rezvan on 30/06/2019.
 */
data class Transaction(
        val id: Long,
        val date: String,
        val category: Category?,
        val amount: Float,
        val attachment: String?
)