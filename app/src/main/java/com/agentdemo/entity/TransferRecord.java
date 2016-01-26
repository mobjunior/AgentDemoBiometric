package com.agentdemo.entity;

public class TransferRecord {
	
	private String id = "";
	private String transnum = "";
	private String senderphonenumber = "";
	private String receivername = "";
	private String receiverphonenumber = "";
	private String receiverlocation = "";
	private String receiverbirthday = "";
	private String secretcode = "";
	private double amount = 0.0;
	private int checkout = 0;
	
	
	
	public TransferRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public TransferRecord(String transnum, String senderphonenumber,
			String receivername, String receiverphonenumber,
			String receiverlocation, String receiverbirthday,
			String secretcode, double amount, int checkout) {
		super();
		this.transnum = transnum;
		this.senderphonenumber = senderphonenumber;
		this.receivername = receivername;
		this.receiverphonenumber = receiverphonenumber;
		this.receiverlocation = receiverlocation;
		this.receiverbirthday = receiverbirthday;
		this.secretcode = secretcode;
		this.amount = amount;
		this.checkout = checkout;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTransnum() {
		return transnum;
	}
	public void setTransnum(String transnum) {
		this.transnum = transnum;
	}
	public String getSenderphonenumber() {
		return senderphonenumber;
	}
	public void setSenderphonenumber(String senderphonenumber) {
		this.senderphonenumber = senderphonenumber;
	}
	public String getReceivername() {
		return receivername;
	}
	public void setReceivername(String receivername) {
		this.receivername = receivername;
	}
	public String getReceiverphonenumber() {
		return receiverphonenumber;
	}
	public void setReceiverphonenumber(String receiverphonenumber) {
		this.receiverphonenumber = receiverphonenumber;
	}
	public String getReceiverlocation() {
		return receiverlocation;
	}
	public void setReceiverlocation(String receiverlocation) {
		this.receiverlocation = receiverlocation;
	}
	public String getReceiverbirthday() {
		return receiverbirthday;
	}
	public void setReceiverbirthday(String receiverbirthday) {
		this.receiverbirthday = receiverbirthday;
	}
	public String getSecretcode() {
		return secretcode;
	}
	public void setSecretcode(String secretcode) {
		this.secretcode = secretcode;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getCheckout() {
		return checkout;
	}

	public void setCheckout(int checkout) {
		this.checkout = checkout;
	}
	
}
