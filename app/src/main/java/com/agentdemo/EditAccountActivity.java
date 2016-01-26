package com.agentdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agentdemo.biz.AccountBiz;
import com.agentdemo.biz.impl.AccountBizImpl;
import com.agentdemo.entity.Account;
import com.agentdemo.util.NFCUtil;

public class EditAccountActivity extends Activity {
	
	private static final String TAG = "EditAccountActivity";
	
	private EditText editAccount = null;
	private EditText editPassword = null;
	private Button buttonValidate = null;
	Intent i;

	private String strAccountNumber = "";
	private String strPassword = "";
	
	private NFCUtil nfcUtil;
	private AccountBiz accountBizImpl = null;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editAccount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.edit_account);
		
		accountBizImpl = new AccountBizImpl(this);
		initViews();
		
		nfcUtil = new NFCUtil(this);
	}

	public void back (View v){
		i=new Intent(this,CustomerManagementActivity.class);
		startActivity(i);
	}

	private void initViews() {
		editAccount = (EditText) findViewById(R.id.edit_accountnumber);
		editAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.i("stan","hasFocus = " + hasFocus);
				if (hasFocus) {
					//mHandler.sendEmptyMessage(0);
				}
			}
		});
		
		editAccount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("stan", "click");
				//mHandler.sendEmptyMessage(0);
			}
		});
		
		editAccount.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Log.i("stan", "longclick");
				//mHandler.sendEmptyMessage(0);
				return false;
			}
		});
		editPassword = (EditText) findViewById(R.id.edit_password);
		buttonValidate = (Button) findViewById(R.id.button_create);
		buttonValidate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(TextUtils.isEmpty(editAccount.getText().toString())
						|| TextUtils.isEmpty(editPassword.getText().toString())) {

					Toast.makeText(EditAccountActivity.this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
					return;
				} else {
					strAccountNumber = editAccount.getText().toString();
					strPassword = editPassword.getText().toString();
				}
				
				Account account = accountBizImpl.findAccount(strAccountNumber, true);
				if(null != account){
					if(!strPassword.equals(account.getPassword())){
						Toast.makeText(EditAccountActivity.this, "Password Error! Ignore this operation!", Toast.LENGTH_SHORT).show();
						return;
					}
					Intent intent = new Intent(EditAccountActivity.this, InformationEditActivity.class);
					intent.putExtra("account", account);
					startActivityForResult(intent, 0);
				}
				
			}
			
		});
	}
	
	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}	
	
	@Override
	protected void onPause() {
		super.onPause();
		if (nfcUtil.isNFCUse()) {
			nfcUtil.nfcAdapter.disableForegroundDispatch(this);
		}
		Log.d(TAG, "onPause...");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (nfcUtil.isNFCUse()) {
			nfcUtil.nfcAdapter.enableForegroundDispatch(this, nfcUtil.pendingIntent, nfcUtil.mFilters, nfcUtil.mTechLists);
		}
		Log.d(TAG, "onResume...");
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		Log.d(TAG, "onNewIntent into : " + intent.getAction());
			
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())
				|| NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			String readInfo = nfcUtil.readNfcTag(intent);
			
			if (null == readInfo || "".equals(readInfo)) {
				showToast("Tag data is null!");
				return;
			}
			
			String tagInfo[] = readInfo.split(";");
			if (4 == tagInfo.length) {
				strAccountNumber = tagInfo[3];
				editAccount.setText(strAccountNumber);
			} else {
				showToast("Tag data formate error!");
			}
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 0 && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == 5) {
			if(TextUtils.isEmpty(editAccount.getText().toString())
					|| TextUtils.isEmpty(editPassword.getText().toString())) {

				Toast.makeText(EditAccountActivity.this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
				return super.onKeyDown(keyCode, event);
			} else {
				strAccountNumber = editAccount.getText().toString();
				strPassword = editPassword.getText().toString();
			}
			
			Account account = accountBizImpl.findAccount(strAccountNumber, true);
			if(null != account){
				if(!strPassword.equals(account.getPassword())){
					Toast.makeText(EditAccountActivity.this, "Password Error! Ignore this operation!", Toast.LENGTH_SHORT).show();
					return super.onKeyDown(keyCode, event);
				}
				Intent intent = new Intent(EditAccountActivity.this, InformationEditActivity.class);
				intent.putExtra("account", account);
				startActivityForResult(intent, 0);
			}
		}
		return super.onKeyDown(keyCode, event);
		
	}
}
