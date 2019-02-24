package hulkdx.com.repository.repository

import hulkdx.com.repository.datasource.IDataBase
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
@RunWith(JUnit4::class)
class UserRepositoryTest {

    @Rule @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

          lateinit var mUserRepository: UserRepository
    @Mock lateinit var mDatabase: IDataBase

    @Before
    fun setUp() {
        this.mUserRepository = UserRepository(mDatabase)
    }

    @Test
    fun veriyGetUser() {
        this.mUserRepository.getUser()

        verify(this.mDatabase).getUser()
    }


}