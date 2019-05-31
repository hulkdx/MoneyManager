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
class LoginUseCaseImplTest {

    // region helper fields ------------------------------------------------------------------------

    @get:Rule
    public var mMockitoJUnit = MockitoJUnit.rule()!!

    @Mock lateinit var mApiManager: ApiManager
    @Mock lateinit var mDatabaseManager: DatabaseManager
    private lateinit var mTestScheduler: Scheduler

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var SUT: LoginUseCaseImpl

    @Before
    fun setup() {
        mTestScheduler = Schedulers.trampoline()
        SUT = LoginUseCaseImpl(mTestScheduler, mTestScheduler, mDatabaseManager, mApiManager)
    }

    @Test
    fun loginAsync_mustPassParamsToApiManager() {
        // Arrange
        success()
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mApiManager).loginSync(USERNAME, PASSWORD)
    }

    @Test
    fun loginAsync_success_callTheCallbackSuccess() {
        // Arrange
        success()
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
        throwsRuntimeException()
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
        throwsIoException()
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
        authError()
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
        generalError()
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
        success()
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
        authError()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mDatabaseManager, never()).saveUser(capture(ac))
    }

    @Test
    fun loginAsync_generalError_NotSaveUserToDatabase() {
        // Arrange
        generalError()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mDatabaseManager, never()).saveUser(capture(ac))
    }

    @Test
    fun loginAsync_throwsRuntimeException_NotSaveUserToDatabase() {
        // Arrange
        generalError()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mDatabaseManager, never()).saveUser(capture(ac))
    }

    @Test
    fun loginAsync_throwsIoException_NotSaveUserToDatabase() {
        // Arrange
        generalError()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {}
        // Assert
        verify(mDatabaseManager, never()).saveUser(capture(ac))
    }

    // region helper methods -----------------------------------------------------------------------

    private fun success() {
        `when`(mApiManager.loginSync(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.just(ApiManager.LoginApiResponse(RemoteStatus.SUCCESS, TEST_USER)))
    }

    private fun throwsRuntimeException() {
        `when`(mApiManager.loginSync(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.fromCallable { throw RuntimeException(THROWABLE_MSG) })
    }

    private fun throwsIoException() {
        `when`(mApiManager.loginSync(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.fromCallable { throw IOException() })
    }

    private fun authError() {
        `when`(mApiManager.loginSync(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.just(ApiManager.LoginApiResponse(RemoteStatus.AUTH_ERROR, EMPTY_USER)))
    }

    private fun generalError() {
        `when`(mApiManager.loginSync(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.just(ApiManager.LoginApiResponse(RemoteStatus.GENERAL_ERROR, EMPTY_USER)))
    }

    // endregion helper methods --------------------------------------------------------------------

}

fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()