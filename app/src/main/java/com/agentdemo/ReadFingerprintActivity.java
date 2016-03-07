package com.agentdemo;
//
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agentdemo.db.DBService;
import com.agentdemo.finger.JugeStep;
import com.agentdemo.entry.Account;

//import fgtit.fpengine.fpdevice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;
//
//import com.agentdemo.finger.JBInterface;
//
//import com.ctrl.gpio.Ioctl;
//
public class ReadFingerprintActivity
        extends Activity {
    static final String TAG = "ReadFingerprintActivity";
//    //    private static fpdevice fpdev = new fpdevice();
//    public static Handler handler = null;
//    private static boolean isFront = false;
//    private static boolean isopening = false;
//    private static boolean isworking = false;
//    private Account account = null;
//    private double balance = 0.0D;
//    private byte[] bmpdata = new byte[74806];
//    private int[] bmpsize = new int[1];
//    private Button buttonCreate = null;
//    private Button buttonReadFingerprint = null;
//    private Button buttonSkip = null;
//    private DBService dbService = null;
//    private ImageView fingerprint = null;
//    private boolean isAlready = true;
//    private Timer mTimer = null;
//    private TimerTask mTimerTask = null;
//    private byte[] matdata = new byte['Ȁ'];
//    private int[] matsize = new int[1];
//    private byte[] refdata = new byte['Ȁ'];
//    private int[] refsize = new int[1];
//    private String savePath = "";
//    private String strAccount = "";
//    private String strFirstName = "";
//    private String strLastName = "";
//    private String strPassword = "";
//    private String strPhoneNumber = "";
//    private TextView textStatus = null;
//    private int type = -1;
//    MyThread thread;
//
//    //*******************
//    private boolean begin;
//    public static boolean working;
//    private TextHandler mHandler;
//    private int rInt = -1;
//    private EditText result;
//    //    EditText result=(EditText)findViewById(R.id.result);
////    TextView resultText=(TextView)findViewById(R.id.resultText);
//    private static int loginORserach = 0;
//    private TextView resultText;
//    private int fingerNum;
//
//
//    private void closeFP() {
//        super.onDestroy();
//        JBInterface.POWEROFF();
//        begin = false;
//        JBInterface.closePort();
//        working = false;
//    }
//
//    @Override
//    protected void onDestroy() {
//        // int powerOff = JBInterface.powerOff();
//        super.onDestroy();
//        JBInterface.POWEROFF();
//        begin = false;
//        JBInterface.closePort();
//        working = false;
//
//
//    }
//
//    private void initData() {
//        Bundle localBundle = getIntent().getExtras();
//        this.type = localBundle.getInt("type");
//        if (localBundle != null) {
//            if (this.type != 0) {
////                break label90;
//            }
//            this.strFirstName = localBundle.getString("firstname");
//            this.strLastName = localBundle.getString("lastname");
//            this.strPhoneNumber = localBundle.getString("phonenumber");
//            this.strAccount = localBundle.getString("account");
//            this.strPassword = localBundle.getString("password");
//            this.balance = localBundle.getDouble("balance");
//        }
//        label90:
//        while (1 != this.type) {
//            return;
//        }
//        this.account = ((Account) localBundle.getSerializable("account"));
//        this.strAccount = this.account.getAccount();
//    }
//
////    private void initOthers() {
////        this.dbService = new DBService(this);
////        fpdev.SetInstance(this);
////        handler = new Handler() {
////            public void handleMessage(Message paramAnonymousMessage) {
////                switch (paramAnonymousMessage.what) {
////                }
////                for (; ; ) {
////                    super.handleMessage(paramAnonymousMessage);
////                    return;
////                    int i = ReadFingerprintActivity.fpdev.GetWorkMsg();
////                    int j = ReadFingerprintActivity.fpdev.GetRetMsg();
////                    switch (i) {
////                        default:
////                            break;
////                        case 1:
////                            ReadFingerprintActivity.this.textStatus.setText("Please Open Device");
////                            break;
////                        case 2:
////                            ReadFingerprintActivity.this.textStatus.setText("Place Finger");
////                            break;
////                        case 3:
////                            ReadFingerprintActivity.this.textStatus.setText("Lift Finger");
////                            break;
////                        case 4:
////                            if (j == 1) {
////                                ReadFingerprintActivity.this.textStatus.setText("Capture Image OK");
////                            }
////                            for (; ; ) {
////                                ReadFingerprintActivity.this.TimerStop();
////                                ReadFingerprintActivity.isworking = false;
////                                break;
////                                ReadFingerprintActivity.this.textStatus.setText("Capture Image Fail");
////                            }
////                        case 5:
////                            if (j == 1) {
////                                ReadFingerprintActivity.this.textStatus.setText("Generate Template OK");
////                                ReadFingerprintActivity.fpdev.GetTemplateByGen(ReadFingerprintActivity.this.matdata, ReadFingerprintActivity.this.matsize);
////                                ReadFingerprintActivity.this.preCreate();
////                            }
////                            for (; ; ) {
////                                ReadFingerprintActivity.this.TimerStop();
////                                ReadFingerprintActivity.isworking = false;
////                                break;
////                                ReadFingerprintActivity.this.textStatus.setText("Generate Template Fail");
////                            }
////                        case 6:
////                            if (j == 1) {
////                                ReadFingerprintActivity.this.textStatus.setText("Enrol Template OK");
////                                ReadFingerprintActivity.fpdev.GetTemplateByEnl(ReadFingerprintActivity.this.refdata, ReadFingerprintActivity.this.refsize);
////                            }
////                            for (; ; ) {
////                                ReadFingerprintActivity.this.TimerStop();
////                                ReadFingerprintActivity.isworking = false;
////                                break;
////                                ReadFingerprintActivity.this.textStatus.setText("Enrol Template Fail");
////                            }
////                        case 7:
////                            ReadFingerprintActivity.fpdev.GetBmpImage(ReadFingerprintActivity.this.bmpdata, ReadFingerprintActivity.this.bmpsize);
////                            Bitmap localBitmap = BitmapFactory.decodeByteArray(ReadFingerprintActivity.this.bmpdata, 0, 74806);
////                            ReadFingerprintActivity.this.fingerprint.setImageBitmap(localBitmap);
////                            break;
////                        case 8:
////                            ReadFingerprintActivity.this.textStatus.setText("Time Out");
////                            ReadFingerprintActivity.isworking = false;
////                            continue;
////                            ReadFingerprintActivity.this.closeFP();
////                            continue;
////                            ReadFingerprintActivity.this.openFP();
////                    }
////                }
////            }
////        };
////    }
//
//    private void initViews() {
//        this.buttonReadFingerprint = ((Button) findViewById(R.id.button_read));
//        this.buttonCreate = ((Button) findViewById(R.id.button_create));
//        this.fingerprint = ((ImageView) findViewById(R.id.fingerprint));
//        this.textStatus = ((TextView) findViewById(R.id.text_status));
//        this.buttonSkip = ((Button) findViewById(R.id.button_read));
//        this.buttonReadFingerprint.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View paramAnonymousView) {
////                if ((!ReadFingerprintActivity.isopening) || (ReadFingerprintActivity.isworking)) {
////                    return;
////                }
////                ReadFingerprintActivity.this.TimerStart();
////                ReadFingerprintActivity.fpdev.GenerateTemplate();
//                //***********************************************
//                //Added by Wahome 16.11.2015
//
//                init();
//                //***********************************************
//            }
//        });
////        this.buttonSkip.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View paramAnonymousView) {
////                if ((ReadFingerprintActivity.this.isExist(ReadFingerprintActivity.this.strAccount)) && (1 != ReadFingerprintActivity.this.type)) {
////                    Toast.makeText(ReadFingerprintActivity.this, "MOBIBANK Account Number has already been registed! Ignore this operation!", 0).show();
////                    return;
////                }
////                paramAnonymousView = ReadFingerprintActivity.this.getIntent();
////                ReadFingerprintActivity.this.saveAccounts();
////                paramAnonymousView.setComponent(new ComponentName(ReadFingerprintActivity.this, IDCardPhotoActivity.class));
////                ReadFingerprintActivity.this.startActivityForResult(paramAnonymousView, 0);
////            }
////        });
////        this.buttonCreate.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View paramAnonymousView) {
////                if ((ReadFingerprintActivity.this.isExist(ReadFingerprintActivity.this.strAccount)) && (1 != ReadFingerprintActivity.this.type)) {
////                    Toast.makeText(ReadFingerprintActivity.this, "MOBIBANK Account Number has already been registed! Ignore this operation!", 0).show();
////                    return;
////                }
////                paramAnonymousView = ReadFingerprintActivity.this.getIntent();
////                if ("".equals(ReadFingerprintActivity.this.savePath)) {
////                    Toast.makeText(ReadFingerprintActivity.this, "All fields are mandatory.", 0).show();
////                    return;
////                }
////                ReadFingerprintActivity.this.saveAccounts();
////                paramAnonymousView.setComponent(new ComponentName(ReadFingerprintActivity.this, IDCardPhotoActivity.class));
////                ReadFingerprintActivity.this.startActivityForResult(paramAnonymousView, 0);
////            }
////        });
//    }
//
//    public void init() {
////        findView();
//
//        JBInterface.convertFinger();
//        JBInterface.POWERON();
////        Toast.makeText(ReadFingerprintActivity.this, "Place your finger on the finger print reader!", Toast.LENGTH_SHORT).show();
////        Toast.makeText(ReadFingerprintActivity.this, "ECLECTICS Account last name can not be empty! Ignore this operation!", Toast.LENGTH_SHORT).show();
//        working = true;
//        begin = true;
//        mHandler = new TextHandler();
//        thread = new MyThread();
//        thread.start();
//    }
//
//    private boolean isExist(String paramString) {
//        SQLiteDatabase localSQLiteDatabase = this.dbService.getReadableDatabase();
//        Cursor localCursor = localSQLiteDatabase.rawQuery("select * from t_account", null);
//        if (localCursor.getCount() == 0) {
//            localCursor.close();
//            localSQLiteDatabase.close();
//            return false;
//        }
//        int i = localCursor.getColumnIndex("account");
//        localCursor.moveToFirst();
//        for (; ; ) {
//            if (localCursor.isAfterLast()) {
//                localCursor.close();
//                localSQLiteDatabase.close();
//                return false;
//            }
//            if (localCursor.getString(i).equals(paramString)) {
//                localCursor.close();
//                localSQLiteDatabase.close();
//                return true;
//            }
//            localCursor.moveToNext();
//        }
//    }
//
////    private void openFP() {
////        if (!isFront) {
////        }
////        while (isopening) {
////            return;
////        }
////        switch (fpdev.OpenDevice()) {
////            default:
////                return;
////            case -3:
////                this.textStatus.setText("Open Device Fail");
////                Toast.makeText(this, "Open Device Fail ", 1).show();
////                return;
////            case 0:
////                isopening = true;
////                this.textStatus.setText("Open Device OK");
////                Toast.makeText(this, "Open Device OK ", 1).show();
////                return;
////            case -1:
////                this.textStatus.setText("Link Device Fail");
////                return;
////        }
////        this.textStatus.setText("Evaluation version expires");
////    }
//
//    private void preCreate() {
//        this.savePath = (Environment.getExternalStorageDirectory().toString() + "/fingerprint/" + this.strAccount + ".txt");
//    }
//
//    private void saveAccounts() {
//        Object localObject1;
//        String str1;
//        Object localObject2;
//        if (this.type == 0) {
//            localObject1 = this.strAccount;
//            str1 = this.strFirstName + " " + this.strLastName;
//            localObject2 = this.strPhoneNumber;
//            double d = this.balance;
//            String str2 = this.strPassword;
//            String str3 = this.savePath;
//            SQLiteDatabase localSQLiteDatabase = this.dbService.getWritableDatabase();
//            localSQLiteDatabase.execSQL("insert into t_account(account,name,phonenumber,amount,password,fingerprint,idcardpicture) values(?, ?, ?, ?, ?, ?, ?)", new Object[]{localObject1, str1, localObject2, Double.valueOf(d), str2, str3, ""});
//            localSQLiteDatabase.close();
//        }
//        for (; ; ) {
//            if (!"".equals(this.savePath)) {
//            }
//            try {
//                localObject1 = new ObjectOutputStream(new FileOutputStream(this.savePath));
//            } catch (FileNotFoundException localFileNotFoundException1) {
//                Log.i("ReadFingerprintActivity", localFileNotFoundException1.getMessage());
//                return;
//            } catch (IOException localIOException1) {
//                Log.i("ReadFingerprintActivity", localIOException1.getMessage());
//                return;
//            }
//            try {
//                ((ObjectOutputStream) localObject1).writeObject(this.matdata);
//                ((ObjectOutputStream) localObject1).writeObject(Integer.valueOf(this.matsize[0]));
//                this.savePath = "";
//                return;
//            } catch (IOException localIOException2) {
////                break label275;
//            }
////            catch (FileNotFoundException localFileNotFoundException2) {
////                break;
////            }
//            if ((1 == this.type) && (!"".equals(this.savePath))) {
//                localObject1 = this.savePath;
//                str1 = this.account.getAccount();
//                localObject2 = this.dbService.getWritableDatabase();
//                ((SQLiteDatabase) localObject2).execSQL("update t_account set fingerprint=? where account=?", new Object[]{localObject1, str1});
//                ((SQLiteDatabase) localObject2).close();
//            }
//        }
//    }
//
//    public void TimerStart() {
//        if (this.mTimer == null) {
//            this.mTimer = new Timer();
//        }
//        if (this.mTimerTask == null) {
//            this.mTimerTask = new TimerTask() {
//                public void run() {
//                    Message localMessage = new Message();
//                    localMessage.what = 1;
//                    ReadFingerprintActivity.handler.sendMessage(localMessage);
//                }
//            };
//        }
//        if ((this.mTimer != null) && (this.mTimerTask != null)) {
//            this.mTimer.schedule(this.mTimerTask, 200L, 200L);
//        }
//    }
//
//    public void TimerStop() {
//        if (this.mTimer != null) {
//            this.mTimer.cancel();
//            this.mTimer = null;
//            this.mTimerTask.cancel();
//            this.mTimerTask = null;
//        }
//    }
//
//    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
//        super.onActivityResult(paramInt1, paramInt2, paramIntent);
//        if ((paramInt1 == 0) && (paramInt2 == -1)) {
//            setResult(-1);
//            finish();
//        }
//    }
//
//    protected void onCreate(Bundle paramBundle) {
//        super.onCreate(paramBundle);
//        setContentView(R.layout.read_fingerprint);
//        initData();
//        initViews();
////        initOthers();
////        paramBundle = new File(Environment.getExternalStorageDirectory().toString() + "/fingerprint");
////        if (!paramBundle.exists()) {
////            paramBundle.mkdirs();
////        }
//    }
//
//    protected void onStart() {
//        super.onStart();
//        isFront = true;
////        openFP();
//    }
//
//    protected void onStop() {
//        super.onStop();
//        isFront = false;
//        closeFP();
//    }
//
//    private class MyThread extends Thread {
//        public void run() {
//            while (working) {
//                if (begin) {
//                    int i = JBInterface.JCSZ();
//                    if (i == 0) {
//                        begin = false;
//
//                    }
//                    Message msg = new Message();
//                    msg.what = i;
//                    ReadFingerprintActivity.this.mHandler.sendMessage(msg);
//                }
//            }
//        }
//    }
//
//    class TextHandler extends Handler {
//        public TextHandler() {
//
//        }
//
////        public TextHandler(Looper L) {
////            super(L);
////        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            // TODO Auto-generated method stub
//            super.handleMessage(msg);
//            rInt = msg.what;
//            EditText result = (EditText) findViewById(R.id.result);
//            isFront = (TextView) findViewById(R.id.resultText);
//
//            if (rInt == 0) {
////                Toast.makeText(ReadFingerprintActivity.this, "Place your finger on the finger print reader!", Toast.LENGTH_SHORT).show();
//                result.setText("");
//                result.append(getResources().getString(
//                        R.string.detected_the_finger));
//                result.append("\n");
//
//                try {
//
//                    if (loginORserach == 0) {
//                        // 判断是否输入了姓�? //Determines whether a surname
////                        result.setText("");
////                        resultText.setText("");
////                        if (nameEdit.getText().toString().trim() == null
////                                || nameEdit.getText().toString().trim()
////                                .equals("")) {
////                            result.append(getResources().getString(
////                                    R.string.Please_enter_your_name));
////                            // Log.i("info", "this is run");
////                            result.append("\n");
////                            begin = true;
////                            return;
////                        }
//                        // 判断是输入的姓名是否合法 //Enter the name of the judge is whether the legal
////                        if (nameEdit.getText().toString().getBytes().length < 4) {
////                            result.append(getResources().getString(
////                                    R.string.Please_enter_a_valid_name));
////                            // Log.i("info", "this is run");
////                            result.append("\n");
////                            begin = true;
////                            return;
////                        }
//
//                        begin = JugeStep.tezheng1(JBInterface.FSML1(), result,
//                                1, resultText, ReadFingerprintActivity.this);
////                        begin = false;
//                        System.out.println("Begin: " + begin);
//                        if (!begin) {
//                            // 第二次读取指�? //Second read means
//                            System.out.println("Fingerprint input！！！");//Fingerprint input
//                            int i = JBInterface.JCSZ();
////                            result.setText("" + i);
//                            System.out.println("i === " + i);
//                            if (i == 0) {
////                            if (i == 2) {
//                                Thread.sleep(10);
//                                //Judge read the second fingerprint is correct , then merge the two entry refers to
//                                // 判断第二次读取指纹是否正确，然后合并两次录入的指�?
//                                if (!JugeStep.tezheng1(JBInterface.FSML2(),
//                                        result, 2, resultText, ReadFingerprintActivity.this)) {
//                                    // 搜索指纹库中是否已经存在该指�? //Search fingerprint library already exists in the finger
//                                    int Num = JBInterface.searchFinger();
//                                    if (Num > 144) {
//                                        resultText.setText(getResources()
//                                                .getText(R.string.chaoguo));
//                                        result.append(getResources().getText(
//                                                R.string.chaoguo));
//                                        return;
//                                    }
//                                    if (Num == 0) {
//                                        result.append(getResources()
//                                                .getString(
//                                                        R.string.The_fingerprint_already_exists));
//                                        result.append("\n");
//                                        begin = true;
//                                        resultText
//                                                .setText(getResources()
//                                                        .getString(
//                                                                R.string.The_fingerprint_already_exists));
//                                        return;
//                                    } else {
//
//                                        if (JugeStep.regMsg(
//                                                JBInterface.regModel(), result,
//                                                resultText, ReadFingerprintActivity.this) == 0) {
//                                            int value = JBInterface
//                                                    .storeModel();
//                                            JugeStep.storeMsg(value, result,
//                                                    resultText, ReadFingerprintActivity.this);
//                                            if (value == 0) {
////                                                sqlite.saveFinger(
////                                                        JBInterface.num,
////                                                        nameEdit.getText()
////                                                                .toString());
//                                            }
//                                            begin = true;
//                                            resultText
//                                                    .setText(getResources()
//                                                            .getString(
//                                                                    R.string.Fingerprint_save_success));
////                                            play();
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    } else if (loginORserach == 1) { // ָ��ʶ��
//                        System.out.println("指纹匹配！！！！");
//                        if (!JugeStep.tezheng1(JBInterface.FSML2(), result, 2,
//                                resultText, ReadFingerprintActivity.this)) {
//                            // 返回指纹在指纹库的ID
//                            fingerNum = JBInterface.mathFinger();
//                            if (fingerNum == -1) {
//
//                            } else if (fingerNum == -9) {
//                                result.append(getResources()
//                                        .getString(
//                                                R.string.No_matching_fingerprint_records));
//                                result.append("\n");
//                                resultText
//                                        .setText(getResources()
//                                                .getString(
//                                                        R.string.No_matching_fingerprint_records));
//                            } else {
//                                result.append(getResources()
//                                        .getString(
//                                                R.string.Successful_match_Fingerprint_ID_is)
//                                        + fingerNum);
//                                result.append("\n");
////                                play();
//                                Thread.sleep(0);
////                                resultText.setText(getResources().getString(
////                                        R.string.Successful_match)
////                                        + getName(fingerNum));
//
//                            }
//                        }
//                    }
//                    begin = true;
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            } else if (rInt == 2) {
//
//            } else if (rInt == 3) {
//                result.setText("");
//                result.append(getResources().getString(
//                        R.string.Enroll_unsuccessful_please_try_again));
//                result.append("\n");
//                resultText.setText(getResources().getString(
//                        R.string.Enroll_unsuccessful_please_try_again));
//            } else if (rInt == 1) {
//                result.setText("");
//                result.append("Data is incorrect!!");
//                result.append("\n");
//
//            } else {
//                result.setText("");
//                result.append("\n");
//            }
//        }
//
//    }
//
////    public String getName(int id) {
////        return sqlite.readResult(id);
////    }
//
////    public void play() {
////        new Thread() {
////            public void run() {
////                try {
////                    AssetManager assetManager = getAssets();
////                    AssetFileDescriptor fileDescriptor = assetManager
////                            .openFd("sheng3.wav");
////                    mediaPlayer = new MediaPlayer();
////                    mediaPlayer.setDataSource(
////                            fileDescriptor.getFileDescriptor(),
////                            fileDescriptor.getStartOffset(),
////                            fileDescriptor.getLength());
////                    mediaPlayer.prepare();
////                    mediaPlayer.start();
////                    try {
////                        Thread.sleep(3000);
////                        mediaPlayer.stop();
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                } catch (IOException e1) {
////                    // TODO Auto-generated catch block
////                    e1.printStackTrace();
////                }
////            }
////        }.start();
////    }
//
////    public String getName(int id) {
////        return sqlite.readResult(id);
////    }
}
//
//
///* Location:              E:\backup\App\dex2jar-2.0(1)\dex2jar-2.0\classes-dex2jar.jar!\com\nbbse\printerdemo\ReadFingerprintActivity.class
// * Java compiler version: 6 (50.0)
// * JD-Core Version:       0.7.1
// */