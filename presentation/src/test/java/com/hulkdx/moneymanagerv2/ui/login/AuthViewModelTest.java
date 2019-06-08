package com.hulkdx.moneymanagerv2.ui.login;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.hulkdx.moneymanagerv2.viewmodel.AuthViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import hulkdx.com.domain.data.remote.RemoteStatus;
import hulkdx.com.domain.usecase.LoginUseCase;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
public class AuthViewModelTest {

    // region constants ----------------------------------------------------------------------------

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    public static final String THROWABLE_MESSAGE = "THROWABLE_MESSAGE";

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    @Rule public MockitoRule mMockitoJUnit = MockitoJUnit.rule();
    @Rule public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock public LoginUseCase mLoginUseCase;

    // endregion helper fields ---------------------------------------------------------------------

    private AuthViewModel SUT;

    @Before
    public void setup() {
        SUT = new AuthViewModel(mLoginUseCase);
    }

    @Test
    public void login_success_passToUseCase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        success();
        // Act
        SUT.login(USERNAME, PASSWORD);
        // Assert
        verify(mLoginUseCase).loginAsync(ac.capture(), ac.capture(), any());
        List<String> values = ac.getAllValues();
        String username = values.get(0);
        String password = values.get(1);
        assertThat(username, is(USERNAME));
        assertThat(password, is(PASSWORD));
    }

    // region helper methods -----------------------------------------------------------------------

    private void success() {
        doAnswer(invocation -> {
            Function1<LoginUseCase.LoginResult, Unit> argument = invocation.getArgument(2);
            argument.invoke(new LoginUseCase.LoginResult(RemoteStatus.SUCCESS, null));
            return null;
        }).when(mLoginUseCase).loginAsync(anyString(), anyString(), any());
    }

    private void authError() {
        doAnswer(invocation -> {
            Function1<LoginUseCase.LoginResult, Unit> argument = invocation.getArgument(2);
            argument.invoke(new LoginUseCase.LoginResult(RemoteStatus.AUTH_ERROR, null));
            return null;
        }).when(mLoginUseCase).loginAsync(anyString(), anyString(), any());
    }

    private void networkError() {
        doAnswer(invocation -> {
            Function1<LoginUseCase.LoginResult, Unit> argument = invocation.getArgument(2);
            argument.invoke(new LoginUseCase.LoginResult(RemoteStatus.NETWORK_ERROR, null));
            return null;
        }).when(mLoginUseCase).loginAsync(anyString(), anyString(), any());
    }

    private void generalError() {
        doAnswer(invocation -> {
            Function1<LoginUseCase.LoginResult, Unit> argument = invocation.getArgument(2);
            argument.invoke(new LoginUseCase.LoginResult(RemoteStatus.GENERAL_ERROR,
                    new RuntimeException(THROWABLE_MESSAGE)));
            return null;
        }).when(mLoginUseCase).loginAsync(anyString(), anyString(), any());
    }

    // endregion helper methods --------------------------------------------------------------------

}