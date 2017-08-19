/**
 * Created by Mohammad Jafarzadeh Rezvan on 6/13/2017.
 */

package com.hulkdx.moneymanager.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.ui.base.BaseActivity;
import com.hulkdx.moneymanager.ui.main.MainActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;

public class LoginActivity extends BaseActivity implements LoginMvpView {

    @Inject LoginPresenter mLoginPresenter;

    @BindView(R.id.et_name) EditText nameEditText;
    @BindView(R.id.etInitialMoney) EditText initialMoneyEditText;
    @BindView(R.id.name_input_layout) TextInputLayout nameInputLayout;
    @BindView(R.id.initial_input_layout) TextInputLayout initialInputLayout;
    @BindView(R.id.enterBtn) Button enterBtn;
    @BindView(R.id.spinner_currency) Spinner currencySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mLoginPresenter.attachView(this);
        //  validation
        Observable<CharSequence> nameObservable = RxTextView.textChanges(nameEditText);
        Observable<CharSequence> initialMoneyObservable = RxTextView.textChanges(initialMoneyEditText);
        mLoginPresenter.validation(nameObservable, initialMoneyObservable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.detachView();
    }

    @OnClick(R.id.enterBtn)
    void onEnterClicked() {
        // Save name
        mLoginPresenter.saveUserInformation( nameEditText.getText().toString(),
                Integer.parseInt(initialMoneyEditText.getText().toString()), String.valueOf(currencySpinner.getSelectedItem()) );

        // Go to MoneyManager activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showNameError() {
        nameInputLayout.setError(getString(R.string.error_invalid_name));
        nameInputLayout.setErrorEnabled(true);
    }

    @Override
    public void hideNameError() {
        nameInputLayout.setErrorEnabled(false);
    }

    @Override
    public void showInitialError() {
        initialInputLayout.setError(getString(R.string.error_invalid_initial));
        initialInputLayout.setErrorEnabled(true);
    }

    @Override
    public void setEnabledButton(Boolean isValid) {
        enterBtn.setEnabled(isValid);
    }

    @Override
    public void hideInitialError() {
        initialInputLayout.setErrorEnabled(false);
    }

}
