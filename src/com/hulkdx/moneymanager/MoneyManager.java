package com.hulkdx.moneymanager;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MoneyManager extends ActionBarActivity implements ChangeMoneyDialog.Communicator {

	TextView totalMoneyTextView;
	TextView earnedTextView;
	TextView spentTextView;
	SharedPreferences sp;
	int balance = 0;
	int earned = 0;
	int spent = 0;
	
	//DataBaseTransactionAdapter dbTransaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_money_manager);

		// load name
		sp = getSharedPreferences("MyDataName", Context.MODE_PRIVATE);
		String name = sp.getString("name", "");
		balance = sp.getInt("totalMoney", 0);
		earned = sp.getInt("earned", 0);
		spent = sp.getInt("spent", 0);
		boolean isFirstRunning = sp.getBoolean("firstTime", true);
		HulkDataBaseAdapter db = new HulkDataBaseAdapter(this);

		// if name is not exist go to MainActivity activity To create a name
		if (name.equals("")) {
			// add some date to category Table
			db.insertDataCategoryTable("Shopping");
			db.insertDataCategoryTable("Foods and Drinks");
			db.insertDataCategoryTable("Educations");
			
			// go to MainActivity activity
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			finish();
		}
		// if Name is exist change textView
		else {
			TextView textViewName = (TextView) findViewById(R.id.nametext);
			totalMoneyTextView = (TextView) findViewById(R.id.totalMoney);
			earnedTextView = (TextView) findViewById(R.id.earnedTextView);
			spentTextView = (TextView) findViewById(R.id.spentTextView);
			textViewName.setText("User : " + name);
			
			// for first time running equal earned to balance
			if (isFirstRunning) {
				earned = balance;

				SharedPreferences.Editor editor = sp.edit();
				editor.putBoolean("firstTime", false);
				editor.putInt("earned", earned);
				editor.commit();
			}
			
			// If Intent from AddNewTransaction Class exist
			if (getIntent().getExtras() != null) 
			{
				// Increase or reduce money from total money
				int newMoney = getIntent().getExtras().getInt("Amount");
				boolean isExpense = getIntent().getExtras().getBoolean("isExpense");
				
				if (isExpense) {
					balance = balance - newMoney;
					spent += newMoney;
					SharedPreferences.Editor editor = sp.edit();
					editor.putInt("spent", spent);
					editor.commit();
				} else {
					balance = balance + newMoney;
					earned += newMoney;
					SharedPreferences.Editor editor = sp.edit();
					editor.putInt("earned", earned);
					editor.commit();
				}
				
				// save to database
				SharedPreferences.Editor editor = sp.edit();
				editor.putInt("totalMoney", balance);
				editor.commit();
			}
			
			// setting the (total money, spent, earned) Text View
			totalMoneyTextView.setText(String.valueOf(balance));
			spentTextView.setText(String.valueOf(spent));
			earnedTextView.setText(String.valueOf(earned));
			
			// Setting the list View from the table
			LinearLayout transLinear = (LinearLayout) findViewById(R.id.addItemHere);
			int countdb = 0;
			
			TextView tv = new TextView(this);
			tv.setText("You have No transaction");
			tv.setId(10);
			transLinear.addView(tv);
			
			for (String s : db.selectCatTransactionTable()) {
				
				// TODO if No Transaction text is exist just remove it
				TextView NoTrsTxtView = (TextView) findViewById(10);
				if (NoTrsTxtView != null){
					Toast.makeText(this, "yea", Toast.LENGTH_SHORT).show();
				}
				
				// TODO if it is expanse
				if (db.selectExpenseTransactionTable()[countdb]){

				} 
				// if its not
				else {
					
				}
				countdb++;
			}
			
			// TODO if DB is empty TODO set 3 to 0
			if (countdb == 0){
				// TODO remove comments Create a TextView with id = 10
				//TextView tv = new TextView(this);
				//tv.setText("You have No transaction");
				//tv.setId(10);
				
				//transLinear.addView(tv);
			}
		}
	}

	// clicking on change for total money
	public void changeTotalMoney(View v) {
		// Fragment Dialog
		FragmentManager manager = getFragmentManager();
		ChangeMoneyDialog myDialog = new ChangeMoneyDialog();
		myDialog.show(manager, "changeMoneyDialog");

	}
	
	// Communication function
	@Override
	public void onDialogMessage(String money) {
		// change total amount of money in TextView(totalMoney)
		totalMoneyTextView.setText(money);
		// save totalMoney
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("totalMoney", money);
		editor.commit();
	}

	// clicking on plus sign
	public void startTransaction(View v) {
		// redirect to new activity
		Intent i = new Intent(this, AddNewTransaction.class);
		startActivity(i);
	}

}
