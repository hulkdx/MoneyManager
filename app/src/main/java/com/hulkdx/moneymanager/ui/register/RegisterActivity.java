/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/21/2017.
 */

package com.hulkdx.moneymanager.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.ui.base.BaseActivity;
import com.hulkdx.moneymanager.ui.main.MainActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

public class RegisterActivity extends BaseActivity implements RegisterMvpView {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mPresenter.attachView(this);

        // TODO validation
        Observable<CharSequence> usernameObservable = RxTextView.textChanges(mUsernameET);
        Observable<CharSequence> passwordObservable = RxTextView.textChanges(mPasswordET);
        Observable<CharSequence> emailObservable = RxTextView.textChanges(mEmailET);
        Observable<CharSequence> confirmRmailObservable = RxTextView.textChanges(mConfirmEmailET);
        mPresenter.validation(usernameObservable, passwordObservable, emailObservable, confirmRmailObservable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @OnClick(R.id.register)
    void onClickRegister(){
        // TODO
        mPresenter.register(mUsernameET.getText().toString(), mPasswordET.getText().toString(),
                mEmailET.getText().toString());
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showUserNameError() {
        mUsernameInputLayout.setError(getString(R.string.error_invalid_username));
        mUsernameInputLayout.setErrorEnabled(true);
    }

    @Override
    public void hideUserNameError() {
        mUsernameInputLayout.setErrorEnabled(false);
    }

    @Override
    public void showPasswordError() {
        mPasswordInputLayout.setError(getString(R.string.error_invalid_username));
        mPasswordInputLayout.setErrorEnabled(true);
    }

    @Override
    public void hidePasswordError() {
        mPasswordInputLayout.setErrorEnabled(false);
    }

    @Override
    public void setEnableLoginBtn(Boolean isValid) {
        mRegisterBtn.setEnabled(isValid);
    }

    @Override
    public void hideEmailError() {
        mEmailInputLayout.setErrorEnabled(false);
    }

    @Override
    public void showEmailError(int errorNumber) {
        mEmailInputLayout.setErrorEnabled(true);
        if (errorNumber == 0) {
            mEmailInputLayout.setError(getString(R.string.error_invalid_email));
        } else if (errorNumber == 1) {
            mEmailInputLayout.setError(getString(R.string.error_is_not_valid_email));
        }
    }

    @Override
    public void showConfirmEmailError(int errorNumber) {
        mConfirmEmailInputLayout.setErrorEnabled(true);
        if (errorNumber == 0) {
            mConfirmEmailInputLayout.setError(getString(R.string.error_invalid_email));
        } else if (errorNumber==1) {
            mConfirmEmailInputLayout.setError(getString(R.string.error_is_not_valid_email));
        } else if (errorNumber==2) {
            mConfirmEmailInputLayout.setError(getString(R.string.error_not_equal_email_and_confirm));
        }
    }

    @Override
    public void hideConfirmEmailError() {
        mConfirmEmailInputLayout.setErrorEnabled(false);
    }

    @Override
    public void successfullyRegistered() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showRegisterError(String errorMessage) {
        // TODO show it in a better way!
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
