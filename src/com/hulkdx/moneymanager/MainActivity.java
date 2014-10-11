package com.hulkdx.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	EditText name;
	EditText money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		name = (EditText) findViewById(R.id.username);
		money = (EditText) findViewById(R.id.editText1);
	}
	
	public void openMain(View v){
		
		String nameString = name.getText().toString();
		String moneyString = money.getText().toString();
		
		
		// checking if name is empty
		if(nameString.isEmpty()){
			Toast.makeText(this, "Please provide a name", Toast.LENGTH_SHORT).show();
		}
		else {
			// checking if money is empty
			if ( moneyString.isEmpty() || moneyString.equals("0") ) {
				Toast.makeText(this, "Please provide your money", Toast.LENGTH_SHORT).show();
			}
			else {
				
				// Save name
				SharedPreferences sp = getSharedPreferences("MyDataName", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("name", nameString);
				editor.putInt("totalMoney", Integer.parseInt(moneyString));
				editor.commit();
				
				
				// Go to MoneyManager activity
				Intent i = new Intent(this, MoneyManager.class);
				startActivity(i);
				finish();
			}
		}
	}

}
