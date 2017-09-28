/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/14/2017.
 */

package com.hulkdx.moneymanagerv2.ui.loginsync;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.hulkdx.moneymanagerv2.R;
import com.hulkdx.moneymanagerv2.ui.base.BaseActivity;
import com.hulkdx.moneymanagerv2.ui.main.MainActivity;
import com.hulkdx.moneymanagerv2.ui.register.RegisterActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class LoginSyncActivity extends BaseActivity implements LoginSyncMvpView {

    @Inject LoginSyncPresenter mPresenter;

    @BindView(R.id.et_username) EditText mUsernameET;
    @BindView(R.id.et_password) EditText mPasswordET;
    @BindView(R.id.login) Button mLoginBtn;
    @BindView(R.id.username_input_layout) TextInputLayout mUsernameInputLayout;
    @BindView(R.id.password_input_layout) TextInputLayout mPasswordInputLayout;
    @BindView(R.id.error_tv) TextView mErrorTextView;

    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_login_sync);
        ButterKnife.bind(this);
        mPresenter.attachView(this);

        // Validate the fields
        validation();

        // Checking if it is redirected from @link RegisterActivity.class:successfullyRegistered
        if (getIntent().getExtras() != null) {
            mUsernameET.setText(
                    getIntent().getExtras().getString(RegisterActivity.REGISTERED_USERNAME, ""));
            mPasswordET.requestFocus();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    @OnClick(R.id.login)
    void onClickLogin() {
        mErrorTextView.setText(getString(R.string.loading));
        mLoginBtn.setEnabled(false);
        mPresenter.login(mUsernameET.getText().toString(), mPasswordET.getText().toString());
    }

    @OnClick(R.id.register)
    void onClickRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void validation() {
        Observable<CharSequence> username = RxTextView.textChanges(mUsernameET);
        Observable<CharSequence> password = RxTextView.textChanges(mPasswordET);

        if (mDisposable != null) mDisposable.dispose();
        mDisposable =
                Observable.combineLatest(
                        username, password,
                        (newName, newPassword) -> {
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

                            return nameValid && passwordValid;
                        })
                        .distinctUntilChanged()
                        .subscribe(this::setEnableLoginBtn);

    }

    public void showUserNameError() {
        mUsernameInputLayout.setError(getString(R.string.error_invalid_username));
        mUsernameInputLayout.setErrorEnabled(true);
    }

    public void hideUserNameError() {
        mUsernameInputLayout.setErrorEnabled(false);
    }

    public void showPasswordError() {
        mPasswordInputLayout.setError(getString(R.string.error_invalid_password));
        mPasswordInputLayout.setErrorEnabled(true);
    }

    public void hidePasswordError() {
        mPasswordInputLayout.setErrorEnabled(false);
    }

    /***** MVP View methods implementation *****/

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
