package com.agentdemo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.agentdemo.util.MonitorUsbThread;

import static com.agentdemo.R.id.action_settings;


public class MainActivity extends Activity {
    public static String TAG = "agentdemo_MainActivity";
    private static final String ACTION_USB_PERMISSION = "com.cytmxk.usbutils.USB_PERMISSION";
    private static int MAIN_MENU_NUM = 8;
    private ListView mainlistview;
    private MonitorUsbThread monitorUsbThread = null;
    private UsbManager usbManger = null;
    private PendingIntent mPermissionIntent = null;



    private String[] tvdata = new String[] {
            "Withdraw", "Deposit", "Balance", "Settings"
    };

    private int[] ivdata = new int[] {
            R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5, R.drawable.icon6, R.drawable.icon7, R.drawable.icon8
    };

    private String topup_list[] = new String[]{"a. Cash Payment", "b. m-Payment"};
    private String account_feature[]=new String[]{"a. Create Customer Account", "b. Credit Customer Account"};

    private Button button1 = null;
    private Button button2 = null;
    private Button button3 = null;
    private Button button4 = null;
    private Button button5 = null;
    private Button button6 = null;
    private Button button7 = null;
    private Button button8 = null;
    private Button button9 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        new Thread(monitorUsbThread).start();
    }

    private void initOthers() {
        usbManger = (UsbManager) getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        //registerReceiver(mUsbReceiver, filter);
        monitorUsbThread = new MonitorUsbThread(usbManger,mPermissionIntent);
    }

    private void initViews() {
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(myButtonClickListener);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(myButtonClickListener);
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(myButtonClickListener);
        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(myButtonClickListener);
        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(myButtonClickListener);
        button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(myButtonClickListener);
        button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(myButtonClickListener);
    }
    MyButtonClickListener myButtonClickListener = new MyButtonClickListener();


    class MyButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button1:
                    Intent intent = new Intent(MainActivity.this, WithdrawOptionActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button2:
                    Intent intent2 = new Intent(MainActivity.this, DepositOptionActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.button3:
                    Intent intent3 = new Intent(MainActivity.this, BalanceOptionActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.button4:
                    Intent intent4 = new Intent(MainActivity.this, CustomerManagementActivity.class);
                    startActivity(intent4);
                    break;

                case R.id.button5:
                    Intent intent5 = new Intent(MainActivity.this, TopUpOtionalActivity.class);
                    startActivity(intent5);
                    break;
                case R.id.button6:
                    Intent intent6 = new Intent(MainActivity.this, FundTransferActivity.class);
                    startActivity(intent6);
                    break;
                case R.id.button7:
                    Intent intent7 = new Intent(MainActivity.this, PayUtilitiesOptionalActivity.class);
                    startActivity(intent7);
                    break;
                default:
                    break;
            }

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
  /* public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Exit").setMessage("Sure to Exit?").setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Log.d(TAG, "Dialog Item " + arg1 + " clicked!");
                monitorUsbThread.setRun(false);
              // unregisterReceiver(mUsbReceiver);
                onBackReally();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Log.d(TAG, "Dialog Item " + arg1 + " clicked!");
            }
        }).show();
//
    }

    private void onBackReally() {
        super.onBackPressed();
    }*/

}
