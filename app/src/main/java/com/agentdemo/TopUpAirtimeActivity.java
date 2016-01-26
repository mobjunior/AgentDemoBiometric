package com.agentdemo;

/**
 * Created by Stan on 10/1/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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



public class TopUpAirtimeActivity extends Activity {
    public static String TAG = "TopUpAirtimeActivity";
    private EditText editNumber = null;
    private EditText editAmount = null;
    private Button buttonOK = null;
    Intent i;
    String number;
    String amount;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editNumber.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_up_airtime);

        initViews();
    }
    public void back(View v) {
        i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void initViews() {
        editNumber = (EditText) findViewById(R.id.edit_number);
        editNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i("stan", "hasFocus = " + hasFocus);
                if (hasFocus) {
                    // mHandler.sendEmptyMessage(0);
                }
            }
        });

        editNumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("stan","click");
                // mHandler.sendEmptyMessage(0);
            }
        });

        editNumber.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                Log.i("stan","longclick");
                // mHandler.sendEmptyMessage(0);
                return false;
            }
        });
        editAmount = (EditText) findViewById(R.id.edit_amount);
        buttonOK = (Button) findViewById(R.id.button_ok);
        buttonOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                number = editNumber.getText().toString();
                amount = editAmount.getText().toString();
                Log.d(TAG, "number:" + number + " amount:" + amount);

                if(TextUtils.isEmpty(number) || TextUtils.isEmpty(amount)) {
                    Toast.makeText(TopUpAirtimeActivity.this, "All fields are mandatory!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(TopUpAirtimeActivity.this, TopUpPreviewActivity.class);
                intent.putExtra("number", number);
                intent.putExtra("amount", amount);

                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK) {
            finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == 5) {
            number = editNumber.getText().toString();
            amount = editAmount.getText().toString();
            Log.d(TAG, "number:" + number + " amount:" + amount);

            if(TextUtils.isEmpty(number) || TextUtils.isEmpty(amount)) {
                Toast.makeText(TopUpAirtimeActivity.this, "All fields are mandatory!", Toast.LENGTH_SHORT).show();
                return super.onKeyDown(keyCode, event);
            }

            Intent intent = new Intent(TopUpAirtimeActivity.this, TopUpPreviewActivity .class);
            intent.putExtra("number", number);
            intent.putExtra("amount", amount);

            startActivityForResult(intent, 0);
        }
        return super.onKeyDown(keyCode, event);

    }

}
