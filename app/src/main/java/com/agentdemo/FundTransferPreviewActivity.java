package com.agentdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.agentdemo.printer.JBInterface;

//import com.nbbse.mobiprint3.Printer;

/**
 * Created by Stan on 10/2/2015.
 */
public class FundTransferPreviewActivity extends Activity{
    private TextView textDateTime = null;
    //	private TextView textFirstName = null;
//	private TextView textLastName = null;
    private TextView textName = null;
    private TextView textPhoneNumber = null;
    private TextView textAccount = null;
    private TextView textAccountTo = null;
    private TextView textAmount = null;

    private Button buttonPrint = null;

    private String strDateTime;
    //	private String strFirstName;
//	private String strLastName;
    private String strName;
    private String strPhoneNumber;
    private String strAccount;
    private String strAccountTo;

    private double amount;
//    private Printer print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fund_transfer_preview);
        JBInterface.initPrinter();


    }

    public void print(View arg0) {
//        if(print.getPaperStatus() != 1) {
//            Toast.makeText(FundTransferPreviewActivity.this, "No Paper.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        print.printBitmap(getResources().openRawResource(R.raw.mobiwire_logo));
//        print.printText("     Fund Transfer ", 2);
//        print.printText("\n  " + "5/5/2015"
//                + "\n\nName: " + "stan kamau"
//                + "\nPhone Number: " + "074546336"
//                + "\nAccount Number: " + "09876543"
//                + "\n--------------------------------"
//                + "\nYour account have transfered amount " + "500" + " KSH"
//                + "\nTo Account: " + "34323444" + " KSH"
//                + "\n--------------------------------"
//                + "\n\nThank you for choosing Eclectics  Solution!");
//
//        print.printEndLine();
//
//        setResult(RESULT_OK);
//        finish();

        JBInterface.setBold();
        JBInterface.setLeft();
//                JBInterface.printImageWithPath(uriPath);

//                JBInterface.printImageInAssets(BalancePreview.this, "pic.bmp");
        JBInterface.print("     FUND TRANSFER ");
        JBInterface.print("\n  " + "5/5/2015"
                + "\n\nName: " + "stan kamau"
                + "\nPhone Number: " + "074546336"
                + "\nAccount Number: " + "09876543"
                + "\n--------------------------------"
                + "\nYour account have transfered amount " + "500" + " KSH"
                + "\nTo Account: " + "34323444" + " KSH"
                + "\n--------------------------------"
                + "\n\nThank you for choosing Eclectics  Solution!");
        JBInterface.printEndLine();
    }

    @Override
    protected void onDestroy() {

        JBInterface.closePrinter();
//        begin = false;
        super.onDestroy();
    }
}
