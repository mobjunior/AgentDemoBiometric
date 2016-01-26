package com.agentdemo.finger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqliteMethod {
	private CreatSql sql;

	public SqliteMethod(Context context){
		if(sql == null){
			sql = new CreatSql(context);
			Log.i("info", "creat new databases!` ");
		}
	}
	
	public synchronized void saveFinger(int id,String name){
		SQLiteDatabase db = sql.getWritableDatabase();
		db.execSQL("insert into finger(person_id,name) " +
				"values(?,?)",new Object[]{id,name});		
		Log.i("info", "====");
		db.close();
	}
	
	public synchronized String readResult(int id){
		SQLiteDatabase db = sql.getReadableDatabase();
		String sqlcode = "select * from finger where person_id = "+id;
		Cursor cur = db.rawQuery(sqlcode,new String[]{});
		String name = "";
		while(cur.moveToNext()){
			name = cur.getString(2);
		}	
		db.close();
		return name;
	}
	
	public synchronized void clearResult(){
		String clear = "drop table finger";
		String create = "CREATE TABLE  if not exists finger(id integer primary key autoincrement," +
				"person_id integer,name text)";
		SQLiteDatabase db = sql.getWritableDatabase();
		db.execSQL(clear);
		db.execSQL(create);
		db.close();
	}
	
	public synchronized int getCount(){
		SQLiteDatabase db = sql.getReadableDatabase();
		String sql = "select count(id) from finger ";
		Cursor cur = db.rawQuery(sql, new String[]{});
		
		int sum = 0;
		while (cur.moveToNext()) {
			sum = cur.getInt(0);
		}
		Log.i("info", "sum == "+sum);
		return sum;
	}
}
