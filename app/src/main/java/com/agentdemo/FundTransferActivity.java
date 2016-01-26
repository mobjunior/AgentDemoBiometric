package com.agentdemo;

/**
 * Created by Stan on 9/30/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class FundTransferActivity extends Activity {

    Intent i;

    private Spinner spinner;
    private EditText accountTo;
    private EditText amount;
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fundtransfer);

        accountTo=(EditText) findViewById(R.id.edit_accountto_number);
        spinner = (Spinner) findViewById(R.id.spinner1);
            amount=(EditText) findViewById(R.id.edit_amountfund_number);

        ArrayAdapter<CharSequence> ATPaccounts_spinner = ArrayAdapter
                .createFromResource(this, R.array.spinner1,
                        android.R.layout.simple_spinner_item);

        ATPaccounts_spinner
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(ATPaccounts_spinner);

            }

    public void back(View v) {
        i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
    public void fund(View v){

        if (TextUtils.isEmpty(accountTo.getText().toString())
        || TextUtils.isEmpty(amount.getText().toString())){
            Toast.makeText(FundTransferActivity.this, "Fill All Fields!", Toast.LENGTH_SHORT).show();
            return;
    }
        else {
            Toast.makeText(FundTransferActivity.this,"Sent successfully", Toast.LENGTH_LONG).show();
            i=new Intent(this,FundTransferPreviewActivity.class);
            startActivity(i);
           /* i.putExtra("name","");
            i.putExtra("account", "");
            i.putExtra("accountto", "");
            i.putExtra("phone", "");
            i.putExtra("newbalance", "");
            startActivityForResult(i, 0);*/

        }
    }
}
