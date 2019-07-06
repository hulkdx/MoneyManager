package hulkdx.com.domain.usecase

import hulkdx.com.domain.*
import hulkdx.com.domain.data.model.User
import hulkdx.com.domain.data.remote.ApiManager
import hulkdx.com.domain.repository.TransactionRepository
import hulkdx.com.domain.repository.UserRepository
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
    @Mock lateinit var mTransactionRepository: TransactionRepository
    @Mock lateinit var mUserRepository: UserRepository
    private lateinit var mTestScheduler: Scheduler

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var SUT: TransactionUseCaseImpl

    @Before
    fun setup() {
        mTestScheduler = Schedulers.trampoline()
        SUT = TransactionUseCaseImpl(mTestScheduler, mTestScheduler, mApiManager, mUserRepository,
                mTransactionRepository)
    }

    // region getTransactions ----------------------------------------------------------------------

    @Test
    fun getTransactions_shouldGetCurrentUser() {
        // Arrange
        // Act
        SUT.getTransactionsAsync {  }
        // Assert
        verify(mUserRepository).getCurrentUser()
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
        // Act
        SUT.getTransactionsAsync {}
        // Assert
        verify(mApiManager).getTransactions(TOKEN)
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
    fun getTransactions_validUserAndApiSuccess_saveTransactions() {
        // Arrange
        validUser()
        apiSuccessGetTransactions()
        // Act
        SUT.getTransactionsAsync {}
        // Assert
        verify(mTransactionRepository).save(TEST_TRANSACTION_LIST)
    }

    @Test
    fun searchTransactionsAsync_emptySearchText_findAllTransaction() {
        // Arrange
        // Act
        SUT.searchTransactionsAsync("") {}
        // Assert
        verify(mTransactionRepository).findAll()
    }

    @Test
    fun searchTransactionsAsync_numberSearchText_findByAmountTransaction() {
        // Arrange
        // Act
        SUT.searchTransactionsAsync("2") {}
        // Assert
        verify(mTransactionRepository).findByAbsoluteAmount(2F)
    }


    @Test
    fun searchTransactionsAsync_stringSearchText_findByCategoryNameTransaction() {
        // Arrange
        // Act
        SUT.searchTransactionsAsync("MoneyManager") {}
        // Assert
        verify(mTransactionRepository).findByCategoryName("MoneyManager")
    }

    // endregion getTransactions -------------------------------------------------------------------

    // region helper methods -----------------------------------------------------------------------

    private fun noUser() {
        `when`(mUserRepository.getCurrentUser()).thenReturn(null)
    }

    private fun validUser() {
        `when`(mUserRepository.getCurrentUser()).thenReturn(TEST_USER)
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

    private fun apiSuccessDeleteTransactions() {
        `when`(mApiManager.deleteTransactions(anyKotlin(), anyKotlin())).thenReturn(
                Single.just(ApiManager.TransactionApiResponse.Success(TEST_TRANSACTION_LIST, TOTAL_AMOUNT))
        )
    }

    // endregion helper methods --------------------------------------------------------------------

}