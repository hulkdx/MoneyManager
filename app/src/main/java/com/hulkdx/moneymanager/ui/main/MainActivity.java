/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.data.model.Category;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.ui.base.BaseActivity;
import com.hulkdx.moneymanager.util.DialogFactory;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemSelected;
import butterknife.OnTouch;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainMvpView,
        CategoryDialogFragment.CategoryFragmentListener, CategoryAdapter.Callback,
        SearchView.OnQueryTextListener {

    private static final int PICKED_IMAGE = 1;

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
    @BindView(R.id.rootListView) LinearLayout mRootListView;
    @BindView(R.id.topBarDate) LinearLayout mRootTopBar;
    @BindView(R.id.et_add_new_balance) EditText mAddNewEditText;
    @BindView(R.id.button_date_done) Button mDateDoneButton;
    @BindView(R.id.date_picker) DatePicker mDatePicker;
    @BindView(R.id.searchView) SearchView mSearchView;
    @BindView(R.id.nestedScrollView) NestedScrollView mScrollView;
    @BindView(R.id.current_selected_date_textview) TextView mCurrentSelectedDateTV;
    @BindView(R.id.spinner_chooserList) Spinner mChooserDateSpinner;
    @BindView(R.id.previous_arrow_ImageView) ImageView mPreviousArrowIV;
    @BindView(R.id.next_arrow_ImageView) ImageView mNextArrowIV;

    private long mSelectedCategoryId = -1;
    private String mSelectedAttachment = null;
    // For searching date in database.
    private Calendar mCurrentDateCalendar;
    private Calendar mSelectedCalendar;

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
        mCurrentDateCalendar = Calendar.getInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAddNewEditText.isFocused()) {
            // It needs to post to show the keyboard onResume function.
            mAddNewEditText.post(
                    () -> showKeyboard(true)
            );
        }
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
     * @param isShown : true -> show bottomLayout / false -> hide it.
     */
    private void expandBottomLayout(boolean isShown) {
        // Icons
        mBottomExpandedLayout.setVisibility(isShown ? View.VISIBLE : View.GONE);
        mBottomLayout.setVisibility(isShown ? View.GONE : View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mRootListView.getLayoutParams();
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
            imm.showSoftInput(mAddNewEditText, 0);
        } else {
            mAddNewEditText.clearFocus();
            imm.hideSoftInputFromWindow(mAddNewEditText.getWindowToken(), 0);
        }
    }
    /*
     * Show top layout with arrows.
     * @param show : show layout when its true. or hide it when its false.
     */
    private void showTopLayout(boolean show) {
        mRootTopBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    /*
     * on touch recycler view make the edit text focus off and hide the keyboard.
     */
    @OnTouch(R.id.transaction_recycler_view)
    public boolean onTouchRecycleView(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && mAddNewEditText.isFocused()) {
            expandBottomLayout(false);
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
                expandBottomLayout(false);
                return false;
            }
            float amount = Float.parseFloat(mAddNewEditText.getText().toString());
            Transaction newTransaction = new Transaction(
                    mDatePicker.getDayOfMonth(), mDatePicker.getMonth(), mDatePicker.getYear(),
                    mCurrencyPlusTextView.getText().equals("+") ? amount : -1 * amount,
                    mSelectedAttachment);
            mMainPresenter.addTransaction(newTransaction, mSelectedCategoryId);
            expandBottomLayout(false);
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
    // Spinner Chooser
    @OnItemSelected(R.id.spinner_chooserList)
    void chooserListSelectedItem(AdapterView<?> parentView, View selectedItemView, int position, long id){
        switch (position) {
            // Total Screen
            case 0:
                showTopLayout(false);
                mMainPresenter.loadTransactions();
                break;
            // Daily Screen
            case 1:
                showTopLayout(true);
                // get today date
                mSelectedCalendar = Calendar.getInstance();
                mCurrentSelectedDateTV.setText(getString(R.string.today));
                // Update the list with today transactions
                updateTransactionList(0);
                break;
            // Monthly Screen
            case 2:
                // get today date
                mSelectedCalendar = Calendar.getInstance();
                mCurrentSelectedDateTV.setText(getString(R.string.this_month));
                // Update the list with today transactions
                updateTransactionList(1);
                showTopLayout(true);
                break;
            // Yearly Screen
            case 3:
                // get today date
                mSelectedCalendar = Calendar.getInstance();
                mCurrentSelectedDateTV.setText(getString(R.string.this_year));
                // Update the list with today transactions
                updateTransactionList(2);
                showTopLayout(true);
                break;
        }
    }
    /*
     * Search data from the db.
     * @param isDailyOrMonthlyOrYearly: 0 -> daily, 1 -> Monthly, 2 -> yearly.
     */
    private void updateTransactionList(int isDailyOrMonthlyOrYearly) {
        mMainPresenter.searchTransactionWithDate(
                mSelectedCalendar.get(Calendar.DATE),
                mSelectedCalendar.get(Calendar.MONTH),
                mSelectedCalendar.get(Calendar.YEAR),
                isDailyOrMonthlyOrYearly);
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
        if (!mAddNewEditText.isFocused()) { expandBottomLayout(true); }
    }

    @OnClick(R.id.imageview_date)
    public void onClickDateIV() {
        if (mAddNewEditText.isFocused()) { showDateLayout(true); }
    }

    @OnClick(R.id.button_date_done)
    public void onClickDateDoneBtn() {
        showDateLayout(false);
    }

    @OnClick(R.id.previous_arrow_ImageView)
    public void onClickPrevArrowIV() {
        onClickedArrows(false);
    }

    @OnClick(R.id.next_arrow_ImageView)
    public void onClickNextArrowIV() {
        onClickedArrows(true);
    }
    // @param arrowDirection: false -> previous arrow, true -> next arrow.
    private void onClickedArrows(boolean arrowDirection) {
        boolean isCurrentDateTextSet = false;
        // Daily is selected
        if (mChooserDateSpinner.getSelectedItemPosition() == 1) {
            mSelectedCalendar.add(Calendar.DATE, arrowDirection ? 1 : -1);
            // checking for today.
            if (mCurrentDateCalendar.get(Calendar.YEAR) == mSelectedCalendar.get(Calendar.YEAR) &&
                    mCurrentDateCalendar.get(Calendar.MONTH) == mSelectedCalendar.get(Calendar.MONTH) &&
                    mSelectedCalendar.get(Calendar.DATE) == mCurrentDateCalendar.get(Calendar.DATE)) {

                mCurrentSelectedDateTV.setText(getString(R.string.today));
                isCurrentDateTextSet = true;
            }
            // Note: tomorrow or yesterday can be in another months/year
            // Check for tomorrow
            mCurrentDateCalendar.add(Calendar.DATE, 1);
            if (mCurrentDateCalendar.get(Calendar.YEAR) == mSelectedCalendar.get(Calendar.YEAR) &&
                    mCurrentDateCalendar.get(Calendar.MONTH) == mSelectedCalendar.get(Calendar.MONTH) &&
                    mSelectedCalendar.get(Calendar.DATE) == mCurrentDateCalendar.get(Calendar.DATE)) {

                mCurrentSelectedDateTV.setText(getString(R.string.tomorrow));
                isCurrentDateTextSet = true;
            }
            // Yesterday
            mCurrentDateCalendar.add(Calendar.DATE, -2);
            if (mCurrentDateCalendar.get(Calendar.YEAR) == mSelectedCalendar.get(Calendar.YEAR) &&
                    mCurrentDateCalendar.get(Calendar.MONTH) == mSelectedCalendar.get(Calendar.MONTH) &&
                    mSelectedCalendar.get(Calendar.DATE) == mCurrentDateCalendar.get(Calendar.DATE)) {

                mCurrentSelectedDateTV.setText(getString(R.string.yesterday));
                isCurrentDateTextSet = true;
            }
            // Set it back to today's date.
            mCurrentDateCalendar.add(Calendar.DATE, 1);
        }
        // Monthly is selected.
        else if (mChooserDateSpinner.getSelectedItemPosition() == 2) {
            mSelectedCalendar.add(Calendar.MONTH, arrowDirection ? 1 : -1);
            // This month
            if (mCurrentDateCalendar.get(Calendar.YEAR) == mSelectedCalendar.get(Calendar.YEAR) &&
                    mCurrentDateCalendar.get(Calendar.MONTH) == mSelectedCalendar.get(Calendar.MONTH)){
                mCurrentSelectedDateTV.setText(getString(R.string.this_month));
                isCurrentDateTextSet = true;
            }
            // Next month
            mCurrentDateCalendar.add(Calendar.MONTH, 1);
            if (mCurrentDateCalendar.get(Calendar.YEAR) == mSelectedCalendar.get(Calendar.YEAR) &&
                    mCurrentDateCalendar.get(Calendar.MONTH) == mSelectedCalendar.get(Calendar.MONTH)){
                mCurrentSelectedDateTV.setText(getString(R.string.next_month));
                isCurrentDateTextSet = true;
            }
            // Prev month
            mCurrentDateCalendar.add(Calendar.MONTH, -2);
            if (mCurrentDateCalendar.get(Calendar.YEAR) == mSelectedCalendar.get(Calendar.YEAR) &&
                    mCurrentDateCalendar.get(Calendar.MONTH) == mSelectedCalendar.get(Calendar.MONTH)){
                mCurrentSelectedDateTV.setText(getString(R.string.pre_month));
                isCurrentDateTextSet = true;
            }
            // Set the month back
            mCurrentDateCalendar.add(Calendar.MONTH, 1);
        }
        // Yearly is selected.
        else if (mChooserDateSpinner.getSelectedItemPosition() == 3) {
            mSelectedCalendar.add(Calendar.YEAR, arrowDirection ? 1 : -1);
            // This month
            if (mCurrentDateCalendar.get(Calendar.YEAR) == mSelectedCalendar.get(Calendar.YEAR)){
                mCurrentSelectedDateTV.setText(getString(R.string.this_year));
                isCurrentDateTextSet = true;
            }
            else if ((mSelectedCalendar.get(Calendar.YEAR)) == mCurrentDateCalendar.get(Calendar.YEAR) + 1){
                mCurrentSelectedDateTV.setText(getString(R.string.next_year));
                isCurrentDateTextSet = true;
            }
            else if ((mSelectedCalendar.get(Calendar.YEAR)) == mCurrentDateCalendar.get(Calendar.YEAR) - 1){
                mCurrentSelectedDateTV.setText(getString(R.string.pre_year));
                isCurrentDateTextSet = true;
            }
        }

        if (!isCurrentDateTextSet) mCurrentSelectedDateTV.setText(getString(R.string.setDate,
                mSelectedCalendar.get(Calendar.DATE), mSelectedCalendar.get(Calendar.MONTH),
                mSelectedCalendar.get(Calendar.YEAR)));
        // isDailyOrMonthlyOrYearly: 0 -> daily, 1 -> Monthly, 2 -> yearly.
        int isDailyOrMonthlyOrYearly = mChooserDateSpinner.getSelectedItemPosition()-1;
        updateTransactionList(isDailyOrMonthlyOrYearly);
    }

    /***** attachment section *****/
    @OnClick(R.id.imageview_add_attachment)
    public void onClickAddAttachment(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICKED_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKED_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) { return; }
            mSelectedAttachment = data.getData().toString();
        }
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
        mSelectedCategoryId = categoryId;
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
