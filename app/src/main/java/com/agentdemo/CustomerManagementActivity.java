package com.agentdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Stan on 9/23/2015.
 */
public class CustomerManagementActivity extends Activity {
    Intent i;

    private String account_feature[] = new String[]{"Create Eclectics Account", "Credit Eclectics Account", "Edit Eclectics Account"};


    private Button buttonCustomer = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.customer_management);

        initViews();
    }
    public void back (View v){
        i=new Intent(this,MainActivity.class);
        startActivity(i);
    }

        private void initViews(){
            buttonCustomer = (Button) findViewById(R.id.button_customer_mgmt);
            buttonCustomer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    showAccountFeatureDialog();
                }
            });
    }

    private void showAccountFeatureDialog() {
        new AlertDialog.Builder(this).setTitle("Account:").setItems(account_feature, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int which) {
                if(which == 0) {
                    Intent intent = new Intent(CustomerManagementActivity.this, CreateAccountActivity.class);
                    startActivityForResult(intent, 0);
                } else if(which == 1) {
                    Intent intent = new Intent(CustomerManagementActivity.this, DepositAccount.class);
                    startActivityForResult(intent, 0);
                } else if(which == 2){
                    Intent intent = new Intent(CustomerManagementActivity.this, EditAccountActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        }).show();
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);

    }
}




