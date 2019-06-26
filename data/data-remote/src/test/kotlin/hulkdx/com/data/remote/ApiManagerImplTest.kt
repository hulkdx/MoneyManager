package hulkdx.com.data.remote

import hulkdx.com.domain.data.remote.RegisterAuthError
import hulkdx.com.domain.data.remote.RemoteStatus
import io.reactivex.Single
import io.reactivex.functions.Consumer
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.isNotNull
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

import org.mockito.Mockito.*
import retrofit2.Response
import org.mockito.ArgumentMatchers.anyString as anyString

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Suppress("RedundantVisibilityModifier", "MemberVisibilityCanBePrivate", "PropertyName")
class ApiManagerImplTest {

    // region constants ----------------------------------------------------------------------------
    val USERNAME = "USERNAME"
    val PASSWORD = "PASSWORD"
    val JSON_USERNAME = "jusername"
    val JSON_FIRST_NAME = "jpassword"
    val JSON_LAST_NAME = "jlastname"
    val JSON_EMAIL = "jemail"
    val JSON_TOKEN = "gwqrpH_JiiiPcblEpsslXuqqHIIC8HXuBmWdxhwGOpU.eyJleHAiOjE1NjQ0MjA4NTIsInVzZXJuYW1lIjoiYWRtaW4iLCJ1c2VyX2lkIjoxLCJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSJ9.eyJleHAiOjE1NjQ0MjA4NTIsInVzZXJuYW1lIjo"
    val JSON_CURRENCY = "EUR"
    val LOGIN_SUCCESSFUL_JSON = "{\n" +
            "    \"username\": \"$JSON_USERNAME\",\n" +
            "    \"first_name\": \"$JSON_FIRST_NAME\",\n" +
            "    \"last_name\": \"$JSON_LAST_NAME\",\n" +
            "    \"email\": \"$JSON_EMAIL\",\n" +
            "    \"token\": \"$JSON_TOKEN\",\n" +
            "    \"currency\": \"$JSON_CURRENCY\"\n" +
            "}"
    val ERROR_JSON = "{\n" +
            "    \"error\": \"Incorrect username and password!\"\n" +
            "}"

    val REGISTER_SUCCESSFUL_JSON = "{\n" +
            "\"username\": \"test667\",\n" +
            "\"email\": \"test25@test.com\"\n" +
            "}"

    val REGISTER_ERROR_EMAIL_EXISTS_JSON = "{\n" +
            "\"error\": \"This email address has already registered!\"\n" +
            "}"
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    @get:Rule public var mMockitoJUnit = MockitoJUnit.rule()!!
    @Mock lateinit var mApiManagerRetrofit: ApiManagerRetrofit

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var SUT: ApiManagerImpl

    @Before
    fun setup() {
        SUT = ApiManagerImpl(mApiManagerRetrofit)
    }

    @Test
    fun loginSync_callApiManagerRetrofit() {
        // Arrange
        loginSuccess()
        // Act
        SUT.loginSync(USERNAME, PASSWORD)
        // Assert
        verify(mApiManagerRetrofit).postLogin(USERNAME, PASSWORD)
    }

    @Test
    fun loginSync_successWithStatus200WithValidToken_sendRemoteStatusSUCCESSAndUser() {
        // Arrange
        loginSuccess()
        var status: RemoteStatus? = null
        var username  = ""           
        var firstName = ""            
        var lastName  = ""           
        var email     = ""        
        var currency  = ""           
        var token     = ""        
        
        // Act
        SUT.loginSync(USERNAME, PASSWORD).subscribe(Consumer {
            status    = it.status   
            username  = it.user.username
            firstName = it.user.firstName
            lastName  = it.user.lastName
            email     = it.user.email
            currency  = it.user.currency
            token     = it.user.token    
        })
        // Assert
        assertThat(status,   `is`(RemoteStatus.SUCCESS))
        assertThat(username, `is`(JSON_USERNAME))
        assertThat(firstName,`is`(JSON_FIRST_NAME))
        assertThat(lastName, `is`(JSON_LAST_NAME))
        assertThat(email,    `is`(JSON_EMAIL))
        assertThat(currency, `is`(JSON_CURRENCY))
        assertThat(token,    `is`(JSON_TOKEN))
    }

    @Test
    fun loginSync_authProblem_sendAuthErrorStatus() {
        // Arrange
        loginAuthProblem()
        // Act
        var status: RemoteStatus? = null
        SUT.loginSync(USERNAME, PASSWORD).subscribe(Consumer {
            status = it.status
        })
        // Assert
        assertThat(status,   `is`(RemoteStatus.AUTH_ERROR))

    }

    @Test
    fun loginSync_emptyString400_sendGeneralError() {
        // Arrange
        loginEmptyString()
        // Act
        var status: RemoteStatus? = null
        SUT.loginSync(USERNAME, PASSWORD).subscribe(Consumer {
            status = it.status
        })
        // Assert
        assertThat(status,   `is`(RemoteStatus.GENERAL_ERROR))
    }

    @Test
    fun registerSync_callApiManagerRetrofit() {
        // Arrange
        registerSuccess()
        // Act
        SUT.registerSync(JSON_FIRST_NAME, JSON_LAST_NAME, USERNAME, PASSWORD, JSON_EMAIL, JSON_CURRENCY)
        // Assert
        verify(mApiManagerRetrofit).postRegister(USERNAME, PASSWORD, JSON_EMAIL, JSON_EMAIL, JSON_CURRENCY)
    }

    @Test
    fun registerSync_errorEmailExists_sendAuthError() {
        // Arrange
        registerErrorEmailExists()
        var status: RemoteStatus = RemoteStatus.SUCCESS
        var authError: RegisterAuthError? = null
        // Act
        SUT.registerSync(JSON_FIRST_NAME, JSON_LAST_NAME, USERNAME, PASSWORD, JSON_EMAIL, JSON_CURRENCY)
                .subscribe(Consumer {
                    authError = it.authError
                    status    = it.status
                })
        // Assert
        assertThat(status, `is`(RemoteStatus.AUTH_ERROR))
        assertThat(authError, `is`(RegisterAuthError.EMAIL_EXISTS))
    }


    // region helper methods -----------------------------------------------------------------------

    private fun loginSuccess() {
        `when`(mApiManagerRetrofit.postLogin(anyString(), anyString()))
                .thenReturn(Single.just(Response.success(200,
                        ResponseBody.create(null, LOGIN_SUCCESSFUL_JSON))))
    }

    private fun loginAuthProblem() {
        `when`(mApiManagerRetrofit.postLogin(anyString(), anyString()))
                .thenReturn(Single.just(Response.error(500,
                        ResponseBody.create(null, ERROR_JSON))))
    }

    private fun loginEmptyString() {
        `when`(mApiManagerRetrofit.postLogin(anyString(), anyString()))
                .thenReturn(Single.just(Response.error(400,
                        ResponseBody.create(null, ""))))
    }

    private fun registerSuccess() {
        `when`(mApiManagerRetrofit.postRegister(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Single.just(Response.success(200,
                        ResponseBody.create(null, REGISTER_SUCCESSFUL_JSON))))
    }

    private fun registerErrorEmailExists() {
        `when`(mApiManagerRetrofit.postRegister(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Single.just(Response.error(500,
                        ResponseBody.create(null, REGISTER_ERROR_EMAIL_EXISTS_JSON))))
    }

    // endregion helper methods --------------------------------------------------------------------

}
