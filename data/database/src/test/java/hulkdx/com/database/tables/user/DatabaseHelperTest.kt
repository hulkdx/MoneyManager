package hulkdx.com.database.tables.user

import hulkdx.com.database.DatabaseHelper
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
@RunWith(JUnit4::class)
class DatabaseHelperTest {

    @Rule @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

          lateinit var mDatabaseHelper: DatabaseHelper
    @Mock lateinit var mUserTable: UserTable

    @Before
    fun setUp() {
        this.mDatabaseHelper = DatabaseHelper(mUserTable)
    }

    @Test
    fun testGetUser() {
        this.mDatabaseHelper.getUser()

        verify(this.mUserTable).getUser()
    }


}