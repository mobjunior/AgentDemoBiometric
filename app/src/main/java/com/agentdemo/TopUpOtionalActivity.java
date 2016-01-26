package com.agentdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Stan on 10/5/2015.
 */
public class TopUpOtionalActivity  extends Activity{
    Button saf,airtel,orange;
    Intent i;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_up_optional);
        saf = (Button) findViewById(R.id.btnsafaricom);
        airtel = (Button) findViewById(R.id.btnairtel);
        orange = (Button) findViewById(R.id.btnairtel);

    }

    public void safi (View v){
    i=new Intent(this,TopUpAirtimeActivity.class);
        startActivity(i);
            }

    public void airte (View v){
        i=new Intent(this,TopUpAirtimeActivity.class);
        startActivity(i);
    }

    public void oran (View v){
        i=new Intent(this,TopUpAirtimeActivity.class);
        startActivity(i);
    }

    public void back (View v){
        i=new Intent(this,MainActivity.class);
        startActivity(i);
    }


}
