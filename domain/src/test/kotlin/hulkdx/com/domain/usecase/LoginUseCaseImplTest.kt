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
        var throwable: Throwable? = null
        // Act
        SUT.loginAsync(USERNAME, PASSWORD) {
            // Assert
            assertTrue(it.status == RemoteStatus.NETWORK_ERROR)
        }
    }

    // region helper methods -----------------------------------------------------------------------

    private fun success() {
        `when`(mApiManager.loginSync(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.just(ApiManager.LoginApiResponse(RemoteStatus.SUCCESS, User("", "", "", "", "", TOKEN))))
    }

    private fun throwsRuntimeException() {
        `when`(mApiManager.loginSync(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.fromCallable { throw RuntimeException(THROWABLE_MSG) })
    }

    private fun throwsIoException() {
        `when`(mApiManager.loginSync(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Single.fromCallable { throw IOException() })
    }

    // endregion helper methods --------------------------------------------------------------------

}