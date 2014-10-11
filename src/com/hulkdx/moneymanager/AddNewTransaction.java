package com.hulkdx.moneymanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewTransaction extends ActionBarActivity implements OnTouchListener {
	private static final String COLOR_AFTER_CLICKING_CATEGORY = "#000000";
	private static final String COLOR_BEFORE_CLICKING_CATEGORY = "#808080";
	private static final String COLOR_WHILE_CLICKING = "#0000FF";
	
	TextView categoryTextView;
	EditText amountEditText;
	RadioButton rbExpense;
	RadioButton rbIncome;
	
	// this is for setting the color after category picked and checking for clicking
	Boolean isClickedOnCategory = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_transaction);
		
		categoryTextView = (TextView) findViewById(R.id.categoryTextView);
		amountEditText = (EditText) findViewById(R.id.amountEditText);
		rbExpense = (RadioButton) findViewById(R.id.radioButton1);
		rbIncome = (RadioButton) findViewById(R.id.radioButton2);
		
		categoryTextView.setOnTouchListener(this);
		
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
			if (!setEditText.isEmpty()){
				amountEditText.setText(setEditText);
				amountEditText.setSelection(amountEditText.getText().length());
			}
		}
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
			startActivity(i);
			
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (isClickedOnCategory){
				// set to black color
				categoryTextView.setTextColor(Color.parseColor(COLOR_AFTER_CLICKING_CATEGORY));
			} else {
				// set to gray color
				categoryTextView.setTextColor(Color.parseColor(COLOR_BEFORE_CLICKING_CATEGORY));
			}

		}
		return true;
	}
	
	public void onClickAddButton(View v){
		if (!isClickedOnCategory) 
		{
			Toast.makeText(this, "Please choose a Category...", Toast.LENGTH_LONG).show();
		} 
		else 
		{
			// define category, amount, expense
			String category = categoryTextView.getText().toString();
			
			int amount = 0;
			String amountString = amountEditText.getText().toString();
			if (!amountString.isEmpty()){
				amount = Integer.parseInt(amountString);
			}
			if (amount == 0){
				Toast.makeText(this, "Please choose a money", Toast.LENGTH_LONG).show();
			}
			else
			{
				boolean expense = isExpense();
				
				// Save data to database
				HulkDataBaseAdapter helper = new HulkDataBaseAdapter(this);
				helper.insertDataTransactionTable(category, amount, expense);
				
				/* TODO Save data to Money Management Activity
				 * 1. increase or reduce money from total money and save it
				 * 2. add some interfaces
				 * 3. intent to money Manager
				*/
				
				Intent i = new Intent(this, MoneyManager.class);
				i.putExtra("Amount", amount);
				i.putExtra("isExpense", expense);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			}
		}
	}
	
	public boolean isExpense(){
		if (rbExpense.isChecked()) {
			return true;
		} else {
			return false;
		}
	}

}
