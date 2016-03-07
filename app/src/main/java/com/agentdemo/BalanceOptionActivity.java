package com.agentdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//import package com.agentdemo.mPOS;

/**
 * Created by Stan on 9/30/2015.
 */
public class BalanceOptionActivity extends Activity {
    Button bymwallet,bankacc,byBankCard;
    Intent i;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_option);
        bymwallet = (Button) findViewById(R.id.btndepositmwallet);
        bankacc = (Button) findViewById(R.id.btndepositaccount);
        byBankCard=(Button)findViewById(R.id.btnbybankcard);
    }


    public void mwallet(View v) {

           // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            i = new Intent(this, BalanceMwallentActivity.class);
            startActivity(i);
        }
    public void bankacc(View v){

        i = new Intent(this, BalanceActivity.class);
        startActivity(i);

    }
    public  void byBankcard(View v)
    {
        i=new Intent(this,BalanceBankCardActivity.class );
        startActivity(i);
    }
    public void back (View v){
        i=new Intent(this,MainActivity.class);
        startActivity(i);
    }






}
