/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/14/2017.
 */

package com.hulkdx.moneymanager.ui.login_sync;

import android.os.Bundle;
import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.ui.base.BaseActivity;
import javax.inject.Inject;
import butterknife.ButterKnife;

public class LoginSyncActivity extends BaseActivity implements LoginSyncMvpView {

    @Inject LoginSyncPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_login_sync);
        ButterKnife.bind(this);
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
