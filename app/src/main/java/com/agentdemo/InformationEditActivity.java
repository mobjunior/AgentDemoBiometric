package com.agentdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agentdemo.db.DBService;
import com.agentdemo.entity.Account;

public class InformationEditActivity extends Activity {

	private EditText editFirstName = null;
	private EditText editLastName = null;
	private EditText editPhoneNumber = null;
	private EditText editPassword = null;
	private Button buttonOk = null;
	
	private DBService dbService = null;

	private Account account = null;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editPhoneNumber.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_edit);

		initData();
		initViews();
	}

	private void initViews() {
		editFirstName = (EditText) findViewById(R.id.edit_firstname);
		editFirstName.setText(account.getName().split(" ")[0]);
		editLastName = (EditText) findViewById(R.id.edit_lastname);
		editLastName.setText(account.getName().split(" ")[1]);
		editPhoneNumber = (EditText) findViewById(R.id.edit_phonenumber);
		editPhoneNumber.setText(account.getPhoneNumber());
		editPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				android.util.Log.i("chenyang","hasFocus = " + hasFocus);
				if (hasFocus) {
					//mHandler.sendEmptyMessage(0);
				}
			}
		});
		
		editPhoneNumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				android.util.Log.i("chenyang", "click");
				//mHandler.sendEmptyMessage(0);
			}
		});
		
		editPhoneNumber.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				android.util.Log.i("chenyang", "longclick");
				//mHandler.sendEmptyMessage(0);
				return false;
			}
		});
		editPassword = (EditText) findViewById(R.id.edit_password);
		editPassword.setText(account.getPassword());
		buttonOk = (Button) findViewById(R.id.button_ok);
		buttonOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(editFirstName.getText().toString())
						|| TextUtils.isEmpty(editLastName.getText().toString())
						|| TextUtils.isEmpty(editPhoneNumber.getText().toString())
						|| TextUtils.isEmpty(editPassword.getText().toString())) {
					Toast.makeText(InformationEditActivity.this,
							"All fields are mandatory.", Toast.LENGTH_SHORT)
							.show();
					return;
				} 
				
				saveAccounts();
				
				account.setName(editFirstName.getText().toString() + " " + editLastName.getText().toString());
				account.setPhoneNumber(editPhoneNumber.getText().toString());
				account.setPassword(editPassword.getText().toString());
				
				Intent intent = InformationEditActivity.this.getIntent();
				intent.setComponent(new ComponentName(
						InformationEditActivity.this, NfcTagWriteActivity.class));
				intent.putExtra("account", account);
				startActivityForResult(intent, 0);
			}

		});
	}

	private void initData() {
		dbService = new DBService(this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			account = (Account) bundle.getSerializable("account");
		}
	}
	
	private void saveAccounts() {
		
		String sql = "";
		Object[] args = null;

		sql = "update t_account set name=?,phonenumber=?,password=? where account=?";
		args = new Object[] { editFirstName.getText().toString() + " " + editLastName.getText().toString(), 
				editPhoneNumber.getText().toString(), editPassword.getText().toString(), account.getAccount() };
		SQLiteDatabase db = dbService.getWritableDatabase();
		db.execSQL(sql, args);
		db.close();
			
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
			if (TextUtils.isEmpty(editFirstName.getText().toString())
					|| TextUtils.isEmpty(editLastName.getText().toString())
					|| TextUtils.isEmpty(editPhoneNumber.getText().toString())
					|| TextUtils.isEmpty(editPassword.getText().toString())) {
				Toast.makeText(InformationEditActivity.this,
						"All fields are mandatory.", Toast.LENGTH_SHORT)
						.show();
				return super.onKeyDown(keyCode, event);
			} 
			
			saveAccounts();
			
			account.setName(editFirstName.getText().toString() + " " + editLastName.getText().toString());
			account.setPhoneNumber(editPhoneNumber.getText().toString());
			account.setPassword(editPassword.getText().toString());
			
			Intent intent = InformationEditActivity.this.getIntent();
			intent.setComponent(new ComponentName(
					InformationEditActivity.this, MainActivity.class));
			intent.putExtra("account", account);
			startActivityForResult(intent, 0);
		}
		return super.onKeyDown(keyCode, event);
		
	}
}
