package com.agentdemo.finger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

//import com.sqlite.util.SqliteMethod;

import android.content.Context;
import android.util.Log;
import android_serialport_api.SerialPort;

public class SerialPortTools {
	
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private SqliteMethod sqlite;
	public static int num;
	
	public SerialPort getSerialPort(String port,int baudrate) throws SecurityException, IOException,
			InvalidParameterException {
		if (mSerialPort == null) {
			if ((port.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}
			mSerialPort = new SerialPort(new File(port), baudrate, 0);
		}
		return mSerialPort;
	}
	
	public SerialPortTools(String port,int baudrate,Context context)
	{
		try {
			mSerialPort = this.getSerialPort(port,baudrate);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
			sqlite = new SqliteMethod(context);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	}
	
	void initp()
	{
		if (mOutputStream != null) {
			try {
				mOutputStream.write(new byte[]{0x1B,'@'});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class ReadThread extends Thread {
		public void run() {
			super.run();
			while(!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[64];
					if (mInputStream == null) return;
					size = mInputStream.read(buffer);
					if (size > 0) {
						System.out.println("ï¿½ï¿½ï¿½Õµï¿½ï¿½ï¿½ï¿? ï¿½ï¿½Ð¡: " + size);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}

	protected void destroy() {
		if (mReadThread != null)
			mReadThread.interrupt();
		this.closeSerialPort();
		mSerialPort = null;
	}
	
	// [s] ï¿½ï¿½ï¿?
	public void write(String msg)
	{
		try {
			if(allowToWrite())
			{
				if(msg == null)
					msg = "";
				mOutputStream.write(msg.getBytes("GBK"));
				mOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//æ?œç´¢æŒ‡çº¹ï¼Œå‰?æ??æ˜¯æ£?æµ‹åˆ°æŒ‡çº¹
	public int searchFinger(int i){
		try {
			if(allowToWrite())
			{
				
				mOutputStream.write(FingerControl.search);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(300);
			if (mInputStream == null){
				
				return -1;				
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];	
			int size = mInputStream.read(buffer);
			if(size == 0){
				return -1;
			}
			byte hasFinger = buffer[9];			
			if(hasFinger == (byte)0x00 ){	
				if(i == 1){
					return 0; //è¡¨ç¤ºæ?œç´¢åˆ°ç›¸å?Œçš„æŒ‡çº¹ï¼?
				}
				int positon = decode(buffer[11]);
				return positon; //å½“å‰?æŒ‡çº¹åº“çš„æ•°é‡? = positon+1
			}else if(hasFinger == (byte)0x01){
				return -1;
			}else if(hasFinger == 0x09){
				return -9;  //æ²¡æœ‰æ?œç´¢åˆ°ç›¸å?Œçš„æŒ‡çº¹
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public byte searchModel(){
		try {
			if(allowToWrite())
			{
				
				mOutputStream.write(FingerControl.templeteNum);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(500);
			if (mInputStream == null){
				
				return -1;				
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];	
			int size = mInputStream.read(buffer);	
			Log.i("info", "ï¿½ï¿½ï¿½ï¿½ï¿½Ö? == "+size+"  --length ==  "+buffer.length);			
			Log.i("info", "ï¿½ï¿½ï¿½ï¿½ï¿½Ö¸buffer === "+bytesToHexString1234(buffer));			
			byte hasFinger =buffer[9];
			
			if(hasFinger == (byte)0x00 ){	
				Log.i("info", "Ö¸ï¿½Æ¸ï¿½ï¿½ï¿½ == "+buffer[11]);
				return buffer[11];
			}else if(hasFinger == (byte)0x01){
				return 1;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	//æŽ¢æµ‹æ‰‹æŒ‡ï¼ŒæŽ¢æµ‹åˆ°å?Žå½•å…¥æŒ‡çº¹å›¾åƒ?å­˜äº? ImageBufferï¼Œå¹¶è¿”å›žå½•å…¥æˆ?åŠŸç¡®è®¤ç ?ã?? 
		//è‹¥æŽ¢æµ‹ä¸?åˆ°æ‰‹æŒ‡ï¼Œç›´æŽ¥è¿”å›žæ— æ‰‹æŒ‡ç¡®è®¤ç ?ã€?
	
	public int JCSZ(byte[] b,Context context){
		try {
			if(allowToWrite())
			{
				if(b == null)
					return -1;
				mOutputStream.write(b);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(3 * 100);
			if (mInputStream == null){
				
				return -1;				
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];	
			int size = mInputStream.read(buffer);		
			if(size == 0){
				return -1;
			}
			byte hasFinger = buffer[9];
			if(hasFinger == (byte)0x02 ){	
				return 2;//è¡¨ç¤ºä¼ æ„Ÿå™¨ä¸Šæ— æ‰‹æŒ‡ï¼›
			}else if(hasFinger == (byte)0x01){
				return 1; //è¡¨ç¤ºæ”¶åŒ…æœ‰é”™ï¼?
			}else if(hasFinger == (byte)0x03){
				return 3;//è¡¨ç¤ºå½•å…¥ä¸?æˆ?åŠŸï¼›
			}else if(hasFinger == (byte)0x00){
				return 0;  //è¡¨ç¤ºç”Ÿæˆ?ç‰¹å¾?æˆ?åŠŸï¼?
			}					
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	//ï¼šå°† ImageBuffer ä¸­çš„åŽŸå§‹å›¾åƒ?ç”Ÿæˆ?æŒ‡çº¹ç‰¹å¾?,æ–‡ä»¶å­˜äºŽ CharBuffer1 æˆ–CharBuffer2. 
	
	
	public int FSML(byte[] bytes){
		try {
			if(allowToWrite())
			{				
				mOutputStream.write(bytes);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(800);
			if (mInputStream == null){				
				return -2;				
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];	
			int size = mInputStream.read(buffer);
			if(buffer.length == 0){
				return -3;
			}	
			byte hasFinger = buffer[9];
			if(hasFinger == (byte)0x00 ){		
				return 0; //è¡¨ç¤ºç”Ÿæˆ?ç‰¹å¾?æˆ?åŠŸï¼?
			}else if(hasFinger == (byte)0x06){
				return 6;//è¡¨ç¤ºæŒ‡çº¹å›¾åƒ?å¤ªä¹±è€Œç”Ÿä¸?æˆ?ç‰¹å¾?ï¼?
			}else if(hasFinger == (byte)0x07){
				return 7;//è¡¨ç¤ºæŒ‡çº¹å›¾åƒ?æ­£å¸¸ï¼Œä½†ç‰¹å¾?ç‚¹å¤ªå°‘è?Œç”Ÿä¸?æˆ?ç‰¹å¾?ï¼?
			}else if(hasFinger == (byte)0x15){
				return 15;//è¡¨ç¤ºå›¾åƒ?ç¼“å†²åŒºå†…æ²¡æœ‰æœ‰æ•ˆåŽŸå§‹å›¾è?Œç”Ÿä¸?æˆ?å›¾åƒ?ï¼?
			}else if(hasFinger == (byte)0x01){
				return 1;//è¡¨ç¤ºæ”¶åŒ…æœ‰é”™ï¼?
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		return -1;
	}
//	å?ˆæˆ?ä¸¤æ¬¡å½•å…¥çš„æŒ‡çº¹å›¾åƒ?
	public int regModel(){
		try {
			if(allowToWrite())
			{				
				mOutputStream.write(FingerControl.regModel);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(300);
			if (mInputStream == null){				
				return -2;				
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];	
			int size = mInputStream.read(buffer);
			Log.i("info", "ï¿½Ï³ï¿½ï¿½ï¿½ï¿½ï¿½1 == "+size+"  --length ==  "+buffer.length);			
			Log.i("info", "ï¿½Ï³ï¿½ï¿½ï¿½ï¿½ï¿½1buffer === "+bytesToHexString1234(buffer));	
			if(buffer.length == 0){
				return -3;
			}	
			byte hasFinger = buffer[9];
			if(hasFinger == (byte)0x00 ){		
				return 0;//è¡¨ç¤ºå?ˆå¹¶æˆ?åŠŸï¼?
			}else if(hasFinger == (byte)0x01){
				return 1;
			}else if(hasFinger == (byte)0x0a){
				return 10;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		return -1;
	}
	//å‚¨å­˜æŒ‡çº¹
	public int storeModel(byte[] bytes ,String name,Context context){
		try {
			if(allowToWrite())
			{				
				mOutputStream.write(bytes);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(500);
			if (mInputStream == null){				
				return -2;				
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];	
			int size = mInputStream.read(buffer);
			 num = decode(bytes[12]);		
			sqlite.saveFinger(num, name);
			if(buffer.length == 0){
				return -3;
			}	
			byte hasFinger = buffer[9];
			if(hasFinger == (byte)0x00 ){		
				return 0;//è¡¨ç¤ºå‚¨å­˜æˆ?åŠŸï¼?
			}else if(hasFinger == (byte)0x01){
				return 1;//è¡¨ç¤ºæ”¶åŒ…æœ‰é”™ï¼?
			}else if(hasFinger == (byte)0x0b){
				return 11;//è¡¨ç¤º PageID è¶…å‡ºæŒ‡çº¹åº“èŒƒå›?
			}else if(hasFinger == (byte)0x18){
				return 18;//è¡¨ç¤ºå†? FLASH å‡ºé”™ï¼?
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		return -1;
	}
	public String getName(int id){
		Log.i("info", "fingerID = "+id+"  --  name = "+sqlite.readResult(id));
		return sqlite.readResult(id);
	}
	  public  int decode(byte src) {
	        int i;

	        i = src;
	        i &= 0xFF;

	        return i;
	    }
	//æ¸…ç©ºæŒ‡çº¹åº?
	public int clearModel(){
		try {
			if(allowToWrite())
			{				
				mOutputStream.write(FingerControl.clear);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(500);
			if (mInputStream == null){				
				return -2;				
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];	
			int size = mInputStream.read(buffer);	
			Log.i("info", "1 == "+size);
			Log.i("info", "11  ==  "+bytesToHexString1234(buffer));
			if(buffer.length == 0  || buffer == null){
				mOutputStream.write(FingerControl.clear);
				mOutputStream.flush(); // 1
				Thread.sleep(500);
				 cout = mInputStream.available();
				 buffer = new byte[cout];	
				size = mInputStream.read(buffer);	
				Log.i("info", "2 == "+size);
				Log.i("info", "22  ==  "+bytesToHexString1234(buffer));				
			}	
		
			
			byte hasFinger = buffer[9];
			if(hasFinger == (byte)0x00 ){
				sqlite.clearResult();
				return 0;//æ¸…é™¤æˆ?åŠŸ
			}else if(hasFinger == (byte)0x01){
				return 1;//è¡¨ç¤ºæ”¶åŒ…æœ‰é”™ï¼?
			}else if(hasFinger == (byte)0x011){
				return 11;//è¡¨ç¤ºæ¸…ç©ºå¤±è´¥ï¼?
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		return 11;
	}
	
	public void reSet(){
		if(sqlite.getCount() == 0){
			if(clearModel() != 0 ){
				clearModel();
			}
		}
	}
	public void setClear(){
		sqlite.clearResult();
		reSet();
	}
	public  String bytesToHexString1234(byte src){

	     StringBuilder stringBuilder = new StringBuilder("");

	     	int v = src & 0xFF;

	     	String hv = Integer.toHexString(v);

	     	if (hv.length() < 2) {
	     	stringBuilder.append(0);
	     	}
	     	stringBuilder.append(hv);

	   
	     	return stringBuilder.toString();
	   }
	public void write(byte[] b)
	{
		try {
			if(allowToWrite())
			{
				if(b == null)
					return;
				mOutputStream.write(b);
				mOutputStream.flush(); // 1
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(int oneByte)
	{
		try {
			if(allowToWrite())
			{
				mOutputStream.write(oneByte);
				mOutputStream.flush(); // 1
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean allowToWrite()
	{
		if (mOutputStream == null) {
			System.out.println("ï¿½ï¿½ï¿½ï¿½ï¿½Îªï¿½ï¿?! ï¿½ï¿½ï¿½Ü´ï¿½Ó¡! ");
			return false;
		}
		return true;
	}
	
	// [e]
	public static String bytesToHexString123(byte[] src){

	     StringBuilder stringBuilder = new StringBuilder("");

	     if (src == null || src.length <= 0) {
	     	return null;
	     	}

	    for (int i = 0; i < src.length; i++) {

	     	int v = src[i] & 0xFF;

	     	String hv = Integer.toHexString(v);

	     	if (hv.length() < 2) {
	     	stringBuilder.append(0);
	     	}
	     	stringBuilder.append(hv);

	     }
	     	return stringBuilder.toString();
	   }
	
	public static String bytesToHexString(byte[] src){
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {

			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);

		}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();

	}
	
	public static String bytesToHexString1234(byte[] src){

	     StringBuilder stringBuilder = new StringBuilder("");

	     if (src == null || src.length <= 0) {
	     	return null;
	     	}

	    for (int i = 0; i < src.length; i++) {

	     	int v = src[i] & 0xFF;

	     	String hv = Integer.toHexString(v);

	     	if (hv.length() < 2) {
	     	stringBuilder.append(0);
	     	}
	     	stringBuilder.append(hv);

	     }
	     	return stringBuilder.toString();
	   }
	
	public class FingerReulst{
		private String result;

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}
		
	}
	public interface OnReadSerialPortDataListener {
		public void onReadSerialPortData(FingerReulst data);
	}
}
