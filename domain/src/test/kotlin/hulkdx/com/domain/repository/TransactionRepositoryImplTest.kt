package hulkdx.com.domain.repository

import hulkdx.com.domain.TEST_CATEGORY_1
import hulkdx.com.domain.TEST_TRANSACTION_LIST
import hulkdx.com.domain.capture
import hulkdx.com.domain.data.local.CacheManager
import hulkdx.com.domain.data.local.DatabaseManager
import hulkdx.com.domain.data.model.Transaction
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.ArgumentCaptor
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

import org.junit.Assert.*
import org.mockito.Mockito.*

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/07/2019.
 */
@Suppress("UNCHECKED_CAST")
class TransactionRepositoryImplTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    @get:Rule
    var mMockitoJUnit: MockitoRule = MockitoJUnit.rule()
    @Mock lateinit var mCacheManager: CacheManager
    @Mock lateinit var mDatabaseManager: DatabaseManager

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var SUT: TransactionRepositoryImpl

    @Before
    fun setup() {
        SUT = TransactionRepositoryImpl(mDatabaseManager, mCacheManager)
    }

    // region findAll ------------------------------------------------------------------------------

    @Test
    fun findAll_transactionInDatabaseAndTransactionNotCached_transactionIsNotEmpty() {
        // Arrange
        transactionInDatabase()
        transactionNotCached()
        // Act
        val transaction = SUT.findAll()
        // Assert
        assertTrue(transaction.isNotEmpty())
    }

    @Test
    fun findAll_transactionNotInDatabaseAndTransactionNotCached_transactionIsEmpty() {
        // Arrange
        transactionNotInDatabase()
        transactionNotCached()
        // Act
        val transaction = SUT.findAll()
        // Assert
        assertTrue(transaction.isEmpty())
    }

    @Test
    fun findAll_callCacheFirst() {
        // Arrange
        // Act
        SUT.findAll()
        // Assert
        verify(mCacheManager).getTransactions()
    }

    @Test
    fun findAll_transactionCached_doNotCallDatabase() {
        // Arrange
        transactionCached()
        // Act
        SUT.findAll()
        // Assert
        verify(mDatabaseManager, never()).getTransactions()
    }

    @Test
    fun findAll_transactionNotCached_callDatabase() {
        // Arrange
        transactionNotCached()
        // Act
        SUT.findAll()
        // Assert
        verify(mDatabaseManager).getTransactions()
    }

    @Test
    fun findAll_transactionCached_resultIsNotEmpty() {
        // Arrange
        transactionCached()
        // Act
        val result = SUT.findAll()
        // Assert
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun findAll_transactionNotCached_resultIsEmpty() {
        // Arrange
        transactionNotCached()
        // Act
        val result = SUT.findAll()
        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun findAll_transactionNotCachedAndTransactionInDatabase_saveCache() {
        // Arrange
        transactionNotCached()
        transactionInDatabase()
        val ac: ArgumentCaptor<List<Transaction>> = ArgumentCaptor.forClass(List::class.java) as ArgumentCaptor<List<Transaction>>
        // Act
        SUT.findAll()
        // Assert
        verify(mCacheManager).saveTransactions(capture(ac))
    }

    @Test
    fun findAll_transactionNotCachedAndTransactionNotInDatabase_neverSaveCache() {
        // Arrange
        transactionNotCached()
        transactionNotInDatabase()
        val ac: ArgumentCaptor<List<Transaction>> = ArgumentCaptor.forClass(List::class.java) as ArgumentCaptor<List<Transaction>>
        // Act
        SUT.findAll()
        // Assert
        verify(mCacheManager, never()).saveTransactions(capture(ac))
    }

    // endregion findAll ---------------------------------------------------------------------------
    // region findByAbsoluteAmount -------------------------------------------------------------------------

    @Test
    fun findByAbsoluteAmount_searchPositiveNumber_returnNegativeAmountTransaction() {
        // Arrange
        val searchTextPositive = 11F
        val transaction = Transaction(1, "2018-09-29", null, -11F, null)
        `when`(mCacheManager.getTransactions()).thenReturn(listOf(
                transaction
        ))
        // Act
        val result = SUT.findByAbsoluteAmount(searchTextPositive)
        // Assert
        assertThat(result.size, `is`(1))
        assertThat(result, hasItem(transaction))
    }

    @Test
    fun findByAbsoluteAmount_searchTextNegative_returnPositiveAmountTransaction() {
        // Arrange
        val searchTextPositive = -11F
        val transaction = Transaction(1, "2018-09-29", null, 11F, null)
        `when`(mCacheManager.getTransactions()).thenReturn(listOf(
                transaction
        ))
        // Act
        val result = SUT.findByAbsoluteAmount(searchTextPositive)
        // Assert
        assertThat(result.size, `is`(1))
        assertThat(result, hasItem(transaction))
    }

    @Test
    fun findByAbsoluteAmount_searchTextWithPoint_returnTransaction() {
        // Arrange
        val searchTextWithPoint = -11.1F
        val transaction = Transaction(1, "2018-09-29", null, -11.1F, null)
        `when`(mCacheManager.getTransactions()).thenReturn(listOf(
                transaction
        ))
        // Act
        val result = SUT.findByAbsoluteAmount(searchTextWithPoint)
        // Assert
        assertThat(result.size, `is`(1))
        assertThat(result, hasItem(transaction))
    }

    // endregion findByAbsoluteAmount ----------------------------------------------------------------------
    // region findByCategoryName -------------------------------------------------------------------

    @Test
    fun findByCategoryName_validCategoryName_returnTransaction() {
        // Arrange
        val transaction1 = Transaction(1, "2018-09-29", TEST_CATEGORY_1, -11.1F, null)
        `when`(mCacheManager.getTransactions()).thenReturn(listOf(
                transaction1
        ))
        // Act
        val result = SUT.findByCategoryName(TEST_CATEGORY_1.name)
        // Assert
        assertThat(result.size, `is`(1))
        assertThat(result, hasItem(transaction1))
    }

    @Test
    fun findByCategoryName_validCategoryName_doNotReturnTransactionWithNullCategory() {
        // Arrange
        val transaction1 = Transaction(1, "2018-09-29", TEST_CATEGORY_1, -11.1F, null)
        val transaction2 = Transaction(1, "2018-09-29", null, -11.1F, null)
        `when`(mCacheManager.getTransactions()).thenReturn(listOf(
                transaction1, transaction2
        ))
        // Act
        val result = SUT.findByCategoryName(TEST_CATEGORY_1.name)
        // Assert
        assertThat(result, not(hasItem(transaction2)))
    }

    // endregion findByCategoryName ----------------------------------------------------------------
    // region save ---------------------------------------------------------------------------------

    @Test
    fun save_CacheManagerSaveTransactions() {
        // Arrange
        // Act
        SUT.save(TEST_TRANSACTION_LIST)
        // Assert
        verify(mCacheManager).saveTransactions(TEST_TRANSACTION_LIST)
    }


    @Test
    fun save_DatabaseManagerManagerSaveTransactions() {
        // Arrange
        // Act
        SUT.save(TEST_TRANSACTION_LIST)
        // Assert
        verify(mDatabaseManager).saveTransactions(TEST_TRANSACTION_LIST)
    }

    // endregion save ------------------------------------------------------------------------------
    // region helper methods -----------------------------------------------------------------------

    private fun transactionInDatabase() {
        `when`(mDatabaseManager.getTransactions())
                .thenReturn(TEST_TRANSACTION_LIST)
    }

    private fun transactionNotInDatabase() {
        `when`(mDatabaseManager.getTransactions())
                .thenReturn(null)
    }

    private fun transactionCached() {
        `when`(mCacheManager.getTransactions())
                .thenReturn(TEST_TRANSACTION_LIST)
    }

    private fun transactionNotCached() {
        `when`(mCacheManager.getTransactions())
                .thenReturn(null)
    }

    // endregion helper methods --------------------------------------------------------------------

}