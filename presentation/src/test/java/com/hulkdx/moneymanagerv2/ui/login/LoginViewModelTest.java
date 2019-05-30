package com.hulkdx.moneymanagerv2.ui.login;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.*;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
public class LoginViewModelTest {
    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    @Rule
    public MockitoRule mMockitoJUnit = MockitoJUnit.rule();

    // endregion helper fields ---------------------------------------------------------------------

    private com.hulkdx.moneymanagerv2.ui.login.LoginViewModel SUT;

    @Before
    public void setup() {
        SUT = new com.hulkdx.moneymanagerv2.ui.login.LoginViewModel();
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

}