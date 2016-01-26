package com.agentdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.agentdemo.entity.Account;
import com.agentdemo.printer.JBInterface;
//import com.nbbse.mobiprint3.Printer;


public class CreateAccountPreviewActivity extends Activity {
    private TextView textFirstName = null;
    private TextView textLastName = null;
    private TextView textPhoneNumber = null;
    private TextView textAccount = null;
    private TextView textBalance = null;
    private TextView textPassword = null;
    private Button buttonPrint = null;

    private Account account = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_preview);
        JBInterface.initPrinter();
        initData();
        initViews();
    }

    private void initData() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            account = (Account) bundle.getSerializable("account");
            android.util.Log.i("stan", "CreateAccountPreviewActivity");
            android.util.Log.i("stan", "Account = " + account.getAccount());
            android.util.Log.i("stan", "Name = " + account.getName());
            android.util.Log.i("stan", "PhoneNumber = " + account.getPhoneNumber());
        }

    }

    private void initViews() {
        textFirstName = (TextView) findViewById(R.id.text_firstname);
        textFirstName.setText("First Name: " + account.getName().split(" ")[0]);
        textLastName = (TextView) findViewById(R.id.text_lastname);
        textLastName.setText("Last Name: " + account.getName().split(" ")[1]);
        textPhoneNumber = (TextView) findViewById(R.id.text_phonenumber);
        textPhoneNumber.setText("Phone Number: " + account.getPhoneNumber());
        textAccount = (TextView) findViewById(R.id.text_account);
        textAccount.setText("Account Number: " + account.getAccount());
        textBalance = (TextView) findViewById(R.id.text_balance);
        textBalance.setText("Balance: " + account.getAmount() + " KSH");
        textPassword = (TextView) findViewById(R.id.text_password);
        textPassword.setText("Password: " + account.getPassword());

        buttonPrint = (Button) findViewById(R.id.button_print);
        buttonPrint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                if(print.getPaperStatus() != 1) {
//                    Toast.makeText(BalancePreview.this, "No Paper.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                setResult(RESULT_OK);
//                finish();
                //path to eclectics logo
//                Uri uri = Uri.parse(uriPath);
                JBInterface.setBold();
                JBInterface.setLeft();
//                JBInterface.printImageWithPath(uriPath);

//                JBInterface.printImageInAssets(BalancePreview.this, "pic.bmp");
                JBInterface.print("      Eclectics Account");
                JBInterface.print("\nPlease find the details below:"
                        + "\n\nFirst Name: " + account.getName().split(" ")[0]
                        + "\nLast Name: " + account.getName().split(" ")[1]
                        + "\nPhone Number: " + account.getPhoneNumber() + "\n");
                JBInterface.print("\nAccount Number: " + account.getAccount());
                JBInterface.print("\nAccount Balance: " + account.getAmount() + " KSH");
                JBInterface.print("\nPassword: " + account.getPassword());
                JBInterface.print("\nThank you for choosing Eclectics  solution!");
                JBInterface.printEndLine();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 5) {
//			if (print.getPaperStatus() != 1) {
//				Toast.makeText(CreateAccountPreviewActivity.this, "No Paper.", Toast.LENGTH_SHORT).show();
//				return super.onKeyDown(keyCode, event);
//			}
//			print.printBitmap(getResources().openRawResource(R.raw.mobiwire_logo));
//			print.printText("MobiBank Account", 2);
//			print.printText("\n Please find the details below:"
//					+ "\n\nFirst Name: " + account.getName().split(" ")[0]
//					+ "\nLast Name: " + account.getName().split(" ")[1]
//					+ "\nPhone Number: " + account.getPhoneNumber() + "\n");
//			print.printText("Account Number: " + account.getAccount());
//			print.printText("Account Balance: " + account.getAmount() + " KSH");
//			print.printText("Password: " + account.getPassword());
//			print.printText("\nThank you for choosing Eclectics  solution!");
//			print.printEndLine();
//
//			setResult(RESULT_OK);
//
//			finish();
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



