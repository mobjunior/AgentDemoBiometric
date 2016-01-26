package com.agentdemo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBService extends SQLiteOpenHelper {
	private final static int DATABASE_VERSION = 1;
	private final static String DATABASE_NAME = "printerdb.db";
	
	public DBService(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql_t_account = "CREATE TABLE t_account (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "account VARCHAR(20) NOT NULL UNIQUE ON CONFLICT FAIL,"
				+ "name VARCHAR(30) NOT NULL ON CONFLICT FAIL,"
				+ "phonenumber VARCHAR(20) NOT NULL UNIQUE ON CONFLICT FAIL,"
				+ "amount DOUBLE,"
				+ "password VARCHAR(20) NOT NULL ON CONFLICT FAIL,"
				+ "fingerprint INT NOT NULL,"
				+ "idcardpicture VARCHAR(50) NOT NULL ON CONFLICT FAIL)";
		
		db.execSQL(sql_t_account);
		
		String sql_t_money = "CREATE TABLE t_money (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "transnum VARCHAR(10) NOT NULL UNIQUE ON CONFLICT FAIL,"
				+ "senderphonenumber VARCHAR(20) NOT NULL ON CONFLICT FAIL,"
				+ "receiverfirstname VARCHAR(30) NOT NULL ON CONFLICT FAIL,"
				+ "receiverlastname VARCHAR(30) NOT NULL ON CONFLICT FAIL,"
				+ "receiverphonenumber VARCHAR(20) NOT NULL ON CONFLICT FAIL,"
				+ "receiverlocation VARCHAR(50) NOT NULL ON CONFLICT FAIL,"
				+ "receiverbirthday VARCHAR(20) NOT NULL ON CONFLICT FAIL,"
				+ "secretcode VARCHAR(10) NOT NULL ON CONFLICT FAIL,"
				+ "amount DOUBLE, checkout INT)";
		db.execSQL(sql_t_money);
		
		String sql_t_item_lib = "CREATE TABLE t_item_lib (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "name VARCHAR(10) NOT NULL ON CONFLICT FAIL,"
				+ "barcode VARCHAR(15) NOT NULL UNIQUE ON CONFLICT FAIL,"
				+ "price DOUBLE,"
				+ "quantity INT)";
		db.execSQL(sql_t_item_lib);
		
		String sql_t_bill_lib = "create table if not exists t_bill_lib (_id integer primary key autoincrement,"
				+ "transnum varchar(15) not null on conflict fail,"
				+ "name varchar(30),phonenumber varchar(20),"
				+ "amount double,pay int)";
		db.execSQL(sql_t_bill_lib);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	public void insert(String sql) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.execSQL(sql);
	}
	
	public void insert(String sql, Object[] bindArgs) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.execSQL(sql, bindArgs);
	}
	
	public Cursor query(String sql, String[] args) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, args);

		return cursor;
	}
}
