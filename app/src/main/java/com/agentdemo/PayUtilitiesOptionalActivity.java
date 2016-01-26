package com.agentdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Stan on 10/5/2015.
 */
public class PayUtilitiesOptionalActivity extends Activity {
    Button kplc,dstv,water;
    Intent i;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payutitiesoptional);
        kplc = (Button) findViewById(R.id.btnkplc);
        dstv = (Button) findViewById(R.id.btndstv);
        water = (Button) findViewById(R.id.btnwater);

    }
    public void back(View v){
        i=new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void kplc(View v) {
        i = new Intent(this, PayUtilityActivity.class);
        startActivity(i);
    }

    public void dstv(View v) {
        i = new Intent(this, PayUtilityActivity.class);
        startActivity(i);
    }

    public void water(View v) {
        i = new Intent(this, PayUtilityActivity.class);
        startActivity(i);
    }
}
