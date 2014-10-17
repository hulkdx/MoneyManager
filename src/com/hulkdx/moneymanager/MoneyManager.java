package com.hulkdx.moneymanager;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MoneyManager extends ActionBarActivity implements ChangeMoneyDialog.Communicator {

	TextView totalMoneyTextView;
	TextView earnedTextView;
	TextView spentTextView;
	ListView transactionListV;

	int balance = 0;
	int earned = 0;
	int spent = 0;

	HulkDataBaseAdapter db;
	SharedPreferences sp;

	int countdbTransaction;

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
		db = new HulkDataBaseAdapter(this);

		// if name is not exist go to MainActivity activity To create a name
		if (name.equals("")) {
			// init Category DB
			initCatDB();

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
			transactionListV = (ListView) findViewById(R.id.transactionData1);
			textViewName.setText("User : " + name);

			// for first time running equal earned to balance
			if (isFirstRunning) {
				earned = balance;

				SharedPreferences.Editor editor = sp.edit();
				editor.putBoolean("firstTime", false);
				editor.putInt("earned", earned);
				editor.commit();
			}

			// If pressing Add button from AddNewTransaction Class exist
			int amountTransactionIntent = 0;
			boolean expanseTransactionIntent = false;
			boolean isTransactionIntent = false;
			if (getIntent().getExtras() != null) {
				// if it is Transaction Intent
				isTransactionIntent = getIntent().getExtras().getBoolean("isTransaction");
				if (isTransactionIntent) {
					// Increase or reduce money from total money
					amountTransactionIntent = getIntent().getExtras().getInt("Amount");
					expanseTransactionIntent = getIntent().getExtras().getBoolean("isExpense");

					if (expanseTransactionIntent) {
						balance = balance - amountTransactionIntent;
						spent += amountTransactionIntent;
						SharedPreferences.Editor editor = sp.edit();
						editor.putInt("spent", spent);
						editor.commit();
					} else {
						balance = balance + amountTransactionIntent;
						earned += amountTransactionIntent;
						SharedPreferences.Editor editor = sp.edit();
						editor.putInt("earned", earned);
						editor.commit();
					}
					// save to database
					SharedPreferences.Editor editor = sp.edit();
					editor.putInt("totalMoney", balance);
					editor.commit();
				}
			}

			// setting the (total money, spent, earned) Text View
			totalMoneyTextView.setText(String.valueOf(balance));
			spentTextView.setText(String.valueOf(spent));
			earnedTextView.setText(String.valueOf(earned));

			// Setting the Transaction Views from the table
			CustomAdapter adapter;
			// initialization
			String[] catArray = null;
			String[] amountArray = null;
			boolean[] expenseArray = null;
			countdbTransaction = db.selectCatTransactionTable().length;
			// if DB is not empty
			if (countdbTransaction != 0) {
				// initialization
				catArray = new String[countdbTransaction];
				amountArray = new String[countdbTransaction];
				expenseArray = new boolean[countdbTransaction];
				// set cat amount expanse to array
				for (int i = 0, c = countdbTransaction; i < countdbTransaction; i++) {
					String catThis = String.valueOf(db.selectCatTransactionTable()[i]);
					String amountThis = String.valueOf(db.selectAmountTransactionTable()[i]);
					boolean expenseThis = db.selectExpenseTransactionTable()[i];
					// saving in reverse format
					c--;
					catArray[c] = catThis;
					amountArray[c] = amountThis;
					expenseArray[c] = expenseThis;
				}
				// set the adapter and show it
				adapter = new CustomAdapter(this, catArray, amountArray, expenseArray);
				transactionListV.setAdapter(adapter);
			}
			// if Transaction DB is empty
			else {
				String[] noString = { "NOTHING IN DATABASE" };
				ArrayAdapter<String> adapterNo = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noString);
				transactionListV.setAdapter(adapterNo);
			}
		}
	}

	// initialization category database
	public void initCatDB() {
		// add some date to category Table
		db.insertDataCategoryTable("Shopping");
		db.insertDataCategoryTable("Foods and Drinks");
		db.insertDataCategoryTable("Educations");
		db.insertDataCategoryTable("House");
		db.insertDataCategoryTable("Other");
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

		/*
		 * reset all of informations 1. set the earned to Total Money 2. change
		 * the local database for earned and ...
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		boolean handle;
		switch (id) {
		case R.id.resetMoney:
			handle = true;
			FragmentManager manager = getFragmentManager();
			ChangeMoneyDialog myDialog = new ChangeMoneyDialog();
			myDialog.show(manager, "changeMoneyDialog");

		case R.id.resetTrans:
			handle = true;
			if (countdbTransaction != 0) {
				// Remove all DB
				db.deleteAllTransactionTable();
				// set list view to nothing
				String[] noString = { "NOTHING IN DATABASE" };
				ArrayAdapter<String> adapterNo = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noString);
				transactionListV.setAdapter(adapterNo);
			}

		case R.id.resetAll:
			handle = true;
			if (countdbTransaction != 0) {
				// Remove Transaction Table
				db.deleteAllTransactionTable();
				// set list view to nothing
				String[] noString = { "NOTHING IN DATABASE" };
				ArrayAdapter<String> adapterNo = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noString);
				transactionListV.setAdapter(adapterNo);
			}
			// Remove Category Table
			db.deleteAllCategoryTable();
			// delete shared preferences
			sp.edit().clear().commit();
			// Restart application
			Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);

		default:
			handle = super.onOptionsItemSelected(item);
		}
		return handle;
	}

	// TODO Remove if after finished...
	public void message(String s) {
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}
}

class CustomAdapter extends ArrayAdapter<String> {
	Context context;
	String[] categoryArray;
	String[] amountArray;
	boolean[] expanseB;

	public CustomAdapter(Context c, String[] categoty, String[] amount, boolean[] expanse) {
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
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.list_view_in_money_manager, parent, false);
		}

		TextView category = (TextView) row.findViewById(R.id.textView1x);
		TextView amount = (TextView) row.findViewById(R.id.textView2x);

		category.setText(categoryArray[position]);
		amount.setText(amountArray[position]);

		if (expanseB[position]) {
			amount.setTextColor(Color.parseColor("#FFFF4444"));
		} else {
			amount.setTextColor(Color.parseColor("#0000FF"));
		}

		return row;
	}
}
