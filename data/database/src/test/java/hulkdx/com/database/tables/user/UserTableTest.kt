package hulkdx.com.database.tables.user

import hulkdx.com.database.*
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
@RunWith(JUnit4::class)
class UserTableTest {

    @Rule @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    private lateinit var mUserTable: UserTable
    @Mock   lateinit var mRealm: Realm

    @Before
    fun setUp() {
        this.mUserTable = UserTable(mRealm)
    }

    @Test
    fun testGetUserReturnValue() {
        val testUser = RANDOM_USER_REALMOBJECT
        stub_realm(testUser)
        val user = mUserTable.getUser()
        assertTrue(user != null)
        assertEquals(user!!.firstName, testUser.firstName)
        assertEquals(user.lastName, testUser.lastName)
        assertEquals(user.currency, testUser.currency)
        assertEquals(user.email, testUser.email)
        assertEquals(user.token, testUser.token)
        assertEquals(user.token, testUser.token)
    }

    @Test
    fun testGetUserNull() {
        stub_realm_null(UserRealmObject::class.java)
        val user = mUserTable.getUser()
        assertTrue(user == null)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T: RealmModel> stub_realm(testUser: T) {
        val query = mock(RealmQuery::class.java) as RealmQuery<T>

        `when`(mRealm.where(testUser.javaClass)).thenReturn(query)

        `when`(query.findFirst()).thenReturn(testUser)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T: RealmModel> stub_realm_null(clazz: Class<T>) {
        val query = mock(RealmQuery::class.java) as RealmQuery<T>
        `when`(mRealm.where(clazz)).thenReturn(query)

        `when`(query.findFirst()).thenReturn(null)
    }

}