/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.data.model.Category;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.ui.base.BaseActivity;

import java.util.List;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnTouch;
import timber.log.Timber;
import java.text.DateFormatSymbols;

public class MainActivity extends BaseActivity implements MainMvpView, View.OnClickListener, CategoryDialogFragment.CategoryFragmentListener {

    @Inject MainPresenter mMainPresenter;
    @Inject TransactionAdapter mTransactionAdapter;
    @Inject CategoryAdapter mCategoryAdapter;

    @BindView(R.id.tv_balance) TextView mBalanceTextView;
    @BindView(R.id.tv_empty_list) TextView mEmptyListTextView;
    @BindView(R.id.tv_plus) TextView mPlusTextView;
    @BindView(R.id.tv_currency_bottom) TextView mCurrencyBottomTextView;
    @BindView(R.id.tv_currency) TextView mCurrencyTextView;
    @BindView(R.id.transaction_recycler_view) RecyclerView mTranscationsRecyclerView;
    @BindView(R.id.category_recycler_view) RecyclerView mCategoryRecyclerView;
    @BindView(R.id.bottom_layout) LinearLayout mBottomLayout;
    @BindView(R.id.bottom_layout_date) LinearLayout mDateBottomLayout;
    @BindView(R.id.bottom_layout_category) LinearLayout mCategoryBottomLayout;
    @BindView(R.id.et_add_new_balance) EditText mAddNewEditText;
    @BindView(R.id.imageview_category) ImageView mCategoryImageView;
    @BindView(R.id.imageview_plus) ImageView mPlusAndDateImageView;
    @BindView(R.id.button_date_done) Button mDateDoneButton;
    @BindView(R.id.button_cate_add) Button mCatAddButton;
    @BindView(R.id.button_cate_done) Button mCatDoneButton;
    @BindView(R.id.date_picker) DatePicker mDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mBottomLayout.setOnClickListener(this);
        mAddNewEditText.setOnClickListener(this);
        mPlusAndDateImageView.setOnClickListener(this);
        mDateDoneButton.setOnClickListener(this);
        mPlusTextView.setOnClickListener(this);
        mCurrencyBottomTextView.setOnClickListener(this);
        mCategoryImageView.setOnClickListener(this);
        mCatAddButton.setOnClickListener(this);
        mCatDoneButton.setOnClickListener(this);

        String currencyName = mMainPresenter.getCurrencyName();
        mCurrencyBottomTextView.setText(currencyName);
        mCurrencyTextView.setText(currencyName);

        mTranscationsRecyclerView.setAdapter(mTransactionAdapter);
        mTranscationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCategoryRecyclerView.setAdapter(mCategoryAdapter);
        mCategoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mMainPresenter.attachView(this);
        mMainPresenter.loadTransactions();
        mMainPresenter.loadCategories();
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
        changeIconsBottomBar(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_plus:
            case R.id.tv_currency_bottom:
                // Toggle plus to minus
                if (mAddNewEditText.isFocused()) { togglePlusTextView(); }
            case R.id.bottom_layout:
            case R.id.et_add_new_balance:
                // Change the icons of bottom bar.
                if (!mAddNewEditText.isFocused()) { changeIconsBottomBar(true); }
                break;
            case R.id.imageview_plus:
                if (mAddNewEditText.isFocused()) { showDateLayout(true); }
                else { changeIconsBottomBar(true); }
                break;
            case R.id.button_date_done:
                showDateLayout(false);
                break;
            case R.id.imageview_category:
                showCategoryLayout(true);
                break;
            case R.id.button_cate_add:
                // show CategoryDialogFragment Fragment.
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                CategoryDialogFragment newFragment = CategoryDialogFragment.newInstance();
                newFragment.show(ft, "dialog");
                break;
            case R.id.button_cate_done:
                showCategoryLayout(false);
                break;
            default:
                break;
        }
    }
    /*
     * Toggle plus text view to minus.
     */
    private void togglePlusTextView() {
        if (mPlusTextView.getText().equals("+")) { mPlusTextView.setText("-"); }
        else { mPlusTextView.setText("+"); }
    }
    /*
     * Show/hide date Layout when the button is clicked
     * @param showDate : true => show the date layout, false => don't show it.
     */
    private void showDateLayout(boolean showDate) {
        mBottomLayout.setVisibility(showDate ? View.GONE : View.VISIBLE);
        mDateBottomLayout.setVisibility(showDate ? View.VISIBLE : View.GONE);
        showKeyboard(!showDate);
    }
    /*
     * Change the bottom bar icons and make the EditText focusable.
     * @param isShown : when it is true the EditText should be focus.
     */
    private void changeIconsBottomBar(boolean isShown) {
        // Icons
        mCategoryImageView.setVisibility(isShown ? View.VISIBLE : View.GONE);
        mPlusAndDateImageView.setImageResource(isShown ? R.drawable.ic_date : R.drawable.ic_plus);
        // Set the EditText focusable and show/hide the keyboad
        mAddNewEditText.setFocusable(isShown);
        mAddNewEditText.setFocusableInTouchMode(isShown);
        showKeyboard(isShown);
    }
    /*
     * @param show: show or hide the keyboard
     */
    private void showKeyboard(boolean show) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show) {
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
    @OnTouch(R.id.transaction_recycler_view)
    public boolean onTouchRecycleView(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && mAddNewEditText.isFocused()) {
            changeIconsBottomBar(false);
        }
        return false;
    }

    @OnEditorAction(R.id.et_add_new_balance)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            Timber.i("done pressed");
            float amount = Float.parseFloat(mAddNewEditText.getText().toString());
            // TODO Category!
            Transaction newTransaction = new Transaction(String.valueOf(mDatePicker.getDayOfMonth()),
                    String.valueOf(new DateFormatSymbols().getMonths()[mDatePicker.getMonth()]), String.valueOf(mDatePicker.getYear()),
                    "", mPlusTextView.getText().equals("+") ? amount : -1 * amount);
            mMainPresenter.addTransaction(newTransaction);
            changeIconsBottomBar(false);
            mEmptyListTextView.setVisibility(View.GONE);
            mAddNewEditText.setText("");
            return false;
        }
        return true;
    }
    /*
     * Show/hide category Layout when the button is clicked
     * @param showCategory : true => show the layout, false => don't show it.
     */
    private void showCategoryLayout(boolean showCategory) {
        mBottomLayout.setVisibility(showCategory ? View.GONE : View.VISIBLE);
        mCategoryBottomLayout.setVisibility(showCategory ? View.VISIBLE : View.GONE);
        showKeyboard(!showCategory);
    }
    /*
     * It gets the Category data from @link CategoryDialogFragment
     * and send it to MainPresenter.
     */
    @Override
    public void onClickOkCategory(Category category) {
        mMainPresenter.addCategory(category);

    }

    /***** MVP View methods implementation *****/

    @Override
    public void showEmptyTransactions(List<Transaction> transactions) {
        mEmptyListTextView.setVisibility(View.VISIBLE);
        mTransactionAdapter.setTransactions(transactions);
        mTransactionAdapter.notifyDataSetChanged();
    }

    @Override
    public void showTransactions(List<Transaction> transactions) {
        mEmptyListTextView.setVisibility(View.GONE);
        mTransactionAdapter.setTransactions(transactions);
        mTransactionAdapter.notifyDataSetChanged();
    }

    @Override
    public void setBalanceTextView(float amount) {
        mBalanceTextView.setText(getString(R.string.balance_value, amount));
    }

    @Override
    public void showCategories(List<Category> categories) {
        mCategoryAdapter.setCategories(categories);
        mCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void addCategoryDataSet(Category newCategory) {
        mCategoryAdapter.notifyDataSetChanged();
    }

}
