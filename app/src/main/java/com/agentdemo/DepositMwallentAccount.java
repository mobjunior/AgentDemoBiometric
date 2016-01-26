package com.agentdemo;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agentdemo.db.DBService;

public class DepositMwallentAccount extends Activity {
    public static String TAG = "PrintDemo_DepositAccount";

    private EditText editFirstName = null;
    private EditText editLastName = null;
    private EditText editPhoneNumber = null;
    private EditText editCredit = null;
    private EditText editAccountNumber = null;
    private Button buttonCredit = null;
    Intent i;

  //  private NFCUtil nfcUtil;

    private DBService dbService;

    private Double mAmount = 0.0;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editPhoneNumber.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_mwallent_account);

        dbService = new DBService(this);
        initViews();

       // nfcUtil = new NFCUtil(this);
    }
    public void back (View v){
        i=new Intent(this,MainActivity.class);
        startActivity(i);
    }

    private void initViews() {
        editFirstName = (EditText) findViewById(R.id.edit_firstname);
        editLastName = (EditText) findViewById(R.id.edit_lastname);
        editPhoneNumber = (EditText) findViewById(R.id.edit_phonenumber);
        editCredit = (EditText) findViewById(R.id.edit_credit);
        editAccountNumber = (EditText) findViewById(R.id.edit_accountnumber);
        editPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i("stan","hasFocus = " + hasFocus);
                if (hasFocus) {
                  //  mHandler.sendEmptyMessage(0);
                }
            }
        });

        editPhoneNumber.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("stan","click");
               // mHandler.sendEmptyMessage(0);
            }
        });

        editPhoneNumber.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                Log.i("chenyang","longclick");
              //  mHandler.sendEmptyMessage(0);
                return false;
            }
        });
        buttonCredit = (Button) findViewById(R.id.button_credit);
        buttonCredit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                creditAccount();
            }
        });
    }

    private void creditAccount() {
        SQLiteDatabase db = dbService.getWritableDatabase();
        String sql = "select * from t_account where account=?";

        Cursor cursor = db.rawQuery(sql, new String[]{editPhoneNumber.getText().toString()});
        Log.d(TAG, "creditAccount -- cursor count: " + cursor.getCount());
        if(cursor.getCount() == 0) {
            //showResultDialog(1);
            Toast.makeText(DepositMwallentAccount.this, "Can't find this account in database, please check!", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToFirst();
            //String phonenumber = cursor.getString(cursor.getColumnIndex("phonenumber"));
            //if(phonenumber.equals(editPhoneNumber.getText().toString())) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String account = cursor.getString(cursor.getColumnIndex("account"));
            String phone = cursor.getString(cursor.getColumnIndex("phonenumber"));
            double prebalance = cursor.getDouble(cursor.getColumnIndex("amount"));


            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            Double fAmount = cursor.getDouble(cursor.getColumnIndex("amount"));
            Double fCredit = Double.parseDouble(editCredit.getText().toString());
            if(fCredit > 0) {
                fAmount += fCredit;
                String sql_update = "update t_account set amount=? where _id=?";
                db.execSQL(sql_update, new Object[]{fAmount,id});
                mAmount = fAmount;
                //showResultDialog(0);
                Intent intent = new Intent(DepositMwallentAccount.this, DepositPreview.class);
                intent.putExtra("name", name);
                intent.putExtra("account", account);
                intent.putExtra("phone", phone);
                intent.putExtra("prebalance", prebalance);
                intent.putExtra("credit", fCredit);
                intent.putExtra("newbalance", mAmount);
                startActivityForResult(intent, 0);
            } else {
                //showResultDialog(3);
                Toast.makeText(DepositMwallentAccount.this, "Credit amount must be larger than 0, please check!", Toast.LENGTH_SHORT).show();
            }
//			} else {
//				showResultDialog(2);
//			}

        }
        cursor.close();
        db.close();
    }

    private void showResultDialog(int result) {
        String message;
        if(result == 1) {
            message = "Can't find this account in database, please check!";
        } else if (result == 2) {
            message = "account and phonenumber don't match, please check!";
        } else if (result == 3) {
            message = "Credit amount must be larger than 0, please check!";
        } else {
            message ="Account: " + editAccountNumber.getText() + "\nCredit: " + editCredit.getText() + "\nTotal: " + mAmount;
        }

        new AlertDialog.Builder(this).setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        }).show();
    }


    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


}


