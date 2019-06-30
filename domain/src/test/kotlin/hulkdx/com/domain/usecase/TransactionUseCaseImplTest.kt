package hulkdx.com.domain.usecase

import hulkdx.com.domain.TRANSACTION_LIST
import hulkdx.com.domain.data.local.CacheManager
import hulkdx.com.domain.data.local.DatabaseManager
import hulkdx.com.domain.data.manager.DataSourceManager
import hulkdx.com.domain.data.model.Transaction
import hulkdx.com.domain.data.model.User
import hulkdx.com.domain.data.remote.ApiManager
import hulkdx.com.domain.data.remote.RemoteStatus
import hulkdx.com.domain.usecase.TransactionUseCase.TransactionResult
import hulkdx.com.domain.usecase.TransactionUseCase.TransactionResult.AuthenticationError
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.ArgumentCaptor
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

import org.junit.Assert.*
import org.mockito.Mockito.*
import org.hamcrest.CoreMatchers.*
import org.mockito.ArgumentMatchers.*
import org.mockito.ArgumentMatchers.*

/**
 * Created by Mohammad Jafarzadeh Rezvan on 30/06/2019.
 */
@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
class TransactionUseCaseImplTest {

    // region constants ----------------------------------------------------------------------------

    val USERNAME      = "username"
    val PASSWORD      = "username"
    val TOKEN         = "token"
    val TEST_USER     = User(hulkdx.com.domain.usecase.USERNAME, "", "", "", "", TOKEN)

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    @get:Rule var mMockitoJUnit = MockitoJUnit.rule()!!
    @Mock lateinit var mApiManager: ApiManager
    @Mock lateinit var mDatabaseManager: DatabaseManager
    @Mock lateinit var mCacheManager: CacheManager
    @Mock lateinit var mDataSourceManager: DataSourceManager
    private lateinit var mTestScheduler: Scheduler

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var SUT: TransactionUseCaseImpl

    @Before
    fun setup() {
        mTestScheduler = Schedulers.trampoline()
        SUT = TransactionUseCaseImpl(mTestScheduler, mTestScheduler, mDatabaseManager,
                mCacheManager, mApiManager, mDataSourceManager)
    }

    @Test
    fun getTransactions_shouldCallDataSourceManager() {
        // Arrange
        // Act
        SUT.getTransactions {  }
        // Assert
        verify(mDataSourceManager).getUser()
    }

    @Test
    fun getTransactions_noUser_callAuthError() {
        // Arrange
        noUser()
        var result: TransactionResult? = null
        // Act
        SUT.getTransactions {
            result = it
        }
        // Assert
        assertTrue(result is AuthenticationError)
    }

    @Test
    fun getTransactions_validUser_callApiManagerWithValidToken() {
        // Arrange
        validUser()
        var result: TransactionResult? = null
        // Act
        SUT.getTransactions {
            result = it
        }
        // Assert
        verify(mApiManager).getTransactions(TOKEN)
    }


    @Test
    fun getTransactions_validUserAndApiSuccess_saveTransactionsInDatabase() {
        // Arrange
        validUser()
        apiSuccess()
        // Act
        SUT.getTransactions {}
        // Assert
        TODO()
    }

    // region helper methods -----------------------------------------------------------------------

    private fun noUser() {
        `when`(mDataSourceManager.getUser()).thenReturn(null)
    }

    private fun validUser() {
        `when`(mDataSourceManager.getUser()).thenReturn(TEST_USER)
    }

    private fun apiSuccess() {
        `when`(mApiManager.getTransactions(any())).thenReturn(
                Single.just(ApiManager.TransactionApiResponse(
                        RemoteStatus.SUCCESS, TRANSACTION_LIST
                ))
        )
    }

    // endregion helper methods --------------------------------------------------------------------

}