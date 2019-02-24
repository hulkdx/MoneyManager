package hulkdx.com.domain.interactor

import hulkdx.com.domain.RANDOM_USER
import hulkdx.com.domain.interactor.AuthUseCase
import hulkdx.com.domain.repository.IUserRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/02/2019.
 */
@RunWith(JUnit4::class)
class AuthUseCaseTest {

    @Rule @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

          lateinit var authUseCase: AuthUseCase
    @Mock lateinit var mUserRepository: IUserRepository

    @Before
    fun setUp() {
        authUseCase = AuthUseCase(mUserRepository)
    }

    @Test
    fun isUserLoggedInSyncCallGetUser() {
        authUseCase.isLoggedIn()

        verify(mUserRepository).getUser()
    }

    @Test
    fun isUserLoggedInSyncReturnsFalseUponNull() {
        `when`(mUserRepository.getUser()).thenReturn(null)
        val result = authUseCase.isLoggedIn()
        assertFalse(result)
    }

    @Test
    fun isUserLoggedInSyncReturnsTrueUponValidUser() {
        `when`(mUserRepository.getUser()).thenReturn(RANDOM_USER)
        val result = authUseCase.isLoggedIn()
        assertTrue(result)
    }

}