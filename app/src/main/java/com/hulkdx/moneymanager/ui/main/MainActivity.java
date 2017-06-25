package com.hulkdx.moneymanager.ui.main;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.EditText;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.ui.base.BaseActivity;
import com.jakewharton.rxbinding.widget.RxTextView;

public class MainActivity extends BaseActivity implements MainMvpView {

    @Inject MainPresenter mMainPresenter;
    @BindView(R.id.et_name) EditText nameEditText;
    @BindView(R.id.etInitialMoney) EditText initialMoneyEditText;
    @BindView(R.id.name_input_layout) TextInputLayout nameInputLayout;
    @BindView(R.id.initial_input_layout) TextInputLayout initialInputLayout;
    @BindView(R.id.enterBtn) Button enterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainPresenter.attachView(this);
        //  validation
        Observable<CharSequence> nameObservable = RxTextView.textChanges(nameEditText);
        Observable<CharSequence> initialMoneyObservable = RxTextView.textChanges(initialMoneyEditText);
        mMainPresenter.validation(nameObservable, initialMoneyObservable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    @OnClick(R.id.enterBtn)
    void onEnterClicked() {
//        if (!validation(etUsername.getText().toString())) return;
        // checking if name is empty
//        if(nameString.isEmpty()){
//            Toast.makeText(this, "Please provide a name", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            // checking if money is empty
//            if ( moneyString.isEmpty() || moneyString.equals("0") ) {
//                Toast.makeText(this, "Please provide your money", Toast.LENGTH_SHORT).show();
//            }
//            else {
//
//                // Save name
//                SharedPreferences sp = getSharedPreferences("MyDataName", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putString("name", nameString);
//                editor.putInt("totalMoney", Integer.parseInt(moneyString));
//                editor.commit();
//
//
//                // Go to MoneyManager activity
//                Intent i = new Intent(this, MoneyManager.class);
//                startActivity(i);
//                finish();
//            }
//        }
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
