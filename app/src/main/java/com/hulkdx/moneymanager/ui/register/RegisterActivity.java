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
import com.hulkdx.moneymanager.ui.login_sync.LoginSyncMvpView;
import com.hulkdx.moneymanager.ui.login_sync.LoginSyncPresenter;
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
        mPresenter.validation(usernameObservable, passwordObservable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @OnClick(R.id.register)
    void onClickRegister(){
        // TODO
//        mPresenter.register(mUsernameET.getText().toString(), mPasswordET.getText().toString());
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
}
