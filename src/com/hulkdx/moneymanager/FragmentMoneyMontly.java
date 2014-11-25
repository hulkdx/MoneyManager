package com.hulkdx.moneymanager;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMoneyMontly extends Fragment {
	private static final String TEXT_AT_END = "Total : ";
	ListView transactionListV;
	TextView totalMontlyTV;
	int dateposition = 0;

	public static FragmentMoneyMontly newInstance(int index, boolean radioGTrack) {
		FragmentMoneyMontly f = new FragmentMoneyMontly();
		// Supply index, radioTrack input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		args.putBoolean("radioGtrack", radioGTrack);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_fragment_money_manager, container, false);

		transactionListV = (ListView) view.findViewById(R.id.transactionData1);
		totalMontlyTV = (TextView) view.findViewById(R.id.totalMontly);
		HulkDataBaseAdapter db = new HulkDataBaseAdapter(getActivity());

		// getting from new instance method
		dateposition = getArguments().getInt("index", -1000);
		boolean rgTrack = getArguments().getBoolean("radioGtrack", false);

		// error checking
		if (dateposition == -1000) {
			Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
		} else {
			setDateListView(db, rgTrack);
		}
		return view;
	}

	public void setTextView() {

	}

	public void setDateListView(HulkDataBaseAdapter db, boolean rgTrack) {
		Calendar cal = Calendar.getInstance();
		for (int i = -200; i < 0; i++) {
			if (dateposition == i) {
				cal.add(Calendar.MONTH, i);

				String monthString = String.format("%02d", (cal.get(Calendar.MONTH) + 1));
				String yearString = String.valueOf(cal.get(Calendar.YEAR));
				showListView(monthString, yearString, db, rgTrack);
			}
		}
		// show today month
		if (dateposition == 0) {
			String currentMonth = String.format("%02d", cal.get(Calendar.MONTH) + 1);
			String currentYear = String.valueOf(cal.get(Calendar.YEAR));
			showListView(currentMonth, currentYear, db, rgTrack);
		}
		// Show next month
		for (int i = 1; i <= 200; i++) {
			if (dateposition == i) {
				cal.add(Calendar.MONTH, i);
				String monthString = String.format("%02d", (cal.get(Calendar.MONTH) + 1));
				String yearString = String.valueOf(cal.get(Calendar.YEAR));
				showListView(monthString, yearString, db, rgTrack);
			}
		}
	}

	public void showListView(String currentMonth, String currentYear, HulkDataBaseAdapter db, boolean rgTrack) {
		// init
		String[] catArray = null;
		String[] amountArray = null;
		boolean[] expenseArray = null;
		int currCount = db.selectCatFromDate(currentMonth, currentYear).length;
		// if DB is not empty
		if (currCount != 0) {
			// ADD count to amount and disply it in TextView
			int countAmount = 0;
			// Show each individual (based on RadioGroup)
			if (rgTrack == false) {
				// initialization
				catArray = new String[currCount];
				amountArray = new String[currCount];
				expenseArray = new boolean[currCount];
				// set cat amount expanse to array
				for (int i = 0, c = currCount; i < currCount; i++) {
					// Show it in reverse (last item first)
					String catThis = db.selectCatFromDate(currentMonth, currentYear)[i];
					int amountInt = db.selectAmountFromDate(currentMonth, currentYear)[i];
					boolean expenseThis = db.selectExpenseFromDate(currentMonth, currentYear)[i];
					String amountThis;
					if (expenseThis) {
						amountInt = amountInt * (-1);
						countAmount -= amountInt;
						amountThis = String.valueOf(amountInt);
					} else {
						countAmount += amountInt;
						amountThis = "+"+String.valueOf(amountInt);
					}
					
					// saving in reverse format
					c--;
					catArray[c] = catThis;
					amountArray[c] = amountThis;
					expenseArray[c] = expenseThis;
				}
				
				// set the adapter and show it
				CustomAdapter adapter = new CustomAdapter(getActivity(), catArray, amountArray, expenseArray);
				transactionListV.setAdapter(adapter);
				// show count to textview text: Total : x $
				String textCountAll = TEXT_AT_END + countAmount + MoneyManager.DOLLAR;
				totalMontlyTV.setText(textCountAll);
			}
			
			// Show by Category (based on RadioGroup)
			else {
				String[] catAllArray = db.selectAllCategoryTable();
				int countCat = catAllArray.length;
				String[] amountAllArray = new String[countCat];
				boolean[] expenseAllArray = new boolean[countCat];
				for (int i = 0; i < countCat; i++) {
					int ThisAmount = db.totalAmountOfCatFromDateCategoryTable(catAllArray[i], currentMonth, currentYear);
					countAmount += ThisAmount;
					if (ThisAmount < 0){
						expenseAllArray[i] = true;
						amountAllArray[i] = String.valueOf(ThisAmount);
					}
					else {
						expenseAllArray[i] = false;
						amountAllArray[i] = "+"+String.valueOf(ThisAmount);
					}
				}
				
				CustomAdapter adapter = new CustomAdapter(getActivity(), catAllArray, amountAllArray, expenseAllArray);
				transactionListV.setAdapter(adapter);
			}
			
			String textCountAll = TEXT_AT_END + countAmount + MoneyManager.DOLLAR;
			totalMontlyTV.setText(textCountAll);
		}
		// if Transaction DB is empty
		else {
			String[] noString = { MoneyManager.NOTHING };
			ArrayAdapter<String> adapterNo = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, noString);
			transactionListV.setAdapter(adapterNo);
		}
	}
}

// Custom adapter for List View
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
		amount.setText(amountArray[position] + MoneyManager.DOLLAR);

		if (expanseB[position]) {
			amount.setTextColor(Color.parseColor("#FFFF4444"));
		} else {
			amount.setTextColor(Color.parseColor("#0000FF"));
		}
		return row;
	}
}