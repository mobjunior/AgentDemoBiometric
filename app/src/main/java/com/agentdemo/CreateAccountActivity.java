package com.agentdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agentdemo.biz.AccountBiz;
import com.agentdemo.biz.impl.AccountBizImpl;
import com.agentdemo.db.DBService;
import com.agentdemo.entity.Account;
import com.agentdemo.finger.JBInterface;
import com.agentdemo.finger.JugeStep;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.ctrl.gpio.Ioctl;
import com.lxl.code.MSRInterface;
import com.lxl.code.MSRInterface.Attation;
import com.lxl.code.MSRInterface.OnReadSerialPortDataListener;
import com.lxl.code.MSRInterface.SerialPortData;

import com.imagpay.ttl.TTLHandler;
import com.imagpay.MessageHandler;
import com.imagpay.Settings;

public class CreateAccountActivity extends Activity {
    private MSRInterface control;
    TTLHandler ttlhandler;
    MessageHandler _mHandler;
    Settings settings;
    ImageView viewImage;
    Button b, buttonFingerPrint, buttonMSR, buttonICCard;

    private EditText editFirstName = null;
    private EditText editLastName = null;
    private EditText editPhoneNumber = null;
    private EditText editDeposit = null;
    private Button buttonCreate = null;

    Intent i;

    private String strAccountNumber = null;
    private String strPassword = null;
    private String strFullName = null;
    private Double fAmount = null;
    private int fingerPrint = 0;
    private String MSRCardData = null;
    private String ICCardData = null;


    private AccountBiz accountBizImpl = null;

    static final String TAG = "ReadFingerprintActivity";
    //    private static fpdevice fpdev = new fpdevice();
    public static Handler handler = null;
    MyThread thread;

    //*******************
    private boolean begin;
    public static boolean working;
    private static boolean isFront = false;
    boolean flag = false;

    private int rInt = -1;
    //    EditText result=(EditText)findViewById(R.id.result);
//    TextView resultText=(TextView)findViewById(R.id.resultText);
    private static int loginORserach = 0;
    private TextView resultText;
    private TextView msrResultText;
    private TextView iccardResultText;
    private int fingerNum;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            switch (msg.what) {
                case 0:
                    imm.hideSoftInputFromWindow(editPhoneNumber.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    break;
                case 1:
                    imm.hideSoftInputFromWindow(editDeposit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        accountBizImpl = new AccountBizImpl(this);
        initViews();
    }

    public void back(View v) {
        i = new Intent(this, CustomerManagementActivity.class);
        startActivity(i);
    }

    private void initViews() {
        viewImage = (ImageView) findViewById(R.id.viewImage);
        editFirstName = (EditText) findViewById(R.id.edit_firstname);
        editLastName = (EditText) findViewById(R.id.edit_lastname);
        editPhoneNumber = (EditText) findViewById(R.id.edit_phonenumber);
        b = (Button) findViewById(R.id.btnSelectPhoto);
        buttonFingerPrint = (Button) findViewById(R.id.button_read_fingerprint);
        buttonMSR = (Button) findViewById(R.id.button_register_msr);
        buttonICCard = (Button) findViewById(R.id.button_register_iccard);


        editPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                android.util.Log.i("chenyang", "hasFocus = " + hasFocus);
                if (hasFocus) {
                    //mHandler.sendEmptyMessage(0);
                }
            }
        });

        editPhoneNumber.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                android.util.Log.i("chenyang", "click");
                //mHandler.sendEmptyMessage(0);
            }
        });

        editPhoneNumber.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                android.util.Log.i("chenyang", "longclick");
                //mHandler.sendEmptyMessage(0);
                return false;
            }
        });

        editDeposit = (EditText) findViewById(R.id.edit_deposit);
        editDeposit.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //mHandler.sendEmptyMessage(1);
                }
            }
        });

        editDeposit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //mHandler.sendEmptyMessage(1);
            }
        });

        editDeposit.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                //mHandler.sendEmptyMessage(1);
                return false;
            }
        });

        buttonCreate = (Button) findViewById(R.id.button_create);
        buttonCreate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (("".equals(editFirstName.getText().toString().trim())) || (null == editFirstName.getText().toString())) {
                    Toast.makeText(CreateAccountActivity.this, "ECLECTICS Account firstname can not be empty! Ignore this operation!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (("".equals(editLastName.getText().toString().trim())) || (null == editLastName.getText().toString())) {
                    Toast.makeText(CreateAccountActivity.this, "ECLECTICS Account lastname can not be empty! Ignore this operation!", Toast.LENGTH_SHORT).show();
                    return;
                }
                strFullName = editFirstName.getText().toString() + " " + editLastName.getText().toString();

                if (("".equals(editPhoneNumber.getText().toString().trim())) || (null == editPhoneNumber.getText().toString())) {
                    Toast.makeText(CreateAccountActivity.this, "ECLECTICS Account phonenumber can not be empty! Ignore this operation!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (("".equals(editDeposit.getText().toString().trim())) || (null == editDeposit.getText().toString())) {
                    Toast.makeText(CreateAccountActivity.this, "ECLECTICS Account Deposit can not be empty! Ignore this operation!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (0 == fingerPrint) {
                    Toast.makeText(CreateAccountActivity.this, "FingerPrint Is Required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String amount = editDeposit.getText().toString();
                try {
                    fAmount = Double.parseDouble(amount);
                } catch (NumberFormatException e) {
                    // TODO: handle exception
                    Toast.makeText(CreateAccountActivity.this, "ECLECTICS Account Deposit incorrect format! Ignore this operation!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fAmount < 0) {
                    Toast.makeText(CreateAccountActivity.this, "ECLECTICS Account amount not less than zero! Ignore this operation!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (null == accountBizImpl.findLastAccount()) {
                    strAccountNumber = "10000000";
                } else {
                    strAccountNumber = String.valueOf(Integer.parseInt(accountBizImpl.findLastAccount().getAccount()) + 1);
                }


                strPassword = genPassword();

                Account account = new Account();
                account.setAccount(strAccountNumber);
                account.setName(strFullName);
                account.setPhoneNumber(editPhoneNumber.getText().toString());
                account.setPassword(strPassword);
                account.setAmount(fAmount);
                account.setFingerprint(fingerPrint);

                int registerResult = accountBizImpl.register(account);
                if (1 == registerResult) {
                    accountBizImpl.updateBasicInformation(account);
                } else if (2 == registerResult) {
                    return;
                }
//                Intent intent = new Intent(CreateAccountActivity.this, InformationEditActivity.class);
                Intent intent = new Intent(CreateAccountActivity.this, CreateAccountActivity.class);
                intent.putExtra("account", account);
                startActivityForResult(intent, 0);
            }

        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        buttonFingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFingerPrint();
            }
        });
        buttonMSR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readMSRCard();
            }
        });

        buttonICCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readICCard();
            }
        });

    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void readFingerPrint() {
        resultText = (TextView) findViewById(R.id.resultText);
        resultText.setText("");
        resultText
                .setText(getResources()
                        .getString(
                                R.string.read_fingerprint_text_status));
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    viewImage.setImageBitmap(bitmap);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                Log.w("path of image from gallery......******************.........", picturePath + "");
                viewImage.setImageBitmap(thumbnail);
            }
        }
    }

    private String genPassword() {
        String s = "";
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            char c = (char) (97 + random.nextInt(26));
            s += c;
        }
        return "1122";
    }

    private void showResultDialog(int result) {
        String message;
        if (result == 1) {
            message = "This Phone Number has already been registered!";
        } else if (result == 2) {
            message = "Infomation should not be blank. Abort!";
        } else {
            message = "Name: " + strFullName + "\nPhoneNumber: " + editPhoneNumber.getText() + "\n Created!"
                    + "\n\nAccountNumber: " + strAccountNumber + "\nPassword: " + strPassword + "\nCredit amount: "
                    + fAmount;
        }
    }
        /*new AlertDialog.Builder(this).setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
				
			}
		}).show();*/

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

    //***************************************************************
                            //Finger Print
    //***************************************************************

    public void init() {
//        findView();

        JBInterface.convertFinger();
        JBInterface.POWERON();

        working = true;
        begin = true;
        mHandler = new TextHandler();
        thread = new MyThread();
        thread.start();

    }

    class TextHandler extends Handler {
        public TextHandler() {

        }

//        public TextHandler(Looper L) {
//            super(L);
//        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            rInt = msg.what;
            resultText = (TextView) findViewById(R.id.resultText);

            if (rInt == 0) {

                try {

                    if (loginORserach == 0) {
//                        int j = (int)JBInterface.JCSZ();
//                        resultText.setText(""+j);

                        //take care of second authentication
//                        if((j != 0) || (j != 2)) {
//                            resultText.setText(""+j);
                            begin = JugeStep.tezheng1(JBInterface.FSML1(),
                                    1, resultText, CreateAccountActivity.this);
                            System.out.println("Begin: " + begin);
//                        }
                        if (!begin) {
                            //Second read means
                            System.out.println("Fingerprint input！！！");//Fingerprint input
                            int i = JBInterface.JCSZ();
//                            result.setText("" + i);
                            System.out.println("i === " + i);
                            if (i == 0) {
//                                Thread.sleep(10);
                                //Judge read the second fingerprint is correct , then merge the two entry refers to
                                if (!JugeStep.tezheng1(JBInterface.FSML2(),
                                        2, resultText, CreateAccountActivity.this)) {
                                    //Search fingerprint library already exists in the finger
                                    int Num = JBInterface.searchFinger();
                                    if (Num > 144) {
                                        resultText.setText("");
                                        resultText.setText(getResources()
                                                .getText(R.string.chaoguo));
//                                        result.append(getResources().getText(
//                                                R.string.chaoguo));
                                        return;
                                    }
//                                    if (Num == 0) {
//                                        result.append(getResources()
//                                                .getString(
//                                                        R.string.The_fingerprint_already_exists));
//                                        result.append("\n");
//                                        begin = true;
//                                        resultText.setText("");
//                                        resultText
//                                                .setText(getResources()
//                                                        .getString(
//                                                                R.string.The_fingerprint_already_exists));
//                                        return;
//                                    } else {

                                    if (JugeStep.regMsg(
                                            JBInterface.regModel(),
                                            resultText, CreateAccountActivity.this) == 0) {
                                        int value = JBInterface
                                                .storeModel();
                                        JugeStep.storeMsg(value,
                                                resultText, CreateAccountActivity.this);
                                        if (value == 0) {
//                                                sqlite.saveFinger(
//                                                        JBInterface.num,
//                                                        nameEdit.getText()
//                                                                .toString());
                                            fingerPrint = JBInterface.num;
                                        }
                                        begin = true;
                                        resultText.setText("");
//                                        resultText
//                                                .setText(getResources()
//                                                        .getString(
//                                                                R.string.Fingerprint_save_success));

                                        resultText
                                                .setText(""+JBInterface.num);
//                                            play();
                                    }
//                                    }
                                }
                            }
                        }
                    } else if (loginORserach == 1) {
                        if (!JugeStep.tezheng1(JBInterface.FSML2(), 2,
                                resultText, CreateAccountActivity.this)) {
                            // Returns fingerprints in the fingerprint database ID
                            fingerNum = JBInterface.mathFinger();
                            if (fingerNum == -1) {

                            } else if (fingerNum == -9) {
//                                result.append(getResources()
//                                        .getString(
//                                                R.string.No_matching_fingerprint_records));
//                                result.append("\n");
                                resultText.setText("");
                                resultText
                                        .setText(getResources()
                                                .getString(
                                                        R.string.No_matching_fingerprint_records));
                            } else {
//                                result.append(getResources()
//                                        .getString(
//                                                R.string.Successful_match_Fingerprint_ID_is)
//                                        + fingerNum);
//                                result.append("\n");
//                                play();
                                Thread.sleep(0);
                                resultText.setText("");
                                resultText.setText(getResources().getString(
                                                R.string.Successful_match)
//                                        + getName(fingerNum)
                                );

                            }
                        }
                    }
                    begin = true;
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (rInt == 2) {

            } else if (rInt == 3) {
//                result.setText("");
//                result.append(getResources().getString(
//                        R.string.Enroll_unsuccessful_please_try_again));
//                result.append("\n");
                resultText.setText("");
                resultText.setText(getResources().getString(
                        R.string.Enroll_unsuccessful_please_try_again));
            } else if (rInt == 1) {
//                result.setText("");
//                result.append("Data is incorrect!!");
//                result.append("\n");

            } else {
//                result.setText("");
//                result.append("\n");
            }
        }
    }

    private class MyThread extends Thread {
        public void run() {
            while (working) {
                if (begin) {
                    int i = JBInterface.JCSZ();
                    if (i == 0) {
                        begin = false;

                    }
                    Message msg = new Message();
                    msg.what = i;
                    CreateAccountActivity.this.mHandler.sendMessage(msg);
                }
            }
        }
    }

    protected void onStart() {
        super.onStart();
        isFront = true;
//        openFP();
    }

    protected void onStop() {
        super.onStop();
        isFront = false;
        closeFP();
    }

    private void closeFP() {
        super.onDestroy();
        JBInterface.POWEROFF();
        begin = false;
        JBInterface.closePort();
        working = false;
    }

    //************************************************************
    //             magnetic strip reader(MSR) code
    //************************************************************
    private void readMSRCard() {
        msrResultText = (TextView) findViewById(R.id.msrResultText);
        initDevice();
        setMode(1);
        msrResultText.setText(R.string.msr_swipe);
    }

    Handler MSRhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                msrResultText.setText(msg.obj + "");
            } else {
                String val = (String) msg.obj;
                if (val != null && !val.equals("")) {
                    if (val.length() < 10)
                        return;
                    if (msrResultText.getText().toString().equals(R.string.msr_swipe)) {
                        msrResultText.setText(val.toString());
                    } else {
                        msrResultText.setText(val.toString());
                    }
                }
            }
        }

    };
    OnReadSerialPortDataListener oListener = new OnReadSerialPortDataListener() {

        @Override
        public void onReadSerialPortData(SerialPortData serialPortData) {
            String str;
            str = new String(serialPortData.getDataByte());
            Message messge = new Message();
//            messge.obj = str;
            //read track 2 data
            MSRCardData = str.substring(str.indexOf(";") + 1, str.lastIndexOf("?"));
            if (MSRCardData != null) {
                messge.obj = getResources().getString(R.string.read_success);
            } else {
                messge.obj = getResources().getString(R.string.read_failure);
            }
            MSRhandler.sendMessage(messge);


        }
    };

    private int setMode(int k) {
        for (int i = 0; i <= 5; i++) {
            if (control.setEncryption(k) == 0) {
                return 0;
            } else {
                return -1;
            }
        }
        return -1;
    }

    Attation attation = new Attation() {

        @Override
        public void attation(String str) {
            // TODO Auto-generated method stub
            Message message = new Message();
            message.what = 1;
            MSRCardData = str.substring(str.indexOf(";") + 1, str.lastIndexOf("?"));
            if (MSRCardData != null) {
                message.obj = getResources().getString(R.string.read_success);
            } else {
                message.obj = getResources().getString(R.string.read_failure);
            }
            MSRhandler.sendMessage(message);
        }
    };


    private void initDevice() {
        control = new MSRInterface(CreateAccountActivity.this) {
        };
        control.MSR_shangDian();
        control.read = true;
        control.read(oListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        control.read = false;
        control.MSR_xiaDian();
        Ioctl.activate(20, 0);
        control.closeSerialPort();
    }

    //************************************************************
    //                   IC Card code
    //************************************************************
    Handler IChandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String val = (String) msg.obj;
            if (val != null && !val.equals("")) {
                iccardResultText.setText(val.toString());
            }
        }

    };

    private void readICCard() {
        String message = null;
        ttlhandler = new TTLHandler(this);
        Ioctl.convertIdReader();
        ttlhandler.setParameters("/dev/ttyS3", 115200);
        settings = new Settings(ttlhandler);
        iccardResultText = (TextView) findViewById(R.id.iccardResultText);
        iccardResultText.setText(R.string.running);
        _mHandler = new MessageHandler(iccardResultText);


        try {

            if (!ttlhandler.isConnected()) {
                ttlhandler.connect();
            } else {
                ttlhandler.close();
                ttlhandler.connect();
            }
        } catch (Exception e) {

            message = getResources().getString(R.string.read_failure);
            sendMessage(message);
        }
        iCCardTest();

    }

    public void sendMessage(String str) {
//        return;
        _mHandler.sendMessage(str);
    }

    private void iCCardTest() {
        if (flag) {
            sendMessage("Running......");
//            return;
        }
        flag = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ICResult, ICResultFinal;
//                String message = "";
                Message message = new Message();
                try {
                    ICResult = settings.icCardNo();
//                    ICResultFinal = ICResult;
//                    ICResultFinal = ICResult.substring(ICResult.indexOf("C") + 1, ICResult.indexOf("d"));
//                    ICResult.length()

                    if (ICResult != null) {
//                        sendMessage("IC CardNo:" + ICResult);
                        message.obj = getResources().getString(R.string.read_success);

                    } else {
                        message.obj = getResources().getString(R.string.read_failure);

                    }
                    IChandler.sendMessage(message);
                    ttlhandler.close();
                    flag = false;
                } catch (Exception e) {
                    message.obj = getResources().getString(R.string.read_failure);
                    IChandler.sendMessage(message);
                    ttlhandler.close();
                }
            }
        }).start();
    }

//    public synchronized void test(View v) {
//        switch (v.getId()) {
//            case R.id.ttlConn:
//                try {
//
//                    if (!ttlhandler.isConnected()) {
//                        sendMessage("Connect Res:" + ttlhandler.connect());
//                        ssy = true;
//                    } else {
//                        ttlhandler.close();
//                        ssy = false;
//                        sendMessage("Connect Res:" + ttlhandler.connect());
//                    }
//                } catch (Exception e) {
//                    sendMessage(e.getMessage());
//                }
//
//                break;
//            case R.id.ttlM1:
//                at24Test();
//                break;
//            case R.id.ver:
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        sle4442Test();
//                    }
//                }).start();
//                // String str = settings.icDetect();
//                // System.out.println("str ==== " + str);
//                // String version = settings.readVersion();
//                // System.out.println("version ==== " + version);
//                // String[] ss = version.replaceAll("..", "$0 ").split(" ");
//                // StringBuffer sb = new StringBuffer();
//                // for (String d : ss) {
//                // sb.append((char) Integer.parseInt(d, 16));
//                // }
//                // System.out.println(sb.toString());
//                break;
//            case R.id.icTest:
//                iCCardTest();
//                break;
//            case R.id.bt_gsv:
//                if (ssy) {
//                    new Thread() {
//                        public void run() {
//                            Get_System_Version();
//                        }
//                    }.start();
//                } else {
//                    sendMessage("TLL has connected!");
//                }
//                break;
//            default:
//                break;
//        }
//    }
}








	/*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0 && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == 5) {
			if(("".equals(editFirstName.getText().toString().trim())) || (null == editFirstName.getText().toString())){
				Toast.makeText(CreateAccountActivity.this, "ECLECTICS Account firstname can not be empty! Ignore this operation!", Toast.LENGTH_SHORT).show();
				return super.onKeyDown(keyCode, event);
			}
			if(("".equals(editLastName.getText().toString().trim())) || (null == editLastName.getText().toString())){
				Toast.makeText(CreateAccountActivity.this, "ECLECTICS Account lastname can not be empty! Ignore this operation!", Toast.LENGTH_SHORT).show();
				return super.onKeyDown(keyCode, event);
			}
			strFullName = editFirstName.getText().toString() + " " + editLastName.getText().toString();
			
			if(("".equals(editPhoneNumber.getText().toString().trim())) || (null == editPhoneNumber.getText().toString())){
				Toast.makeText(CreateAccountActivity.this, "ECLECTICS Account phonenumber can not be empty! Ignore this operation!", Toast.LENGTH_SHORT).show();
				return super.onKeyDown(keyCode, event);
			}
			
			if(("".equals(editDeposit.getText().toString().trim())) || (null == editDeposit.getText().toString())){
				Toast.makeText(CreateAccountActivity.this, "ECLECTICS Account Deposit can not be empty! Ignore this operation!", Toast.LENGTH_SHORT).show();
				return super.onKeyDown(keyCode, event);
			}
			String amount = editDeposit.getText().toString();
			try {
				fAmount = Double.parseDouble(amount);
			} catch (NumberFormatException e) {
				// TODO: handle exception
				Toast.makeText(CreateAccountActivity.this, "ECLECTICS Account Deposit incorrect format! Ignore this operation!", Toast.LENGTH_SHORT).show();
				return super.onKeyDown(keyCode, event);
			}
			if(fAmount < 0){
				Toast.makeText(CreateAccountActivity.this, "ECLECTICS Account amount not less than zero! Ignore this operation!", Toast.LENGTH_SHORT).show();
				return super.onKeyDown(keyCode, event);
			}
			
			if(null == accountBizImpl.findLastAccount()){
				strAccountNumber = "10000000";
			}else{
				strAccountNumber = String.valueOf(Integer.parseInt(accountBizImpl.findLastAccount().getAccount()) + 1);
			}
			
			strPassword = genPassword();

			Account account = new Account();
			account.setAccount(strAccountNumber);
			account.setName(strFullName);
			account.setPhoneNumber(editPhoneNumber.getText().toString());
			account.setPassword(strPassword);
			account.setAmount(fAmount);
			
			int registerResult = accountBizImpl.register(account);
			if(1 == registerResult){
				accountBizImpl.updateBasicInformation(account);
			}else if(2 == registerResult){
				return super.onKeyDown(keyCode, event);
			}
			
			/*Intent intent = new Intent(CreateAccountActivity.this, InformationEditActivity.class);
			intent.putExtra("account", account);
			startActivityForResult(intent, 0);
		}
		return super.onKeyDown(keyCode, event);


	}*/

