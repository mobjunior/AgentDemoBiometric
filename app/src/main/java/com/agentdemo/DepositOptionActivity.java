package com.agentdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Stan on 9/30/2015.
 */
public class DepositOptionActivity extends Activity {
    Button mwallet,bankacc;
    Intent i;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_option);
        mwallet = (Button) findViewById(R.id.btndepositmwallet);
        bankacc = (Button) findViewById(R.id.btndepositaccount);
    }


    public void mwallet(View v) {

           // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            i = new Intent(this, DepositMwallentAccount.class);
            startActivity(i);
        }
    public void bankacc(View v){

        i = new Intent(this, DepositAccount.class);
        startActivity(i);
    }
    public void back (View v){
        i=new Intent(this,MainActivity.class);
        startActivity(i);
    }






}
