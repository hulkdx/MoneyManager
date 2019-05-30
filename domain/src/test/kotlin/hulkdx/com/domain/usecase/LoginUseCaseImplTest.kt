package hulkdx.com.domain.usecase

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
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
class LoginUseCaseImplTest {
    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    @Rule
    var mMockitoJUnit = MockitoJUnit.rule()

    // endregion helper fields ---------------------------------------------------------------------

    private var SUT: LoginUseCaseImpl? = null

    @Before
    fun setup() {
        SUT = LoginUseCaseImpl()
    }

//    @Test
//    fun loginAsync() {
//        // Arrange
//        // Act
//        SUT.()
//        // Assert
//    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

}