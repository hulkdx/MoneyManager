package com.hulkdx.moneymanagerv2.ui.login;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import hulkdx.com.domain.data.remote.RemoteStatus;
import hulkdx.com.domain.usecase.AuthUseCase;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static org.mockito.Mockito.*;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
public class LoginViewModelTest {

    // region constants ----------------------------------------------------------------------------

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    public static final String THROWABLE_MESSAGE = "THROWABLE_MESSAGE";

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    @Rule public MockitoRule mMockitoJUnit = MockitoJUnit.rule();
    @Rule public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock public AuthUseCase mAuthUseCase;

    // endregion helper fields ---------------------------------------------------------------------

    private LoginViewModel SUT;

    @Before
    public void setup() {
        SUT = new LoginViewModel(mAuthUseCase);
    }

    @Test
    public void login_success_passToUseCase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        success();
        // Act
        SUT.login(USERNAME, PASSWORD);
        // Assert
        verify(mAuthUseCase).loginAsync(ac.capture(), ac.capture(), any());
    }

    // region helper methods -----------------------------------------------------------------------

    private void success() {
        doAnswer(invocation -> {
            Function1<AuthUseCase.LoginResult, Unit> argument = invocation.getArgument(2);
            argument.invoke(new AuthUseCase.LoginResult(RemoteStatus.SUCCESS, null));
            return null;
        }).when(mAuthUseCase).loginAsync(anyString(), anyString(), any());
    }

    private void authError() {
        doAnswer(invocation -> {
            Function1<AuthUseCase.LoginResult, Unit> argument = invocation.getArgument(2);
            argument.invoke(new AuthUseCase.LoginResult(RemoteStatus.AUTH_ERROR, null));
            return null;
        }).when(mAuthUseCase).loginAsync(anyString(), anyString(), any());
    }

    private void networkError() {
        doAnswer(invocation -> {
            Function1<AuthUseCase.LoginResult, Unit> argument = invocation.getArgument(2);
            argument.invoke(new AuthUseCase.LoginResult(RemoteStatus.NETWORK_ERROR, null));
            return null;
        }).when(mAuthUseCase).loginAsync(anyString(), anyString(), any());
    }

    private void generalError() {
        doAnswer(invocation -> {
            Function1<AuthUseCase.LoginResult, Unit> argument = invocation.getArgument(2);
            argument.invoke(new AuthUseCase.LoginResult(RemoteStatus.GENERAL_ERROR,
                    new RuntimeException(THROWABLE_MESSAGE)));
            return null;
        }).when(mAuthUseCase).loginAsync(anyString(), anyString(), any());
    }

    // endregion helper methods --------------------------------------------------------------------

}