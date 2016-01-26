package com.agentdemo;

/**
 * Created by Stan on 10/1/2015.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agentdemo.barcode.util.CodeUtils;
import com.agentdemo.printer.JBInterface;
import com.agentdemo.util.BitmapUtil;
//import com.nbbse.mobiprint3.Printer;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TopUpPreviewActivity extends Activity {

    private static final String bmp_path = "/data/data/com.nbbse.printerdemo/code.bmp";
    private TextView textCode = null;
    private TextView textTrans = null;
    private ImageView imageBarcode = null;
    //private Button buttonPrint = null;

//    private Printer print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.top_up_airtime_preview);
        JBInterface.initPrinter();

        initViews();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                print();
            }
        }, 2000);


    }

    private void initViews() {
        textCode = (TextView) findViewById(R.id.text_topup_code);
        textTrans = (TextView) findViewById(R.id.text_transnum);
        imageBarcode = (ImageView) findViewById(R.id.image_barcode);

        /** buttonPrint = (Button) findViewById(R.id.button_print);
         buttonPrint.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View arg0) {
        print();
        }
        }); */


        textCode.setText(genCode());
        textTrans.setText(genTransId());

        String str = textTrans.getText().toString().trim();

        Bitmap bmp = null;
        try {
            if (str != null && !"".equals(str)) {
                bmp = CodeUtils.CreateOneDCode(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bmp != null) {
            BitmapUtil.saveBmp(bmp, bmp_path);
            imageBarcode.setImageBitmap(bmp);
        }

    }

    private String genTransId() {
        String s = "";
        Random random = new Random();
        for(int i=0; i<13; i++) {
            char c = (char)(48 + random.nextInt(10));
            s += c;
        }
        return s;
    }

    private String genCode() {
        String s = "";
        Random random = new Random();
        for(int i=0; i<13; i++) {
            int x = random.nextInt(36);
            if(x >= 10) {
                x += 7;
            }

            char c = (char)(48 + x);
            s += c;
        }
        return s;
    }

    public void print() {

//        if (print.getPaperStatus() != 1) {
//            runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    Toast.makeText(TopUpPreviewActivity.this, "No Paper.", Toast.LENGTH_SHORT).show();
//                }
//            });
//            return;
//        }
//
//        print.printText("     TOP UP", 2);
//        print.printText("You have requested an airtime   voucher."
//                + "\nPlease use the following code to credit your account."
//                + "\n           TOP UP CODE");
//        print.printText(" " + textCode.getText(), 2);
//        print.printText("         Transaction ID" + "\n         " + textTrans.getText());
//
//        print.printBitmap(bmp_path);
//        print.printEndLine();
//
//        setResult(RESULT_OK);
//        finish();
        JBInterface.setBold();
        JBInterface.setLeft();
//                JBInterface.printImageWithPath(uriPath);

//                JBInterface.printImageInAssets(BalancePreview.this, "pic.bmp");
        JBInterface.print("     TOPUP ");
        JBInterface.print("\nPlease use the following code to credit your account."
                + "\n\n         TOP UP CODE");
        JBInterface.print("\n         " + textCode.getText());
        JBInterface.print("\n\nTransaction ID: " + textTrans.getText());
        JBInterface.printEndLine();
    }

    @Override
    protected void onDestroy() {

        JBInterface.closePrinter();
//        begin = false;
        super.onDestroy();
    }

}

