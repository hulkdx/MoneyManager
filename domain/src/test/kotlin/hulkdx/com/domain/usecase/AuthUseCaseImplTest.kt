package hulkdx.com.domain.usecase

import hulkdx.com.domain.data.database.DatabaseManager
import hulkdx.com.domain.data.model.User
import hulkdx.com.domain.data.remote.ApiManager
import hulkdx.com.domain.data.remote.RemoteStatus
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
import org.mockito.ArgumentMatchers
import java.io.IOException
import java.lang.RuntimeException

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */

// region constants ----------------------------------------------------------------------------
const val USERNAME      = "username"
const val PASSWORD      = "username"
const val TOKEN         = "token"
      val TEST_USER     = User(USERNAME, "", "", "", "", TOKEN)
      val EMPTY_USER    = User("", "", "", "", "", "")
const val THROWABLE_MSG = "THROWABLE_MSG"
// endregion constants -------------------------------------------------------------------------

@Suppress("RedundantVisibilityModifier")
class AuthUseCaseImplTest {

    // region helper fields ------------------------------------------------------------------------

    @get:Rule
    public var mMockitoJUnit = MockitoJUnit.rule()!!

    @Mock lateinit var mApiManager: ApiManager
    @Mock lateinit var mDatabaseManager: DatabaseManager
    private lateinit var mTestScheduler: Scheduler

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var SUT: AuthUseCaseImpl

    @Before
    fun setup() {
        mTestScheduler = Schedulers.trampoline()
        SUT = AuthUseCaseImpl(mTestScheduler, mTestScheduler, mDatabaseManager, mApiManager)
    }

    @Test
    fun loginAsync_mustPassParamsToApiManager() {
        // Arrange
        loginSuccess()
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mApiManager).loginSync(USERNAME, PASSWORD)
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
    fun loginAsync_success_saveUserToDatabase() {
        // Arrange
        loginSuccess()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mDatabaseManager).saveUser(capture(ac))
        val user = ac.value
        assertThat(user.username, `is`(USERNAME))
        assertThat(user.token, `is`(TOKEN))
    }

    @Test
    fun loginAsync_authError_NotSaveUserToDatabase() {
        // Arrange
        loginAuthError()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mDatabaseManager, never()).saveUser(capture(ac))
    }

    @Test
    fun loginAsync_generalError_NotSaveUserToDatabase() {
        // Arrange
        loginGeneralError()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mDatabaseManager, never()).saveUser(capture(ac))
    }

    @Test
    fun loginAsync_throwsRuntimeException_NotSaveUserToDatabase() {
        // Arrange
        loginGeneralError()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mDatabaseManager, never()).saveUser(capture(ac))
    }

    @Test
    fun loginAsync_throwsIoException_NotSaveUserToDatabase() {
        // Arrange
        loginGeneralError()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mDatabaseManager, never()).saveUser(capture(ac))
    }

    @Test
    fun registerAsync_mustPassParamsToApiManager() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // region helper methods -----------------------------------------------------------------------

    private fun loginSuccess() {
        `when`(mApiManager.loginSync(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.just(ApiManager.LoginApiResponse(RemoteStatus.SUCCESS, TEST_USER)))
    }

    private fun loginThrowsRuntimeException() {
        `when`(mApiManager.loginSync(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.fromCallable { throw RuntimeException(THROWABLE_MSG) })
    }

    private fun loginThrowsIoException() {
        `when`(mApiManager.loginSync(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.fromCallable { throw IOException() })
    }

    private fun loginAuthError() {
        `when`(mApiManager.loginSync(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.just(ApiManager.LoginApiResponse(RemoteStatus.AUTH_ERROR, EMPTY_USER)))
    }

    private fun loginGeneralError() {
        `when`(mApiManager.loginSync(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.just(ApiManager.LoginApiResponse(RemoteStatus.GENERAL_ERROR, EMPTY_USER)))
    }

    // endregion helper methods --------------------------------------------------------------------

}

fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()