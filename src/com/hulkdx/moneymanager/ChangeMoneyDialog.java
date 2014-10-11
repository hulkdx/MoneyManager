package com.hulkdx.moneymanager;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ChangeMoneyDialog extends DialogFragment implements View.OnClickListener {
	
	private static final String TITLE = "Change My Money";
	Button okButton;
	EditText moneyEditText;
	
	//define communication
	Communicator comm;
	interface Communicator {
		public void onDialogMessage(String money);
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		comm = (Communicator) activity;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// set title for this dialog
		getDialog().setTitle(TITLE);
		
		View view = inflater.inflate(R.layout.my_change_money_dialog, null);
		
		// initiation
		okButton = (Button) view.findViewById(R.id.okdialog);
		moneyEditText = (EditText) view.findViewById(R.id.moneyEditText);
		
		okButton.setOnClickListener(this);
		//setCancelable(false);
				
		return view;
	}
	
	// on clicking OK button
	@Override
	public void onClick(View v) {
		comm.onDialogMessage(moneyEditText.getText().toString());
		dismiss();
	}
	
	
	
}
