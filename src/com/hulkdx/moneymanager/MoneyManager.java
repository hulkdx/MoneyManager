package com.hulkdx.moneymanager;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MoneyManager extends FragmentActivity implements FragmentChangeMoneyDialog.Communicator, OnCheckedChangeListener {

	static final String NOTHING = "No Transaction, add more by clicking below";
	static final String DOLLAR = " $";

	TextView totalMoneyTextView;
	TextView earnedTextView;
	TextView spentTextView;
	ViewPager myViewpager;
	RadioGroup radioGroup1;
	RadioGroup radioGroup2;

	int balance = 0;
	int earned = 0;
	int spent = 0;

	// checking for Radio Group
	boolean rG1;
	boolean rG2;

	HulkDataBaseAdapter db;
	SharedPreferences sp;
	FragmentMoneyTotal frgTotalListView;

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
			// go to MainActivity activity
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			finish();
		}
		// if Name is exist change textView
		else {
			// init
			TextView textViewName = (TextView) findViewById(R.id.nametext);
			totalMoneyTextView = (TextView) findViewById(R.id.totalMoney);
			earnedTextView = (TextView) findViewById(R.id.earnedTextView);
			spentTextView = (TextView) findViewById(R.id.spentTextView);
			radioGroup1 = (RadioGroup) findViewById(R.id.rgroup1);
			radioGroup2 = (RadioGroup) findViewById(R.id.rgroup2);
			myViewpager = (ViewPager) findViewById(R.id.pager);
			// radio Groups Value
			rG1 = false;
			rG2 = false;

			textViewName.setText("User : " + name);
			// for first time running equal earned to balance
			if (isFirstRunning) {
				// init Category DB
				initCatDB();

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
			totalMoneyTextView.setText(String.valueOf(balance) + DOLLAR);
			spentTextView.setText(String.valueOf(spent) + DOLLAR);
			earnedTextView.setText(String.valueOf(earned) + DOLLAR);

			// on clicking radio button radio group
			radioGroup1.setOnCheckedChangeListener(this);
			radioGroup2.setOnCheckedChangeListener(this);
			
			// Create a ViewPager for montly
			FragmentManager fragManag = getSupportFragmentManager();
			myViewpager.setAdapter(new ViewPagerAdapter(fragManag, false));
			myViewpager.setCurrentItem(200);
			
			// defining FragmentManager and FragmentTransaction
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			// Add list View Fragment for total transactions
			if (savedInstanceState == null) {
				// first varible for showing total or by cat
				// second varible for hide(true)/unhide fragment
				frgTotalListView = FragmentMoneyTotal.newInstance(false, false);
				// add it and hide 
				transaction.add(R.id.frames, frgTotalListView, "frag1");
				transaction.hide(frgTotalListView);
				transaction.commit();
			} else {
				// retrive the save one
				frgTotalListView = (FragmentMoneyTotal) getSupportFragmentManager().findFragmentById(R.id.frames);
				transaction.hide(frgTotalListView);
				transaction.commit();
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
		db.insertDataCategoryTable("Salary");
		db.insertDataCategoryTable("Other");
	}

	// Communication function
	@Override
	public void onDialogMessage(int money) {
		// change total amount of money in TextView(totalMoney)
		String stringMoney = String.valueOf(money);
		totalMoneyTextView.setText(stringMoney + DOLLAR);
		// save totalMoney
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("totalMoney", money);

		/*
		 * reset all of informations 1. set the earned to Total Money 2. change
		 * the local database for earned and ...
		 */
		earnedTextView.setText(stringMoney + DOLLAR);
		spentTextView.setText("0" + DOLLAR);
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
		int countdbTransaction = db.selectCatTransactionTable().length;
		int id = item.getItemId();
		boolean handle;
		switch (id) {
		case R.id.resetMoney:
			handle = true;
			//FragmentManager manager = getFragmentManager();
			//FragmentChangeMoneyDialog myDialog = new FragmentChangeMoneyDialog();
			//myDialog.show(manager, "changeMoneyDialog");
			break;

		case R.id.resetTrans:
			handle = true;
			if (countdbTransaction != 0) {
				// Remove all DB
				db.deleteAllTransactionTable();
				// restart the app
				Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
			break;

		case R.id.resetAll:
			handle = true;
			if (countdbTransaction != 0) {
				// Remove Transaction Table
				db.deleteAllTransactionTable();
			}
			// Remove Category Table
			db.deleteAllCategoryTable();
			// delete shared preferences
			sp.edit().clear().commit();
			// Restart application
			Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			break;

		default:
			handle = super.onOptionsItemSelected(item);
		}
		return handle;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// montly or total
		if (group == radioGroup1) {
			switch (checkedId) {
			case R.id.radioButton1:
				rG1 = false;
				rgCheck();
				break;
			case R.id.radioButton2:
				rG1 = true;
				rgCheck();
				break;
			}
		}
		// show all or show by cat
		if (group == radioGroup2) {
			// false : showAll True: show by category
			switch (checkedId) {
			// show all
			case R.id.rg2radioButton1:
				rG2 = false;
				rgCheck();
				break;
			// show by cat
			case R.id.rg2radioButton2:
				rG2 = true;
				rgCheck();
				break;
			}
		}
	}

	// TODO checking for radio group
	public void rgCheck() {
		FragmentManager fragManag = getSupportFragmentManager();
		FragmentTransaction transaction = fragManag.beginTransaction();

		if (!rG1) {
			// 1. hide fragment
			transaction.hide(frgTotalListView);
			transaction.commit();
			// 2. visible myViewpager
			myViewpager.setVisibility(View.VISIBLE);

			if (!rG2) {
				// Montly
				myViewpager.setAdapter(new ViewPagerAdapter(fragManag, false));
				myViewpager.setCurrentItem(200);
			} else {
				// Montly		
				myViewpager.setAdapter(new ViewPagerAdapter(fragManag, true));
				myViewpager.setCurrentItem(200);
			}
		} else {
			// 1. hide myViewpager
			myViewpager.setVisibility(View.GONE);
			// 2. visible fragment
			transaction.show(frgTotalListView);
			if (!rG2) {
				// Total
				// Show it
				frgTotalListView = FragmentMoneyTotal.newInstance(false, false);
				transaction.replace(R.id.frames, frgTotalListView, "frag1");
				transaction.commit();
			} else {
				// Total
				// Show it
				frgTotalListView = FragmentMoneyTotal.newInstance(true, false);
				transaction.replace(R.id.frames, frgTotalListView, "frag1");
				transaction.commit();
			}
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		rgCheck();
	}

	// TODO
	public void msg(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	// View pager for Scroll Slide
	class ViewPagerAdapter extends FragmentStatePagerAdapter {
		boolean radioGroup2Track;
		
		public ViewPagerAdapter(android.support.v4.app.FragmentManager fm, boolean rG) {
			super(fm);
			this.radioGroup2Track = rG;
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			android.support.v4.app.Fragment frag = null;
			frag = FragmentMoneyMontly.newInstance(position - 200, radioGroup2Track);
			return frag;
		}

		@Override
		public int getCount() {
			return 401;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Calendar cal = Calendar.getInstance();
			for (int i = 0; i < 199; i++) {
				if (position == i) {
					cal.add(Calendar.MONTH, i - 200);
					return (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);
				}
			}

			if (position == 199) {
				return "Prev Month";
			}
			// show today month
			if (position == 200) {
				return "This Month";
			}

			if (position == 201) {
				return "Next Month";
			}
			// Show next month
			for (int i = 202; i <= 400; i++) {
				if (position == i) {
					cal.add(Calendar.MONTH, i - 200);
					return (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);
				}
			}
			return null;
		}
	}
}
