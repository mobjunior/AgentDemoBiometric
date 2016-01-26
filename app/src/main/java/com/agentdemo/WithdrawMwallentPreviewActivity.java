package com.agentdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.agentdemo.entity.Account;
//import com.nbbse.mobiprint3.Printer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WithdrawMwallentPreviewActivity extends Activity {
	
	private TextView textFirstName = null;
	private TextView textLastName = null;
	private TextView textAccount = null;
	private Button buttonPrint = null;
	private TextView textDateTime = null;
	private TextView textPhoneNumber = null;
	private TextView textCredit = null;
	private TextView textPreBalance = null;
	private TextView textNewBalance = null;
	Intent i;

//	Printer print;
	
	private String strFirstName;
	private String strLastName;
	private Account account;
	private String withdraw;
	private String strDateTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.withdraw_preview);
//		print = Printer.getInstance();
		
		initData();
		initViews();
	}
	
	private void initData() {
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			strFirstName = bundle.getString("firstname");
			strLastName = bundle.getString("lastname");
			account = (Account) bundle.getSerializable("account");
			withdraw = bundle.getString("withdraw");
		}
	}


	private void initViews() {
		textDateTime = (TextView) findViewById(R.id.text_datetime);
		SimpleDateFormat df = new SimpleDateFormat("dd MMMMM yyyy      HH:mm", Locale.ENGLISH);
		strDateTime = df.format(new Date());
		textDateTime.setText(strDateTime);
		
		textFirstName = (TextView) findViewById(R.id.text_firstname);
		textFirstName.setText("First Name: " + strFirstName);
		textLastName = (TextView) findViewById(R.id.text_lastname);
		textLastName.setText("Last Name: " + strLastName);
		
		textPhoneNumber = (TextView) findViewById(R.id.text_phonenumber);
		textPhoneNumber.setText("Phone Number: " + account.getPhoneNumber());
		textAccount = (TextView) findViewById(R.id.text_account);
		textAccount.setText("Account Number: " + account.getAccount());
		
		textCredit = (TextView) findViewById(R.id.text_credit_amount);
		textCredit.setText("Your account has been debited with " + Double.valueOf(withdraw) + " ksh");
		textPreBalance = (TextView) findViewById(R.id.text_pre_balance);
		textPreBalance.setText("Previous Balance: " + account.getAmount() + " ksh");
		textNewBalance = (TextView) findViewById(R.id.text_new_balance);
		textNewBalance.setText("New Balance: " + (account.getAmount() - Double.valueOf(withdraw)) + " ksh");
		
		buttonPrint = (Button) findViewById(R.id.button_print);
		buttonPrint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				if(print.getPaperStatus() != 1) {
//					Toast.makeText(WithdrawMwallentPreviewActivity.this, "No Paper.", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				print.printBitmap(getResources().openRawResource(R.raw.mobiwire_logo));
//				print.printText("     WITHDRAW", 2);
//				print.printText("\n  " + strDateTime
//						+ "\n\nName: " + strFirstName + " " + strLastName
//						+ "\nPhone Number: " + account.getPhoneNumber()
//						+ "\nAccount Number: " + account.getAccount()
//						+ "\n--------------------------------"
//						+ "\nYour account has been debited  with " + Double.valueOf(withdraw) + " ksh"
//						+ "\nPreview Balance: " + account.getAmount() + " ksh"
//						+ "\nNew Balance: " + (account.getAmount() - Double.valueOf(withdraw)) + " ksh"
//						+ "\n--------------------------------"
//						+ "\n\nThank you for choosing Eclectics  Solution!");
//
//				print.printEndLine();
//
//				setResult(RESULT_OK);
//				finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == 5) {
//			if(print.getPaperStatus() != 1) {
//				Toast.makeText(WithdrawMwallentPreviewActivity.this, "No Paper.", Toast.LENGTH_SHORT).show();
//				return super.onKeyDown(keyCode, event);
//			}
//			print.printBitmap(getResources().openRawResource(R.raw.mobiwire_logo));
//			print.printText("     WITHDRAW", 2);
//			print.printText("\n  " + strDateTime
//					+ "\n\nName: " + strFirstName + " " + strLastName
//					+ "\nPhone Number: " + account.getPhoneNumber()
//					+ "\nAccount Number: " + account.getAccount()
//					+ "\n--------------------------------"
//					+ "\nYour account has been debited  with " + Double.valueOf(withdraw) + " ksh"
//					+ "\nPreview Balance: " + account.getAmount() + " ksh"
//					+ "\nNew Balance: " + (account.getAmount() - Double.valueOf(withdraw)) + " ksh"
//					+ "\n--------------------------------"
//					+ "\n\nThank you for choosing Eclectics  Solution!");
//
//			print.printEndLine();
//
//			setResult(RESULT_OK);
//			finish();
		}
		return super.onKeyDown(keyCode, event);
		
	}
}
