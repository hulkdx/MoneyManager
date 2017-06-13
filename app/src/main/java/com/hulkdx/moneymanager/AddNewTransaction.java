package com.hulkdx.moneymanager;

import java.util.Date;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewTransaction extends ActionBarActivity implements OnTouchListener, FragmentChangeDate.Communicator {
	private static final String COLOR_AFTER_CLICKING_CATEGORY = "#000000";
	private static final String COLOR_BEFORE_CLICKING_CATEGORY = "#808080";
	private static final String COLOR_WHILE_CLICKING = "#0000FF";

	TextView categoryTextView;
	EditText amountEditText;
	RadioButton rbExpense;
	TextView dayTodayTextView;
	TextView monthTodayTextView;
	TextView yearTodayTextView;

	// this is for setting the color after category picked and checking for
	// clicking
	Boolean isClickedOnCategory = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_transaction);

		categoryTextView = (TextView) findViewById(R.id.categoryTextView);
		amountEditText = (EditText) findViewById(R.id.amountEditText);
		rbExpense = (RadioButton) findViewById(R.id.radioButton1);
		dayTodayTextView = (TextView) findViewById(R.id.dayToday);
		monthTodayTextView = (TextView) findViewById(R.id.monthToday);
		yearTodayTextView = (TextView) findViewById(R.id.yearToday);

		categoryTextView.setOnTouchListener(this);

		String checkingTextView = "";
		// If extra in intent is not null ( if user clicked on a category )
		if (getIntent().getExtras() != null) {

			String newCategory = getIntent().getExtras().getString("category");
			categoryTextView.setText(newCategory);

			// Styling the text view
			categoryTextView.setTextColor(Color.parseColor(COLOR_AFTER_CLICKING_CATEGORY));
			categoryTextView.setTextSize(25);
			isClickedOnCategory = true;

			// returning value of saved before by setting EditText
			String setEditText = getIntent().getExtras().getString("AMOUNT", "");
			if (!setEditText.isEmpty()) {
				amountEditText.setText(setEditText);
				amountEditText.setSelection(amountEditText.getText().length());
			}
			checkingTextView = getIntent().getExtras().getString("NewDate", "");
		}
		// set Day, Month, Year TextViews
		setDayMonthYearTextView(checkingTextView);
	}
	
	public void setDayMonthYearTextView(String checkingTextView){
		String delims = "[ ]+";
		// setting day, Month, year
		if (!checkingTextView.isEmpty()) {
			String[] newText = checkingTextView.split(delims);
			dayTodayTextView.setText(newText[0]);
			monthTodayTextView.setText(newText[1]);
			yearTodayTextView.setText(newText[2]);
		} else {
			// Date
			Date date = new Date();
			String phrase = (String) DateFormat.format("dd MMMM yyyy", date.getTime());
			// 0 = day, 1 = Month, 2 = Year
			String[] todayDate = phrase.split(delims);
			dayTodayTextView.setText(todayDate[0]);
			monthTodayTextView.setText(todayDate[1]);
			yearTodayTextView.setText(todayDate[2]);
		}
	}

	// On clicking date
	public void onClickDate(View v) {
		// Open a fragment
		FragmentManager manager = getFragmentManager();
		FragmentChangeDate myDialog = new FragmentChangeDate();
		myDialog.show(manager, "changeDate");
	}

	// for clicking category
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// if clicked
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// change color
			categoryTextView.setTextColor(Color.parseColor(COLOR_WHILE_CLICKING));

			// open a new Add New Transaction Category Activity
			Intent i = new Intent(this, AddNewTransactionCategory.class);
			// checking if amount is pressed to transfer
			String amount = amountEditText.getText().toString();
			if (!amount.isEmpty()) {
				i.putExtra("AMOUNT", amount);
			}
			String date = dayTodayTextView.getText().toString() + " " + monthTodayTextView.getText().toString() + " " + yearTodayTextView.getText().toString();
			i.putExtra("NewDate", date);
			startActivity(i);

		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (isClickedOnCategory) {
				// set to black color
				categoryTextView.setTextColor(Color.parseColor(COLOR_AFTER_CLICKING_CATEGORY));
			} else {
				// set to gray color
				categoryTextView.setTextColor(Color.parseColor(COLOR_BEFORE_CLICKING_CATEGORY));
			}

		}
		return true;
	}

	public void onClickAddButton(View v) {
		if (!isClickedOnCategory) {
			Toast.makeText(this, "Please choose a Category...", Toast.LENGTH_LONG).show();
		} else {
			// define category, amount, expense
			String category = categoryTextView.getText().toString();

			int amount = 0;
			String amountString = amountEditText.getText().toString();
			if (!amountString.isEmpty()) {
				amount = Integer.parseInt(amountString);
			}
			if (amount == 0) {
				Toast.makeText(this, "Please choose a money", Toast.LENGTH_LONG).show();
			} else {
				boolean expense = isExpense();
				// Getting date
				String days = dayTodayTextView.getText().toString();
				String years = yearTodayTextView.getText().toString();
				// Convert month to int
				String months = convertToInt(monthTodayTextView.getText().toString());
				String date = years + "-" + months + "-" + days;

				// Save data to database
				HulkDataBaseAdapter helper = new HulkDataBaseAdapter(this);
				helper.insertDataTransactionTable(category, amount, expense, date);

				// Save data to Money Management Activity
				Intent i = new Intent(this, MoneyManager.class);
				i.putExtra("Category", category);
				i.putExtra("Amount", amount);
				i.putExtra("isExpense", expense);
				i.putExtra("isTransaction", true);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			}
		}
	}

	// set Date Text View (Communicator)
	@Override
	public void sentDate(String date) {
		String delims = "[ ]+";
		// todayDate[0] = day, 1 = Month, 2 = Year
		String[] todayDate = date.split(delims);
		// setting day, Month, year
		dayTodayTextView.setText(todayDate[0]);
		monthTodayTextView.setText(todayDate[1]);
		yearTodayTextView.setText(todayDate[2]);
	}

	// Helping Functions
	private String convertToInt(String month) {
		if (month.equals("January")) return "01";
		else if (month.equals("February")) return "02";
		else if (month.equals("March")) return "03";
		else if (month.equals("April")) return "04";
		else if (month.equals("May")) return "05";
		else if (month.equals("June")) return "06";
		else if (month.equals("July")) return "07";
		else if (month.equals("August")) return "08";
		else if (month.equals("September")) return "09";
		else if (month.equals("October")) return "10";
		else if (month.equals("November")) return "11";
		else if (month.equals("December")) return "12";
		else return "Invalid month";
	}

	public boolean isExpense() {
		if (rbExpense.isChecked()) {
			return true;
		} else {
			return false;
		}
	}

	// TODO Remove if after finished...
	public void message(String s) {
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}
}
