/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/14/2017.
 */

package com.hulkdx.moneymanager.ui.login_sync;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.ui.base.BaseActivity;
import com.hulkdx.moneymanager.ui.main.MainActivity;
import com.hulkdx.moneymanager.ui.register.RegisterActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

public class LoginSyncActivity extends BaseActivity implements LoginSyncMvpView {

    @Inject LoginSyncPresenter mPresenter;

    @BindView(R.id.et_username) EditText mUsernameET;
    @BindView(R.id.et_password) EditText mPasswordET;
    @BindView(R.id.login) Button mLoginBtn;
    @BindView(R.id.username_input_layout) TextInputLayout mUsernameInputLayout;
    @BindView(R.id.password_input_layout) TextInputLayout mPasswordInputLayout;
    @BindView(R.id.error_tv) TextView mErrorTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_login_sync);
        ButterKnife.bind(this);
        mPresenter.attachView(this);

        // Validation
        Observable<CharSequence> usernameObservable = RxTextView.textChanges(mUsernameET);
        Observable<CharSequence> passwordObservable = RxTextView.textChanges(mPasswordET);
        mPresenter.validation(usernameObservable, passwordObservable);

        // Checking if it is redirected from @link RegisterActivity.class:successfullyRegistered
        if (getIntent().getExtras() != null){
            mUsernameET.setText(
                    getIntent().getExtras().getString(RegisterActivity.REGISTERED_USERNAME, ""));
            mPasswordET.requestFocus();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @OnClick(R.id.login)
    void onClickLogin(){
        mErrorTextView.setText(getString(R.string.loading));
        mLoginBtn.setEnabled(false);
        mPresenter.login(mUsernameET.getText().toString(), mPasswordET.getText().toString());
    }

    @OnClick(R.id.register)
    void onClickRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
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
        mLoginBtn.setEnabled(isValid);
    }

    @Override
    public void showLoginError(String errorMessage) {
        mErrorTextView.setText(errorMessage);
    }

    @Override
    public void successfullyLoggedIn() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
