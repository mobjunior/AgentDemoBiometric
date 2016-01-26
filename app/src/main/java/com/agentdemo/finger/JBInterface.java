package com.agentdemo.finger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import com.ctrl.gpio.Ioctl;

import android.content.Context;
import android.util.Log;
import android_serialport_api.SerialPort;

public class JBInterface {

	public static SerialPort mSerialPort;
	protected static OutputStream mOutputStream;
	private static InputStream mInputStream;
	private static SqliteMethod sqlite;
	private static Context context;
	public static int num;

	// //ä¸Šç”µ
	// public static final int powerON(Context context) {
	// JBInterface.context = context;
	// FileInputStream mCalfdIn = null;
	// String str = gB(true) + "FINGBR_PWR_EN";
	// byte[] buff = str.getBytes();
	// try {
	// mCalfdIn = new FileInputStream(new File("/dev/ctrl_gpio"));
	// mCalfdIn.read(buff);
	// mCalfdIn.close();
	//
	// try {
	// if (mSerialPort == null) {
	//
	// mSerialPort = new SerialPort(new File("/dev/ttyS6"), 57600, 0);
	// }
	// mOutputStream = mSerialPort.getOutputStream();
	// mInputStream = mSerialPort.getInputStream();
	//
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (InvalidParameterException e) {
	// e.printStackTrace();
	// }
	//
	//
	// return 0;
	// } catch (IOException e) {
	// e.printStackTrace();
	// return -1;
	// }
	// }
	// æŒ‡çº¹ä¸Šç”µ
	public static void POWERON() {
		Ioctl.activate(12, 1);
		int power = Ioctl.get_status(12);
		Log.i("info", "æŒ‡çº¹ä¸Šç”µï¼? " + power);

		if (mSerialPort == null) {

			try {
				mSerialPort = new SerialPort(new File("/dev/ttyS3"), 57600, 0);
				mOutputStream = mSerialPort.getOutputStream();
				mInputStream = mSerialPort.getInputStream();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void POWEROFF() {
		Ioctl.activate(12, 0);
		int power = Ioctl.get_status(2);
		Log.i("info", "æŒ‡çº¹ä¸Šç”µï¼? " + power);
	}

	public static void convertFinger() {
		int switchOn = Ioctl.convertFinger();
		Log.i("info", "åˆ‡æ?¢è‡³æŒ‡çº¹ï¼š " + switchOn);
	}

	// //ä¸‹ç”µ
	// public static final int powerOff() {
	// FileInputStream mCalfdIn = null;
	// String str = gB(false) + "FINGBR_PWR_EN";
	// byte[] buff = str.getBytes();
	// try {
	// mCalfdIn = new FileInputStream(new File("/dev/ctrl_gpio"));
	// mCalfdIn.read(buff);
	// mCalfdIn.close();
	// return 0;
	// } catch (IOException e) {
	// e.printStackTrace();
	// return -1;
	// }
	// }

	private static final String gB(boolean open) {
		return open ? "01" : "00";
	}

	// åˆ‡æ?¢è‡³æŒ‡çº¹æ¨¡å??
	public static final int convertGPIO() {
		FileInputStream mCalfdIn = null;
		String str_en = "00UART7_EN";
		String str_sel0 = "00UART7_SEL00";
		String str_sel1 = "00UART7_SEL10";
		byte[] buff_en = str_en.getBytes();
		byte[] buff_sel0 = str_sel0.getBytes();
		byte[] buff_sel1 = str_sel1.getBytes();

		buff_sel0[1] = '0';
		buff_sel1[1] = '0';

		buff_sel0[str_sel0.length() - 1] = 0;
		buff_sel1[str_sel1.length() - 1] = 0;
		try {
			mCalfdIn = new FileInputStream(new File("/dev/ctrl_gpio"));
			mCalfdIn.read(buff_en);
			mCalfdIn.read(buff_sel0);
			mCalfdIn.read(buff_sel1);
			mCalfdIn.read(buff_en);
			mCalfdIn.close();
			return 0;
		} catch (IOException e) {
			System.err.println("ï¿½ï¿½ï¿½Ð»ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ì³£ï¿½ï¿½");
			e.printStackTrace();
			return -1;
		}
	}

	public SerialPort getSerialPort(String port, int baudrate) throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPort == null) {
			if ((port.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}
			mSerialPort = new SerialPort(new File(port), baudrate, 0);
		}
		return mSerialPort;
	}

	public static int JCSZ() {
		byte[] genImg = { (byte) 0xEF, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x01, 0x00, 0x03, 0x01, 0x00, 0x05 };
		try {
			if (allowToWrite()) {

				mOutputStream.write(genImg);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(5 * 100);
			if (mInputStream == null) {

				return -1;
			}

			//:get an estimate of the number of bytes that can be read (or skipped over) from this input stream without blocking
			int cout = mInputStream.available();
			//create a byte array with a size of int(cout)
			byte[] buffer = new byte[cout];
			// Log.i("info", "cont == "+cout);
			//read contents of input stream and write it to buffer
			//Note: InputStream.read(byte[] b) method reads b.length number of bytes from the input stream to the buffer array b
			//The bytes read is returned as integer.
			int size = mInputStream.read(buffer);
			if (size == 0) {
				return -1;
			}
			//finger print data read
			if (size > 0)
				Log.i("info", "jcsz buffer == " + bytesToHexString1234(buffer));
			byte hasFinger = buffer[9];
			if (hasFinger == (byte) 0x02) {
				return 2;// è¡¨ç¤ºä¼ æ„Ÿå™¨ä¸Šæ— æ‰‹æŒ‡ï¼›
			} else if (hasFinger == (byte) 0x01) {
				return 1; // è¡¨ç¤ºæ”¶åŒ…æœ‰é”™ï¼?
			} else if (hasFinger == (byte) 0x03) {
				return 3;// è¡¨ç¤ºå½•å…¥ä¸?æˆ?åŠŸï¼›
			} else if (hasFinger == (byte) 0x00) {
				return 0; // è¡¨ç¤ºç”Ÿæˆ?ç‰¹å¾?æˆ?åŠŸï¼?
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	// ç¬¬ä¸€æ¬¡ç”Ÿæˆ?ç‰¹å¾?ï¼Œå‚¨å­˜åœ¨CharBuffer2ä¸?
	public static int FSML1() {
		byte[] img2Tz = { (byte) 0xEF, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x01, 0x00, 0x04, 0x02, 0x01, 0x00, 0x08 };
		try {
			if (allowToWrite()) {
				mOutputStream.write(img2Tz);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(800);
			if (mInputStream == null) {
				return -2;
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];
			int size = mInputStream.read(buffer);
			if (buffer.length == 0) {
				return -3;
			}
			byte hasFinger = buffer[9];
			if (hasFinger == (byte) 0x00) {
				return 0; // è¡¨ç¤ºç”Ÿæˆ?ç‰¹å¾?æˆ?åŠŸï¼?
			} else if (hasFinger == (byte) 0x06) {
				return 6;// è¡¨ç¤ºæŒ‡çº¹å›¾åƒ?å¤ªä¹±è€Œç”Ÿä¸?æˆ?ç‰¹å¾?ï¼?
			} else if (hasFinger == (byte) 0x07) {
				return 7;// è¡¨ç¤ºæŒ‡çº¹å›¾åƒ?æ­£å¸¸ï¼Œä½†ç‰¹å¾?ç‚¹å¤ªå°‘è?Œç”Ÿä¸?æˆ?ç‰¹å¾?ï¼?
			} else if (hasFinger == (byte) 0x15) {
				return 15;// è¡¨ç¤ºå›¾åƒ?ç¼“å†²åŒºå†…æ²¡æœ‰æœ‰æ•ˆåŽŸå§‹å›¾è?Œç”Ÿä¸?æˆ?å›¾åƒ?ï¼?
			} else if (hasFinger == (byte) 0x01) {
				return 1;// è¡¨ç¤ºæ”¶åŒ…æœ‰é”™ï¼?
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return -1;
	}

	// ç¬¬äºŒæ¬¡ç”Ÿæˆ?æŒ‡çº¹ç‰¹å¾?å¹¶å­˜å‚¨åœ¨CharBuffer1ä¸?
	public static int FSML2() {
		byte[] img2Tz1 = { (byte) 0xEF, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x01, 0x00, 0x04, 0x02, 0x02, 0x00, 0x09 };
		try {
			if (allowToWrite()) {
				mOutputStream.write(img2Tz1);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(800);
			if (mInputStream == null) {
				return -2;
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];
			int size = mInputStream.read(buffer);
			if (buffer.length == 0) {
				return -3;
			}
			byte hasFinger = buffer[9];
			if (hasFinger == (byte) 0x00) {
				return 0; // è¡¨ç¤ºç”Ÿæˆ?ç‰¹å¾?æˆ?åŠŸï¼?
			} else if (hasFinger == (byte) 0x06) {
				return 6;// è¡¨ç¤ºæŒ‡çº¹å›¾åƒ?å¤ªä¹±è€Œç”Ÿä¸?æˆ?ç‰¹å¾?ï¼?
			} else if (hasFinger == (byte) 0x07) {
				return 7;// è¡¨ç¤ºæŒ‡çº¹å›¾åƒ?æ­£å¸¸ï¼Œä½†ç‰¹å¾?ç‚¹å¤ªå°‘è?Œç”Ÿä¸?æˆ?ç‰¹å¾?ï¼?
			} else if (hasFinger == (byte) 0x15) {
				return 15;// è¡¨ç¤ºå›¾åƒ?ç¼“å†²åŒºå†…æ²¡æœ‰æœ‰æ•ˆåŽŸå§‹å›¾è?Œç”Ÿä¸?æˆ?å›¾åƒ?ï¼?
			} else if (hasFinger == (byte) 0x01) {
				return 1;// è¡¨ç¤ºæ”¶åŒ…æœ‰é”™ï¼?
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return -1;
	}

	// å?ˆæˆ?ä¸¤æ¬¡å½•å…¥çš„æŒ‡çº¹å›¾åƒ?
	public static int regModel() {
		byte[] regModel = { (byte) 0xEF, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x01, 0x00, 0x03, 0x05, 0x00, 0x09 };

		try {
			if (allowToWrite()) {
				mOutputStream.write(regModel);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(300);
			if (mInputStream == null) {
				return -2;
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];
			int size = mInputStream.read(buffer);
			if (buffer.length == 0) {
				return -3;
			}
			byte hasFinger = buffer[9];
			if (hasFinger == (byte) 0x00) {
				return 0;// è¡¨ç¤ºå?ˆå¹¶æˆ?åŠŸï¼?
			} else if (hasFinger == (byte) 0x01) {
				return 1;
			} else if (hasFinger == (byte) 0x0a) {
				return 10;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return -1;
	}

	// å‚¨å­˜æŒ‡çº¹
	public static int storeModel() {
		byte[] storeCode = { (byte) 0xEF, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x01, 0x00, 0x06, 0x06, 0x02, 0x00, 0x00, 0x00, (byte) 0x00 };
		storeCode = setByte(storeCode);
		try {
			if (allowToWrite()) {
				mOutputStream.write(storeCode);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(500);
			if (mInputStream == null) {
				return -2;
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];
			int size = mInputStream.read(buffer);
			//storeCode[12] is either one or equal to buffer[11]
			num = decode(storeCode[12]);
//			 sqlite.saveFinger(num, name);
			if (buffer.length == 0) {
				return -3;
			}
			byte hasFinger = buffer[9];
			if (hasFinger == (byte) 0x00) {
				return 0;// è¡¨ç¤ºå‚¨å­˜æˆ?åŠŸï¼?
			} else if (hasFinger == (byte) 0x01) {
				return 1;// è¡¨ç¤ºæ”¶åŒ…æœ‰é”™ï¼?
			} else if (hasFinger == (byte) 0x0b) {
				return 11;// è¡¨ç¤º PageID è¶…å‡ºæŒ‡çº¹åº“èŒƒå›?
			} else if (hasFinger == (byte) 0x18) {
				return 18;// è¡¨ç¤ºå†? FLASH å‡ºé”™ï¼?
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static byte[] setByte(byte[] bytes) {
		//bytes - byte array
		//set last(bytes[14]) array element to zero
		//bytes.length is equals to 15
		bytes[bytes.length - 1] = 0x00;
		//set array element bytes[12] to zero
		bytes[12] = 0x00;
		//set array element bytes[12] to one or contents of buffer[11]
		bytes[12] = searchModel();

		//loop from i equals 6 to i equals 14
		for (int i = 6; i < bytes.length - 1; i++) {
			//add sum of bytes[i] ie i=6 to i=14 to bytes[14]
			bytes[bytes.length - 1] += bytes[i];
		}
		return bytes;
	}

	// æ?œç´¢æŒ‡çº¹ï¼Œæ£€æµ‹æ˜¯å?¦å­˜å‚¨äº†ç›¸å?Œçš„æŒ‡çº?
	public static int searchFinger() {
		byte[] search = { (byte) 0xEF, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x01, 0x00, 0x08, 0x04, 0x02, 0x00, 0x00, 0x03, (byte) 0xA1, 0x00, (byte) 0xB3 };
		try {
			if (allowToWrite()) {

				mOutputStream.write(search);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(300);
			if (mInputStream == null) {

				return -1;
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];
			int size = mInputStream.read(buffer);
			if (size == 0) {
				return -1;
			}
			byte hasFinger = buffer[9];
			if (hasFinger == (byte) 0x00) {

				return 0; // è¡¨ç¤ºæ?œç´¢åˆ°ç›¸å?Œçš„æŒ‡çº¹ï¼?

			} else if (hasFinger == (byte) 0x01) {
				return -1;
			} else if (hasFinger == 0x09) {
				return -9; // æ²¡æœ‰æ?œç´¢åˆ°ç›¸å?Œçš„æŒ‡çº¹
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static byte searchModel() {
		try {
			if (allowToWrite()) {

				mOutputStream.write(FingerControl.templeteNum);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(500);
			if (mInputStream == null) {

				return -1;
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];
			int size = mInputStream.read(buffer);

			byte hasFinger = buffer[9];

			if (hasFinger == (byte) 0x00) {
				Log.i("info", "Ö¸ï¿½Æ¸ï¿½ï¿½ï¿½ == " + buffer[11]);
				return buffer[11];
			} else if (hasFinger == (byte) 0x01) {
				return 1;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -9;
	}

	// åŒ¹é…?æŒ‡çº¹
	public static int mathFinger() {
		try {
			if (allowToWrite()) {

				mOutputStream.write(FingerControl.search);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(300);
			if (mInputStream == null) {

				return -1;
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];
			int size = mInputStream.read(buffer);
			if (size == 0) {
				return -1;
			}
			byte hasFinger = buffer[9];
			if (hasFinger == (byte) 0x00) {

				int positon = decode(buffer[11]);
				return positon; // å½“å‰?æŒ‡çº¹åº“çš„æ•°é‡? = positon+1
			} else if (hasFinger == (byte) 0x01) {
				return -1;
			} else if (hasFinger == 0x09) {
				return -9; // æ²¡æœ‰æ?œç´¢åˆ°ç›¸å?Œçš„æŒ‡çº¹
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static void closePort() {
		if (null == mSerialPort) {
			return;
		}
		try {
			mInputStream.close();
			mOutputStream.close();
			mInputStream = null;
			mOutputStream = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}

	public static boolean allowToWrite() {
		if (mOutputStream == null) {
			return false;
		}
		return true;
	}

	public static int decode(byte src) {
		int i;

		i = src;
		i &= 0xFF;

		return i;
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

	public static void reSet() {

		if (clearModel() != 0) {
			clearModel();

		}
	}

	public static int clearModel() {
		byte[] clear = { (byte) 0xEF, 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x01, 0x00, 0x03, 0x0d, 0x00, 0x11 };
		try {
			if (allowToWrite()) {
				mOutputStream.write(clear);
				mOutputStream.flush(); // 1
			}
			Thread.sleep(500);
			if (mInputStream == null) {
				return -2;
			}
			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];
			int size = mInputStream.read(buffer);
			Log.i("info", "1 == " + size);
			Log.i("info", "11  ==  " + bytesToHexString1234(buffer));
			if (buffer.length == 0 || buffer == null) {
				mOutputStream.write(FingerControl.clear);
				mOutputStream.flush(); // 1
				Thread.sleep(500);
				cout = mInputStream.available();
				buffer = new byte[cout];
				size = mInputStream.read(buffer);
			}

			byte hasFinger = buffer[9];
			if (hasFinger == (byte) 0x00) {
				return 0;// æ¸…é™¤æˆ?åŠŸ
			} else if (hasFinger == (byte) 0x01) {
				return 1;// è¡¨ç¤ºæ”¶åŒ…æœ‰é”™ï¼?
			} else if (hasFinger == (byte) 0x011) {
				return 11;// è¡¨ç¤ºæ¸…ç©ºå¤±è´¥ï¼?
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 11;
	}
}
