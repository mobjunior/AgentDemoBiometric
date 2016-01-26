package com.agentdemo.dao.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.agentdemo.dao.AccountDao;
import com.agentdemo.db.DBService;
import com.agentdemo.entity.Account;

public class AccountDaoImpl implements AccountDao {

	private DBService dbService = null;
	private Context context = null;

	public AccountDaoImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccountDaoImpl(Context context) {
		super();
		this.context = context;
		dbService = new DBService(this.context);
	}


	public int addAccount(Account account) {
		// TODO Auto-generated method stub
		
		if(isExist(account.getAccount(),true)){
			return 1;
		}
		
		if(isExist(account.getPhoneNumber(),false)){
			Account account1 = getAccount(account.getPhoneNumber(),false);
			if(!account1.getAccount().equals(account.getAccount())){
				Toast.makeText(context, "This PhoneNumber has already been registed! Ignore this operation!", Toast.LENGTH_SHORT).show();
				return 2;
			}
		}

		String sql = "";
		Object[] args = null;
		sql = "insert into t_account(account,name,phonenumber,amount,password,fingerprint,idcardpicture) values(?, ?, ?, ?, ?, ?, ?)";
		args = new Object[] { account.getAccount(), account.getName(),
				account.getPhoneNumber(), account.getAmount(),
				account.getPassword(), account.getFingerprint(),
				account.getIdcardPicture() };
		SQLiteDatabase db = dbService.getWritableDatabase();
		db.execSQL(sql, args);
		db.close();
		return 3;
	}


	public void updateBasicInformation(Account account) {
		// TODO Auto-generated method stub

		if(isExist(account.getAccount(),true)){

			Account oldAccount = getAccount(account.getAccount(),true);
			if(oldAccount.getPhoneNumber().equals(account.getPhoneNumber())){
				Toast.makeText(context, "This PhoneNumber has already been registed! Ignore this operation!", Toast.LENGTH_SHORT).show();
				return;
			}

			String sql = "";
			Object[] args = null;
			sql = "update t_account set name=?,phonenumber=?,password=? where account=?";
			args = new Object[] { account.getName(), account.getPhoneNumber(), account.getPassword(), account.getAccount() };
			SQLiteDatabase db = dbService.getWritableDatabase();
			db.execSQL(sql, args);
			db.close();
		}else{
			Toast.makeText(context, "Eclectics Account Number does not exist! Ignore this operation!", Toast.LENGTH_SHORT).show();
			return ;
		}
	}

	@Override
	public void updateAmount(String accountNumber, double value) {
		// TODO Auto-generated method stub

		if(isExist(accountNumber,true)){
			Account account = getAccount(accountNumber,true);
			if((account.getAmount() + value) < 0){
				Toast.makeText(context, "Insufficient account balance! Ignore this operation!", Toast.LENGTH_SHORT).show();
				return ;
			}
			String sql = "";
			Object[] args = null;
			sql = "update t_account set amount=? where account=?";
			args = new Object[] { account.getAmount() + value, accountNumber };
			SQLiteDatabase db = dbService.getWritableDatabase();
			db.execSQL(sql, args);
			db.close();
		}else{
			Toast.makeText(context, "Eclectics Account Number does not exist! Ignore this operation!", Toast.LENGTH_SHORT).show();
			return ;
		}
	}

	@Override
	public void updatePassword(String accountNumber, String password) {

		if(isExist(accountNumber,true)){
			String sql = "";
			Object[] args = null;
			sql = "update t_account set password=? where account=?";
			args = new Object[] { password, accountNumber };
			SQLiteDatabase db = dbService.getWritableDatabase();
			db.execSQL(sql, args);
			db.close();
		}else{
			Toast.makeText(context, "Eclectics Account Number does not exist! Ignore this operation!", Toast.LENGTH_SHORT).show();
			return ;
		}
	}

	@Override
	public void updateFingerprint(String accountNumber, int fingerprint) {
		// TODO Auto-generated method stub

		if(isExist(accountNumber,true)){
			String sql = "";
			Object[] args = null;
			sql = "update t_account set fingerprint=? where account=?";
			args = new Object[] { fingerprint, accountNumber };
			SQLiteDatabase db = dbService.getWritableDatabase();
			db.execSQL(sql, args);
			db.close();
		}else{
			Toast.makeText(context, "Eclectics Account Number does not exist! Ignore this operation!", Toast.LENGTH_SHORT).show();
			return ;
		}
	}

	@Override
	public void updateIdcardPicture(String accountNumber, String idcardPicture) {
		// TODO Auto-generated method stub

		if(isExist(accountNumber,true)){
			String sql = "";
			Object[] args = null;
			sql = "update t_account set idcardpicture=? where account=?";
			args = new Object[] { idcardPicture, accountNumber };
			SQLiteDatabase db = dbService.getWritableDatabase();
			db.execSQL(sql, args);
			db.close();
		}else{
			Toast.makeText(context, "Eclectics Account Number does not exist! Ignore this operation!", Toast.LENGTH_SHORT).show();
			return ;
		}
	}

	@Override
	public Account findAccount(String number, boolean isAccountNumber) {
		// TODO Auto-generated method stub

		Account account = null;

		if(isExist(number,isAccountNumber)){
			account = getAccount(number,isAccountNumber);
		}else{
			Toast.makeText(context, "Eclectics Account Number does not exist! Ignore this operation!", Toast.LENGTH_SHORT).show();
			return null;
		}

		return account;
	}

	@Override
	public Account findLastAccount() {
		// TODO Auto-generated method stub
		Account account = null;
		SQLiteDatabase db = dbService.getReadableDatabase();
		String sql_query = "select * from t_account order by _id desc limit ?";
		Cursor cursor = db.rawQuery(sql_query, new String[]{1 + ""});
		if(cursor.getCount() == 0) {
			return null;
		}else{
			cursor.moveToFirst();
			String strAccount = cursor.getString(cursor.getColumnIndex("account"));
			account = getAccount(strAccount,true);
			return account;
		}
	}

	@Override
	public Account findPhoneNumber(String number, String phoneNumber) {
		return null;
	}

	private boolean isExist(String number, boolean isAccountNumber) {
		String sql = "";
		if(true == isAccountNumber){
			sql = "select * from t_account where account = ?";
		}else{
			sql = "select * from t_account where phonenumber = ?";
		}
		String[] args = new String[]{number};
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

	private Account getAccount(String number, boolean isAccountNumber){
		Account account = new Account();
		String sql = "";
		if(true == isAccountNumber){
			sql = "select * from t_account where account = ?";
		}else{
			sql = "select * from t_account where phonenumber = ?";
		}

		SQLiteDatabase db = dbService.getReadableDatabase();
		String[] args = new String[]{number};
		Cursor cursor = db.rawQuery(sql, args);
		if(cursor.getCount() == 0) {
			cursor.close();
			db.close();
			return null;
		} else {
			cursor.moveToFirst();
			account.setAccount(cursor.getString(cursor.getColumnIndex("account")));
			account.setName(cursor.getString(cursor.getColumnIndex("name")));
			account.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phonenumber")));
			account.setAmount(cursor.getDouble(cursor.getColumnIndex("amount")));
			account.setPassword(cursor.getString(cursor.getColumnIndex("password")));
			account.setFingerprint(cursor.getInt(cursor.getColumnIndex("fingerprint")));
			account.setIdcardPicture(cursor.getString(cursor.getColumnIndex("idcardpicture")));

			cursor.close();
			db.close();
			return account;
		}
	}
}
