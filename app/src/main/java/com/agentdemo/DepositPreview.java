package com.agentdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.nbbse.mobiprint3.Printer;

import com.agentdemo.printer.JBInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DepositPreview extends Activity {

    private TextView textDateTime = null;
    //	private TextView textFirstName = null;
//	private TextView textLastName = null;
    private TextView textName = null;
    private TextView textPhoneNumber = null;
    private TextView textAccount = null;
    private TextView textCredit = null;
    private TextView textPreBalance = null;
    private TextView textNewBalance = null;

    private Button buttonPrint = null;

    private String strDateTime;
    //	private String strFirstName;
//	private String strLastName;
    private String strName;
    private String strPhoneNumber;
    private String strAccount;

    private double credit;
    private double preBalance;
    private double newBalance;

//    private Printer print;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.deposit_account_preview);
        JBInterface.initPrinter();
        initData();
        initViews();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            strName = bundle.getString("name");
            strPhoneNumber = bundle.getString("phone");
            strAccount = bundle.getString("account");
            credit = bundle.getDouble("credit");
            preBalance = bundle.getDouble("prebalance");
            newBalance = bundle.getDouble("newbalance");
        }
    }

    private void initViews() {
        textDateTime = (TextView) findViewById(R.id.text_datetime);
        SimpleDateFormat df = new SimpleDateFormat("dd MMMMM yyyy      HH:mm", Locale.ENGLISH);
        strDateTime = df.format(new Date());
        textDateTime.setText(strDateTime);

        textName = (TextView) findViewById(R.id.text_name);
        textName.setText("Name: " + strName);

        textPhoneNumber = (TextView) findViewById(R.id.text_phonenumber);
        textPhoneNumber.setText("Phone Number: " + strPhoneNumber);
        textAccount = (TextView) findViewById(R.id.text_account);
        textAccount.setText("Account Number: " + strAccount);

        textCredit = (TextView) findViewById(R.id.text_credit_amount);
        textCredit.setText("Your account has been credited with " + credit + " KSH");
        textPreBalance = (TextView) findViewById(R.id.text_pre_balance);
        textPreBalance.setText("Previous Balance: " + preBalance + " KSH");
        textNewBalance = (TextView) findViewById(R.id.text_new_balance);
        textNewBalance.setText("New Balance: " + newBalance + " KSH");
        buttonPrint = (Button) findViewById(R.id.button_print);
        buttonPrint.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                if(print.getPaperStatus() != 1) {
//                    Toast.makeText(DepositPreview.this, "No Paper.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                print.printBitmap(getResources().openRawResource(R.raw.mobiwire_logo));
//                print.printText("     DEPOSIT ", 2);
//                print.printText("\n  " + strDateTime
//                        + "\n\nName: " + strName
//                        + "\nPhone Number: " + strPhoneNumber
//                        + "\nAccount Number: " + strAccount
//                        + "\n--------------------------------"
//                        + "\nYour account has been credited  with " + credit + " KSH"
//                        + "\nPreview Balance: " + preBalance + " KSH"
//                        + "\nNew Balance: " + newBalance + " KSH"
//                        + "\n--------------------------------"
//                        + "\n\nThank you for choosing Eclectics  Solution!");
//
//                print.printEndLine();
//
//                setResult(RESULT_OK);
//                finish();
                JBInterface.setBold();
                JBInterface.setLeft();
//                JBInterface.printImageWithPath(uriPath);

//                JBInterface.printImageInAssets(BalancePreview.this, "pic.bmp");
                JBInterface.print("     CASH DEPOSIT ");
                JBInterface.print("\n  " + strDateTime
                        + "\n\nName: " + strName
                        + "\nPhone Number: " + strPhoneNumber
                        + "\nAccount Number: " + strAccount
                        + "\n--------------------------------"
                        + "\nYour account has been credited  with " + credit + " KSH"
                        + "\nPrevious Balance: " + preBalance + " KSH"
                        + "\nNew Balance: " + newBalance + " KSH"
                        + "\n--------------------------------"
                        + "\n\nThank you for choosing Eclectics  Solution!");
                JBInterface.printEndLine();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == 5) {
//            if(print.getPaperStatus() != 1) {
//                Toast.makeText(DepositPreview.this, "No Paper.", Toast.LENGTH_SHORT).show();
//                return super.onKeyDown(keyCode, event);
//            }
//            print.printBitmap(getResources().openRawResource(R.raw.mobiwire_logo));
//            print.printText("     DEPOSIT", 2);
//            print.printText("\n  " + strDateTime
//                    + "\n\nName: " + strName
//                    + "\nPhone Number: " + strPhoneNumber
//                    + "\nAccount Number: " + strAccount
//                    + "\n--------------------------------"
//                    + "\nYour account has been credited  with " + credit + " KSH"
//                    + "\nPreview Balance: " + preBalance + " KSH"
//                    + "\nNew Balance: " + newBalance + " KSH"
//                    + "\n--------------------------------"
//                    + "\n\nThank you for choosing Eclectics  Solution!");
//
//            print.printEndLine();
//
//            setResult(RESULT_OK);
//            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {

        JBInterface.closePrinter();
//        begin = false;
        super.onDestroy();
    }

}
