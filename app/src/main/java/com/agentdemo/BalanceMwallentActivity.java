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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agentdemo.db.DBService;

/**
 * Created by Stan on 9/24/2015.
 */
public class BalanceMwallentActivity extends Activity {
    public static String TAG = "AgentDemo_BalanceActivity";

    private EditText editFirstName = null;
    private EditText editLastName = null;
    private EditText editPhoneNumber = null;
    private EditText editAccountNumber = null;
    private EditText editCredit=null;
    private Button buttonCredit = null;
    Intent i;


    //private NFCUtil nfcUtil;

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
        setContentView(R.layout.balance_mwallent);

        dbService = new DBService(this);
        initViews();

        //  nfcUtil = new NFCUtil(this);
    }
    public void back (View v){
        i=new Intent(this,BalanceOptionActivity.class);
        startActivity(i);
    }


    private void  initViews(){
        editFirstName = (EditText) findViewById(R.id.edit_firstname);
        editLastName = (EditText) findViewById(R.id.edit_lastname);
        editPhoneNumber = (EditText) findViewById(R.id.edit_phonenumber);
        editAccountNumber = (EditText) findViewById(R.id.edit_accountnumber);
        editCredit=(EditText)findViewById(R.id.edit_credit);
        buttonCredit = (Button) findViewById(R.id.button_credit);
        buttonCredit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                balanceAccount();
            }
        });
    }

    private void balanceAccount(){
        SQLiteDatabase db = dbService.getWritableDatabase();
        String sql = "select * from t_account where account=?";

        Cursor cursor = db.rawQuery(sql, new String[]{editAccountNumber.getText().toString()});
        Log.d(TAG, "balanceAccount -- cursor count: " + cursor.getCount());
        if(cursor.getCount() == 0) {
            //showResultDialog(1);
            Toast.makeText(BalanceMwallentActivity.this, "Can't find this account in database, please check!", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToFirst();
            //String phonenumber = cursor.getString(cursor.getColumnIndex("phonenumber"));
            //if(phonenumber.equals(editPhoneNumber.getText().toString())) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String account = cursor.getString(cursor.getColumnIndex("account"));
            String phone = cursor.getString(cursor.getColumnIndex("phonenumber"));
            double balance = cursor.getDouble(cursor.getColumnIndex("amount"));


            int id = cursor.getInt(cursor.getColumnIndex("_id"));
             Double fAmount = cursor.getDouble(cursor.getColumnIndex("amount"));
           // balance = Double.parseDouble(editCredit.getText().toString());
            if(fAmount > 0) {
                //fAmount += fCredit;
                String sql1 = "select * from t_account where _id=?";
                Cursor cursorAmt = db.rawQuery(sql1, new String[]{String.valueOf(id)});
                cursorAmt.moveToFirst();
                fAmount=cursorAmt.getDouble(cursor.getColumnIndex("amount"));

                Log.d(TAG,"fMOUNT"+fAmount);
              //  balance = fAmount;
                //showResultDialog(0);
                Intent intent = new Intent(BalanceMwallentActivity.this, BalancePreview.class);
                intent.putExtra("name", name);
                intent.putExtra("account", account);
                intent.putExtra("phone", phone);
                intent.putExtra("Balance", balance);
                intent.putExtra("credit", fAmount);
               intent.putExtra("newbalance", mAmount);
                startActivityForResult(intent, 0);
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
            message = "Balance amount must be larger than 0, please check!";
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
