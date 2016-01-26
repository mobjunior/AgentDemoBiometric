package com.agentdemo.printer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
import android_serialport_api.SerialPort;

public class SerialPortTools {

	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;

	public SerialPort getSerialPort(String port, int baudrate) throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPort == null) {
			if ((port.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}
			mSerialPort = new SerialPort(new File(port), baudrate, 0);
		}
		return mSerialPort;
	}

	/**
	 * @param port
	 *            绔彛
	 * @param baudrate
	 *            娉㈢壒鐜�
	 * */
	public SerialPortTools(String port, int baudrate) {
		try {
			mSerialPort = this.getSerialPort(port, baudrate);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	}

	void initp() {
		if (mOutputStream != null) {
			try {
				mOutputStream.write(new byte[] { 0x1B, '@' });
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ReadThread extends Thread {
		public void run() {
			super.run();
			while (!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[64];
					if (mInputStream == null)
						return;
					size = mInputStream.read(buffer);
					if (size > 0) {
						System.out.println("鎺ユ敹鍒版暟鎹� 澶у皬: " + size);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	/** 鍏抽棴涓插彛 */
	public void closeSerialPort() {
		try {
			mOutputStream.close();
			mOutputStream = null;
			mInputStream.close();
			mInputStream = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	// [s] 杈撳嚭
	public void write(String msg) {
		try {
			if (allowToWrite()) {
				if (msg == null)
					msg = "";
				mOutputStream.write(msg.getBytes("unicode"));
				mOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write_gb2312(String msg) {
		try {
			if (allowToWrite()) {
				if (msg == null)
					msg = "";
				mOutputStream.write(msg.getBytes("GB2312"));
				mOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// [s] 杈撳嚭
	public void write_unicode(String msg) {
		try {
			if (allowToWrite()) {
				if (msg == null)
					msg = "";
				mOutputStream.write(msg.getBytes("unicode"));
				mOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	// [s] 杈撳嚭
//	public void write_Unicode(String msg) {
//
//		if (msg == null) {
//			msg = "";
//		}
//		try {
//			JBInterface.setLeft();
//			if (MainActivity.lan == 2) {
//				JBInterface.setRight();
//				ArrayList strs16 = str16(msg);
//				for (int j = 0; j < strs16.size(); j++) {
//					int a = (Integer) strs16.get(j);
//					mOutputStream.write(a);
//				}
//			} else if (MainActivity.lan == 1) {
//				mOutputStream.write(msg.getBytes("GB2312"));
//			} else {
//				String[] str = new String[msg.length()];
//
//				for (int i = 0; i < msg.length(); i++) {
//					str[i] = msg.substring(i, i + 1);
//				}
//				for (int j = 0; j < str.length; j++) {
//					byte[] a = { str[j].getBytes("unicode")[3], str[j].getBytes("unicode")[2] };
//					mOutputStream.write(a);
//				}
//			}
//
//			mOutputStream.flush();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	static ArrayList str16(String s) {

		int str16s[] = new int[s.length() + 1];
		int str16sb[] = new int[s.length() + 1];

		ArrayList albstr = new ArrayList();
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			Log.i("info", Integer.toHexString(ch) + "");
			str16s[i] = ch;

		}

		bs bs = new bs();

		boolean zj = false;
		int c = bs.IsIncludeArbic(str16s);
		if (c == 1) {
			zj = true;
		}
		if (zj) {
			bs.Arbic_Convert(str16s, str16sb);
		} else {
			str16sb = str16s;
		}

		ArrayList<Integer> sgb1 = new ArrayList<Integer>();
		ArrayList<Integer> sgb2 = new ArrayList<Integer>();
		for (int i = 0; i < str16sb.length; i++) {

			if (i == str16sb.length - 1) {
				for (int i1 = sgb1.size(); i1 > 0; i1--) {
					sgb2.add(sgb1.get(i1 - 1));
				}
			}

			if (str16sb[i] != 10) {
				sgb1.add(str16sb[i]);
			} else {
				for (int c1 = sgb1.size(); c1 > 0; c1--) {
					sgb2.add(sgb1.get(c1 - 1));
				}
				sgb1.clear();
				sgb2.add(10);
			}
		}

		for (int i = 0; i < sgb2.size(); i++) {
			if (sgb2.get(i) == 10) {
				albstr.add(sgb2.get(i));
			} else {
				int b = sgb2.get(i) / 256;
				albstr.add(b);
				int d = sgb2.get(i) % 256;
				albstr.add(d);
			}

		}

		for (int j = 0; j < str16s.length; j++) {
			System.out.println(str16s[j]);
		}
		System.out.println("\n================");
		for (int k = 0; k < sgb2.size() - 1; k++) {
			System.out.println(sgb2.get(k));
		}

		return albstr;
	}

	// [s] 杈撳嚭
	public void write_Unicode(String msg, boolean test, String Persian) {
		try {

			if (msg == null) {
				msg = "";
			}
			Log.i("info", "msg.length == " + msg.length());

			// if(msg.length()<=32){
			String[] str = new String[msg.length()];

			for (int i = 0; i < msg.length(); i++) {
				str[i] = msg.substring(i, i + 1);
			}
			if (Persian.equals("English")) {
				write(JBInterface.SET_LEFT);
				Thread.sleep(10);
				for (int j = 0; j < str.length; j++) {
					//
					byte[] a = { str[j].getBytes("unicode")[3], str[j].getBytes("unicode")[2] };

					mOutputStream.write(a);
					Thread.sleep(10);
				}
			} else {
				write(JBInterface.SET_RIGHT);
				Thread.sleep(10);

				for (int j = str.length - 1; j >= 0; j--) {
					//
					byte[] a = { str[j].getBytes("unicode")[3], str[j].getBytes("unicode")[2] };

					mOutputStream.write(a);
					Thread.sleep(10);
				}
				// Thread.sleep(300);
			}
			mOutputStream.flush();

			// }else{
			// //
			// }
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public void write_Unicode(String msg, boolean test, String Persian) {
	// try {
	//
	// if (msg == null) {
	// msg = "";
	// }
	// Log.i("info", "msg.length == " + msg.length());
	//
	// // if(msg.length()<=32){
	//
	// /**
	// * 鍔犲叆鐨勪唬鐮�
	// */
	// ArrayList<String> arraystr = textArrayList(msg); // 鍔犲叆鐨勪唬鐮�
	// ArrayList<String[]> arrayone = new ArrayList<String[]>(); // 鍔犲叆鐨勪唬鐮�
	//
	// /**
	// * 鍔犲叆鐨勪唬鐮�
	// */
	// for (int i = 0; i < arraystr.size(); i++) {
	// String strs = arraystr.get(i);
	// String[] str = new String[strs.length()];
	// for (int j = 0; j < msg.length(); j++) {
	// str[j] = msg.substring(j, j + 1);
	// }
	// arrayone.add(str);
	//
	// }
	//
	// // for (int i = 0; i < msg.length(); i++) {
	// // str[i] = msg.substring(i, i + 1);
	// // }
	// if (Persian.equals("English")) {
	// write(JBInterface.SET_LEFT);
	// Thread.sleep(10);
	// // for (int j = 0; j < str.length; j++) {
	// // //
	// // byte[] a = { str[j].getBytes("unicode")[3],
	// str[j].getBytes("unicode")[2] };
	// //
	// // mOutputStream.write(a);
	// // Thread.sleep(5);
	// // }
	//
	// /**
	// * 鍔犲叆鐨勪唬鐮�
	// */
	// for (int i = 0; i < arrayone.size(); i++) {
	// String[] str = arrayone.get(i);
	// for (int j = 0; j < str.length; j++) {
	//
	// byte[] a = { str[j].getBytes("unicode")[3], str[j].getBytes("unicode")[2]
	// };
	//
	// mOutputStream.write(a);
	// Thread.sleep(5);
	// }
	// }
	// } else {
	// write(JBInterface.SET_RIGHT);
	// Thread.sleep(10);
	//
	// // for (int j = str.length - 1; j >= 0; j--) {
	// // //
	// // byte[] a = { str[j].getBytes("unicode")[3],
	// str[j].getBytes("unicode")[2] };
	// //
	// // mOutputStream.write(a);
	// // Thread.sleep(5);
	// // }
	//
	// /**
	// * 鍔犲叆鐨勪唬鐮�
	// */
	// for (int i = 0; i < arrayone.size(); i++) {
	// String[] str = arrayone.get(i);
	// for (int j = 0; j < str.length; j++) {
	//
	// byte[] a = { str[j].getBytes("unicode")[3], str[j].getBytes("unicode")[2]
	// };
	//
	// mOutputStream.write(a);
	// Thread.sleep(5);
	// }
	// }
	// // Thread.sleep(300);
	// }
	// mOutputStream.flush();
	//
	// // }else{
	// //
	// // }
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	public ArrayList<String> textArrayList(String str) {

		System.out.println("瀛楃闀垮害锛�= " + str.length());
		int a = str.length() / 32;
		int b = str.length() % 32;
		System.out.println("a = " + a);
		System.out.println("b = " + b);

		String stra = str.substring(b, str.length());
		String strb = str.substring(0, b);
		ArrayList<String> cstr = new ArrayList<String>();
		if (a == 1 && str.length() > 32) {
			cstr.add(stra);
			cstr.add(strb);
			System.out.println("==1+1");
		} else if (a > 1) {
			for (int i = 0; i < a; i++) {
				String ca = stra.substring(0, 32);
				stra = stra.substring(32, stra.length());
				cstr.add(ca);
				System.out.println("澶т簬1");
			}
			cstr.add(strb);
		} else {
			System.out.println("==1");
			cstr.add(str);
		}
		System.out.println("闆嗗悎鎵惧害 = " + cstr.size());
		return cstr;
	}

	public void ceshi(String msg) {
		int k = msg.length() / 32;
		int j = msg.length() % 32;
		String[] str = new String[j];
		String[] str32 = new String[32];
		for (int i = 0; i < j; i++) {

			str[i] = msg.substring(k * 32, k * 32 + 1);

		}
		for (int a = k; a > 0; a--) {
			for (int i = 32; i > 0; i--) {
				str32[i] = msg.substring(a * 32, a * 32 + 1);
			}
		}
	}

	private byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);

		return bb.array();

	}

	// 鍒ゆ柇鏄惁涓鸿嫳鏂囷紱
	private static boolean checkPwdChars(final String str) {
		// 鍏堟鏌ユ渶鍚庝竴浣�(鎻愰珮鏁堢巼)
		char tmp;
		int i = str.length() - 1;
		for (; i >= 0; i--) {
			tmp = str.charAt(i);
			if (!(('0' <= tmp && tmp <= '9') || ('a' <= tmp && tmp <= 'z') || ('A' <= tmp && tmp <= 'Z'))) {
				return false;
			}
		}
		;
		return true;
	}

	// 妫�娴嬫槸鍚︽湁绾�
	public boolean getState() {
		try {
			if (allowToWrite()) {

				mOutputStream.write(new byte[] { 0x10, 0x04, 0x05 });
				mOutputStream.flush(); // 1
			}
			Thread.sleep(50);

			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];
			int size = mInputStream.read(buffer);
			if (buffer[0] == 0x00) {
				return true;
			} else
				return false;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static byte[] getByteArray(String hexString) {

		return new BigInteger(hexString, 16).toByteArray();

	}

	public static String bytesToHexString123(byte[] src) {

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

	/**
	 * 杈撳嚭
	 * */
	public void write(byte[] b) {
		try {
			if (allowToWrite()) {
				if (b == null)
					return;
				mOutputStream.write(b);
				mOutputStream.flush(); // 1
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 杈撳嚭
	 * */
	public void write(int oneByte) {
		try {
			if (allowToWrite()) {
				mOutputStream.write(oneByte);
				mOutputStream.flush(); // 1
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鏄惁鍏佽鎵撳嵃
	 * */
	public boolean allowToWrite() {
		if (mOutputStream == null) {
			System.out.println("杈撳嚭娴佷负绌�! 涓嶈兘鎵撳嵃! ");
			return false;
		}
		return true;
	}

	// [e]

	public void write_Unicode(String msg, String str) {
		try {
			if (allowToWrite()) {
				if (msg == null)
					msg = "";

				if (str.equals("涓枃锛堢箒/绠� 浣擄級")) {
					// 瀵硅鎵撳嵃鐨勫瓧绗︿覆閫愪釜鍒ゆ柇鏄惁涓轰腑鏂囥�佽嫳鏂囥�佺鍙枫�佹暟瀛�,閫愪釜鎵撳嵃锛�
					for (int i = 0; i < msg.length(); i++) {
						String s = msg.substring(i, i + 1);
						// 鑻ヤ笉涓轰腑鏂�
						if (!JBInterface.isChinese(s)) {
							byte[] bytes = s.getBytes();
							byte[] writebytes = { 0x00, bytes[0] };
							mOutputStream.write(writebytes);
						} else {
							// 鑻ヤ负涓枃
							mOutputStream.write(JBInterface.getStringToHexBytes(s));
						}
					}
				} else if (str.equals("English")) {
					mOutputStream.write(printerENByte(msg));
					mOutputStream.flush();
					Thread.sleep(50);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 鎵撳嵃鑻辨枃閲嶅仛byte[]
	public static byte[] printerENByte(String msg) {
		byte[] b = msg.getBytes();
		byte[] writebytes = new byte[b.length * 2];
		for (int i = 0; i < b.length; i++) {
			writebytes[i * 2] = 0x00;
			writebytes[i * 2 + 1] = msg.getBytes()[i];
		}
		return writebytes;
	}

	public static String transferString(String oldString) {
		StringBuffer newStringBuffer = new StringBuffer(oldString);

		int length = oldString.length();

		for (int i = 0; i < length / 2 + 1; i++) {
			char a = oldString.charAt(i);
			char b = oldString.charAt(length - i - 1);
			newStringBuffer.replace(i, i + 1, String.valueOf(b));
			newStringBuffer.replace(length - i - 1, length - i, String.valueOf(a));
		}
		return new String(newStringBuffer);
	}

	public static void test1(String str) {
		System.out.println("瀛楃闀垮害锛�= " + str.length());
		int a = str.length() / 32;
		int b = str.length() % 32;
		System.out.println("a = " + a);
		System.out.println("b = " + b);

		String stra = str.substring(b, str.length());
		String strb = str.substring(0, b);
		ArrayList<String> cstr = new ArrayList<String>();
		if (a == 1) {

		} else {
			for (int i = 0; i < a; i++) {
				String ca = stra.substring(0, 32);
				stra = stra.substring(32, stra.length());
				cstr.add(ca);
			}
		}

		System.out.println("闆嗗悎鎵惧害 = " + cstr.size());
		if (a > 1) {
			for (int i = cstr.size(); i > 0; i--) {
				System.out.println(cstr.get(i - 1));
			}
			System.out.println(strb);
		} else if (a == 1 && str.length() > 32) {
			System.out.println(stra + "\n" + strb);
		} else {
			System.out.println("111 = " + str);
		}
	}

}
