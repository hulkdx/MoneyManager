package com.hulkdx.moneymanager;

import java.util.ArrayList;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AddNewTransactionCategory extends ActionBarActivity implements AddNewCategoryDialog.Communicator, OnItemClickListener {
	
	ListView listView;
	HulkDataBaseAdapter db;
	ArrayList<String> categoryString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_transaction_category);
		
		listView = (ListView) findViewById(R.id.categoryListView);
		db = new HulkDataBaseAdapter(this);
		
		// receiving category data from DataBase
		categoryString = db.getAllDataCategoryTable();
		// if category is empty just in case
		if (categoryString.isEmpty()) {
			db.insertDataCategoryTable("Shopping");
			categoryString.add("Shopping");
		}
		
		// Set the adapter and list view
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryString);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(this);
	}
	
	public void addCategory(View v){
		FragmentManager manager = getFragmentManager();
		AddNewCategoryDialog mydialog = new AddNewCategoryDialog();
		mydialog.show(manager, "AddNewCategoryDialog");
	}
	
	// on click add button in dialog (Communication metod)
	@Override
	public void onClickAdd(String category) {
		// add to table
		db.insertDataCategoryTable(category);
		
		// update the list view
		categoryString.add(category);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryString);
		listView.setAdapter(adapter);
	}

	// on click each item in list view
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i = new Intent(this, AddNewTransaction.class);
		TextView textView = (TextView) arg1;
		i.putExtra("category", textView.getText().toString());
		
		// give back the same amount and isExpense value
		if (getIntent().getExtras() != null) {
			i.putExtra("AMOUNT", getIntent().getExtras().getString("AMOUNT", ""));
		}
		
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}
}
