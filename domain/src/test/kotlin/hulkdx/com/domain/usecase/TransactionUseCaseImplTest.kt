package hulkdx.com.domain.usecase

import hulkdx.com.domain.*
import hulkdx.com.domain.data.local.CacheManager
import hulkdx.com.domain.data.local.DatabaseManager
import hulkdx.com.domain.data.manager.DataSourceManager
import hulkdx.com.domain.data.model.Transaction
import hulkdx.com.domain.data.model.User
import hulkdx.com.domain.data.remote.ApiManager
import hulkdx.com.domain.usecase.TransactionUseCase.TransactionResult
import hulkdx.com.domain.usecase.TransactionUseCase.TransactionResult.AuthenticationError
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

import org.junit.Assert.*
import org.mockito.Mockito.*
import org.hamcrest.CoreMatchers.*
import java.io.IOException

/**
 * Created by Mohammad Jafarzadeh Rezvan on 30/06/2019.
 */
@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
class TransactionUseCaseImplTest {

    // region constants ----------------------------------------------------------------------------

    val USERNAME      = "username"
    val TOKEN         = "token"
    val TEST_USER     = User(USERNAME, "", "", "", "", TOKEN)

    val TOTAL_AMOUNT = 10.4252F
    val IO_EXCEPTION_MESSAGE = "IO_EXCEPTION_MESSAGE"

    val TEST_SEARCH_TEXT = "searchText"

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
    fun getTransactions_shouldGetUser() {
        // Arrange
        // Act
        SUT.getTransactionsAsync {  }
        // Assert
        verify(mDataSourceManager).getUser()
    }

    @Test
    fun getTransactions_noUser_callAuthError() {
        // Arrange
        noUser()
        var result: TransactionResult? = null
        // Act
        SUT.getTransactionsAsync {
            result = it
        }
        // Assert
        assertTrue(result is AuthenticationError)
    }

    @Test
    fun getTransactions_validUser_callApiManagerWithValidToken() {
        // Arrange
        validUser()
        apiSuccessGetTransactions()
        var result: TransactionResult? = null
        // Act
        SUT.getTransactionsAsync {
            result = it
        }
        // Assert
        verify(mApiManager).getTransactions(TOKEN)
    }

    @Test
    fun getTransactions_validUserAndApiSuccess_saveTransactionsInDatabase() {
        // Arrange
        validUser()
        apiSuccessGetTransactions()
        // Act
        SUT.getTransactionsAsync {}
        // Assert
        verify(mDatabaseManager).saveTransactions(TEST_TRANSACTION_LIST)
    }

    @Test
    fun getTransactions_validUserAndApiSuccess_callOnComplete() {
        // Arrange
        validUser()
        apiSuccessGetTransactions()
        var result: TransactionResult? = null
        // Act
        SUT.getTransactionsAsync {
            result = it
        }
        // Assert
        assertTrue(result is TransactionResult.Success)
    }

    @Test
    fun getTransactions_validUserAndApiIoException_resultIsNetworkError() {
        // Arrange
        validUser()
        apiIoExceptionGetTransactions()
        var result: TransactionResult? = null
        // Act
        SUT.getTransactionsAsync {
            result = it
        }
        // Assert
        assertTrue(result is TransactionResult.NetworkError)
    }

    @Test
    fun getTransactions_validUserAndApiGeneralException_resultIsGeneralError() {
        // Arrange
        validUser()
        apiGeneralExceptionGetTransactions()
        var result: TransactionResult? = null
        // Act
        SUT.getTransactionsAsync {
            result = it
        }
        // Assert
        assertTrue(result is TransactionResult.GeneralError)
    }

    @Test
    fun getTransactions_validUserAndApiGeneralError_resultIsGeneralError() {
        // Arrange
        validUser()
        apiGeneralErrorGetTransactions()
        var result: TransactionResult? = null
        // Act
        SUT.getTransactionsAsync {
            result = it
        }
        // Assert
        assertTrue(result is TransactionResult.GeneralError)
    }

    @Test
    fun getTransactions_validUserAndApiAuthWrongToken_resultIsAuthenticationError() {
        // Arrange
        validUser()
        apiAuthWrongTokenGetTransactions()
        var result: TransactionResult? = null
        // Act
        SUT.getTransactionsAsync {
            result = it
        }
        // Assert
        assertTrue(result is AuthenticationError)
    }

    @Test
    fun getTransactions_validUserAndApiSuccess_amountIsFormattedCorrectly() {
        // Arrange
        validUser()
        apiSuccessGetTransactions()
        var amount = ""
        // Act
        SUT.getTransactionsAsync {
            amount = (it as TransactionResult.Success).amount
        }
        // Assert
        assertThat(amount, `is`("10.43"))
    }

    @Test
    fun getTransactions_validUserAndApiSuccess_saveItToCache() {
        // Arrange
        validUser()
        apiSuccessGetTransactions()
        // Act
        SUT.getTransactionsAsync {
        }
        // Assert
        verify(mCacheManager).saveTransactions(TEST_TRANSACTION_LIST)
    }

    @Test
    fun searchTransactions_validUserAndApiSuccess_saveItToCache() {
        // Arrange
        // Act
        SUT.searchTransactionsAsync(TEST_SEARCH_TEXT) {}
        // Assert
        verify(mDataSourceManager).getTransactions()
    }

    @Test
    fun searchTransactions_validDataAndSearchTextIsNumber_searchTransactionByAmount() {
        // Arrange
        searchTransactionsValidData()
        var result: List<Transaction> = emptyList()
        // Act
        SUT.searchTransactionsAsync(TEST_TRANSACTION_2.amount.toString()) {
            result = it
        }
        // Assert
        assertThat(result.size, `is`(1))
        assertThat(result, hasItem(TEST_TRANSACTION_2))
    }

    @Test
    fun searchTransactions_validDataAndSearchTextIsString_searchTransactionByCategoryName() {
        // Arrange
        searchTransactionsValidData()
        var result: List<Transaction> = emptyList()
        // Act
        SUT.searchTransactionsAsync(TEST_CATEGORY_2.name) {
            result = it
        }
        // Assert
        assertThat(result.size, `is`(2))
        assertThat(result, hasItem(TEST_TRANSACTION_3))
        assertThat(result, hasItem(TEST_TRANSACTION_4))
    }

    @Test
    fun searchTransactions_searchTextPositive_returnNegativeAmountTransaction() {
        // Arrange
        val searchTextPositive = "11"
        val transaction = Transaction(1, "2018-09-29", null, -11F, null)
        `when`(mDataSourceManager.getTransactions()).thenReturn(listOf(
                transaction
        ))
        var result: List<Transaction> = emptyList()
        // Act
        SUT.searchTransactionsAsync(searchTextPositive) {
            result = it
        }
        // Assert
        assertThat(result.size, `is`(1))
        assertThat(result, hasItem(transaction))
    }

    @Test
    fun searchTransactions_searchTextNegative_returnPositiveAmountTransaction() {
        // Arrange
        val searchTextNegative = "-11"
        val transaction = Transaction(1, "2018-09-29", null, 11F, null)
        `when`(mDataSourceManager.getTransactions()).thenReturn(listOf(
                transaction
        ))
        var result: List<Transaction> = emptyList()
        // Act
        SUT.searchTransactionsAsync(searchTextNegative) {
            result = it
        }
        // Assert
        assertThat(result.size, `is`(1))
        assertThat(result, hasItem(transaction))
    }

    @Test
    fun searchTransactions_searchTextWithPoint_returnTransaction() {
        // Arrange
        val searchTextWithPoint = "-11.1"
        val transaction = Transaction(1, "2018-09-29", null, -11.1F, null)
        `when`(mDataSourceManager.getTransactions()).thenReturn(listOf(
                transaction
        ))
        var result: List<Transaction> = emptyList()
        // Act
        SUT.searchTransactionsAsync(searchTextWithPoint) {
            result = it
        }
        // Assert
        assertThat(result.size, `is`(1))
        assertThat(result, hasItem(transaction))
    }

    @Test
    fun searchTransactions_emptySearchText_returnValidData() {
        // Arrange
        searchTransactionsValidData()
        val searchTextEmpty = ""
        var result: List<Transaction> = emptyList()
        // Act
        SUT.searchTransactionsAsync(searchTextEmpty) {
            result = it
        }
        // Assert
        assertThat(result, `is`(TEST_TRANSACTION_LIST))
    }

    @Test
    fun deleteTransactionsAsync_shouldGetUser() {
        // Arrange
        // Act
        SUT.deleteTransactionsAsync(listOf()) {  }
        // Assert
        verify(mDataSourceManager).getUser()
    }

    @Test
    fun deleteTransactionsAsync_noUser_callAuthError() {
        // Arrange
        noUser()
        var result: TransactionResult? = null
        // Act
        SUT.deleteTransactionsAsync(listOf()) {
            result = it
        }
        // Assert
        assertTrue(result is AuthenticationError)
    }

    @Test
    fun deleteTransactionsAsync_validUser_shouldCallApiManager() {
        // Arrange
        validUser()
        apiSuccessDeleteTransactions()
        val id: List<Long> = listOf()
        // Act
        SUT.deleteTransactionsAsync(id) {}
        // Assert
        verify(mApiManager).deleteTransactions(TEST_USER.token, id)
    }

    // region helper methods -----------------------------------------------------------------------

    private fun noUser() {
        `when`(mDataSourceManager.getUser()).thenReturn(null)
    }

    private fun validUser() {
        `when`(mDataSourceManager.getUser()).thenReturn(TEST_USER)
    }

    private fun apiSuccessGetTransactions() {
        `when`(mApiManager.getTransactions(anyKotlin())).thenReturn(
                Single.just(ApiManager.TransactionApiResponse.Success(TEST_TRANSACTION_LIST, TOTAL_AMOUNT))
        )
    }

    private fun apiIoExceptionGetTransactions() {
        `when`(mApiManager.getTransactions(anyKotlin())).thenReturn(
                Single.fromCallable { throw IOException(IO_EXCEPTION_MESSAGE) }
        )
    }

    private fun apiGeneralExceptionGetTransactions() {
        `when`(mApiManager.getTransactions(anyKotlin())).thenReturn(
                Single.fromCallable { throw Exception(THROWABLE_MSG) }
        )
    }

    private fun apiGeneralErrorGetTransactions() {
        `when`(mApiManager.getTransactions(anyKotlin())).thenReturn(
                Single.fromCallable { ApiManager.TransactionApiResponse.GeneralError }
        )
    }

    private fun apiAuthWrongTokenGetTransactions() {
        `when`(mApiManager.getTransactions(anyKotlin())).thenReturn(
                Single.fromCallable { ApiManager.TransactionApiResponse.AuthWrongToken }
        )
    }

    private fun searchTransactionsValidData() {
        `when`(mDataSourceManager.getTransactions()).thenReturn(TEST_TRANSACTION_LIST)
    }

    private fun apiSuccessDeleteTransactions() {
        `when`(mApiManager.deleteTransactions(anyKotlin(), anyKotlin())).thenReturn(
                Single.just(ApiManager.TransactionApiResponse.Success(TEST_TRANSACTION_LIST, TOTAL_AMOUNT))
        )
    }

    // endregion helper methods --------------------------------------------------------------------

}