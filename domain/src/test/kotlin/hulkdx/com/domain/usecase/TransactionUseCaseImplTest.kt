package hulkdx.com.domain.usecase

import hulkdx.com.domain.*
import hulkdx.com.domain.data.model.User
import hulkdx.com.domain.data.remote.ApiManager
import hulkdx.com.domain.repository.TransactionRepository
import hulkdx.com.domain.repository.UserRepository
import hulkdx.com.domain.usecase.TransactionUseCase.*
import hulkdx.com.domain.usecase.TransactionUseCase.GetTransactionResult.AuthenticationError
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

import org.junit.Assert.*
import org.mockito.Mockito.*
import java.io.IOException
import java.lang.RuntimeException

/**
 * Created by Mohammad Jafarzadeh Rezvan on 30/06/2019.
 */
@Suppress("MemberVisibilityCanBePrivate", "PropertyName", "UNCHECKED_CAST")
class TransactionUseCaseImplTest {

    // region constants ----------------------------------------------------------------------------

    val USERNAME      = "username"
    val TOKEN         = "token"
    val TEST_USER     = User(USERNAME, "", "", "", "", TOKEN, 0F)

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
        var result: GetTransactionResult? = null
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
        var result: GetTransactionResult? = null
        // Act
        SUT.getTransactionsAsync {
            result = it
        }
        // Assert
        assertTrue(result is GetTransactionResult.Success)
    }

    @Test
    fun getTransactions_validUserAndApiIoException_resultIsNetworkError() {
        // Arrange
        validUser()
        apiIoExceptionGetTransactions()
        var result: GetTransactionResult? = null
        // Act
        SUT.getTransactionsAsync {
            result = it
        }
        // Assert
        assertTrue(result is GetTransactionResult.NetworkError)
    }

    @Test
    fun getTransactions_validUserAndApiGeneralException_resultIsGeneralError() {
        // Arrange
        validUser()
        apiGeneralExceptionGetTransactions()
        var result: GetTransactionResult? = null
        // Act
        SUT.getTransactionsAsync {
            result = it
        }
        // Assert
        assertTrue(result is GetTransactionResult.GeneralError)
    }

    @Test
    fun getTransactions_validUserAndApiGeneralError_resultIsGeneralError() {
        // Arrange
        validUser()
        apiGeneralErrorGetTransactions()
        var result: GetTransactionResult? = null
        // Act
        SUT.getTransactionsAsync {
            result = it
        }
        // Assert
        assertTrue(result is GetTransactionResult.GeneralError)
    }

    @Test
    fun getTransactions_validUserAndApiAuthWrongToken_resultIsAuthenticationError() {
        // Arrange
        validUser()
        apiAuthWrongTokenGetTransactions()
        var result: GetTransactionResult? = null
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
            amount = (it as GetTransactionResult.Success).amount
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
    fun getTransactions_validUserAndApiSuccess_updateAmount() {
        // Arrange
        validUser()
        apiSuccessGetTransactions()
        // Act
        SUT.getTransactionsAsync {}
        // Assert
        verify(mUserRepository).updateCurrentUserAmount(TOTAL_AMOUNT)
    }

    // endregion getTransactions -------------------------------------------------------------------
    // region searchTransactionsAsync --------------------------------------------------------------

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

    // endregion searchTransactionsAsync -----------------------------------------------------------
    // region deleteTransactionsAsync --------------------------------------------------------------

    @Test
    fun deleteTransactionsAsync_shouldGetCurrentUser() {
        // Arrange
        // Act
        SUT.deleteTransactionsAsync(emptySet(), emptyList()) {  }
        // Assert
        verify(mUserRepository).getCurrentUser()
    }

    @Test
    fun deleteTransactionsAsync_noUser_callAuthError() {
        // Arrange
        noUser()
        var result: DeleteTransactionResult? = null
        // Act
        SUT.deleteTransactionsAsync(emptySet(), emptyList()) {
            result = it
        }
        // Assert
        assertTrue(result is DeleteTransactionResult.AuthenticationError)
    }

    @Test
    fun deleteTransactionsAsync_noUser_authErrorWithOldTransactions() {
        // Arrange
        noUser()
        val oldTransactions = listOf(
                TEST_TRANSACTION_1,
                TEST_TRANSACTION_2,
                TEST_TRANSACTION_3,
                TEST_TRANSACTION_4
        )
        `when`(mTransactionRepository.findAll()).thenReturn(oldTransactions)
        val positions = listOf(0, 2)
        var result: DeleteTransactionResult? = null
        // Act
        SUT.deleteTransactionsAsync(setOf(0, 1), emptyList()) {
            result = it
        }
        // Assert
        assertTrue(result is DeleteTransactionResult.AuthenticationError)
        val resultOldTransaction = (result as DeleteTransactionResult.AuthenticationError).oldTransactions
        assertThat(oldTransactions, `is`(resultOldTransaction))
    }

    @Test
    fun deleteTransactionsAsync_validUser_callOnCompleteFirstTime() {
        // Arrange
        validUser()
        apiSuccessDeleteTransactions()
        var result: DeleteTransactionResult? = null
        // Act
        SUT.deleteTransactionsAsync(emptySet(), emptyList()) {
            if (result == null) result = it
        }
        // Assert
        assertTrue(result is DeleteTransactionResult.Success)
    }

    @Test
    fun deleteTransactionsAsync_validUser_callOnCompleteWithNewTransactions() {
        // Arrange
        validUser()
        apiSuccessDeleteTransactions()
        var result: DeleteTransactionResult? = null

        val oldTransactions = listOf(
                TEST_TRANSACTION_1, // 0
                TEST_TRANSACTION_2, // 1
                TEST_TRANSACTION_3, // 2
                TEST_TRANSACTION_4  // 3
        )
        `when`(mTransactionRepository.findAll()).thenReturn(oldTransactions)

        val positions = sortedSetOf (
                1, 3
        )

        val expectedTransactions = listOf(
                TEST_TRANSACTION_1,
                TEST_TRANSACTION_3
        )

        // Act
        SUT.deleteTransactionsAsync(positions, emptyList()) {
            result = it
        }
        // Assert
        val resultTransacitons = (result as DeleteTransactionResult.Success).newTransactions
        assertThat(resultTransacitons, `is`(expectedTransactions))
    }

    @Test
    fun deleteTransactionsAsync_validUser_callApiManagerWithValidToken() {
        // Arrange
        validUser()
        apiSuccessDeleteTransactions()
        // Act
        SUT.deleteTransactionsAsync(emptySet(), emptyList()) {}
        // Assert
        verify(mApiManager).deleteTransactions(TOKEN, emptyList())
    }

    @Test
    fun deleteTransactionsAsync_validUserAndApiIoException_resultIsNetworkError() {
        // Arrange
        validUser()
        apiIoExceptionDeleteTransactions()
        var result: DeleteTransactionResult? = null
        // Act
        SUT.deleteTransactionsAsync(emptySet(), emptyList()) {
            result = it
        }
        // Assert
        assertTrue(result is DeleteTransactionResult.NetworkError)
    }

    @Test
    fun deleteTransactionsAsync_validUserAndApiGeneralException_resultIsGeneralError() {
        // Arrange
        validUser()
        apiGeneralExceptionDeleteTransactions()
        var result: DeleteTransactionResult? = null
        // Act
        SUT.deleteTransactionsAsync(emptySet(), emptyList()) {
            result = it
        }
        // Assert
        assertTrue(result is DeleteTransactionResult.GeneralError)
    }

    @Test
    fun deleteTransactionsAsync_validUserAndApiAuthWrongToken_resultIsAuthenticationError() {
        // Arrange
        validUser()
        apiAuthWrongTokenDeleteTransactions()
        var result: DeleteTransactionResult? = null
        // Act
        SUT.deleteTransactionsAsync(emptySet(), emptyList()) {
            result = it
        }
        // Assert
        assertTrue(result is DeleteTransactionResult.AuthenticationError)
    }

    @Test
    fun deleteTransactionsAsync_validUserAndApiGeneralError_resultIsGeneralError() {
        // Arrange
        validUser()
        apiGeneralErrorDeleteTransactions()
        var result: DeleteTransactionResult? = null
        // Act
        SUT.deleteTransactionsAsync(emptySet(), emptyList()) {
            result = it
        }
        // Assert
        assertTrue(result is DeleteTransactionResult.GeneralError)
    }

    @Test
    fun deleteTransactionsAsync_validUserAndApiGeneralError_shouldCallOnCompleteTwoTimesSecondGeneralError() {
        // Arrange
        validUser()
        apiGeneralErrorDeleteTransactions()
        val result = mutableListOf<DeleteTransactionResult>()
        // Act
        SUT.deleteTransactionsAsync(emptySet(), emptyList()) {
            result.add(it)
        }
        // Assert
        assertThat(result.size, `is`(2))
        assertTrue(result[0] is DeleteTransactionResult.Success)
        assertTrue(result[1] is DeleteTransactionResult.GeneralError)
    }

    @Test
    fun deleteTransactionsAsync_validUserAndAuthWrongToken_shouldCallOnCompleteTwoTimesSecondAuthenticationError() {
        // Arrange
        validUser()
        apiAuthWrongTokenDeleteTransactions()
        val result = mutableListOf<DeleteTransactionResult>()
        // Act
        SUT.deleteTransactionsAsync(emptySet(), emptyList()) {
            result.add(it)
        }
        // Assert
        assertThat(result.size, `is`(2))
        assertTrue(result[0] is DeleteTransactionResult.Success)
        assertTrue(result[1] is DeleteTransactionResult.AuthenticationError)
    }

    // endregion deleteTransactionsAsync -----------------------------------------------------------
    // region helper methods -----------------------------------------------------------------------

    private fun noUser() {
        `when`(mUserRepository.getCurrentUser()).thenReturn(null)
    }

    private fun validUser() {
        `when`(mUserRepository.getCurrentUser()).thenReturn(TEST_USER)
    }

    private fun apiSuccessGetTransactions() {
        `when`(mApiManager.getTransactions(anyKotlin())).thenReturn(
                ApiManager.TransactionApiResponse.Success(
                        ApiManager.GetTransactionApiResponse(
                                TEST_TRANSACTION_LIST,
                                TOTAL_AMOUNT
                        )
                )
        )
    }

    private fun apiIoExceptionGetTransactions() {
        `when`(mApiManager.getTransactions(anyKotlin())).thenThrow(
                IOException(IO_EXCEPTION_MESSAGE)
        )
    }

    private fun apiGeneralExceptionGetTransactions() {
        `when`(mApiManager.getTransactions(anyKotlin())).thenThrow(
                RuntimeException(THROWABLE_MSG)
        )
    }

    private fun apiGeneralErrorGetTransactions() {
        `when`(mApiManager.getTransactions(anyKotlin())).thenReturn(
                ApiManager.TransactionApiResponse.GeneralError
        )
    }

    private fun apiAuthWrongTokenGetTransactions() {
        `when`(mApiManager.getTransactions(anyKotlin())).thenReturn(
                ApiManager.TransactionApiResponse.AuthWrongToken
        )
    }

    private fun apiSuccessDeleteTransactions() {
        `when`(mApiManager.deleteTransactions(anyKotlin(), anyKotlin())).thenReturn(
                ApiManager.TransactionApiResponse.Success(
                        ApiManager.DeleteTransactionApiResponse(
                                TOTAL_AMOUNT
                ))
        )
    }

    private fun apiIoExceptionDeleteTransactions() {
        `when`(mApiManager.deleteTransactions(anyKotlin(), anyKotlin())).thenThrow(
                IOException(IO_EXCEPTION_MESSAGE)
        )
    }

    private fun apiGeneralExceptionDeleteTransactions() {
        `when`(mApiManager.deleteTransactions(anyKotlin(), anyKotlin())).thenThrow(
                RuntimeException(THROWABLE_MSG)
        )
    }

    private fun apiGeneralErrorDeleteTransactions() {
        `when`(mApiManager.deleteTransactions(anyKotlin(), anyKotlin())).thenReturn(
                ApiManager.TransactionApiResponse.GeneralError
        )
    }

    private fun apiAuthWrongTokenDeleteTransactions() {
        `when`(mApiManager.deleteTransactions(anyKotlin(), anyKotlin())).thenReturn(
                ApiManager.TransactionApiResponse.AuthWrongToken
        )
    }

    // endregion helper methods --------------------------------------------------------------------

}