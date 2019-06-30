package hulkdx.com.domain

import hulkdx.com.domain.data.model.Category
import hulkdx.com.domain.data.model.Transaction

/**
 * Created by Mohammad Jafarzadeh Rezvan on 30/06/2019.
 */

val CATEGORY_1 = Category(1, "1", "#4287f5")
val TRANSACTION_1 = Transaction(1, "2018-09-29", CATEGORY_1, -214F, null)
val TRANSACTION_LIST = listOf(TRANSACTION_1)