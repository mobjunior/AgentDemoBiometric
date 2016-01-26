package com.lxl.code;

import android.content.Context;
import android.util.Log;

import com.ctrl.gpio.Ioctl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

import com.agentdemo.R;

public abstract class MSRInterface {

	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private String portName;
	private boolean begin;
	private byte[] bufferByte;
	public boolean read;
	private int count;
	private Context context;
	private OnReadSerialPortDataListener onReadSerialPortDataListener;

	public void read(OnReadSerialPortDataListener _onReadSerialPortDataListener) {
		this.onReadSerialPortDataListener = _onReadSerialPortDataListener;
	}

	private SerialPort getSerialPort(String port, int baudrate, int bits, char event, int stop) throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPort == null) {
			if ((port.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}
			portName = port;
			mSerialPort = new SerialPort(new File(port), baudrate, bits, event, stop, 0);
		}
		return mSerialPort;
	}

	public int setEncryption(int i) {
		// i=0 璁剧疆鍔犲瘑鐘舵�侊紝i=1 璁剧疆鎴愰潪鍔犲瘑鐘舵��
		try {

			byte[] a = { 0x68, 0x06, 0x01, 0x00, 0x00, 0x6F, 0x16 };

			byte[] b = { 0x68, 0x06, 0x01, 0x00, 0x01, 0x70, 0x16 };

			if (i == 0)
				mOutputStream.write(a);
			else
				mOutputStream.write(b);
			mOutputStream.flush();
			Thread.sleep(10);
			if (mInputStream == null) {
				Log.i("info", "inputstream is null");
				return -1;
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];
			int size = mInputStream.read(buffer);
			Log.i("info", "璇诲彇妯″潡鐘舵�侊細 " + bytesToHexString(buffer));
			if (size > 5) {
				if (buffer[4] == 0x00 || buffer[4] == 0x01)
					return 0;
				else
					return -1;
			} else
				return -1;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return -1;

	}

	public MSRInterface(Context context) {
		try {
			this.context = context;
			int a = Ioctl.convertMagcard();
			// System.out.println("a ==== " + a);
			mSerialPort = this.getSerialPort("/dev/ttyS1", 115200, 8, 'N', 1);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
			begin = true;
			// read = true;
			mReadThread = new ReadThread();
			mReadThread.start();

		} catch (SecurityException e) {
			System.err.println("You do not have read/write permission to the serial port.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("The serial port can not be opened for an unknown reason.");
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			System.err.println("Please configure your serial port first.");
			e.printStackTrace();
		}
	}

	private class ReadThread extends Thread {
		public void run() {
			while (begin) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (read)
					doRead();
			}
		}
	}

	public interface OnReadSerialPortDataListener {
		public void onReadSerialPortData(SerialPortData serialPortData);
	}

	public interface Attation {
		public void attation(String str);
	}

	public void closeSerialPort() {
		if (mSerialPort != null) {
			try {
				mOutputStream.close();
				mOutputStream = null;
				mInputStream.close();
				mInputStream = null;
			} catch (Exception e) {
				e.printStackTrace();
			}

			mSerialPort.close();
			mSerialPort = null;
		}
	}

	public void destroy() {
		if (mReadThread != null)
			begin = false;
		count = 0;
		this.closeSerialPort();
		mSerialPort = null;
	}

	public void write(String msg) {
		try {

			if (msg == null)
				msg = "";
			mOutputStream.write(msg.getBytes("GBK"));
			mOutputStream.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 璁惧涓婄數
	public String MSR_shangDian() {
		try {
			// 涓婄數
			Ioctl.activate(14, 1);
			Thread.sleep(100);
			// 鍒囨崲涓插彛
			Ioctl.convertMagcard();

			Thread.sleep(100);
			// 鍙戦�佷笂鐢垫寚浠�
			byte[] shangdian = { 0x68, 0x02, 0x01, 0x00, 0x00, 0x6B, 0x16 };
			mOutputStream.write(shangdian);
			mOutputStream.flush();

			if (mInputStream == null) {
				return context.getResources().getString(R.string.power_on_error);
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];

			int size = mInputStream.read(buffer);

			if (size == 0) {

				return context.getResources().getString(R.string.power_on_error);
			}
			Log.i("info", "涓婄數杩斿洖鐨刡yte ==" + bytesToHexString(buffer));
			byte result = buffer[4];
			if (result == 0x00) {
				return context.getResources().getString(R.string.power_on_success);
			} else {
				return context.getResources().getString(R.string.power_on_failure);
			}
		} catch (Exception e) {

		}
		return context.getResources().getString(R.string.power_on_success);
	}

	// 璁惧涓嬬數
	public String MSR_xiaDian() {
		try {
			byte[] xiadian = { 0x68, 0x02, 0x01, 0x00, 0x01, 0x6C, 0x16 };
			mOutputStream.write(xiadian);
			mOutputStream.flush();
			Thread.sleep(50);
			if (mInputStream == null) {
				return context.getResources().getString(R.string.power_off_error);
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];
			int size = mInputStream.read(buffer);
			Log.i("info", "byte == " + bytesToHexString1234(buffer));
			if (size == 0) {
				return context.getResources().getString(R.string.power_off_error);
			}
			byte result = buffer[4];
			if (result == 0x00) {
				return context.getResources().getString(R.string.power_off_success);
			} else {
				return context.getResources().getString(R.string.pwer_off_failure);
			}
		} catch (Exception e) {
			Log.i("info", " e == " + e.getMessage().toString());
		}
		try {
			Thread.sleep(50);
			Ioctl.activate(14, 0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return context.getResources().getString(R.string.power_off_error);
	}

	// 璁剧疆瀵嗛挜
	public String MSR_setPassword(String password) {
		try {
			byte[] Password = { 0x68, 0x03, 0x0a, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x16 };

			byte[] bytes = password.getBytes();
			for (int i = 0; i < 10; i++) {
				Password[4 + i] = bytes[i];
			}
			for (int j = 0; j < 14; j++) {
				Password[14] += Password[j];
			}
			mOutputStream.write(Password);
			mOutputStream.flush();
			Thread.sleep(50);
			if (mInputStream == null) {
				Log.i("info", "inputstream is null");
				return context.getResources().getString(R.string.set_failure);
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];
			int size = mInputStream.read(buffer);
			;
			if (size == 0) {
				Log.i("info", "size == 0");
				return context.getResources().getString(R.string.set_failure);
			}

		} catch (Exception e) {
			Log.i("info", " e == " + e.getMessage().toString());
		}
		return context.getResources().getString(R.string.set_success);
	}

	// 璇诲彇鏁版嵁
	public void MSR_readCard() {
		try {
			byte[] read = { 0x68, 0x01, 0x01, 0x00, 0x00, 0x6A, 0x16 };
			mOutputStream.write(read);
			mOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 璇诲彇瀵嗛挜
	public String MSR_readPassword() {
		try {
			byte[] bytes = { 0x68, 0x04, 0x01, 0x00, 0x00, 0x6D, 0x16 };

			mOutputStream.write(bytes);
			mOutputStream.flush();
			Thread.sleep(50);
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];
			int size = mInputStream.read(buffer);

			if (size == 0) {
				return context.getResources().getString(R.string.read_failure);
			}
			byte[] data = new byte[10];
			for (int i = 0; i < 10; i++) {
				data[i] = buffer[4 + i];
			}
			Log.i("info", "璇诲彇鐨勫瘑閽ワ細 " + new String(data));
			return new String(data);
		} catch (Exception e) {
			Log.i("info", "e.getmessage == " + e.getMessage());
		}
		return null;
	}

	public String bytesToHexString1234(byte src) {

		StringBuilder stringBuilder = new StringBuilder("");

		int v = src & 0xFF;

		String hv = Integer.toHexString(v);

		if (hv.length() < 2) {
			stringBuilder.append(0);
		}
		stringBuilder.append(hv);

		return stringBuilder.toString();
	}

	public void write(byte[] b) {
		try {

			if (b == null)
				return;
			mOutputStream.write(b);
			mOutputStream.flush(); // 1

		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public static String bytesToHexString(byte[] src) {
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

	public static String bytesToHexString1234(byte[] src) {

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

	public class SerialPortData {
		private byte[] dataByte;
		private int size;

		public SerialPortData(byte[] _dataByte, int _size) {
			this.setDataByte(_dataByte);
			this.setSize(_size);
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public byte[] getDataByte() {
			return dataByte;
		}

		public void setDataByte(byte[] dataByte) {
			this.dataByte = dataByte;
		}
	}

	private void doRead() {

		int size;

		if (mInputStream == null) {
			return;
		}

		int cout;
		try {
			cout = mInputStream.available();
			byte[] buffer1 = new byte[cout];
			if (buffer1.length >= 16) {
				cout = 0;
				// Thread.sleep(800);
				cout = mInputStream.available();
				buffer1 = new byte[cout];
			} else {
				Thread.sleep(50);
			}
			size = mInputStream.read(buffer1);

			if (size > 0) {
				// Log.i("info", "buffer1 ===  "+bytesToHexString(buffer1));
				SerialPortData data = new SerialPortData(buffer1, size);
				if (onReadSerialPortDataListener != null) {
					onReadSerialPortDataListener.onReadSerialPortData(data);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String t2(String str) {// 瀛楃涓茶浆鎹负ASCII鐮�

		char[] chars = str.toCharArray(); // 鎶婂瓧绗︿腑杞崲涓哄瓧绗︽暟缁�
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {// 杈撳嚭缁撴灉
			sb.append(chars[i] + (int) chars[i]);
		}
		return sb.toString();
	}

	public static String Bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

	private static String toHexUtil(int n) {
		String rt = "";
		switch (n) {
		case 10:
			rt += "A";
			break;
		case 11:
			rt += "B";
			break;
		case 12:
			rt += "C";
			break;
		case 13:
			rt += "D";
			break;
		case 14:
			rt += "E";
			break;
		case 15:
			rt += "F";
			break;
		default:
			rt += n;
		}
		return rt;
	}

	public static String toHex(int n) {
		StringBuilder sb = new StringBuilder();
		if (n / 16 == 0) {
			return toHexUtil(n);
		} else {
			String t = toHex(n / 16);
			int nn = n % 16;
			sb.append(t).append(toHexUtil(nn));
		}
		return sb.toString();
	}

	public static String parseAscii(String str) {
		StringBuilder sb = new StringBuilder();
		byte[] bs = str.getBytes();
		for (int i = 0; i < bs.length; i++)
			sb.append(toHex(bs[i]));
		return sb.toString();
	}

	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	/**
	 * 灏嗘寚瀹氬瓧绗︿覆src锛屼互姣忎袱涓瓧绗﹀垎鍓茶浆鎹负16杩涘埗褰㈠紡 濡傦細"2B44EFD9" 鈥�> byte[]{0x2B,
	 * 0脳44, 0xEF, 0xD9}
	 * 
	 * @param src
	 *            String
	 * @return byte[]
	 */
	public static byte[] HexString2Bytes(String src) {
		byte[] ret = new byte[src.length() / 2];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < tmp.length / 2; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

}
