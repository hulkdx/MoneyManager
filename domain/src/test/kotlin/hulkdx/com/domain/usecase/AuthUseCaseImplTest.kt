package hulkdx.com.domain.usecase

import hulkdx.com.domain.capture
import hulkdx.com.domain.data.model.User
import hulkdx.com.domain.data.remote.ApiManager
import hulkdx.com.domain.data.remote.ApiManager.*
import hulkdx.com.domain.data.remote.RegisterAuthErrorStatus
import hulkdx.com.domain.data.remote.RemoteStatus
import hulkdx.com.domain.repository.UserRepository
import hulkdx.com.domain.usecase.AuthUseCase.RegisterResult
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

import org.junit.Assert.*
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import java.io.IOException
import java.lang.RuntimeException

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */

// region constants ----------------------------------------------------------------------------
const val USERNAME      = "username"
const val PASSWORD      = "username"
const val TOKEN         = "token"
      val TEST_USER     = User(USERNAME, "", "", "", "", TOKEN, 0F)
      val EMPTY_USER    = User("", "", "", "", "", "", 0F)
const val THROWABLE_MSG = "THROWABLE_MSG"
const val FIRST_NAME = "first_name"
const val LAST_NAME  = "last_name"
const val EMAIL      = "email"
const val CURRENCY   = "currency"

// endregion constants -------------------------------------------------------------------------

@Suppress("RedundantVisibilityModifier")
class AuthUseCaseImplTest {

    // region helper fields ------------------------------------------------------------------------

    @get:Rule
    public var mMockitoJUnit = MockitoJUnit.rule()!!

    @Mock lateinit var mApiManager: ApiManager
    @Mock lateinit var mUserRepository: UserRepository
    private lateinit var mTestScheduler: Scheduler

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var SUT: AuthUseCaseImpl

    @Before
    fun setup() {
        mTestScheduler = Schedulers.trampoline()
        SUT = AuthUseCaseImpl(mTestScheduler, mTestScheduler, mApiManager, mUserRepository)
    }

    // region loginAsync ---------------------------------------------------------------------------

    @Test
    fun loginAsync_mustPassParamsToApiManager() {
        // Arrange
        loginSuccess()
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mApiManager).login(USERNAME, PASSWORD)
    }

    @Test
    fun loginAsync_success_callTheCallbackSuccess() {
        // Arrange
        loginSuccess()
        // Act
        var result = false
        SUT.loginAsync(USERNAME, PASSWORD) {
            result = it.status == RemoteStatus.SUCCESS
        }
        // Assert
        assertTrue(result)
    }

    @Test
    fun loginAsync_throwsException_callTheCallbackGeneralError() {
        // Arrange
        loginThrowsRuntimeException()
        var result = false
        var throwable: Throwable? = null
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {
            result = it.status == RemoteStatus.GENERAL_ERROR
            throwable = it.throwable
        }
        // Assert
        assertTrue(result)
        assertThat(throwable!!.message, `is`(THROWABLE_MSG))
    }

    @Test
    fun loginAsync_ioException_callTheCallbackNetworkError() {
        // Arrange
        loginThrowsIoException()
        var status: RemoteStatus? = null
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {
            status = it.status
        }
        // Assert
        assertTrue(status == RemoteStatus.NETWORK_ERROR)
    }

    @Test
    fun loginAsync_authError_callTheCallbackAuthError() {
        // Arrange
        loginAuthError()
        var status: RemoteStatus? = null
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {
            status = it.status
        }
        // Assert
        assertTrue(status == RemoteStatus.AUTH_ERROR)
    }

    @Test
    fun loginAsync_generalError_callTheCallbackGeneralError() {
        // Arrange
        loginGeneralError()
        var status: RemoteStatus? = null
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {
            status = it.status
        }
        // Assert
        assertTrue(status == RemoteStatus.GENERAL_ERROR)
    }

    @Test
    fun loginAsync_success_saveUser() {
        // Arrange
        loginSuccess()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mUserRepository).saveCurrentUser(capture(ac))
        val user = ac.value
        assertThat(user.username, `is`(USERNAME))
        assertThat(user.token, `is`(TOKEN))
    }

    @Test
    fun loginAsync_authError_NotSaveUser() {
        // Arrange
        loginAuthError()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mUserRepository, never()).saveCurrentUser(capture(ac))
    }

    @Test
    fun loginAsync_generalError_NotSaveUser() {
        // Arrange
        loginGeneralError()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mUserRepository, never()).saveCurrentUser(capture(ac))
    }

    @Test
    fun loginAsync_throwsRuntimeException_NotSaveUser() {
        // Arrange
        loginGeneralError()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mUserRepository, never()).saveCurrentUser(capture(ac))
    }

    @Test
    fun loginAsync_throwsIoException_NotSaveUser() {
        // Arrange
        loginGeneralError()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mUserRepository, never()).saveCurrentUser(capture(ac))
    }

    // endregion loginAsync ------------------------------------------------------------------------
    // region registerAsync ------------------------------------------------------------------------

    @Test
    fun registerAsync_mustPassParamsToApiManager() {
        // Arrange
        registerSuccess()
        // Act
        SUT.registerAsync(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, EMAIL, CURRENCY) {}
        // Assert
        verify(mApiManager).register(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, EMAIL, CURRENCY)
    }

    @Test
    fun registerAsync_success_callTheCallbackSuccess() {
        // Arrange
        registerSuccess()
        var isSuccess = false
        // Act
        SUT.registerAsync(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, EMAIL, CURRENCY) {
            if (it is RegisterResult.Successful) {
                isSuccess = true
            }
        }
        // Assert
        assertTrue(isSuccess)
    }

    @Test
    fun registerAsync_throwsException_callTheCallbackGeneralError() {
        // Arrange
        registerThrowsRuntimeException()
        var result: RegisterResult? = null
        // Act
        SUT.registerAsync(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, EMAIL, CURRENCY) {
            result = it
        }
        // Assert
        assertTrue(result is RegisterResult.GeneralError)
        assertThat((result as RegisterResult.GeneralError).throwable!!.message, `is`(THROWABLE_MSG))
    }

    @Test
    fun registerAsync_ioException_callTheCallbackNetworkError() {
        // Arrange
        registerThrowsIoException()
        var status: RegisterResult? = null
        // Act
        SUT.registerAsync(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, EMAIL, CURRENCY) {
            status = it
        }
        // Assert
        assertTrue(status is RegisterResult.NetworkError)
    }

    @Test
    fun registerAsync_authErrorEmailExists_passAuthErrorToRegisterResult() {
        // Arrange
        registerAuthErrorEmailExists()
        var result: RegisterResult? = null
        // Act
        SUT.registerAsync(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, EMAIL, CURRENCY) {
            result = it
        }
        // Assert
        assertTrue(result is RegisterResult.AuthError)
        assertTrue((result as RegisterResult.AuthError).status == RegisterAuthErrorStatus.EMAIL_EXISTS)
    }

    @Test
    fun registerAsync_authErrorUserExists_passAuthErrorToRegisterResult() {
        // Arrange
        registerAuthErrorUserExists()
        var result: RegisterResult? = null
        // Act
        SUT.registerAsync(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, EMAIL, CURRENCY) {
            result = it
        }
        // Assert
        assertTrue(result is RegisterResult.AuthError)
        assertTrue((result as RegisterResult.AuthError).status == RegisterAuthErrorStatus.USER_EXISTS)
    }

    // endregion registerAsync ---------------------------------------------------------------------
    // region isLoggedIn ---------------------------------------------------------------------

    @Test
    fun isLoggedIn_callDataSourceManager() {
        // Arrange
        // Act
        SUT.isLoggedIn()
        // Assert
        verify(mUserRepository).getCurrentUser()
    }


    // endregion isLoggedIn -----------------------------------------------------------------------
    // region helper methods -----------------------------------------------------------------------

    private fun loginSuccess() {
        `when`(mApiManager.login(anyString(), anyString()))
                .thenReturn(Single.just(LoginApiResponse(RemoteStatus.SUCCESS, TEST_USER)))
    }

    private fun loginThrowsRuntimeException() {
        `when`(mApiManager.login(anyString(), anyString()))
                .thenReturn(Single.fromCallable { throw RuntimeException(THROWABLE_MSG) })
    }

    private fun loginThrowsIoException() {
        `when`(mApiManager.login(anyString(), anyString()))
                .thenReturn(Single.fromCallable { throw IOException() })
    }

    private fun loginAuthError() {
        `when`(mApiManager.login(anyString(), anyString()))
                .thenReturn(Single.just(LoginApiResponse(RemoteStatus.AUTH_ERROR, EMPTY_USER)))
    }

    private fun loginGeneralError() {
        `when`(mApiManager.login(anyString(), anyString()))
                .thenReturn(Single.just(LoginApiResponse(RemoteStatus.GENERAL_ERROR, EMPTY_USER)))
    }

    private fun registerSuccess() {
        `when`(mApiManager.register(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Single.just(RegisterApiResponse(RemoteStatus.SUCCESS)))
    }

    private fun registerThrowsRuntimeException() {
        `when`(mApiManager.register(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Single.fromCallable { throw RuntimeException(THROWABLE_MSG) })
    }

    private fun registerThrowsIoException() {
        `when`(mApiManager.register(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Single.fromCallable { throw IOException() })
    }

    private fun registerAuthErrorEmailExists() {
        `when`(mApiManager.register(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Single.just(RegisterApiResponse(RemoteStatus.AUTH_ERROR, RegisterAuthErrorStatus.EMAIL_EXISTS)))
    }

    private fun registerAuthErrorUserExists() {
        `when`(mApiManager.register(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Single.just(RegisterApiResponse(RemoteStatus.AUTH_ERROR, RegisterAuthErrorStatus.USER_EXISTS)))
    }

    // endregion helper methods --------------------------------------------------------------------

}