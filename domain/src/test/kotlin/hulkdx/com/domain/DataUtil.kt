package hulkdx.com.domain

import hulkdx.com.domain.data.model.Category
import hulkdx.com.domain.data.model.Transaction

/**
 * Created by Mohammad Jafarzadeh Rezvan on 30/06/2019.
 */

val TEST_CATEGORY_1 = Category(1, "Category1", "#4287f5")
val TEST_CATEGORY_2 = Category(2, "Category2", "#4287f5")

val TEST_TRANSACTION_1 = Transaction(1, "2018-09-29", TEST_CATEGORY_1, 11F, null)
val TEST_TRANSACTION_2 = Transaction(2, "2018-09-30", null, -12F, null)
val TEST_TRANSACTION_3 = Transaction(3, "2018-10-01", TEST_CATEGORY_2, 13F, null)
val TEST_TRANSACTION_4 = Transaction(4, "2018-10-02", TEST_CATEGORY_2, -14F, null)

val TEST_TRANSACTION_LIST = listOf(
        TEST_TRANSACTION_1,
        TEST_TRANSACTION_2,
        TEST_TRANSACTION_3,
        TEST_TRANSACTION_4
)