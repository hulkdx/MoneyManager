/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/14/2017.
 */

package com.hulkdx.moneymanager.ui.chooser;

import android.content.Intent;
import android.os.Bundle;
import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.data.DataManager;
import com.hulkdx.moneymanager.ui.base.BaseActivity;
import com.hulkdx.moneymanager.ui.login.LoginActivity;
import com.hulkdx.moneymanager.ui.login_sync.LoginSyncActivity;
import com.hulkdx.moneymanager.ui.main.MainActivity;
import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooserActivity extends BaseActivity {

    @Inject DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        ButterKnife.bind(this);

        if (mDataManager.checkLoggedIn()) {
            redirectToMainActivity();
        }
    }

    void redirectToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.btn_yes)
    void onClickYes() {
        Intent intent = new Intent(this, LoginSyncActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_no)
    void onClickNo() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
