/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/14/2017.
 */

package com.hulkdx.moneymanagerv2.ui.chooser;

import android.content.Intent;
import android.os.Bundle;
import com.hulkdx.moneymanagerv2.R;
import com.hulkdx.moneymanagerv2.data.DataManager;
import com.hulkdx.moneymanagerv2.ui.base.BaseActivity;
import com.hulkdx.moneymanagerv2.ui.login.LoginActivity;
import com.hulkdx.moneymanagerv2.ui.loginsync.LoginSyncActivity;
import com.hulkdx.moneymanagerv2.ui.main.MainActivity;
import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooserActivity extends BaseActivity {

    @Inject DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        activityComponent().inject(this);
        ButterKnife.bind(this);

        if (mDataManager.checkLoggedIn()) {
            redirectToMainActivity();
        }
    }

    private void redirectToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.btn_yes)
    public void onClickYes() {
        Intent intent = new Intent(this, LoginSyncActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_no)
    public void onClickNo() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
