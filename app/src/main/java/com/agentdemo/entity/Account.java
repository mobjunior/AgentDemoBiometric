package com.agentdemo.entity;

import java.io.Serializable;

public class Account implements Serializable{

	private String id = "";
	private String account = "";
	private String name = "";
	private String phoneNumber = "";
	private double amount = 0.0;
	private String password = "";
	private int fingerprint = 0;
	private String idcardPicture = "";

	
	
	
	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Account(String account, String name, String phoneNumber,
			double amount, String password, int fingerprint) {
		super();
		this.account = account;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.amount = amount;
		this.password = password;
		this.fingerprint = fingerprint;
	}
	
	public Account(String account, String name, String phoneNumber,
			double amount, String password, int fingerprint,
			String idcardPicture) {
		super();
		this.account = account;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.amount = amount;
		this.password = password;
		this.fingerprint = fingerprint;
		this.idcardPicture = idcardPicture;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {return account;	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {return name;}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {return phoneNumber;	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getFingerprint() {
		return fingerprint;
	}
	public void setFingerprint(int fingerprint) {
		this.fingerprint = fingerprint;
	}

	public String getIdcardPicture() {
		return idcardPicture;
	}
	public void setIdcardPicture(String idcardPicture) {
		this.idcardPicture = idcardPicture;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", account=" + account + ", name=" + name
				+ ", phoneNumber=" + phoneNumber + ", amount=" + amount
				+ ", password=" + password + ", fingerprint=" + fingerprint
				+ ", idcardPicture=" + idcardPicture + "]";
	}
}
