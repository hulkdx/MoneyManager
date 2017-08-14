/**
 * Created by Mohammad Jafarzadeh Rezvan on 8/14/2017.
 */

package com.hulkdx.moneymanager.ui.chooser;

import android.content.Intent;
import android.os.Bundle;
import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.ui.base.BaseActivity;
import com.hulkdx.moneymanager.ui.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_yes)
    void onClickYes() {

    }

    @OnClick(R.id.btn_no)
    void onClickNo() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
