package com.agentdemo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agentdemo.biz.AccountBiz;
import com.agentdemo.biz.impl.AccountBizImpl;
import com.agentdemo.entity.Account;

public class WithdrawFirstMwallent extends Activity {

    private static final String TAG = "WithdarwaFirstMwallent";

    private EditText editPhoneNumber = null;
    private EditText editWithdraw = null;
    private Button validate = null;
    Intent i;

    private String strPhoneNumber = null;
    private String strWithdraw = null;

   // private NFCUtil nfcUtil;

    private AccountBiz accountBizImpl = null;

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
        setContentView(R.layout.withdraw_first_mwallent);

        accountBizImpl = new AccountBizImpl(this);
        initViews();

      //  nfcUtil = new NFCUtil(this);
    }
    public void back (View v){
        i=new Intent(this,WithdrawOptionActivity.class);
        startActivity(i);
    }
    private void initViews() {
        editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        editPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i("chenyang","hasFocus = " + hasFocus);
                if (hasFocus) {
                    // mHandler.sendEmptyMessage(0);
                }
            }
        });

        editPhoneNumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("chenyang", "click");
                // mHandler.sendEmptyMessage(0);
            }
        });

        editPhoneNumber.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                Log.i("chenyang", "longclick");
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

                if (TextUtils.isEmpty(editPhoneNumber.getText().toString())
                        || TextUtils.isEmpty(editWithdraw.getText().toString())) {

                    return;
                } else {
                    strPhoneNumber = editPhoneNumber.getText().toString();
                    strWithdraw = editWithdraw.getText().toString();
                }

                Account account = accountBizImpl.findAccount(strPhoneNumber, true);
                if (null == account) {
                    return;
                } else {
                    if (Double.valueOf(strWithdraw) <= 0) {
                        Toast.makeText(WithdrawFirstMwallent.this, "Withdraw not less than 0!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (account.getAmount() < Double.valueOf(strWithdraw)) {
                        Toast.makeText(WithdrawFirstMwallent.this, "Insufficient account balance!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(WithdrawFirstMwallent.this, WithdrawPreviewActivity.class);
                    intent.putExtra("withdraw", strWithdraw);
                    intent.putExtra("phoneNumber", account);
                    startActivityForResult(intent, 0);
                }

            }
        });
    }


}
