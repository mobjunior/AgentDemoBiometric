package com.agentdemo.biz.impl;

import android.content.Context;

import com.agentdemo.biz.AccountBiz;
import com.agentdemo.dao.AccountDao;
import com.agentdemo.dao.impl.AccountDaoImpl;
import com.agentdemo.entity.Account;

public class AccountBizImpl implements AccountBiz {
	
	private AccountDao accountDao = null;
	private Context context = null;
	

	public AccountBizImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccountBizImpl(Context context) {
		super();
		this.context = context;
		accountDao = (AccountDao) new AccountDaoImpl(this.context);
	}

	@Override
	public int register(Account account) {
		// TODO Auto-generated method stub
		
		return accountDao.addAccount(account);
	}
	
	@Override
	public void updateBasicInformation(Account account) {
		// TODO Auto-generated method stub

		accountDao.updateBasicInformation(account);
	}
	
	@Override
	public void updateFingerprint(String accountNumber, int fingerprint) {
		// TODO Auto-generated method stub

		accountDao.updateFingerprint(accountNumber, fingerprint);
	}
	
	@Override
	public void updateIdcardPicture(String accountNumber, String idcardPicture) {
		// TODO Auto-generated method stub

		accountDao.updateIdcardPicture(accountNumber, idcardPicture);
	}
	
	@Override
	public void updatePassword(String accountNumber, String password) {
		// TODO Auto-generated method stub

		accountDao.updatePassword(accountNumber, password);
	}
	
	@Override
	public Account findLastAccount() {
		// TODO Auto-generated method stub

		return accountDao.findLastAccount();
	}
	
	@Override
	public Account findAccount(String number, boolean isAccountNumber) {
		// TODO Auto-generated method stub

		return accountDao.findAccount(number, isAccountNumber);
	}

	@Override
	public void updateAmount(String accountNumber, double value) {
		// TODO Auto-generated method stub
		
		accountDao.updateAmount(accountNumber, value);
	}


	}

