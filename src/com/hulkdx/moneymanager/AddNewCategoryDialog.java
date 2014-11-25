package com.hulkdx.moneymanager;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddNewCategoryDialog extends DialogFragment implements OnClickListener {
	
	private static final String TITLE = "What to add?";
	
	Button addButton;
	EditText textCat;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(TITLE);
		
		View view = inflater.inflate(R.layout.my_category_add_dialog, null);
		
		addButton = (Button) view.findViewById(R.id.button1);
		addButton.setOnClickListener(this);
		
            textCat =  (EditText) view.findViewById(R.id.editText1);
		
		return view;
	}
	
	//define communication
	Communicator comm;
	interface Communicator {
		public void onClickAdd(String category);
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		comm = (Communicator) activity;
	}
	
	
	@Override
	public void onClick(View v) {
		comm.onClickAdd(textCat.getText().toString());
		dismiss();
	}
}
