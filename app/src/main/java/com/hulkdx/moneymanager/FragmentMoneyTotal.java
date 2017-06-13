package com.hulkdx.moneymanager;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FragmentMoneyTotal extends Fragment {
	ListView transactionListV;

	public static FragmentMoneyTotal newInstance(boolean radioGTrack, boolean firstTime) {
		FragmentMoneyTotal f = new FragmentMoneyTotal();
		// Supply radioTrack input as an argument.
		Bundle args = new Bundle();
		args.putBoolean("radioGtrack", radioGTrack);
		args.putBoolean("firstTime", firstTime);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		boolean firstTime = getArguments().getBoolean("firstTime", true);
		// first time hide it
		if (savedInstanceState == null && firstTime == true) {
			this.getView().setVisibility(View.GONE);
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_fragment_money_manager_total, container, false);
		transactionListV = (ListView) view.findViewById(R.id.transactionData1);
		// getting from new instance method
		boolean rgTrack = getArguments().getBoolean("radioGtrack", false);

		HulkDataBaseAdapter db = new HulkDataBaseAdapter(getActivity());
		addTotalTransaction(db, rgTrack);
		return view;
	}

	public void addTotalTransaction(HulkDataBaseAdapter db, boolean rgTrack) {
		int countdbTransaction = db.selectCatTransactionTable().length;
		// if DB is not empty
		if (countdbTransaction != 0) {
			// Show each individual (based on RadioGroup)
			if (rgTrack == false) {
				// init
				String[] catArray = new String[countdbTransaction];
				String[] amountArray = new String[countdbTransaction];
				boolean[] expenseArray = new boolean[countdbTransaction];
				// Set cat amount expanse to array
				for (int i = 0, c = countdbTransaction; i < countdbTransaction; i++) {
					String catThis = String.valueOf(db.selectCatTransactionTable()[i]);
					String amountThis = String.valueOf(db.selectAmountTransactionTable()[i]);
					boolean expenseThis = db.selectExpenseTransactionTable()[i];
					if (expenseThis) {
						amountThis = "-" + amountThis;
					} else {
						amountThis = "+" + amountThis;
					}
					// Saving in reverse format
					c--;
					catArray[c] = catThis;
					amountArray[c] = amountThis;
					expenseArray[c] = expenseThis;
				}
				// Set the adapter and show it
				CustomAdapter adapter = new CustomAdapter(getActivity(), catArray, amountArray, expenseArray);
				transactionListV.setAdapter(adapter);
			}
			// Show by Category (based on RadioGroup)
			else {
				String[] catAllArray = db.selectAllCategoryTable();
				int countCat = catAllArray.length;
				String[] amountAllArray = new String[countCat];
				boolean[] expenseAllArray = new boolean[countCat];
				for (int i = 0, y = 0; i < countCat; i++) {
					int ThisAmount = db.totalAmountOfCatCategoryTable(catAllArray[i]);
					// TODO
					if (ThisAmount == 0) {

					}

					if (ThisAmount < 0) {
						expenseAllArray[y] = true;
						amountAllArray[y] = String.valueOf(ThisAmount);
					} else {
						expenseAllArray[y] = false;
						amountAllArray[y] = "+" + String.valueOf(ThisAmount);
					}
					y++;
				}
				CustomAdapter adapter = new CustomAdapter(getActivity(), catAllArray, amountAllArray, expenseAllArray);
				transactionListV.setAdapter(adapter);
			}
		}
		// if Transaction DB is empty
		else {
			String[] noString = { MoneyManager.NOTHING };
			ArrayAdapter<String> adapterNo = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, noString);
			transactionListV.setAdapter(adapterNo);
		}

	}

	// TODO
	public void msg(String text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}
}
