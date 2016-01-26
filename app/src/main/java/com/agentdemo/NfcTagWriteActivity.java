package com.agentdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
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
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.agentdemo.entity.Account;
import com.agentdemo.util.NFCUtil;

public class NfcTagWriteActivity extends Activity {
	
	private static final String TAG = "NfcTagWriteActivity";

	private TextView editFirstName;
	private TextView editLastName;
	private TextView editPhoneNumber;
	private TextView editAccountNumber;
	
	private TextView textHintInfo;
	
	private Button buttonWrite;
	private Button buttonSkip;
	
	private Boolean isWrite = false;
	
	private NFCUtil nfcUtil;
	
	private Account account = null;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			switch(msg.what){
			case 0 :
				imm.hideSoftInputFromWindow(editPhoneNumber.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				break;
			case 1 : 
				imm.hideSoftInputFromWindow(editAccountNumber.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				break;
			default:
				break;
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfc_tag_write);

		initData();
		initView();
		
		nfcUtil = new NFCUtil(this);

	}
	
	private void initData() {
		
		isWrite = false;
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			account = (Account) bundle.getSerializable("account");
		}
	}
	
	private void initView() {

		editFirstName = (TextView) findViewById(R.id.edit_firstname);
		editLastName = (TextView) findViewById(R.id.edit_lastname);
		editPhoneNumber = (TextView) findViewById(R.id.edit_phonenumber);	
		editAccountNumber = (TextView) findViewById(R.id.edit_accountnumber);
		
		editFirstName.setText(account.getName().split(" ")[0]);
		editLastName.setText(account.getName().split(" ")[1]);
		editPhoneNumber.setText(account.getPhoneNumber());
		editAccountNumber.setText(account.getAccount());
		
		textHintInfo = (TextView) findViewById(R.id.hint_info);		
		
		buttonSkip = (Button) findViewById(R.id.button_skip);
		buttonSkip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isWrite = false;
				
				Intent skipIntent = getIntent();
				skipIntent.setClass(NfcTagWriteActivity.this, CreateAccountPreviewActivity.class);
				startActivityForResult(skipIntent, 0);						
			}
		});
		
		buttonWrite = (Button) findViewById(R.id.button_write);		
		buttonWrite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				if (TextUtils.isEmpty(account.getName().split(" ")[0])
						|| TextUtils.isEmpty(account.getName().split(" ")[1])
						|| TextUtils.isEmpty(account.getPhoneNumber())
						|| TextUtils.isEmpty(account.getAccount())) {
					textHintInfo.setText(R.string.nfc_tag_write_hintinfo1);
					return;
				}

				textHintInfo.setText(R.string.nfc_tag_write_hintinfo2);				
				isWrite = true;
			}
		});
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

		Log.d(TAG, "onNewIntent into...");

		if (!isWrite) {
			Log.d(TAG, "isWrite is false...");
			return;
		}
		
		if (TextUtils.isEmpty(account.getName().split(" ")[0])
				|| TextUtils.isEmpty(account.getName().split(" ")[1])
				|| TextUtils.isEmpty(account.getPhoneNumber())
				|| TextUtils.isEmpty(account.getAccount())) {
			Log.d(TAG, "All field are mandatory!");
			return;
		}
		
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())
				|| NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			
			Log.d(TAG, "ACTION_NDEF_DISCOVERED || ACTION_NDEF_DISCOVERED");
			
			String tagInfo = account.getName().split(" ")[0] + ";" + account.getName().split(" ")[1] + ";"
			                 + account.getPhoneNumber() + ";" + account.getAccount();
			
			if (nfcUtil.writeNfcTag(intent, tagInfo)) {			
				isWrite = false;
				textHintInfo.setText(R.string.nfc_tag_write_hintinfo1);
				
				Intent nfcIntent = getIntent();			
				nfcIntent.setComponent(new ComponentName(NfcTagWriteActivity.this, 
						CreateAccountPreviewActivity.class));
				//nfcIntent.setClass(NfcTagWriteActivity.this, CreateAccountPreviewActivity.class);
				startActivityForResult(nfcIntent, 0);			
			}	

		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0 && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == 5) {
			if (TextUtils.isEmpty(account.getName().split(" ")[0])
					|| TextUtils.isEmpty(account.getName().split(" ")[1])
					|| TextUtils.isEmpty(account.getPhoneNumber())
					|| TextUtils.isEmpty(account.getAccount())) {
				textHintInfo.setText(R.string.nfc_tag_write_hintinfo1);
				return super.onKeyDown(keyCode, event);
			}

			textHintInfo.setText(R.string.nfc_tag_write_hintinfo2);				
			isWrite = true;
		}
		return super.onKeyDown(keyCode, event);
		
	}
	
	
}
