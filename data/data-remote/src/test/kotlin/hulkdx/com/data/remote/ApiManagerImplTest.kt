package hulkdx.com.data.remote

import hulkdx.com.data.remote.model.DeleteTransactionsApiRequestBody
import hulkdx.com.domain.data.remote.ApiManager.*
import hulkdx.com.domain.data.remote.RegisterAuthErrorStatus
import hulkdx.com.domain.data.remote.RemoteStatus
import io.reactivex.Single
import io.reactivex.functions.Consumer
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

import org.mockito.Mockito.*
import retrofit2.Call
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
    val LOGIN_ERROR_JSON = "{\n" +
            "    \"error\": \"Incorrect username and password!\"\n" +
            "}"

    val REGISTER_SUCCESSFUL_JSON = "{\n" +
            "\"username\": \"test667\",\n" +
            "\"email\": \"test25@test.com\"\n" +
            "}"
    val REGISTER_ERROR_EMAIL_EXISTS_JSON = "{\n" +
            "\"error\": \"This email address has already registered!\"\n" +
            "}"
    val REGISTER_ERROR_USERNAME_EXISTS_JSON = "{\"username\":[\"A user with that username already exists.\"]}"

    val TEST_AUTH = "auth"
    val GET_TRANSACTION_AMOUNT_COUNT = -12423F
    
    val GET_TRANSACTION_CATEGORY_ID_1        = 9
    val GET_TRANSACTION_CATEGORY_NAME_1      = "Shopping"
    val GET_TRANSACTION_CATEGORY_HEX_COLOR_1 = "#FF00FF"
    
    val GET_TRANSACTION_TRANSACTION_ID_1           = 239
    val GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_1 = -214
    val GET_TRANSACTION_TRANSACTION_DATE_1         = "2018-09-29"
    val GET_TRANSACTION_TRANSACTION_ATTACHMENT_1   = "null"
    val GET_TRANSACTION_TRANSACTION_CATEGORY_1     = "null"
    
    val GET_TRANSACTION_TRANSACTION_ID_2           = 240
    val GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_2 = -24
    val GET_TRANSACTION_TRANSACTION_DATE_2         = "2018-09-29"
    val GET_TRANSACTION_TRANSACTION_ATTACHMENT_2   = "null"
    val GET_TRANSACTION_TRANSACTION_CATEGORY_2     = "null"
    
    val GET_TRANSACTION_TRANSACTION_ID_3           = 241
    val GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_3 = -12112
    val GET_TRANSACTION_TRANSACTION_DATE_3         = "2018-09-29"
    val GET_TRANSACTION_TRANSACTION_ATTACHMENT_3   = "null"
    val GET_TRANSACTION_TRANSACTION_CATEGORY_3     = "null"
    
    val GET_TRANSACTION_TRANSACTION_ID_4           = 242
    val GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_4 = -11
    val GET_TRANSACTION_TRANSACTION_DATE_4         = "2018-09-29"
    val GET_TRANSACTION_TRANSACTION_ATTACHMENT_4   = "null"
    val GET_TRANSACTION_TRANSACTION_CATEGORY_4     = "null"
    
    val GET_TRANSACTION_TRANSACTION_ID_5           = 243
    val GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_5 = -4
    val GET_TRANSACTION_TRANSACTION_DATE_5         = "2018-09-29"
    val GET_TRANSACTION_TRANSACTION_ATTACHMENT_5   = "null"
    val GET_TRANSACTION_TRANSACTION_CATEGORY_5     = "null"
    
    val GET_TRANSACTION_TRANSACTION_ID_6           = 244
    val GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_6 = -5
    val GET_TRANSACTION_TRANSACTION_DATE_6         = "2018-09-29"
    val GET_TRANSACTION_TRANSACTION_ATTACHMENT_6   = "null"
    val GET_TRANSACTION_TRANSACTION_CATEGORY_6     = "null"
    
    val GET_TRANSACTION_TRANSACTION_ID_7           = 245
    val GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_7 = -123
    val GET_TRANSACTION_TRANSACTION_DATE_7         = "2018-09-30"
    val GET_TRANSACTION_TRANSACTION_ATTACHMENT_7   = "null"
    val GET_TRANSACTION_TRANSACTION_CATEGORY_7     = "null"
    
    val GET_TRANSACTION_TRANSACTION_ID_8           = 246
    val GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_8 = -20
    val GET_TRANSACTION_TRANSACTION_DATE_8         = "2018-10-29"
    val GET_TRANSACTION_TRANSACTION_ATTACHMENT_8   = "null"
    val GET_TRANSACTION_TRANSACTION_CATEGORY_8     = "null"
    
    val GET_TRANSACTION_TRANSACTION_ID_9           = 247
    val GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_9 = -26
    val GET_TRANSACTION_TRANSACTION_DATE_9         = "2018-10-29"
    val GET_TRANSACTION_TRANSACTION_ATTACHMENT_9   = "null"
    val GET_TRANSACTION_TRANSACTION_CATEGORY_9     = "null"
    
    val GET_TRANSACTION_TRANSACTION_ID_10           = 248
    val GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_10 = 136
    val GET_TRANSACTION_TRANSACTION_DATE_10         = "2018-10-30"
    val GET_TRANSACTION_TRANSACTION_ATTACHMENT_10   = "null"
    val GET_TRANSACTION_TRANSACTION_CATEGORY_10     = "{\n" +
    "\"id\": $GET_TRANSACTION_CATEGORY_ID_1,\n" +
    "\"name\": \"$GET_TRANSACTION_CATEGORY_NAME_1\",\n" +
    "\"hexColor\": \"$GET_TRANSACTION_CATEGORY_HEX_COLOR_1\"\n" +
    "}\n"
    
    val GET_TRANSACTION_TRANSACTION_ID_11           = 249
    val GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_11 = -20
    val GET_TRANSACTION_TRANSACTION_DATE_11         = "2017-10-29"
    val GET_TRANSACTION_TRANSACTION_ATTACHMENT_11   = "/storage/emulated/0/Pictures/MoneyManager_20181029_131212.jpg"
    val GET_TRANSACTION_TRANSACTION_CATEGORY_11   = "null"

    val GET_TRANSACTION_JSON = "{\n" +
            "\"amount_count\": $GET_TRANSACTION_AMOUNT_COUNT,\n" +
            "\"response\": [\n" +
            "{\n" +
            "\"id\": $GET_TRANSACTION_TRANSACTION_ID_1,\n" +
            "\"amount\": $GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_1,\n" +
            "\"date\": \"$GET_TRANSACTION_TRANSACTION_DATE_1\",\n" +
            "\"attachment\": $GET_TRANSACTION_TRANSACTION_ATTACHMENT_1,\n" +
            "\"category\": $GET_TRANSACTION_TRANSACTION_CATEGORY_1\n" +
            "},\n" +
            "{\n" +
            "\"id\": $GET_TRANSACTION_TRANSACTION_ID_2,\n" +
            "\"amount\": $GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_2,\n" +
            "\"date\": \"$GET_TRANSACTION_TRANSACTION_DATE_2\",\n" +
            "\"attachment\": $GET_TRANSACTION_TRANSACTION_ATTACHMENT_2,\n" +
            "\"category\": $GET_TRANSACTION_TRANSACTION_CATEGORY_2\n" +
            "},\n" +
            "{\n" +
            "\"id\": $GET_TRANSACTION_TRANSACTION_ID_3,\n" +
            "\"amount\": $GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_3,\n" +
            "\"date\": \"$GET_TRANSACTION_TRANSACTION_DATE_3\",\n" +
            "\"attachment\": $GET_TRANSACTION_TRANSACTION_ATTACHMENT_3,\n" +
            "\"category\": $GET_TRANSACTION_TRANSACTION_CATEGORY_3\n" +
            "},\n" +
            "{\n" +
            "\"id\": $GET_TRANSACTION_TRANSACTION_ID_4,\n" +
            "\"amount\": $GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_4,\n" +
            "\"date\": \"$GET_TRANSACTION_TRANSACTION_DATE_4\",\n" +
            "\"attachment\": $GET_TRANSACTION_TRANSACTION_ATTACHMENT_4,\n" +
            "\"category\": $GET_TRANSACTION_TRANSACTION_CATEGORY_4\n" +
            "},\n" +
            "{\n" +
            "\"id\": $GET_TRANSACTION_TRANSACTION_ID_5,\n" +
            "\"amount\": $GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_5,\n" +
            "\"date\": \"$GET_TRANSACTION_TRANSACTION_DATE_5\",\n" +
            "\"attachment\": $GET_TRANSACTION_TRANSACTION_ATTACHMENT_5,\n" +
            "\"category\": $GET_TRANSACTION_TRANSACTION_CATEGORY_5\n" +
            "},\n" +
            "{\n" +
            "\"id\": $GET_TRANSACTION_TRANSACTION_ID_6,\n" +
            "\"amount\": $GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_6,\n" +
            "\"date\": \"$GET_TRANSACTION_TRANSACTION_DATE_6\",\n" +
            "\"attachment\": $GET_TRANSACTION_TRANSACTION_ATTACHMENT_6,\n" +
            "\"category\": $GET_TRANSACTION_TRANSACTION_CATEGORY_6\n" +
            "},\n" +
            "{\n" +
            "\"id\": $GET_TRANSACTION_TRANSACTION_ID_7,\n" +
            "\"amount\": $GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_7,\n" +
            "\"date\": \"$GET_TRANSACTION_TRANSACTION_DATE_7\",\n" +
            "\"attachment\": $GET_TRANSACTION_TRANSACTION_ATTACHMENT_7,\n" +
            "\"category\": $GET_TRANSACTION_TRANSACTION_CATEGORY_7\n" +
            "},\n" +
            "{\n" +
            "\"id\": $GET_TRANSACTION_TRANSACTION_ID_8,\n" +
            "\"amount\": $GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_8,\n" +
            "\"date\": \"$GET_TRANSACTION_TRANSACTION_DATE_8\",\n" +
            "\"attachment\": $GET_TRANSACTION_TRANSACTION_ATTACHMENT_8,\n" +
            "\"category\": $GET_TRANSACTION_TRANSACTION_CATEGORY_8\n" +
            "},\n" +
            "{\n" +
            "\"id\": $GET_TRANSACTION_TRANSACTION_ID_9,\n" +
            "\"amount\": $GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_9,\n" +
            "\"date\": \"$GET_TRANSACTION_TRANSACTION_DATE_9\",\n" +
            "\"attachment\": $GET_TRANSACTION_TRANSACTION_ATTACHMENT_9,\n" +
            "\"category\": $GET_TRANSACTION_TRANSACTION_CATEGORY_9\n" +
            "},\n" +
            "{\n" +
            "\"id\": $GET_TRANSACTION_TRANSACTION_ID_10,\n" +
            "\"amount\": $GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_10,\n" +
            "\"date\": \"$GET_TRANSACTION_TRANSACTION_DATE_10\",\n" +
            "\"attachment\": $GET_TRANSACTION_TRANSACTION_ATTACHMENT_10,\n" +
            "\"category\": $GET_TRANSACTION_TRANSACTION_CATEGORY_10" +
            "},\n" +
            "{\n" +
            "\"id\": $GET_TRANSACTION_TRANSACTION_ID_11,\n" +
            "\"amount\": $GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_11,\n" +
            "\"date\": \"$GET_TRANSACTION_TRANSACTION_DATE_11\",\n" +
            "\"attachment\": \"$GET_TRANSACTION_TRANSACTION_ATTACHMENT_11\",\n" +
            "\"category\": $GET_TRANSACTION_TRANSACTION_CATEGORY_11\n" +
            "}\n" +
            "]\n" +
            "}"

    val AUTHENTICATION_ERROR_NOT_PROVIDED = "{\n" +
            "    \"detail\": \"Authentication credentials were not provided.\"\n" +
            "}"

    val AUTHENTICATION_ERROR_SIGNATURE_INVALID = "{\n" +
            "    \"detail\": \"Error decoding signature.\"\n" +
            "}"

    // TODO expired signature?

    val DELETE_TRANSACTION_SUCCESS = "{\n" +
            "    \"amount_count\": -12209\n" +
            "}"

    val DELETE_TRANSACTION_ERROR_ID_DOES_NOT_EXISTS = "{\n" +
            "    \"error\": \"id 239 does not exist\"\n" +
            "}"

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    @get:Rule public var mMockitoJUnit = MockitoJUnit.rule()!!
    @Mock lateinit var mApiManagerRetrofit: ApiManagerRetrofit
    @Mock lateinit var mCallResponseBody: Call<ResponseBody>

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var SUT: ApiManagerImpl

    @Before
    fun setup() {
        SUT = ApiManagerImpl(mApiManagerRetrofit)
    }

    @Test
    fun login_callApiManagerRetrofit() {
        // Arrange
        loginSuccess()
        // Act
        SUT.login(USERNAME, PASSWORD)
        // Assert
        verify(mApiManagerRetrofit).postLogin(USERNAME, PASSWORD)
    }

    @Test
    fun login_successWithStatus200WithValidToken_sendRemoteStatusSUCCESSAndUser() {
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
        SUT.login(USERNAME, PASSWORD).subscribe(Consumer {
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
    fun login_authProblem_sendAuthErrorStatus() {
        // Arrange
        loginAuthProblem()
        // Act
        var status: RemoteStatus? = null
        SUT.login(USERNAME, PASSWORD).subscribe(Consumer {
            status = it.status
        })
        // Assert
        assertThat(status,   `is`(RemoteStatus.AUTH_ERROR))

    }

    @Test
    fun login_emptyString400_sendGeneralError() {
        // Arrange
        loginEmptyString()
        // Act
        var status: RemoteStatus? = null
        SUT.login(USERNAME, PASSWORD).subscribe(Consumer {
            status = it.status
        })
        // Assert
        assertThat(status,   `is`(RemoteStatus.GENERAL_ERROR))
    }

    @Test
    fun register_callApiManagerRetrofit() {
        // Arrange
        registerSuccess()
        // Act
        SUT.register(JSON_FIRST_NAME, JSON_LAST_NAME, USERNAME, PASSWORD, JSON_EMAIL, JSON_CURRENCY)
        // Assert
        verify(mApiManagerRetrofit).postRegister(USERNAME, PASSWORD, JSON_EMAIL, JSON_EMAIL, JSON_CURRENCY)
    }

    @Test
    fun register_errorEmailExists_sendAuthError() {
        // Arrange
        registerErrorEmailExists()
        var status: RemoteStatus = RemoteStatus.SUCCESS
        var authError: RegisterAuthErrorStatus? = null
        // Act
        SUT.register(JSON_FIRST_NAME, JSON_LAST_NAME, USERNAME, PASSWORD, JSON_EMAIL, JSON_CURRENCY)
                .subscribe(Consumer {
                    authError = it.authError
                    status    = it.status
                })
        // Assert
        assertThat(status, `is`(RemoteStatus.AUTH_ERROR))
        assertThat(authError, `is`(RegisterAuthErrorStatus.EMAIL_EXISTS))
    }

    @Test
    fun register_errorUsernameExists_sendAuthError() {
        // Arrange
        registerErrorUsernameExists()
        var result: RegisterApiResponse? = null
        // Act
        SUT.register(JSON_FIRST_NAME, JSON_LAST_NAME, USERNAME, PASSWORD, JSON_EMAIL, JSON_CURRENCY)
                .subscribe(Consumer {
                    result = it
                })
        // Assert
        assertTrue(result != null)
        assertThat(result!!.status, `is`(RemoteStatus.AUTH_ERROR))
        assertThat(result!!.authError, `is`(RegisterAuthErrorStatus.USER_EXISTS))
    }

    @Test
    fun getTransactions_callRetrofitWithJwt() {
        // Arrange
        getTransactionSuccess()
        // Act
        SUT.getTransactions(TEST_AUTH)
        // Assert
        verify(mApiManagerRetrofit).getTransactions("JWT $TEST_AUTH")
    }

    @Test
    fun getTransactions_getTransactionSuccess_resultIsSuccessful() {
        // Arrange
        getTransactionSuccess()
        // Act
        val result = SUT.getTransactions(TEST_AUTH)
        // Assert
        assertTrue(result is TransactionApiResponse.Success)
        val totalAmount = (result as TransactionApiResponse.Success).data.totalAmount
        val transaction = (result as TransactionApiResponse.Success).data.transactions
        assertThat(totalAmount, `is`(GET_TRANSACTION_AMOUNT_COUNT))
        assertThat(transaction.size, `is`(11))

        assertThat(transaction[0].id, `is`(GET_TRANSACTION_TRANSACTION_ID_1.toLong()))
        assertThat(transaction[0].amount, `is`(GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_1.toFloat()))
        assertThat(transaction[0].date, `is`(GET_TRANSACTION_TRANSACTION_DATE_1))
        assertTrue(transaction[0].category == null)
        assertTrue(transaction[0].attachment == null)

        assertThat(transaction[1].id, `is`(GET_TRANSACTION_TRANSACTION_ID_2.toLong()))
        assertThat(transaction[1].amount, `is`(GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_2.toFloat()))
        assertThat(transaction[1].date, `is`(GET_TRANSACTION_TRANSACTION_DATE_2))
        assertTrue(transaction[1].category == null)
        assertTrue(transaction[1].attachment == null)

        assertThat(transaction[2].id, `is`(GET_TRANSACTION_TRANSACTION_ID_3.toLong()))
        assertThat(transaction[2].amount, `is`(GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_3.toFloat()))
        assertThat(transaction[2].date, `is`(GET_TRANSACTION_TRANSACTION_DATE_3))
        assertTrue(transaction[2].category == null)
        assertTrue(transaction[2].attachment == null)

        assertThat(transaction[3].id, `is`(GET_TRANSACTION_TRANSACTION_ID_4.toLong()))
        assertThat(transaction[3].amount, `is`(GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_4.toFloat()))
        assertThat(transaction[3].date, `is`(GET_TRANSACTION_TRANSACTION_DATE_4))
        assertTrue(transaction[3].category == null)
        assertTrue(transaction[3].attachment == null)

        assertThat(transaction[4].id, `is`(GET_TRANSACTION_TRANSACTION_ID_5.toLong()))
        assertThat(transaction[4].amount, `is`(GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_5.toFloat()))
        assertThat(transaction[4].date, `is`(GET_TRANSACTION_TRANSACTION_DATE_5))
        assertTrue(transaction[4].category == null)
        assertTrue(transaction[4].attachment == null)

        assertThat(transaction[5].id, `is`(GET_TRANSACTION_TRANSACTION_ID_6.toLong()))
        assertThat(transaction[5].amount, `is`(GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_6.toFloat()))
        assertThat(transaction[5].date, `is`(GET_TRANSACTION_TRANSACTION_DATE_6))
        assertTrue(transaction[5].category == null)
        assertTrue(transaction[5].attachment == null)

        assertThat(transaction[6].id, `is`(GET_TRANSACTION_TRANSACTION_ID_7.toLong()))
        assertThat(transaction[6].amount, `is`(GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_7.toFloat()))
        assertThat(transaction[6].date, `is`(GET_TRANSACTION_TRANSACTION_DATE_7))
        assertTrue(transaction[6].category == null)
        assertTrue(transaction[6].attachment == null)

        assertThat(transaction[7].id, `is`(GET_TRANSACTION_TRANSACTION_ID_8.toLong()))
        assertThat(transaction[7].amount, `is`(GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_8.toFloat()))
        assertThat(transaction[7].date, `is`(GET_TRANSACTION_TRANSACTION_DATE_8))
        assertTrue(transaction[7].category == null)
        assertTrue(transaction[7].attachment == null)

        assertThat(transaction[8].id, `is`(GET_TRANSACTION_TRANSACTION_ID_9.toLong()))
        assertThat(transaction[8].amount, `is`(GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_9.toFloat()))
        assertThat(transaction[8].date, `is`(GET_TRANSACTION_TRANSACTION_DATE_9))
        assertTrue(transaction[8].category == null)
        assertTrue(transaction[8].attachment == null)

        assertThat(transaction[9].id, `is`(GET_TRANSACTION_TRANSACTION_ID_10.toLong()))
        assertThat(transaction[9].amount, `is`(GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_10.toFloat()))
        assertThat(transaction[9].date, `is`(GET_TRANSACTION_TRANSACTION_DATE_10))
        assertTrue(transaction[9].category!!.id   == GET_TRANSACTION_CATEGORY_ID_1.toLong())
        assertTrue(transaction[9].category!!.name == GET_TRANSACTION_CATEGORY_NAME_1)
        assertTrue(transaction[9].category!!.hexColor == GET_TRANSACTION_CATEGORY_HEX_COLOR_1)
        assertTrue(transaction[9].attachment == null)

        assertThat(transaction[10].id, `is`(GET_TRANSACTION_TRANSACTION_ID_11.toLong()))
        assertThat(transaction[10].amount, `is`(GET_TRANSACTION_TRANSACTION_TOTAL_AMOUNT_11.toFloat()))
        assertThat(transaction[10].date, `is`(GET_TRANSACTION_TRANSACTION_DATE_11))
        assertTrue(transaction[10].category == null)
        assertTrue(transaction[10].attachment == GET_TRANSACTION_TRANSACTION_ATTACHMENT_11)

    }

    @Test
    fun getTransactions_authErrorNotProvided_resultIsAuthWrongToken() {
        // Arrange
        getTransactionAuthErrorNotProvided()
        // Act
        val result = SUT.getTransactions(TEST_AUTH)
        // Assert
        assertTrue(result is TransactionApiResponse.AuthWrongToken)
    }

    @Test
    fun getTransactions_authErrorSignatureInvalid_resultIsAuthWrongToken() {
        // Arrange
        getTransactionAuthErrorSignatureInvalid()
        // Act
        val result = SUT.getTransactions(TEST_AUTH)
        // Assert
        assertTrue(result is TransactionApiResponse.AuthWrongToken)
    }

    @Test
    fun deleteTransactions_callRetrofitWithJwt() {
        // Arrange
        deleteTransactionsSuccess()
        val ac1: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)
        val ac2: ArgumentCaptor<DeleteTransactionsApiRequestBody> = ArgumentCaptor.forClass(DeleteTransactionsApiRequestBody::class.java)
        val id = listOf(1L, 10L)
        // Act
        SUT.deleteTransactions(TEST_AUTH, id)
        // Assert
        verify(mApiManagerRetrofit).deleteTransactions(capture(ac1), capture(ac2))
        assertThat(ac1.value, `is`("JWT $TEST_AUTH"))
    }

    @Test
    fun deleteTransactions_getTransactionSuccess_resultIsSuccessful() {
        // Arrange
        deleteTransactionsSuccess()
        // Act
        val result = SUT.deleteTransactions(TEST_AUTH, emptyList())
        // Assert
        assertTrue(result is TransactionApiResponse.Success)
    }

    @Test
    fun deleteTransactions_authErrorNotProvided_resultIsAuthWrongToken() {
        // Arrange
        deleteTransactionsAuthErrorNotProvided()
        // Act
        val result = SUT.deleteTransactions(TEST_AUTH, emptyList())
        // Assert
        assertTrue(result is TransactionApiResponse.AuthWrongToken)
    }

    @Test
    fun deleteTransactions_authErrorSignatureInvalid_resultIsAuthWrongToken() {
        // Arrange
        deleteTransactionsAuthErrorSignatureInvalid()
        // Act
        val result = SUT.deleteTransactions(TEST_AUTH, emptyList())
        // Assert
        assertTrue(result is TransactionApiResponse.AuthWrongToken)
    }

    @Test
    fun deleteTransactions_errorIdExists_resultIsAuthWrongToken() {
        // Arrange
        // deleteTransactionsErrorIdExists()
        // Act
        // val result = SUT.deleteTransactions(TEST_AUTH, emptyList())
        // Assert
        // TODO
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
                        ResponseBody.create(null, LOGIN_ERROR_JSON))))
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

    private fun registerErrorUsernameExists() {
        `when`(mApiManagerRetrofit.postRegister(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Single.just(Response.error(400,
                        ResponseBody.create(null, REGISTER_ERROR_USERNAME_EXISTS_JSON))))
    }

    private fun getTransactionSuccess() {
        val response = Response.success(200,
                ResponseBody.create(null, GET_TRANSACTION_JSON))

        `when`(mApiManagerRetrofit.getTransactions(anyString()))
                .thenReturn(mCallResponseBody)

        `when`(mCallResponseBody.execute()).thenReturn(response)
    }

    private fun getTransactionAuthErrorNotProvided() {
        val response: Response<ResponseBody> = Response.error(401,
                        ResponseBody.create(null, AUTHENTICATION_ERROR_NOT_PROVIDED))
        `when`(mApiManagerRetrofit.getTransactions(anyString()))
                .thenReturn(mCallResponseBody)

        `when`(mCallResponseBody.execute()).thenReturn(response)
    }

    private fun getTransactionAuthErrorSignatureInvalid() {
        val response: Response<ResponseBody> = Response.error(401,
                        ResponseBody.create(null, AUTHENTICATION_ERROR_SIGNATURE_INVALID))
        `when`(mApiManagerRetrofit.getTransactions(anyString()))
                .thenReturn(mCallResponseBody)

        `when`(mCallResponseBody.execute()).thenReturn(response)
    }

    private fun deleteTransactionsSuccess() {
        val response = Response.success(200,
                        ResponseBody.create(null, DELETE_TRANSACTION_SUCCESS))
        `when`(mApiManagerRetrofit.deleteTransactions(anyString(), anyKotlin()))
                .thenReturn(mCallResponseBody)

        `when`(mCallResponseBody.execute()).thenReturn(response)
    }

    private fun deleteTransactionsAuthErrorNotProvided() {
        val response: Response<ResponseBody> = Response.error(401,
                        ResponseBody.create(null, AUTHENTICATION_ERROR_NOT_PROVIDED))
        `when`(mApiManagerRetrofit.deleteTransactions(anyString(), anyKotlin()))
                .thenReturn(mCallResponseBody)

        `when`(mCallResponseBody.execute()).thenReturn(response)
    }

    private fun deleteTransactionsAuthErrorSignatureInvalid() {
        val response: Response<ResponseBody> = Response.error(401, ResponseBody.create(null, AUTHENTICATION_ERROR_SIGNATURE_INVALID))
        `when`(mApiManagerRetrofit.deleteTransactions(anyString(), anyKotlin()))
                .thenReturn(mCallResponseBody)

        `when`(mCallResponseBody.execute()).thenReturn(response)
    }

    private fun deleteTransactionsErrorIdExists() {
        val response: Response<ResponseBody> = Response.error(401,
                        ResponseBody.create(null, DELETE_TRANSACTION_ERROR_ID_DOES_NOT_EXISTS))
        `when`(mApiManagerRetrofit.deleteTransactions(anyString(), anyKotlin()))
                .thenReturn(mCallResponseBody)

        `when`(mCallResponseBody.execute()).thenReturn(response)
    }

    // endregion helper methods --------------------------------------------------------------------

}
