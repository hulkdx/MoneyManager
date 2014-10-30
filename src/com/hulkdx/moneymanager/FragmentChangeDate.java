package com.hulkdx.moneymanager;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

public class FragmentChangeDate extends DialogFragment implements OnClickListener {
	private static final String TITLE = "Change Date";
	
	Button okButton;
	CalendarView myCalenderView;

	// define communication
	Communicator comm;
	interface Communicator {
		public void sentDate(String date);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		comm = (Communicator) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// set title for this dialog
		getDialog().setTitle(TITLE);
		View view = inflater.inflate(R.layout.my_category_date_dialog, null);
		
		okButton = (Button) view.findViewById(R.id.button1);
		okButton.setOnClickListener(this);
		
		initializeCalendar(view);
		
		return view;
	}
	
	public void initializeCalendar(View view) {
		myCalenderView = (CalendarView) view.findViewById(R.id.calendarView1);
		myCalenderView.setShowWeekNumber(false);
		myCalenderView.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));
		myCalenderView.setWeekSeparatorLineColor(getResources().getColor(R.color.black));
		myCalenderView.setSelectedDateVerticalBar(R.color.darkgreen);
	}

	// clicking on Ok
	@Override
	public void onClick(View arg0) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		String dateStr = sdf.format(myCalenderView.getDate());
		comm.sentDate(dateStr);
		dismiss();
	}
}
