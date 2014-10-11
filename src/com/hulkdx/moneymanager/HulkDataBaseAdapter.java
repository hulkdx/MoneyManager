	package com.hulkdx.moneymanager;
	
	import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
	
	public class HulkDataBaseAdapter {
		HulkDataBaseHelper helper;
		public HulkDataBaseAdapter(Context context) {
			helper = new HulkDataBaseHelper(context);
		}
		
		public long insertDataCategoryTable(String category) {
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put(HulkDataBaseHelper.CATEGORY, category);
			// id will be negative if something went wrong otherwise it contains row id
			long id = db.insert(HulkDataBaseHelper.TABLE_NAME1, null, contentValues);
			return id;
		}
		
		public ArrayList<String> getAllDataCategoryTable() {
			// saving to ArrayList
			ArrayList<String> arraylist = new ArrayList<String>();
			
			// showing data
			SQLiteDatabase db = helper.getWritableDatabase();
			String [] columns = {HulkDataBaseHelper.UID, HulkDataBaseHelper.CATEGORY};
			Cursor cursor = db.query(HulkDataBaseHelper.TABLE_NAME1, columns, null, null, null, null, null);
			
			while (cursor.moveToNext())
			{
				String category = cursor.getString(cursor.getColumnIndex(HulkDataBaseHelper.CATEGORY));
				arraylist.add(category);
			}
	
			return arraylist;
		}
		
		public int updateCategoryTable(String oldCat,String newCat){
			// UPDATE TABLE SET Category='' where Category=''
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put(HulkDataBaseHelper.CATEGORY, newCat);
			String[] whereArgs = {oldCat};
			//contentValues = SET Category='' and whereArgs = Category=''
			int count = db.update(HulkDataBaseHelper.TABLE_NAME1, contentValues, HulkDataBaseHelper.CATEGORY+" =? ", whereArgs);
			return count;
		}
		
		public int deleteCategoryTable(String cat){
			// DELETE * FROM TABLE WHERE category=''
			SQLiteDatabase db = helper.getWritableDatabase();
			String[] whereArgs = {cat};
			int count = db.delete(HulkDataBaseHelper.TABLE_NAME1, HulkDataBaseHelper.CATEGORY+"=?", whereArgs);
			return count;
		}
		
		public long insertDataTransactionTable(String category, int amount, boolean expense) {
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put(HulkDataBaseHelper.CATEGORY, category);
			contentValues.put(HulkDataBaseHelper.AMOUNT, amount);
			contentValues.put(HulkDataBaseHelper.IS_IT_EXPENSE, expense);
			// id will be negative if something went wrong otherwise it contains row id
			long id = db.insert(HulkDataBaseHelper.TABLE_NAME2, null, contentValues);
			return id;
		}

		
		public String[] selectCatTransactionTable(){
			SQLiteDatabase db = helper.getWritableDatabase();
			String [] columns = {HulkDataBaseHelper.CATEGORY};
			Cursor cursor = db.query(HulkDataBaseHelper.TABLE_NAME2, columns, null, null, null, null, null);
			
			String[] arrayStr = new String[cursor.getCount()];
			int count = 0;
			while (cursor.moveToNext())
			{
				String category = cursor.getString(cursor.getColumnIndex(HulkDataBaseHelper.CATEGORY));
				arrayStr[count] = category;
				count++;
			}
			return arrayStr;
		}
		
		public int[] selectAmountTransactionTable(){
			SQLiteDatabase db = helper.getWritableDatabase();
			String [] columns = {HulkDataBaseHelper.AMOUNT};
			Cursor cursor = db.query(HulkDataBaseHelper.TABLE_NAME2, columns, null, null, null, null, null);
			
			int[] arrayInt = new int[cursor.getCount()];
			int count = 0;
			while (cursor.moveToNext())
			{
				int amount = cursor.getInt(cursor.getColumnIndex(HulkDataBaseHelper.AMOUNT));
				arrayInt[count] = amount;
				count++;
			}
			return arrayInt;
		}
		
		public boolean[] selectExpenseTransactionTable(){
			SQLiteDatabase db = helper.getWritableDatabase();
			String [] columns = {HulkDataBaseHelper.UID, HulkDataBaseHelper.CATEGORY, HulkDataBaseHelper.AMOUNT, HulkDataBaseHelper.IS_IT_EXPENSE};
			Cursor cursor = db.query(HulkDataBaseHelper.TABLE_NAME2, columns, null, null, null, null, null);
			
			boolean[] arrayBool = new boolean[cursor.getCount()];
			int count = 0;
			while (cursor.moveToNext())
			{
				boolean expanse = cursor.getInt(cursor.getColumnIndex(HulkDataBaseHelper.IS_IT_EXPENSE))>0;
				arrayBool[count] = expanse;
				count++;
			}
			return arrayBool;
		}
		
		static class HulkDataBaseHelper extends SQLiteOpenHelper {
			
			private static final String DATABASE_NAME = "hulkdb";
			private static final int DATABASE_VERSION = 1;
			
			private static final String TABLE_NAME1 = "CATEGORYTABLE";
			private static final String TABLE_NAME2 = "TRANSACTIONTB";
	
			// FOR TABLE Category
			private static final String UID = "_id";
			private static final String CATEGORY = "category";
			
			// FOR TABLE Transaction
			private static final String AMOUNT = "amount";
			private static final String IS_IT_EXPENSE = "expenseorincome";
	
			private static final String CREATE_TABLE1 = "CREATE TABLE "+ TABLE_NAME1 +" ("+ UID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+CATEGORY+" VARCHAR(255));";
			private static final String CREATE_TABLE2 = "CREATE TABLE "+TABLE_NAME2+" " +
					"("+ UID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+CATEGORY+" VARCHAR(255), "+AMOUNT+" INTEGER, "+IS_IT_EXPENSE+" BOOLEAN );";
			
			private static final String DROP_TABLE1 = "DROP TABLE IF EXISTS "
					+ TABLE_NAME1;
			private static final String DROP_TABLE2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;
	
			public HulkDataBaseHelper(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}
	
			@Override
			public void onCreate(SQLiteDatabase db) {
				try {
					// CREATE TABLE category (_id INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY VARCHAR(255));
					db.execSQL(CREATE_TABLE1);
					// CREATE TABLE category (_id INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY VARCHAR(255), AMOUNT INTEGER, expense BOOLEAN);
					db.execSQL(CREATE_TABLE2);
				}
				catch (SQLException e) {
					Log.e("ERROR","SQL on create");
				}
	
			}
	
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				try {
					db.execSQL(DROP_TABLE1);
					db.execSQL(DROP_TABLE2);
					onCreate(db);
				} catch (SQLException e) {
					Log.e("ERROR","SQL on upgrade");
				}
			}
			
		}
	}
