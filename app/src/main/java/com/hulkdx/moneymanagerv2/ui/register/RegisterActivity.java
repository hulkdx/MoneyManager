/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/21/2017.
 */

package com.hulkdx.moneymanagerv2.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hulkdx.moneymanagerv2.R;
import com.hulkdx.moneymanagerv2.ui.base.BaseActivity;
import com.hulkdx.moneymanagerv2.ui.loginsync.LoginSyncActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.regex.Pattern;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class RegisterActivity extends BaseActivity implements RegisterMvpView {

    public static final String REGISTERED_USERNAME = "username_registered";
    // Valid REGEX pattern for email.
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Inject RegisterPresenter mPresenter;

    @BindView(R.id.et_username) EditText mUsernameET;
    @BindView(R.id.username_input_layout) TextInputLayout mUsernameInputLayout;
    @BindView(R.id.et_email) EditText mEmailET;
    @BindView(R.id.email_input_layout) TextInputLayout mEmailInputLayout;
    @BindView(R.id.et_confirm_email) EditText mConfirmEmailET;
    @BindView(R.id.confirm_email_input_layout) TextInputLayout mConfirmEmailInputLayout;
    @BindView(R.id.et_password) EditText mPasswordET;
    @BindView(R.id.password_input_layout) TextInputLayout mPasswordInputLayout;
    @BindView(R.id.register) Button mRegisterBtn;
    @BindView(R.id.spinner_currency) Spinner mCurrencySpinner;
    @BindView(R.id.error_tv) TextView mErrorTextView;

    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mPresenter.attachView(this);

        // Validate the fields
        validation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();

        if (mDisposable != null) mDisposable.dispose();
    }

    @OnClick(R.id.register)
    void onClickRegister() {
        mErrorTextView.setText(getString(R.string.loading));
        mRegisterBtn.setEnabled(false);
        mPresenter.register(mUsernameET.getText().toString(), mPasswordET.getText().toString(),
                mEmailET.getText().toString(), mCurrencySpinner.getSelectedItem().toString());
    }

    public void validation() {
        Observable<CharSequence> username = RxTextView.textChanges(mUsernameET);
        Observable<CharSequence> password = RxTextView.textChanges(mPasswordET);
        Observable<CharSequence> email = RxTextView.textChanges(mEmailET);
        Observable<CharSequence> confirmEmail = RxTextView.textChanges(mConfirmEmailET);

        if (mDisposable != null) mDisposable.dispose();
        mDisposable =
                Observable.combineLatest(
                        username, password, email, confirmEmail,
                        (newName, newPassword, newEmail, newConfirmEmail) -> {
                            boolean nameValid = !TextUtils.isEmpty(newName);
                            if (!nameValid) {
                                showUserNameError();
                            } else {
                                hideUserNameError();
                            }

                            boolean passwordValid = !TextUtils.isEmpty(newPassword);
                            if (!passwordValid) {
                                showPasswordError();
                            } else {
                                hidePasswordError();
                            }

                            boolean emailValid = !TextUtils.isEmpty(newEmail);
                            boolean isNewEmailAnEmail =
                                    VALID_EMAIL_ADDRESS_REGEX.matcher(newEmail).find();

                            if (!emailValid) {
                                showEmailError(0);
                            } else if (!isNewEmailAnEmail) {
                                showEmailError(1);
                            } else {
                                hideEmailError();
                            }

                            boolean confirmEmailValid = !TextUtils.isEmpty(newConfirmEmail);
                            boolean isNewConfEmailAnEmail =
                                    VALID_EMAIL_ADDRESS_REGEX.matcher(newEmail).find();
                            boolean emailEquals =
                                    newEmail.toString().equals(newConfirmEmail.toString());

                            if (!confirmEmailValid) {
                                showConfirmEmailError(0);
                            } else if (!isNewEmailAnEmail) {
                                showConfirmEmailError(1);
                            } else if (!emailEquals) {
                                showConfirmEmailError(2);
                            } else {
                                hideConfirmEmailError();
                            }

                            return nameValid && passwordValid && emailValid && confirmEmailValid &&
                                    emailEquals && isNewEmailAnEmail && isNewConfEmailAnEmail;
                        })
                        .distinctUntilChanged()
                        .subscribe(this::setEnableRegisterBtn);

    }

    public void showUserNameError() {
        mUsernameInputLayout.setError(getString(R.string.error_invalid_username));
        mUsernameInputLayout.setErrorEnabled(true);
    }

    public void hideUserNameError() {
        mUsernameInputLayout.setErrorEnabled(false);
    }

    public void showPasswordError() {
        mPasswordInputLayout.setError(getString(R.string.error_invalid_username));
        mPasswordInputLayout.setErrorEnabled(true);
    }

    public void hidePasswordError() {
        mPasswordInputLayout.setErrorEnabled(false);
    }

    public void hideEmailError() {
        mEmailInputLayout.setErrorEnabled(false);
    }

    public void showEmailError(int errorNumber) {
        mEmailInputLayout.setErrorEnabled(true);
        if (errorNumber == 0) {
            mEmailInputLayout.setError(getString(R.string.error_invalid_email));
        } else if (errorNumber == 1) {
            mEmailInputLayout.setError(getString(R.string.error_is_not_valid_email));
        }
    }

    public void showConfirmEmailError(int errorNumber) {
        mConfirmEmailInputLayout.setErrorEnabled(true);
        if (errorNumber == 0) {
            mConfirmEmailInputLayout.setError(getString(R.string.error_invalid_email));
        } else if (errorNumber == 1) {
            mConfirmEmailInputLayout.setError(getString(R.string.error_is_not_valid_email));
        } else if (errorNumber == 2) {
            mConfirmEmailInputLayout.setError(
                    getString(R.string.error_not_equal_email_and_confirm));
        }
    }

    public void hideConfirmEmailError() {
        mConfirmEmailInputLayout.setErrorEnabled(false);
    }

    /***** MVP View methods implementation *****/

    @Override
    public void setEnableRegisterBtn(Boolean isValid) {
        mRegisterBtn.setEnabled(isValid);
    }

    @Override
    public void successfullyRegistered(String username) {
        Toast.makeText(this, "Register completed, please login now.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginSyncActivity.class);
        intent.putExtra(REGISTERED_USERNAME, username);
        startActivity(intent);
    }

    @Override
    public void showRegisterError(String errorMessage) {
        mErrorTextView.setText(errorMessage);
    }
}
