/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.ui.main;

import android.os.Bundle;
import android.widget.TextView;

import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.data.local.PreferencesHelper;
import com.hulkdx.moneymanager.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainMvpView {

    @Inject MainPresenter mMainPresenter;
    @Inject PreferencesHelper mPrefrencesHelper;
    @BindView(R.id.tv_balance) TextView balanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainPresenter.attachView(this);

        balanceTextView.setText(String.valueOf(mPrefrencesHelper.getUserMoney()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    /***** MVP View methods implementation *****/

}
