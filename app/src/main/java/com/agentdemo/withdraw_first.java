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

public class withdraw_first extends Activity {

    private static final String TAG = "withdraw_first";

    private EditText editAccountNumber = null;
    private EditText editWithdraw = null;
    private Button validate = null;

    private String strAccountNumber = null;
    private String strWithdraw = null;

    private NFCUtil nfcUtil;
    Intent i;

    private AccountBiz accountBizImpl = null;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editAccountNumber.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdraw_first);

             accountBizImpl = new AccountBizImpl(this);
        initViews();

        nfcUtil = new NFCUtil(this);
    }

    public void back (View v){
        i=new Intent(this,WithdrawOptionActivity.class);
        startActivity(i);
    }

    private void initViews() {
        editAccountNumber = (EditText) findViewById(R.id.edit_account_number);
        editAccountNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i("stan","hasFocus = " + hasFocus);
                if (hasFocus) {
                   // mHandler.sendEmptyMessage(0);
                }
            }
        });

        editAccountNumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("stan", "click");
               // mHandler.sendEmptyMessage(0);
            }
        });

        editAccountNumber.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                Log.i("stan", "longclick");
               // mHandler.sendEmptyMessage(0);
                return false;
            }
        });
        editWithdraw = (EditText) findViewById(R.id.edit_withdraw);
        validate = (Button) findViewById(R.id.button_validate);

        validate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (TextUtils.isEmpty(editAccountNumber.getText().toString())
                        || TextUtils.isEmpty(editWithdraw.getText().toString())) {

                    Toast.makeText(withdraw_first.this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    strAccountNumber = editAccountNumber.getText().toString();
                    strWithdraw = editWithdraw.getText().toString();
                }

                Account account = accountBizImpl.findAccount(strAccountNumber, true);
                if (null == account) {
                    return;
                } else {
                    if (Double.valueOf(strWithdraw) <= 0) {
                        Toast.makeText(withdraw_first.this, "Withdraw not less than 0!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (account.getAmount() < Double.valueOf(strWithdraw)) {
                        Toast.makeText(withdraw_first.this, "Insufficient account balance!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(withdraw_first.this, WithdrawPreviewActivity.class);
                    intent.putExtra("withdraw", strWithdraw);
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
                editAccountNumber.setText(strAccountNumber);
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
            if(TextUtils.isEmpty(editAccountNumber.getText().toString())
                    || TextUtils.isEmpty(editWithdraw.getText().toString())) {

                Toast.makeText(withdraw_first.this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
                return super.onKeyDown(keyCode, event);
            } else {
                strAccountNumber = editAccountNumber.getText().toString();
                strWithdraw = editWithdraw.getText().toString();
            }

            Account account = accountBizImpl.findAccount(strAccountNumber, true);
            if(null == account){
                return super.onKeyDown(keyCode, event);
            }else{
                if(Double.valueOf(strWithdraw) <= 0){
                    Toast.makeText(withdraw_first.this, "Withdraw not less than 0!", Toast.LENGTH_SHORT).show();
                    return super.onKeyDown(keyCode, event);
                }
                if(account.getAmount() < Double.valueOf(strWithdraw)){
                    Toast.makeText(withdraw_first.this, "Insufficient account balance!", Toast.LENGTH_SHORT).show();
                    return super.onKeyDown(keyCode, event);
                }
                Intent intent = new Intent(withdraw_first.this, MainActivity.class);
                intent.putExtra("withdraw", strWithdraw);
                intent.putExtra("account", account);
                startActivityForResult(intent, 0);
            }
        }
        return super.onKeyDown(keyCode, event);

    }
}
