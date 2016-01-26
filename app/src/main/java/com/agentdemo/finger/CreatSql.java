package com.agentdemo.finger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CreatSql extends SQLiteOpenHelper{

	private static final String DATABASENAME = "person.db";
	private static final int DATABASEVERSION = 3;
	
	public CreatSql(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("info", "½¨±í¡¤¡¤¡¤¡¤");
		db.execSQL("CREATE TABLE  if not exists finger(id integer primary key autoincrement," +
				"person_id integer,name text)" );
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS finger");	
		onCreate(db);
	}

}
