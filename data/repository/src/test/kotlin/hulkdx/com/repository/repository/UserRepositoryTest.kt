package hulkdx.com.repository.repository

import hulkdx.com.domain.models.User
import hulkdx.com.repository.datasource.IDataBase
import hulkdx.com.repository.datasource.IMemoryCache
import io.reactivex.Maybe
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
@RunWith(JUnit4::class)
class UserRepositoryTest {

    @Rule @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    private lateinit var mUserRepository: UserRepository
    @Mock   lateinit var mDatabase: IDataBase
    @Mock   lateinit var mMemoryCache: IMemoryCache

    @Before
    fun setUp() {
        this.mUserRepository = UserRepository(mDatabase, mMemoryCache)
    }

    @Test
    fun `get user should call memory cache first`() {
        this.mUserRepository.getUser()

        verify(this.mMemoryCache).getUser()
    }

    @Test
    fun `get user should call database if cache is empty`() {
        `when`(mMemoryCache.getUser()).thenReturn(null)

        this.mUserRepository.getUser()

        verify(this.mDatabase).getUser()
    }

    @Test
    fun `get user should not call database if cache is not empty`() {
        `when`(mMemoryCache.getUser()).thenReturn(RANDOM_USER)

        this.mUserRepository.getUser()

        verify(this.mDatabase, never()).getUser()
    }

    @Test
    fun `get user should cache after db calls`() {
        `when`(mMemoryCache.getUser()).thenReturn(null)
        `when`(mDatabase.getUser()).thenReturn(RANDOM_USER)

        this.mUserRepository.getUser()

        verify(this.mMemoryCache).setUser(RANDOM_USER)
    }

}