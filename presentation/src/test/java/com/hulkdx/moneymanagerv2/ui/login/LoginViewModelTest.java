package com.hulkdx.moneymanagerv2.ui.login;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import java.util.List;

import hulkdx.com.domain.usecase.LoginUseCase;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static hulkdx.com.domain.usecase.LoginUseCase.LOGIN_RESULT_SUCCESS;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
public class LoginViewModelTest {

    // region constants ----------------------------------------------------------------------------

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    @Rule public MockitoRule mMockitoJUnit = MockitoJUnit.rule();
    @Rule public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock public LoginUseCase mLoginUseCase;

    // endregion helper fields ---------------------------------------------------------------------

    private LoginViewModel SUT;

    @Before
    public void setup() {
        SUT = new LoginViewModel(mLoginUseCase);
    }

    @Test
    public void login_success_passToUseCase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        success();
        // Act
        SUT.login(USERNAME, PASSWORD);
        verify(mLoginUseCase).loginAsync(ac.capture(), ac.capture(), any());
        List<String> values = ac.getAllValues();
        String username = values.get(0);
        String password = values.get(1);
        // Assert
        assertThat(username, is(USERNAME));
        assertThat(password, is(PASSWORD));
    }

    @Test
    public void login_success_changeUserLoggerInTrue() throws Exception {
        // Arrange
        success();
        // Act
        SUT.login(USERNAME, PASSWORD);
        Boolean result = SUT.getUserLoggedIn().getValue();
        // Assert
        assertThat(result, is(true));
    }

    // region helper methods -----------------------------------------------------------------------

    private void success() {
        doAnswer(invocation -> {
            Function1<LoginUseCase.LoginResult, Unit> argument = invocation.getArgument(2);
            argument.invoke(new LoginUseCase.LoginResult(LOGIN_RESULT_SUCCESS));
            return null;
        }).when(mLoginUseCase).loginAsync(anyString(), anyString(), any());
    }

    // endregion helper methods --------------------------------------------------------------------

}