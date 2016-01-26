package com.agentdemo.dao;

import com.agentdemo.entity.Account;

public interface AccountDao {

	public abstract int addAccount(Account account);

	public abstract void updateBasicInformation(Account account);

	public abstract void updateAmount(String accountNumber, double value);

	public abstract void updateFingerprint(String accountNumber,
										   int fingerprint);

	public abstract void updateIdcardPicture(String accountNumber,
											 String idcardPicture);
	
	public abstract void updatePassword(String accountNumber,
										String Password);

	public abstract Account findAccount(String number, boolean isAccountNumber);


	public abstract Account findLastAccount();

	public abstract Account findPhoneNumber(String number,String phoneNumber);


}