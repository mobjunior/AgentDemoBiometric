package com.agentdemo.dao.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.agentdemo.dao.TransferRecordDao;
import com.agentdemo.db.DBService;
import com.agentdemo.entity.TransferRecord;

public class TransferRecordDaoImpl implements TransferRecordDao {

	private DBService dbService = null;
	private Context context = null;
	
	
	public TransferRecordDaoImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TransferRecordDaoImpl(Context context) {
		super();
		this.context = context;
		dbService = new DBService(this.context);
	}
	
	@Override
	public void addTransferRecord(TransferRecord transferRecord) {
		// TODO Auto-generated method stub

		if(isExist(transferRecord.getTransnum())){
			Toast.makeText(context, "This Transfer Record already exists! Ignore this operation!", Toast.LENGTH_SHORT).show();
			return ;
		}

		String sql = "";
		Object[] args = null;
		sql = "insert into t_money(transnum,senderphonenumber,receivername,receiverphonenumber,receiverlocation,"
				+ "receiverbirthday,secretcode,amount,checkout) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		args = new Object[] { transferRecord.getTransnum(), transferRecord.getSenderphonenumber(),
				transferRecord.getReceivername(), transferRecord.getReceiverphonenumber(),
				transferRecord.getReceiverlocation(),transferRecord.getReceiverbirthday(),
				transferRecord.getSecretcode(),transferRecord.getAmount(),transferRecord.getCheckout()};
		SQLiteDatabase db = dbService.getWritableDatabase();
		db.execSQL(sql, args);
		db.close();
	}
	
	@Override
	public void updateCheckout(String transnum,int checkout) {
		// TODO Auto-generated method stub

		if(isExist(transnum)){
			String sql = "";
			Object[] args = null;
			sql = "update t_money set checkout=? where transnum=?";
			args = new Object[] { checkout, transnum };
			SQLiteDatabase db = dbService.getWritableDatabase();
			db.execSQL(sql, args);
			db.close();
		}else{
			Toast.makeText(context, "This Transfer Record does not exist! Ignore this operation!", Toast.LENGTH_SHORT).show();
			return ;
		}
	}
	
	private boolean isExist(String transnum) {
		String[] args = new String[]{transnum};
		String sql = "select * from t_money where transnum = ?";
		
		SQLiteDatabase db = dbService.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, args);
		if(cursor.getCount() == 0) {
			cursor.close();
			db.close();
			return false;
		} else {

			cursor.close();
			db.close();
			return true;
		}
	}
}
