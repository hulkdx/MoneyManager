package com.hulkdx.moneymanager;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MoneyManager extends ActionBarActivity implements
		ChangeMoneyDialog.Communicator {

	TextView totalMoneyTextView;
	TextView earnedTextView;
	TextView spentTextView;
	SharedPreferences sp;
	int balance = 0;
	int earned = 0;
	int spent = 0;

	// DataBaseTransactionAdapter dbTransaction;

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
			
			// Setting the Transaction Views from the table
			ListView transactionListV = (ListView) findViewById(R.id.transactionData1);
			CustomAdapter adapter;
			// initialization
			String[] catArray = null;
			String[] amountArray = null;
			boolean[] expenseArray = null;
			int countdb = db.selectCatTransactionTable().length;
			if (countdb != 0){
				catArray = new String[countdb];
				amountArray = new String[countdb];
				expenseArray = new boolean[countdb];
			}
			// TODO CHAnge the order
			countdb = 0;
			// Setting the arrays for the custom adapter
			for (String catThis : db.selectCatTransactionTable()) {
				String amountThis = String.valueOf(db.selectAmountTransactionTable()[countdb]);
				boolean expenseThis = db.selectExpenseTransactionTable()[countdb];
				
				catArray[countdb] = catThis;
				amountArray[countdb] = amountThis;
				expenseArray[countdb] = expenseThis;
				
				countdb++;
			}
			// if Transaction DB is empty
			if (countdb == 0){
				String[] noString = {"NOTHING IN DATABASE"};
				ArrayAdapter<String> adapterNo = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noString);
				transactionListV.setAdapter(adapterNo);
			} 
			// if its not empty then set the custom adapter
			else { 
				adapter = new CustomAdapter(this, catArray, amountArray, expenseArray);
				transactionListV.setAdapter(adapter);
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
	public void onDialogMessage(int money) {
		// change total amount of money in TextView(totalMoney)
		String stringMoney = String.valueOf(money);
		totalMoneyTextView.setText(stringMoney);
		// save totalMoney
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("totalMoney", money);
		
		
		/* reset all of informations
		 * 1. set the earned to Total Money
		 * 2. change the local database for earned and ...
		 */
		earnedTextView.setText(stringMoney);
		spentTextView.setText("0");
		editor.putInt("earned", money);
		editor.putInt("spent", 0);
		editor.commit();
		
		
	}

	// clicking on plus sign
	public void startTransaction(View v) {
		// redirect to new activity
		Intent i = new Intent(this, AddNewTransaction.class);
		startActivity(i);
	}
}

class CustomAdapter extends ArrayAdapter<String> {
	Context context;
	String[] categoryArray;
	String[] amountArray;
	boolean[] expanseB;

	public CustomAdapter(Context c, String[] categoty, String[] amount,
			boolean[] expanse) {
		super(c, R.layout.list_view_in_money_manager, R.id.textView1x, categoty);
		this.context = c;
		this.amountArray = amount;
		this.categoryArray = categoty;
		this.expanseB = expanse;
	}
	
	int count = 0;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.list_view_in_money_manager, parent,
					false);
		}

		TextView category = (TextView) row.findViewById(R.id.textView1x);
		TextView amount = (TextView) row.findViewById(R.id.textView2x);

		category.setText(categoryArray[position]);
		amount.setText(amountArray[position]);
		
		if (expanseB[position]){
			amount.setTextColor(Color.parseColor("#FFFF4444"));
		}
		else {
			amount.setTextColor(Color.parseColor("#0000FF"));
		}

		return row;
	}
}
