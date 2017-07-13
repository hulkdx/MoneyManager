/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.data.local.PreferencesHelper;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.ui.base.BaseActivity;

import java.util.List;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainMvpView, View.OnClickListener, View.OnTouchListener {

    @Inject MainPresenter mMainPresenter;
    @Inject PreferencesHelper mPrefrencesHelper;
    @Inject TransactionAdapter mTransactionAdapter;

    @BindView(R.id.tv_balance) TextView mBalanceTextView;
    @BindView(R.id.tv_empty_list) TextView mEmptyListTextView;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.bottom_layout) LinearLayout mBottomLayout;
    @BindView(R.id.et_add_new_balance) EditText mAddNewEditText;
    @BindView(R.id.imageview_category) ImageView mCategoryImageView;
    @BindView(R.id.imageview_plus) ImageView mPlusImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mBottomLayout.setOnClickListener(this);
        mAddNewEditText.setOnClickListener(this);

        mRecyclerView.setAdapter(mTransactionAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setOnTouchListener(this);
        mMainPresenter.attachView(this);
        mMainPresenter.loadTransactions();

        mBalanceTextView.setText(getString(R.string.balance_value_euro, String.valueOf(mPrefrencesHelper.getUserMoney())));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Hide the keyboard if it is still open.
        ChangeIconsBottomBar(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_layout:
            case R.id.et_add_new_balance:
                // Change the icons of bottom bar.
                if (!mAddNewEditText.isFocused()) { ChangeIconsBottomBar(true); }
                break;

            default:
                break;
        }
    }
    /*
     * Change the bottom bar icons and make the EditText focusable.
     * @param isShown : when it is true the EditText should be focus.
     */
    private void ChangeIconsBottomBar(boolean isShown) {
        // Icons
        mCategoryImageView.setVisibility(isShown ? View.VISIBLE : View.GONE);
        mPlusImageView.setImageResource(isShown ? R.drawable.ic_date : R.drawable.ic_plus);
        // Set the EditText focusable and show/hide the keyboad
        mAddNewEditText.setFocusable(isShown);
        mAddNewEditText.setFocusableInTouchMode(isShown);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShown) {
            mAddNewEditText.requestFocus();
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } else {
            mAddNewEditText.clearFocus();
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    /*
     * on touch recycler view make the edit text focus off and hide the keyboard.
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && mAddNewEditText.isFocused()) {
            ChangeIconsBottomBar(false);
        }
        return false;
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showEmptyTransactions() {
        mEmptyListTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTransactions(List<Transaction> transactions) {
        mEmptyListTextView.setVisibility(View.GONE);
    }

}
