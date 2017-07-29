/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.data.model.Category;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.ui.base.BaseActivity;
import com.hulkdx.moneymanager.util.DialogFactory;

import java.util.List;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTouch;
import timber.log.Timber;
import java.text.DateFormatSymbols;

public class MainActivity extends BaseActivity implements MainMvpView,
        CategoryDialogFragment.CategoryFragmentListener, CategoryAdapter.Callback,
        SearchView.OnQueryTextListener {

    @Inject MainPresenter mMainPresenter;
    @Inject TransactionAdapter mTransactionAdapter;
    @Inject CategoryAdapter mCategoryAdapter;

    @BindView(R.id.tv_balance) TextView mBalanceTextView;
    @BindView(R.id.tv_empty_list) TextView mEmptyListTextView;
    @BindView(R.id.tv_currency_plus) TextView mCurrencyPlusTextView;
    @BindView(R.id.tv_currency_bottom) TextView mCurrencyBottomTextView;
    @BindView(R.id.tv_currency) TextView mCurrencyTextView;
    @BindView(R.id.transaction_recycler_view) RecyclerView mTransactionsRecyclerView;
    @BindView(R.id.category_recycler_view) RecyclerView mCategoryRecyclerView;
    @BindView(R.id.bottom_layout) LinearLayout mBottomLayout;
    @BindView(R.id.bottom_layout_expanded) LinearLayout mBottomExpandedLayout;
    @BindView(R.id.bottom_layout_date) LinearLayout mDateBottomLayout;
    @BindView(R.id.et_add_new_balance) EditText mAddNewEditText;
    @BindView(R.id.button_date_done) Button mDateDoneButton;
    @BindView(R.id.date_picker) DatePicker mDatePicker;
    @BindView(R.id.searchView) SearchView mSearchView;
    @BindView(R.id.nestedScrollView) NestedScrollView mScrollView;

    private long selectedCategoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SetupUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mScrollView.post(() -> mScrollView.scrollTo(0, mSearchView.getBottom()));
    }

    private void SetupUI() {
        String currencyName = mMainPresenter.getCurrencyName();
        mCurrencyBottomTextView.setText(currencyName);
        mCurrencyTextView.setText(currencyName);
        mTransactionAdapter.setCurrencyName(currencyName);
        mSearchView.setOnQueryTextListener(this);

        mTransactionAdapter.setContext(this);
        mTransactionsRecyclerView.setAdapter(mTransactionAdapter);
        mTransactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCategoryRecyclerView.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setCallback(this);
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
        expandButtomLayout(false);
    }

    /*
     * Toggle plus text view to minus.
     */
    private void togglePlusTextView() {
        if (mCurrencyPlusTextView.getText().equals("+")) { mCurrencyPlusTextView.setText("-"); }
        else { mCurrencyPlusTextView.setText("+"); }
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
    private void expandButtomLayout(boolean isShown) {
        // Icons
        mBottomExpandedLayout.setVisibility(isShown ? View.VISIBLE : View.GONE);
        mBottomLayout.setVisibility(isShown ? View.GONE : View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mScrollView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ABOVE, isShown ? R.id.bottom_layout_expanded : R.id.bottom_layout);
        // Set the EditText focusable and show/hide the keyboard
        showKeyboard(isShown);
    }
    /*
     * @param show: show or hide the keyboard
     */
    private void showKeyboard(boolean show) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show) {
            mAddNewEditText.requestFocus();
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
            expandButtomLayout(false);
            mCategoryAdapter.resetSelectedCategories();
            mAddNewEditText.setText("");
        }
        return false;
    }

    @OnEditorAction(R.id.et_add_new_balance)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            // Don't do transaction upon empty string.
            if (mAddNewEditText.getText().toString().equals("")) {
                expandButtomLayout(false);
                return false;
            }
            float amount = Float.parseFloat(mAddNewEditText.getText().toString());
            Transaction newTransaction = new Transaction(String.valueOf(mDatePicker.getDayOfMonth()),
                    String.valueOf(new DateFormatSymbols().getMonths()[mDatePicker.getMonth()]),
                    String.valueOf(mDatePicker.getYear()),
                    mCurrencyPlusTextView.getText().equals("+") ? amount : -1 * amount);
            mMainPresenter.addTransaction(newTransaction, selectedCategoryId);
            expandButtomLayout(false);
            mEmptyListTextView.setVisibility(View.GONE);
            mAddNewEditText.setText("");
            mSearchView.setQuery("", false);
            return false;
        }
        return true;
    }
    // related to SearchView
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }
    // SearchView upon textChange search the model.
    @Override
    public boolean onQueryTextChange(String s) {
        mTransactionAdapter.filter(s);
        return false;
    }

    /***** On Click implementation *****/

    @OnClick(R.id.tv_currency_plus)
    public void onClickCurrencyPlusTV() {
        togglePlusTextView();
    }

    @OnClick(R.id.tv_currency_bottom)
    public void onClickCurrencyBottomTV() {
        togglePlusTextView();
    }

    @OnClick(R.id.bottom_layout)
    public void onClickBottomLayout() {
        if (!mAddNewEditText.isFocused()) { expandButtomLayout(true); }
    }

    @OnClick(R.id.imageview_date)
    public void onClickDateIV() {
        if (mAddNewEditText.isFocused()) { showDateLayout(true); }
    }

    @OnClick(R.id.button_date_done)
    public void onClickDateDoneBtn() {
        showDateLayout(false);
    }

    /***** Callback methods implementation *****/

    /*
     * It gets the Category data from @link CategoryDialogFragment
     * and send it to MainPresenter.
     */
    @Override
    public void onClickOkCategory(Category category) {
        mMainPresenter.addCategory(category);
    }
    /*
     * on clicking category items in categoryAdapter.
     */
    @Override
    public void onCategoryClicked(long categoryId) {
        selectedCategoryId = categoryId;
    }
    /*
     * Show add category dialog
     */
    @Override
    public void showAddCategoryDialogFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        CategoryDialogFragment newFragment = CategoryDialogFragment.newInstance();
        newFragment.show(ft, "dialog");
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

    @Override
    public void showError(String functionName, Throwable error) {
        Timber.i("onError %s : %s", functionName, error.toString());
        DialogFactory.createGenericErrorDialog(this, error.toString());
    }

}
