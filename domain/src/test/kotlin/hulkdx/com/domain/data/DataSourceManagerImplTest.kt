package hulkdx.com.domain.data

import hulkdx.com.domain.data.local.CacheManager
import hulkdx.com.domain.data.local.DatabaseManager
import hulkdx.com.domain.data.manager.DataSourceManagerImpl
import hulkdx.com.domain.data.model.User
import hulkdx.com.domain.usecase.TEST_USER
import hulkdx.com.domain.usecase.capture
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit

/**
 * Created by Mohammad Jafarzadeh Rezvan on 30/06/2019.
 */
class DataSourceManagerImplTest {
    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    @Mock lateinit var mDatabaseManager: DatabaseManager
    @Mock lateinit var mCacheManager: CacheManager
    @get:Rule var mMockitoJUnit = MockitoJUnit.rule()!!

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var SUT: DataSourceManagerImpl

    @Before
    fun setup() {
        SUT = DataSourceManagerImpl(mDatabaseManager, mCacheManager)
    }

    @Test
    fun getUser_userInDatabase_userIsNotNull() {
        // Arrange
        userInDatabase()
        // Act
        val user = SUT.getUser()
        // Assert
        assertTrue(user != null)
    }

    @Test
    fun getUser_userNotInDatabase_userIsNull() {
        // Arrange
        userNotInDatabase()
        // Act
        val user = SUT.getUser()
        // Assert
        assertTrue(user == null)
    }

    @Test
    fun getUser_callCacheFirst() {
        // Arrange
        // Act
        SUT.getUser()
        // Assert
        verify(mCacheManager).getUser()
    }

    @Test
    fun getUser_userCached_doNotCallDatabase() {
        // Arrange
        userCached()
        // Act
        SUT.getUser()
        // Assert
        verify(mDatabaseManager, never()).getUser()
    }

    @Test
    fun getUser_userNotCached_callDatabase() {
        // Arrange
        userNotCached()
        // Act
        SUT.getUser()
        // Assert
        verify(mDatabaseManager).getUser()
    }

    @Test
    fun getUser_userCached_resultIsNotNull() {
        // Arrange
        userCached()
        // Act
        val result = SUT.getUser()
        // Assert
        assertTrue(result != null)
    }

    @Test
    fun getUser_userNotCached_resultIsNull() {
        // Arrange
        userNotCached()
        // Act
        val result = SUT.getUser()
        // Assert
        assertTrue(result == null)
    }

    @Test
    fun getUser_userNotCachedAndUserInDatabase_saveCache() {
        // Arrange
        userNotCached()
        userInDatabase()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.getUser()
        // Assert
        verify(mCacheManager).saveUser(capture(ac))
    }

    @Test
    fun getUser_userNotCachedAndUserNotInDatabase_neverSaveCache() {
        // Arrange
        userNotCached()
        userNotInDatabase()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.getUser()
        // Assert
        verify(mCacheManager, never()).saveUser(capture(ac))
    }

    // region helper methods -----------------------------------------------------------------------

    private fun userInDatabase() {
        `when`(mDatabaseManager.getUser())
                .thenReturn(TEST_USER)
    }

    private fun userNotInDatabase() {
        `when`(mDatabaseManager.getUser())
                .thenReturn(null)
    }

    private fun userCached() {
        `when`(mCacheManager.getUser())
                .thenReturn(TEST_USER)
    }

    private fun userNotCached() {
        `when`(mCacheManager.getUser())
                .thenReturn(null)
    }
    
    // endregion helper methods --------------------------------------------------------------------

}