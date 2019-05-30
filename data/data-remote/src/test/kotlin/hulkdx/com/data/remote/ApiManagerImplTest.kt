package hulkdx.com.data.remote

import hulkdx.com.domain.data.remote.RemoteStatus
import io.reactivex.Single
import io.reactivex.functions.Consumer
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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
    val SUCCESSFUL_JSON = "{\n" +
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
        success()
        // Act
        SUT.loginSync(USERNAME, PASSWORD)
        // Assert
        verify(mApiManagerRetrofit).postLogin(USERNAME, PASSWORD)
    }

    @Test
    fun loginSync_successWithStatus200WithValidToken_sendRemoteStatusSUCCESSAndUser() {
        // Arrange
        success()
        // Act
        var status: RemoteStatus? = null
        var username  = ""           
        var firstName = ""            
        var lastName  = ""           
        var email     = ""        
        var currency  = ""           
        var token     = ""        
        SUT.loginSync(USERNAME, PASSWORD).subscribe(Consumer {
            status    = it.status   
            username  = it.username 
            firstName = it.firstName
            lastName  = it.lastName 
            email     = it.email    
            currency  = it.currency 
            token     = it.token    
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
        authProblem()
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
        emptyString()
        // Act
        var status: RemoteStatus? = null
        SUT.loginSync(USERNAME, PASSWORD).subscribe(Consumer {
            status = it.status
        })
        // Assert
        assertThat(status,   `is`(RemoteStatus.GENERAL_ERROR))
    }


    // region helper methods -----------------------------------------------------------------------

    private fun success() {
        `when`(mApiManagerRetrofit.postLogin(anyString(), anyString()))
                .thenReturn(Single.just(Response.success(200,
                        ResponseBody.create(null, SUCCESSFUL_JSON))))
    }

    private fun authProblem() {
        `when`(mApiManagerRetrofit.postLogin(anyString(), anyString()))
                .thenReturn(Single.just(Response.error(500,
                        ResponseBody.create(null, ERROR_JSON))))
    }

    private fun emptyString() {
        `when`(mApiManagerRetrofit.postLogin(anyString(), anyString()))
                .thenReturn(Single.just(Response.error(400,
                        ResponseBody.create(null, ""))))
    }


    // endregion helper methods --------------------------------------------------------------------

}
