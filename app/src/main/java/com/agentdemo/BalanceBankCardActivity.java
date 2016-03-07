package com.agentdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.StringTokenizer;



import com.cardreader.audio.app.SComboReader_DUKPT;
import com.cardreader.audio.base.CardReaderAudioBase;
import com.cardreader.audio.sdk.AudioSeries;
import com.cardreader.audio.sdk.FileService;



public class BalanceBankCardActivity extends Activity {
    private static final String TAG="BALANCEINQUIRYBYCARD";
    private BalanceBankCardActivity inquiryByCard=null;
    private SComboReader_DUKPT sComboReader=null;
    //private CardOperations cardOperations=null;
    //private MainActivity mainActivity=null;
    private NetworkTask networkTask;

    private Button btnGetBatterylevel=null;
    private Button btnSendRequest=null;
    private TextView textStatus=null;
    private TextView tvVersion=null;
    private TextView txtDisplaycardDetails=null;
    private TextView biresultsinfo=null;

    //request parameters
    String processingCode="310000";//TMS balance enquiry code
    String agencyCashManagement="AGENCY";//Either Mwallet or Agency
    String mPosSerialNo="00000";
    String cardPIN="0000";
    String accountNumber=null;//from Card information
    String agentCode="1001";

    String card_number=null;
    String user_name=null;
    String expired_date=null;

    //progress dialog
    private ProgressDialog pd;
    private ProgressDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_bank_card);
        inquiryByCard=this;
        //cardOperations=new CardOperations();

        //progressdialogs
        pd=new ProgressDialog(this);
        pd.setMessage("Loading Balance.....");

        myDialog=new ProgressDialog(this);
        myDialog.setMessage("System Initialization..");
        myDialog.setTitle("Message");
        networkTask=new NetworkTask();
        networkTask.execute();

        txtDisplaycardDetails=(TextView) findViewById(R.id.txtDisplayCardDetails);
        tvVersion=(TextView)findViewById(R.id.tvReaderVersion);
        biresultsinfo=(TextView)findViewById(R.id.biresultsinfo_text);
        biresultsinfo.setMovementMethod(ScrollingMovementMethod.getInstance());

        btnGetBatterylevel=(Button)findViewById(R.id.btnGetbatterylevel);
        btnGetBatterylevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inquiryByCard.Send_DetectBatteryEnergy();
            }
        });

        btnSendRequest=(Button)findViewById(R.id.btnSendRequest);
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send_Insert();
                //Send_Information();
                //accountNumber="1234567891234567";
//                Log.i("Account No",accountNumber);

            }
        });
///this is put here to test for commit in github
        textStatus=(TextView)findViewById(R.id.textcbiStatus);
        //Filter headset plug
        registerReceiver(mHeadsetReceiver, new IntentFilter(
                Intent.ACTION_HEADSET_PLUG));

        FileService read = new FileService(inquiryByCard);

        try {
            CardReaderAudioBase.readMODEL = read.readFile();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
        SendMessage(MSG_STARTINIT, 0, 0);


    }

    //Async Task to send requst and Receive response from TMS
    public class NetworkTask extends AsyncTask<Void, byte[], Boolean> {
        Socket nsocket; //Network Socket
        InputStream instream; //Network Input Stream
        OutputStream outstream; //Network Output Stream

        @Override
        protected void onPreExecute() {
            Log.i("AsyncTask", "onPreExecute");
        }

        @Override
        protected Boolean doInBackground(Void... params) { //This runs on a different thread
            boolean result = false;
            try {
                Log.i("AsyncTask", "doInBackground: Creating socket");
                SocketAddress sockaddr = new InetSocketAddress("104.209.222.213", 3031);
                nsocket = new Socket();
                nsocket.connect(sockaddr, 5000); //10 second connection timeout
                if (nsocket.isConnected()) {
                    instream = nsocket.getInputStream();
                    outstream = nsocket.getOutputStream();
                    Log.i("AsyncTask", "doInBackground: Socket created, streams assigned");
                    Log.i("AsyncTask", "doInBackground: Waiting for initial data...");
                    byte[] buffer = new byte[4096];
                    int read = instream.read(buffer, 0, 4096); //This is blocking
                    while(read != -1){
                        byte[] tempdata = new byte[read];
                        System.arraycopy(buffer, 0, tempdata, 0, read);
                        publishProgress(tempdata);
                        Log.i("AsyncTask", "doInBackground: Got some data");
                        read = instream.read(buffer, 0, 4096); //This is blocking
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("AsyncTask", "doInBackground: IOException");
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("AsyncTask", "doInBackground: Exception");
                result = true;
            } finally {
                try {
                    instream.close();
                    outstream.close();
                    nsocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Log.i("AsyncTask", "doInBackground: Finished");
            }
            return result;
        }

        public void SendDataToNetwork(String cmd) { //You run this from the main thread.
            try {
                if (nsocket.isConnected()) {
                    Log.i("AsyncTask", "SendDataToNetwork: Writing received message to socket");
                    outstream.write(cmd.getBytes());
                } else {
                    Log.i("AsyncTask", "SendDataToNetwork: Cannot send message. Socket is closed");
                }
            } catch (Exception e) {
                Log.i("AsyncTask", "SendDataToNetwork: Message send failed. Caught an exception");
            }
        }

        @Override
        protected void onProgressUpdate(byte[]... values) {
            if (values.length > 0) {
                Log.i("AsyncTask", "onProgressUpdate: " + values[0].length + " bytes received.");
                String response=new String(values[0]);
                StringTokenizer splitresponse=new StringTokenizer(response,"#");
                //int tokensNo=splitresponse.countTokens();
                biresultsinfo.setText(response);
                //String[] tokens=response.split("#");
                // int tokensNo=tokens.length;
//                for (int i=0;i<tokens.length;i++)
//                {
//                    biresultsinfo.setText(tokens[i].toString());
//                }
//                while(splitresponse.hasMoreTokens()) {
                // biresultsinfo.setText(splitresponse.nextElement().toString());
//                }
                //PutMessage("Number of Tokens: "+tokensNo);
                pd.dismiss();
            }
        }
        @Override
        protected void onCancelled() {
            Log.i("AsyncTask", "Cancelled.");
            btnSendRequest.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Log.i("AsyncTask", "onPostExecute: Completed with an Error.");
                textStatus.setText("There was a connection error.");
            } else {
                Log.i("AsyncTask", "onPostExecute: Completed.");
            }
            btnSendRequest.setVisibility(View.VISIBLE);
            pd.dismiss();
        }
    }

    //@Override
//    protected void onDestroy() {
//        super.onDestroy();
//        networkTask.cancel(true); //In case the task is currently running
//    }
    //Card reader initializations
    public void StartInit() {
        // Volume to the maximum
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        // Start SComboReader processing
        if (sComboReader.SComboReaderStart() != SComboReader_DUKPT.OK) {
            Toast.makeText(this, "initialization failed", Toast.LENGTH_LONG).show();
            return;
        }
        // Toast.makeText(this, "Before starting thread",
        // Toast.LENGTH_LONG).show();
        new InitThread().start();

    }

    private class InitThread extends Thread {
        @Override
        public void run() {
            if (sComboReader.SComboReaderInit() == SComboReader_DUKPT.OK) {
                Log.d(TAG, "MSG_INITDONE");
                SendMessage(MSG_INITDONE, 0, 0);
            } else {
                Log.d(TAG, "MSG_REINIT");
                SendMessage(MSG_REINIT, 0, 0);
            }
        }
    };

    private void ReInit() {

        myDialog.show();

//        myDialog = ProgressDialog.show(inquiryByCard, "Message",
//                "System Initialization...");
        new ReInitThread().start();
    }

    private class ReInitThread extends Thread {
        public void run() {

            if (sComboReader.SComboReaderReInit() == SComboReader_DUKPT.OK) {
                SendMessage(MSG_INITDONE, 0, 0);

                FileService save = new FileService(inquiryByCard);
                try {
                    save.save();
                } catch (Exception e) {
                    // TODO
                    e.printStackTrace();
                }
            }

            else
                SendMessage(MSG_INITFALT, 0, 0, "Initialization failed.");
        }
    };

    // Detect Phone Jack plugged
    private boolean headsetConnected = false;

    private BroadcastReceiver mHeadsetReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_HEADSET_PLUG)) {
                boolean hasHeadset = (intent.getIntExtra("state", 0) == 1);
                boolean hasMicroPhone = (intent.getIntExtra("microphone", 0) == 1);
                if (hasHeadset && hasMicroPhone) {
                    headsetConnected = true;
                    CreateAudio();
                } else {
                    headsetConnected = false;
                }

            }
        }
    };

    private void CreateAudio() {
        // Alloc a Singular Reader object
        if (sComboReader == null) {
            // Alloc a AudioSeriesPort parameter
            AudioSeries.AudioSeriesPort audioseriesport = new AudioSeries.AudioSeriesPort();
            sComboReader = new SComboReader_DUKPT();
            // Register Message callback handle
            sComboReader.RegisterMessage(myMessageHandler);
            // Open SComboReader Audio Device
            if (sComboReader.SComboReaderOpen(audioseriesport) != SComboReader_DUKPT.OK) {
                Toast.makeText(this, "System Error: Can't open audio device.",
                        Toast.LENGTH_LONG).show();
                // this.finish();
                // return;
            }

        }
    }
    // communicate with the device

    public static final int MSG_STARTINIT = 0x01;
    public static final int MSG_INITDONE = 0x02;
    public static final int MSG_INITFALT = 0x03;
    public static final int MSG_REINIT = 0x04;

    private void SendMessage(int i_msg, int arg1, int arg2) {
        Message msg = new Message();
        msg.what = i_msg;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        myHandler.sendMessage(msg);
    }

    private void SendMessage(int i_msg, int arg1, int arg2, Object obj) {
        Message msg = new Message();
        msg.what = i_msg;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        myHandler.sendMessage(msg);
    }

    public Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_STARTINIT: {

                    if (!headsetConnected) {
                        ShowMessage(inquiryByCard, "Message",
                                "Please insert SComboReader.", onDlgClick);
                    } else {
                        StartInit();
                    }
                    break;
                }
                case MSG_INITFALT: {
                    myDialog.dismiss();

                    if (msg.obj != null) {
                        String str_msg = (String) msg.obj;
                        ShowMessage(inquiryByCard, "Message", str_msg, null);
                    }

                    break;
                }
                case MSG_INITDONE: {
                    myDialog.dismiss();
                    PutMessage("Initialization Successful.");
                    //myDialog.dismiss();
                    Send_GetVersion();
                    //myDialog.dismiss();
                    break;
                }
                case MSG_REINIT: {

                    //myDialog.dismiss();
                    if (!headsetConnected) {
                        ShowMessage(inquiryByCard, "Message",
                                "Please insert SComboReader.", onDlgClick);
                    } else {
                        ReInit();
                    }
                    break;
                }
            }

            super.handleMessage(msg);
        }
    };

    public Handler myMessageHandler = new Handler() {

        public void handleMessage(Message msg) {
            int SW = msg.arg1;
            switch (msg.what) {
                case SComboReader_DUKPT.CMD_Get_Version: {
                    SComboReader_DUKPT.ArrayByteValue val = (SComboReader_DUKPT.ArrayByteValue) msg.obj;

                    if (SW == SComboReader_DUKPT.OK) {

                        DispVersion ( val );
                    } else {
                        DispState ( SW );
                    }

                    break;
                }

                case SComboReader_DUKPT.CMD_Detect_Battery_Energy: {
                    SComboReader_DUKPT.ArrayByteValue val = (SComboReader_DUKPT.ArrayByteValue) msg.obj;

                    if (SW == SComboReader_DUKPT.OK) {
                        DispEnergy(val);
                    } else {
                        DispState(SW);
                    }

                    break;
                }
                case SComboReader_DUKPT.CMD_Reset_Chip:
                case SComboReader_DUKPT.CMD_Set_Time_Into_Power_Down:
                case SComboReader_DUKPT.CMD_Select_Encrypt_Mode: {
                    // DispState(SW);
                    break;
                }
                // card inserted case
                case SComboReader_DUKPT.CMD_Detect_ICC_Inserted_Or_Not: {
                    SComboReader_DUKPT.ArrayByteValue val = (SComboReader_DUKPT.ArrayByteValue) msg.obj;

                    if (SW == SComboReader_DUKPT.OK) {
                        DispValueInsert(val);
                    } else {
                        DispState(SW);
                    }

                    break;
                }
                // /////////////////////////// cardinformation case
                case SComboReader_DUKPT.CMD_ICC_Information: {
                    SComboReader_DUKPT.ArrayByteValue val = (SComboReader_DUKPT.ArrayByteValue) msg.obj;

                    if (SW == SComboReader_DUKPT.OK) {
                        DispValueInformation(val);
                    } else {
                        DispState(SW);
                    }

                    break;
                }

                // /////////////////////// emv data case
                case SComboReader_DUKPT.CMD_Get_EMV_Data:
                case SComboReader_DUKPT.CMD_Verify_PIN:
                {
                    SComboReader_DUKPT.ArrayByteValue val = (SComboReader_DUKPT.ArrayByteValue) msg.obj;

                    if (SW == SComboReader_DUKPT.OK)
                    {
                        DispValue(val);
                    } else
                    {
                        DispState(SW);
                    }

                    break;
                }
                //end emv data case
            }

            super.handleMessage(msg);
        }
    };

    // message dialog section
    public static void ShowMessage(Context context, String title, String msg) {
        ShowMessage(context, title, msg, null);
    }

    public static void ShowMessage(Context context, String title, String msg,
                                   final View.OnClickListener onClickListener) {
        String btn = "OK";

        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle(title);
        dlg.setMessage(msg);
        dlg.setPositiveButton(btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                if (onClickListener != null) {
                    onClickListener.onClick(null);
                }
            }
        });

        dlg.show();
    }

    //public ProgressDialog myDialog;
    public View.OnClickListener onDlgClick = new View.OnClickListener() {

        public void onClick(View v) {

            if (!headsetConnected) {
                ShowMessage(inquiryByCard, "Message",
                        "Please insert SComboReader.", onDlgClick);
                return;
            }

            // Start SComboReader processing
            StartInit();
        }
    };

    public void PutMessage(String msg) {
        Log.d(TAG, "Balance Enquiry bty Card");

        // TextView lb = new TextView(sComboActivity);
        // lb.setText(string);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }

    // //cardinformation send
    public void Send_Information() {
        PutMessage("Get CardInformation");
        sComboReader.ICCInformation(null);
    }

    // detect battery level
    public void Send_DetectBatteryEnergy() {
        // Register Message callback handle
        sComboReader.RegisterMessage(myMessageHandler);
        sComboReader.DetectBatteryEnergy(null);
    }

    // Display Energy
    public void DispEnergy(SComboReader_DUKPT.ArrayByteValue energyString) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();

        for (int i = 0; i < energyString.Value.length; i++) {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(String.format("%02x", energyString.Value[i]));
        }

        if (sb.toString().equalsIgnoreCase("00")) {
            this.PutMessage("Battery empty");
        }

        else if (sb.toString().equalsIgnoreCase("01")) {
            this.PutMessage("Battery 1/3");
        } else if (sb.toString().equalsIgnoreCase("02")) {
            this.PutMessage("Battery 2/3");
        } else if (sb.toString().equalsIgnoreCase("03")) {
            this.PutMessage("Battery full");
        }
    }

    // Display Status
    public void DispState(int SW) {

        this.PutMessage(String.format("State: %s.", sComboReader.GetCSW(SW)));
    }

    // Display Receive version
    private void DispVersion(SComboReader_DUKPT.ArrayByteValue versionString) {

        java.lang.StringBuilder sb = new java.lang.StringBuilder();

        for (int i = 0; i < versionString.Value.length; i++) {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(String.format("%s", (char) versionString.Value[i]));
        }

        //this.PutMessage(sb.toString());
        tvVersion.setText(sb.toString());
    }

    // ////////////card reading processing///////////////////
    public void DispValueInformation(SComboReader_DUKPT.ArrayByteValue insertString) {


        Log.d(TAG, "DispValueInformation");
        java.lang.StringBuilder sb = new java.lang.StringBuilder();

        for (int i = 0; i < insertString.Value.length; i++) {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(String.format("%02x", insertString.Value[i]));
        }

        String card_data = sb.toString().replace(" ", "");
        ;
        ;

        int card_num = card_data.lastIndexOf("a1");
        int card_name = card_data.lastIndexOf("b1");
        int card_exp = card_data.lastIndexOf("c1");

        card_number = changeHexString2CharString(card_data.substring(
                card_num + 2, card_name));
        user_name = changeHexString2CharString(card_data.substring(
                card_name + 2, card_exp));
        expired_date = changeHexString2CharString(card_data.substring(
                card_exp + 2, card_data.length()));
        expired_date = expired_date.substring(0, 2) + "/"
                + expired_date.substring(2, expired_date.length());

        int card_type1 = Integer.parseInt(card_number.substring(0, 1));
        int card_type2 = Integer.parseInt(card_number.substring(0, 3)); // 3
        int card_type3 = Integer.parseInt(card_number.substring(0, 4));
        String type;
        if (card_type1 == 4) {
            type = "Visa Card";
        } else if (card_type1 == 5) {
            type = "Master Card";
        } else if ((card_type1 == 3)
                && (card_type2 >= 340 && card_type2 <= 379)
                && (card_number.length() == 15)) {
            type = "AE Card";
        } else if (((card_type1 == 1) && (card_type3 == 1800))
                || ((card_type1 == 2) && (card_type3 == 2131)))//
        {
            type = "JCB Card";

        } else if ((card_type2 >= 300 && card_type2 <= 399)
                && (card_number.length() == 16) || ((card_type1 == 3))) {
            type = "JCB Card";
        } else {
            type = "Card";
        }

        Log.d(TAG, type + " " + card_number + " " + expired_date + " "
                + user_name);
        Log.d(TAG,"accountNumber"+accountNumber);
        Log.d(TAG,"card_number"+card_number);


        txtDisplaycardDetails.setText(type + " " + card_number + " " + expired_date + " "
                + user_name);
        //PutMessage(type + " " + card_number + " " + expired_date + " "
        // + user_name);
        accountNumber=card_number;
        PutMessage("Account No=:"+accountNumber);
        String request=processingCode+"#"+agencyCashManagement+
                "#"+mPosSerialNo+"#"+accountNumber+"#"+accountNumber+"#"+cardPIN+"#"+agentCode+"#";
        //String request="310000#AGENCY#00000#1234567890123456#1234567890123456#0000#1001#";

        PutMessage("Request: "+request);
        pd.show();
        networkTask.SendDataToNetwork(request);
        // PutMessage("Card Type : " + type);
        // PutMessage("Card Number :  " + card_number);
        // PutMessage("Expired Date :  " + expired_date);
        // PutMessage("Name : " + user_name);

    }

    public static String changeHexString2CharString(String e) {
        String char_txt = "";
        for (int i = 0; i < e.length(); i = i + 2) {
            String c = e.substring(i, i + 2);
            char j = (char) Integer.parseInt(c, 16);
            char_txt += j;
        }
        return char_txt;
    }

    // detect insert
    public void Send_Insert() {
        PutMessage("Detect Insert");
        sComboReader.DetectICCInsertedOrNot(null);
    }

    // Disp Insert
    public void DispValueInsert(SComboReader_DUKPT.ArrayByteValue insertString) {

        java.lang.StringBuilder sb = new java.lang.StringBuilder();

        for (int i = 0; i < insertString.Value.length; i++) {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(String.format("%02x", insertString.Value[i]));
        }

        if (sb.toString().equalsIgnoreCase("00")) {
            textStatus.setText("IC Card not Inserted. Please Insert IC card!");
        }

        else if (sb.toString().equalsIgnoreCase("01")) {
            textStatus.setText("IC Card inserted");
            Send_Information();
        }

    }

    //get reader version
    public void Send_GetVersion()
    {
        //message call back handle
        sComboReader.RegisterMessage(myMessageHandler);
        PutMessage(">>>Get Version...");
        sComboReader.GetVersion(null);
    }

    //send emv data
    public void Send_EMV_Data(){
        PutMessage("get EMV Data");
        sComboReader.GetEMVData(null);
    }
    //display emv data
    // Display Receive data
    public void DispValue(SComboReader_DUKPT.ArrayByteValue dataString)
    {

        java.lang.StringBuilder sb = new java.lang.StringBuilder();

        for (int i = 0; i < dataString.Value.length; i++)
        {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(String.format("%02x", dataString.Value[i]));
        }

        textStatus.setText(sb.toString());
    }

    // /////////////////////////////////////////////////////

    public void onDestroy() {
        unregisterReceiver(mHeadsetReceiver);
        networkTask.cancel(true);
        super.onDestroy();

    }




}
