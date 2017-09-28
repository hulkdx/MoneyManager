/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanagerv2.ui.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.hulkdx.moneymanagerv2.R;
import com.hulkdx.moneymanagerv2.data.SyncService;
import com.hulkdx.moneymanagerv2.data.local.PreferencesHelper;
import com.hulkdx.moneymanagerv2.data.model.Category;
import com.hulkdx.moneymanagerv2.data.model.Transaction;
import com.hulkdx.moneymanagerv2.ui.base.BaseActivity;
import com.hulkdx.moneymanagerv2.util.DialogFactory;
import com.hulkdx.moneymanagerv2.util.PermissionChecker;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemSelected;
import butterknife.OnTouch;
import timber.log.Timber;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends BaseActivity implements MainMvpView,
        CategoryDialogFragment.CategoryFragmentListener, CategoryAdapter.Callback,
        SearchView.OnQueryTextListener, PopupMenu.OnMenuItemClickListener {

    private static final int PICKED_IMAGE = 1;
    private static final int CAPTURED_IMAGE = 2;

    @Inject MainPresenter mMainPresenter;
    @Inject TransactionAdapter mTransactionAdapter;
    @Inject CategoryAdapter mCategoryAdapter;
    @Inject PreferencesHelper mPreferencesHelper;

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
    @BindView(R.id.rootListView) RelativeLayout mRootListView;
    @BindView(R.id.et_add_new_balance) EditText mAddNewEditText;
    @BindView(R.id.button_date_done) Button mDateDoneButton;
    @BindView(R.id.date_picker) DatePicker mDatePicker;
    @BindView(R.id.searchView) SearchView mSearchView;
    @BindView(R.id.nestedScrollView) NestedScrollView mScrollView;
    @BindView(R.id.current_selected_date_textview) TextView mCurrentSelectedDateTV;
    @BindView(R.id.spinner_chooserList) Spinner mChooserDateSpinner;
    @BindView(R.id.previous_arrow_ImageView) ImageView mPreviousArrowIV;
    @BindView(R.id.next_arrow_ImageView) ImageView mNextArrowIV;
    @BindView(R.id.imageview_add_attachment) ImageView mAddAttachmentIV;
    @BindView(R.id.delete_imageView) ImageView mDeleteImageView;
    @BindView(R.id.select_all_check_box) CheckBox mSelectAllCheckBox;

    private PopupMenu mAttachmentPopupMenu;

    private long mSelectedCategoryId = -1;
    // The uri string of selected attachment.
    private String mSelectedAttachment = null;
    // The uri string of captured image.
    private String mCapturedImagePath = null;
    // For searching date in database.
    private Calendar mCurrentDateCalendar;
    private Calendar mSelectedCalendar;

    private BroadcastReceiver mBroadcastReceiver = null;
    private boolean mIsDeleteSelected = false;

    private boolean mIsFirstTimeLoadingTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupUI();

        if (mPreferencesHelper.isSync()) {
            // Start the sync service
            startService(new Intent(this, SyncService.class));
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mScrollView.post(() -> mScrollView.scrollTo(0, mSearchView.getBottom()));
    }

    private void setupUI() {
        String currencyName = mMainPresenter.getCurrencyName();
        mCurrencyBottomTextView.setText(currencyName);
        mCurrencyTextView.setText(currencyName);
        mTransactionAdapter.setCurrencyName(currencyName);
        mSearchView.setOnQueryTextListener(this);

        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_currency_item,
                getResources().getStringArray(R.array.transaction_spinner_value));
        mChooserDateSpinner.setAdapter(spinnerAdapter);

        mTransactionAdapter.setContext(this);
        mTransactionsRecyclerView.setAdapter(mTransactionAdapter);
        mTransactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCategoryRecyclerView.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setCallback(this);
        mMainPresenter.attachView(this);
        mIsFirstTimeLoadingTransactions = true;
        mMainPresenter.loadTransactions();
        mMainPresenter.loadCategories();
        mCurrentDateCalendar = Calendar.getInstance();
        // PopupMenu for attachment (take a new picture, or choose from gallery)
        mAttachmentPopupMenu = new PopupMenu(this, mAddAttachmentIV);
        mAttachmentPopupMenu.getMenuInflater().inflate(R.menu.popup_menu_select_pictures,
                mAttachmentPopupMenu.getMenu());
        mAttachmentPopupMenu.setOnMenuItemClickListener(this);
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

        // Register the broadcast from SyncService manually onResume
        if (mPreferencesHelper.isSync()) {
            if (mBroadcastReceiver == null) {
                mBroadcastReceiver = new SyncService.CheckConnectivity();
            }
            // @link : https://developer.android.com/guide/components/broadcasts.html#context-registered_receivers
            registerReceiver(mBroadcastReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the broadcast (it improves performance).
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    /**
     * Toggle plus text view to minus.
     */
    private void togglePlusTextView() {
        if (mCurrencyPlusTextView.getText().equals("+")) {
            mCurrencyPlusTextView.setText("-");
        } else {
            mCurrencyPlusTextView.setText("+");
        }
    }
    /**
     * Show/hide date Layout when the button is clicked
     * @param showDate : true => show the date layout, false => don't show it.
     */
    private void showDateLayout(boolean showDate) {
        mBottomLayout.setVisibility(showDate ? View.GONE : View.VISIBLE);
        mDateBottomLayout.setVisibility(showDate ? View.VISIBLE : View.GONE);
        showKeyboard(!showDate);
    }
    /**
     * Change the bottom bar icons and make the EditText focusable.
     * @param isShown : true -> show bottomLayout / false -> hide it.
     */
    private void expandBottomLayout(boolean isShown) {
        if (isShown) {
            mAddNewEditText.setText("");
            mCategoryAdapter.resetSelectedCategories();
            mSelectedCategoryId = -1;
            mSelectedAttachment = null;
        }
        // Icons
        mBottomExpandedLayout.setVisibility(isShown ? View.VISIBLE : View.GONE);
        mBottomLayout.setVisibility(isShown ? View.GONE : View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) mRootListView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ABOVE,
                isShown ? R.id.bottom_layout_expanded : R.id.bottom_layout);
        // Set the EditText focusable and show/hide the keyboard
        showKeyboard(isShown);
    }
    /**
     * @param show: show or hide the keyboard
     */
    private void showKeyboard(boolean show) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show) {
            mAddNewEditText.requestFocus();
            imm.showSoftInput(mAddNewEditText, 0);
        } else {
            mAddNewEditText.clearFocus();
            imm.hideSoftInputFromWindow(mAddNewEditText.getWindowToken(), 0);
        }
    }
    /**
     * Show top layout with arrows.
     * @param show : show layout when its true. or hide it when its false.
     */
    private void showTopLayout(boolean show) {
        mPreviousArrowIV.setVisibility(show ? View.VISIBLE : View.GONE);
        mNextArrowIV.setVisibility(show ? View.VISIBLE : View.GONE);
        mCurrentSelectedDateTV.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    /**
     * on touch recycler view make the edit text focus off and hide the keyboard.
     */
    @OnTouch(R.id.transaction_recycler_view)
    public boolean onTouchRecycleView(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && mAddNewEditText.isFocused()) {
            expandBottomLayout(false);
        }
        return false;
    }

    @OnTouch(R.id.nestedScrollView)
    public boolean onTouchNestedScrollView(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && mAddNewEditText.isFocused()) {
            expandBottomLayout(false);
        }
        return false;
    }

    private void addTransaction() {
        // Don't do transaction upon empty string.
        if (mAddNewEditText.getText().toString().equals("")) {
            expandBottomLayout(false);
            return;
        }
        float amount = Float.parseFloat(mAddNewEditText.getText().toString());
        String dateFormat = mDatePicker.getYear() + "-" +
                (mDatePicker.getMonth() + 1) + "-" + mDatePicker.getDayOfMonth();

        Transaction newTransaction = new Transaction(
                dateFormat,
                mCurrencyPlusTextView.getText().equals("+") ? amount : -1 * amount,
                mSelectedAttachment);
        mMainPresenter.addTransaction(newTransaction, mSelectedCategoryId);
        expandBottomLayout(false);
    }

    @OnEditorAction(R.id.et_add_new_balance)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            addTransaction();
            return false;
        }
        return true;
    }
    /**
     * Create Image file for Capturing a picture from Camera.
     */
    private File createImageFile() throws IOException {
        // Create an image file for captured image.
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "MoneyManager_" + timeStamp;
        return File.createTempFile(imageFileName, ".jpg", storageDir);
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
    void chooserListSelectedItem(AdapterView<?> parentView,
                                 View selectedItemView, int position, long id) {
        switch (position) {
            // Total Screen
            case 0:
                showTopLayout(false);
                // Do not load Transactions on First Time running the application. only
                // load it when Spinner is selected.
                if (mIsFirstTimeLoadingTransactions) {
                    mIsFirstTimeLoadingTransactions = false;
                    break;
                }
                mCurrentSelectedDateTV.setText("");
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
    /**
     * Search data from the db.
     * @param isDailyOrMonthlyOrYearly: 0 -> daily, 1 -> Monthly, 2 -> yearly.
     */
    private void updateTransactionList(int isDailyOrMonthlyOrYearly) {
        mMainPresenter.searchTransactionWithDate(
                mSelectedCalendar.get(Calendar.DATE),
                mSelectedCalendar.get(Calendar.MONTH) + 1,
                mSelectedCalendar.get(Calendar.YEAR),
                isDailyOrMonthlyOrYearly);
    }

    @OnCheckedChanged(R.id.select_all_check_box)
    void SelectAllOnChange(CompoundButton compoundButton, boolean check) {
        if (check) {
            // select all items
            mTransactionAdapter.checkAllCheckBoxes();
        } else {
            // select non
            mTransactionAdapter.checkNonCheckBoxes();
        }
        mTransactionAdapter.notifyDataSetChanged();
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
        if (!mAddNewEditText.isFocused()) {
            expandBottomLayout(true);
        }
    }

    @OnClick(R.id.imageview_date)
    public void onClickDateIV() {
        if (mAddNewEditText.isFocused()) {
            showDateLayout(true);
        }
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

    @OnClick(R.id.delete_imageView)
    public void onClickDeleteImageView() {
        mIsDeleteSelected = !mIsDeleteSelected;

        if (mIsDeleteSelected) {
            // Show the 'select all checkbox'
            mSelectAllCheckBox.setVisibility(View.VISIBLE);


            mDeleteImageView.setColorFilter(ContextCompat.getColor(this, R.color.red),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            // Show checkboxes in the list view.
            mTransactionAdapter.showCheckbox(true);
            mTransactionAdapter.notifyDataSetChanged();
        } else {
            mSelectAllCheckBox.setVisibility(View.GONE);
            mDeleteImageView.setColorFilter(null);
            mTransactionAdapter.showCheckbox(false);
            mTransactionAdapter.notifyDataSetChanged();

            // check if selected items are empty
            long[] selectedItem = mTransactionAdapter.getSelectedItems();
            if (selectedItem.length == 0) {
                return;
            }
            // Delete transactions from db and api.
            mMainPresenter.deleteTransactions(selectedItem);
        }
    }

    /**
     *
     * @param arrowDirection false -> previous arrow, true -> next arrow.
     */
    private void onClickedArrows(boolean arrowDirection) {

        boolean isCurrentDateTextSet = false;

        // Daily is selected
        if (mChooserDateSpinner.getSelectedItemPosition() == 1) {

            mSelectedCalendar.add(Calendar.DATE, arrowDirection ? 1 : -1);
            // checking for today.
            if (mCurrentDateCalendar.get(Calendar.YEAR) == mSelectedCalendar.get(Calendar.YEAR) &&
                    mCurrentDateCalendar.get(Calendar.MONTH) ==
                            mSelectedCalendar.get(Calendar.MONTH) &&
                    mSelectedCalendar.get(Calendar.DATE) ==
                            mCurrentDateCalendar.get(Calendar.DATE)) {

                mCurrentSelectedDateTV.setText(getString(R.string.today));
                isCurrentDateTextSet = true;
            }
            // Note: tomorrow or yesterday can be in another months/year
            // Check for tomorrow
            mCurrentDateCalendar.add(Calendar.DATE, 1);

            if (mCurrentDateCalendar.get(Calendar.YEAR) == mSelectedCalendar.get(Calendar.YEAR) &&
                    mCurrentDateCalendar.get(Calendar.MONTH) ==
                            mSelectedCalendar.get(Calendar.MONTH) &&
                    mSelectedCalendar.get(Calendar.DATE) ==
                            mCurrentDateCalendar.get(Calendar.DATE)) {

                mCurrentSelectedDateTV.setText(getString(R.string.tomorrow));
                isCurrentDateTextSet = true;
            }

            // Yesterday
            mCurrentDateCalendar.add(Calendar.DATE, -2);

            if (mCurrentDateCalendar.get(Calendar.YEAR) == mSelectedCalendar.get(Calendar.YEAR) &&
                    mCurrentDateCalendar.get(Calendar.MONTH) ==
                            mSelectedCalendar.get(Calendar.MONTH) &&
                    mSelectedCalendar.get(Calendar.DATE) ==
                            mCurrentDateCalendar.get(Calendar.DATE)) {

                mCurrentSelectedDateTV.setText(getString(R.string.yesterday));
                isCurrentDateTextSet = true;
            }

            // Set it back to today's date.
            mCurrentDateCalendar.add(Calendar.DATE, 1);

        // Monthly is selected.
        } else if (mChooserDateSpinner.getSelectedItemPosition() == 2) {

            mSelectedCalendar.add(Calendar.MONTH, arrowDirection ? 1 : -1);

            // This month
            if (mCurrentDateCalendar.get(Calendar.YEAR) == mSelectedCalendar.get(Calendar.YEAR) &&
                    mCurrentDateCalendar.get(Calendar.MONTH) ==
                            mSelectedCalendar.get(Calendar.MONTH)) {

                mCurrentSelectedDateTV.setText(getString(R.string.this_month));
                isCurrentDateTextSet = true;
            }
            // Next month
            mCurrentDateCalendar.add(Calendar.MONTH, 1);
            if (mCurrentDateCalendar.get(Calendar.YEAR) == mSelectedCalendar.get(Calendar.YEAR) &&
                    mCurrentDateCalendar.get(Calendar.MONTH) ==
                            mSelectedCalendar.get(Calendar.MONTH)) {

                mCurrentSelectedDateTV.setText(getString(R.string.next_month));
                isCurrentDateTextSet = true;
            }
            // Prev month
            mCurrentDateCalendar.add(Calendar.MONTH, -2);
            if (mCurrentDateCalendar.get(Calendar.YEAR) == mSelectedCalendar.get(Calendar.YEAR) &&
                    mCurrentDateCalendar.get(Calendar.MONTH) ==
                            mSelectedCalendar.get(Calendar.MONTH)) {

                mCurrentSelectedDateTV.setText(getString(R.string.pre_month));
                isCurrentDateTextSet = true;
            }
            // Set the month back
            mCurrentDateCalendar.add(Calendar.MONTH, 1);

        // Yearly is selected.
        } else if (mChooserDateSpinner.getSelectedItemPosition() == 3) {

            mSelectedCalendar.add(Calendar.YEAR, arrowDirection ? 1 : -1);

            // This month
            if (mCurrentDateCalendar.get(Calendar.YEAR) == mSelectedCalendar.get(Calendar.YEAR)) {

                mCurrentSelectedDateTV.setText(getString(R.string.this_year));
                isCurrentDateTextSet = true;

            } else if ((mSelectedCalendar.get(Calendar.YEAR)) ==
                    mCurrentDateCalendar.get(Calendar.YEAR) + 1) {

                mCurrentSelectedDateTV.setText(getString(R.string.next_year));
                isCurrentDateTextSet = true;

            } else if ((mSelectedCalendar.get(Calendar.YEAR)) ==
                    mCurrentDateCalendar.get(Calendar.YEAR) - 1) {

                mCurrentSelectedDateTV.setText(getString(R.string.pre_year));
                isCurrentDateTextSet = true;
            }
        }

        if (!isCurrentDateTextSet) mCurrentSelectedDateTV.setText(getString(R.string.setDate,
                mSelectedCalendar.get(Calendar.DATE), mSelectedCalendar.get(Calendar.MONTH) + 1,
                mSelectedCalendar.get(Calendar.YEAR)));
        // isDailyOrMonthlyOrYearly: 0 -> daily, 1 -> Monthly, 2 -> yearly.
        int isDailyOrMonthlyOrYearly = mChooserDateSpinner.getSelectedItemPosition() - 1;
        updateTransactionList(isDailyOrMonthlyOrYearly);
    }

    /***** attachment section *****/
    @OnClick(R.id.imageview_add_attachment)
    public void onClickAddAttachment(View view) {
        mAttachmentPopupMenu.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (data == null) {
                return;
            }

            if (requestCode == PICKED_IMAGE ) {
                mSelectedAttachment = data.getData().toString();
            } else if (requestCode == CAPTURED_IMAGE) {
                mSelectedAttachment = mCapturedImagePath;
            }
        }
    }
    /**
     * Popup Menu {@link #mAttachmentPopupMenu} Item clicks
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            // Taking a new Picture
            case R.id.take_new_picture:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // This check is required to prevent crashing.
                if (intent.resolveActivity(getPackageManager()) == null) {
                    break;
                }
                // Create an image file for captured image.
                File imageFile = null;
                if (!PermissionChecker.verifyStoragePermissions(this)) {
                    break;
                }

                try {
                    imageFile = createImageFile();
                } catch (IOException e) {
                    DialogFactory.createGenericErrorDialog(this,
                            "Cannot create a file,\nReason: " + e.getMessage()).show();
                }

                if (imageFile == null) {
                    break;
                }

                Uri imageURI = FileProvider.getUriForFile(this,
                        "com.hulkdx.moneymanagerv2.fileprovider", imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(intent, MainActivity.CAPTURED_IMAGE);
                // Save the image Uri to later get it from onActivityResult, because
                // OnActivityResult return empty intent by putting EXTRA_OUTPUT.
                mCapturedImagePath = imageURI.toString();
                break;
            // Select from galary
            case R.id.choose_gallery:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) == null) {
                    break;
                }
                startActivityForResult(intent, MainActivity.PICKED_IMAGE);
                break;
        }
        return true;
    }

    /***** Callback methods implementation *****

     **
     * It gets the Category data from {@link CategoryDialogFragment}
     * and send it to MainPresenter.
     */
    @Override
    public void onClickOkCategory(Category category) {
        mMainPresenter.addCategory(category);
    }
    /**
     * on clicking category items in categoryAdapter.
     */
    @Override
    public void onCategoryClicked(long categoryId) {
        mSelectedCategoryId = categoryId;
    }
    /**
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
    public void showTransactions(List<Transaction> transactions) {
        mEmptyListTextView.setVisibility(transactions.size() == 0 ? View.VISIBLE : View.GONE);
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
    public void addCategoryCompleted() {
        mCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String functionName, Throwable error) {
        Timber.i("onError %s : %s", functionName, error.toString());
        DialogFactory.createGenericErrorDialog(this, error.toString()).show();
    }

    @Override
    public void onCompleteAddTransactions() {
        mEmptyListTextView.setVisibility(View.GONE);
        mSearchView.setQuery("", false);
    }

    @Override
    public void showErrorDeleteTransactions() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_delete_transactions))
                .show();
    }

    @Override
    public void deleteTransactionsComplete(boolean isEmpty) {
        mEmptyListTextView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        mTransactionAdapter.notifyDataSetChanged();
    }

}
