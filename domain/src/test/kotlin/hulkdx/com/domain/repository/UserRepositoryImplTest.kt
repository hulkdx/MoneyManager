package hulkdx.com.domain.repository

import hulkdx.com.domain.data.local.CacheManager
import hulkdx.com.domain.data.local.DatabaseManager
import hulkdx.com.domain.data.model.User
import hulkdx.com.domain.usecase.TEST_USER
import hulkdx.com.domain.usecase.capture
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

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/07/2019.
 */
class UserRepositoryImplTest {
    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    @get:Rule
    var mMockitoJUnit: MockitoRule = MockitoJUnit.rule()
    @Mock lateinit var mCacheManager: CacheManager
    @Mock lateinit var mDatabaseManager: DatabaseManager

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var SUT: UserRepositoryImpl

    @Before
    fun setup() {
        SUT = UserRepositoryImpl(mDatabaseManager, mCacheManager)
    }

    @Test
    fun deleteCurrentUser_deleteUserCache() {
        // Arrange
        // Act
        SUT.deleteCurrentUser()
        // Assert
        verify(mCacheManager).invalidateUser()
    }

    @Test
    fun deleteCurrentUser_deleteUserDatabase() {
        // Arrange
        // Act
        SUT.deleteCurrentUser()
        // Assert
        verify(mDatabaseManager).deleteUser()
    }

    @Test
    fun getCurrentUser_userInDatabase_userIsNotNull() {
        // Arrange
        userInDatabase()
        // Act
        val user = SUT.getCurrentUser()
        // Assert
        assertTrue(user != null)
    }

    @Test
    fun getCurrentUser_userNotInDatabase_userIsNull() {
        // Arrange
        userNotInDatabase()
        // Act
        val user = SUT.getCurrentUser()
        // Assert
        assertTrue(user == null)
    }

    @Test
    fun getCurrentUser_callCacheFirst() {
        // Arrange
        // Act
        SUT.getCurrentUser()
        // Assert
        verify(mCacheManager).getUser()
    }

    @Test
    fun getCurrentUser_userCached_doNotCallDatabase() {
        // Arrange
        userCached()
        // Act
        SUT.getCurrentUser()
        // Assert
        verify(mDatabaseManager, never()).getUser()
    }

    @Test
    fun getCurrentUser_userNotCached_callDatabase() {
        // Arrange
        userNotCached()
        // Act
        SUT.getCurrentUser()
        // Assert
        verify(mDatabaseManager).getUser()
    }

    @Test
    fun getCurrentUser_userCached_resultIsNotNull() {
        // Arrange
        userCached()
        // Act
        val result = SUT.getCurrentUser()
        // Assert
        assertTrue(result != null)
    }

    @Test
    fun getCurrentUser_userNotCached_resultIsNull() {
        // Arrange
        userNotCached()
        // Act
        val result = SUT.getCurrentUser()
        // Assert
        assertTrue(result == null)
    }

    @Test
    fun getCurrentUser_userNotCachedAndUserInDatabase_saveCache() {
        // Arrange
        userNotCached()
        userInDatabase()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.getCurrentUser()
        // Assert
        verify(mCacheManager).saveUser(capture(ac))
    }

    @Test
    fun getCurrentUser_userNotCachedAndUserNotInDatabase_neverSaveCache() {
        // Arrange
        userNotCached()
        userNotInDatabase()
        val ac: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        SUT.getCurrentUser()
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