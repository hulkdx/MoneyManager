package com.hulkdx.moneymanager.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.ui.base.BaseActivity;

public class MainActivity extends BaseActivity implements MainMvpView {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.hulkdx.moneymanager.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject MainPresenter mMainPresenter;
    @BindView(R.id.etUsername) EditText etUsername;
    @BindView(R.id.etInitialMoney) EditText etInitialMoney;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMainPresenter.detachView();
    }

    @OnClick(R.id.butEnter)
    void onEnterClicked() {
        Timber.i(etUsername.getText().toString());
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

}
