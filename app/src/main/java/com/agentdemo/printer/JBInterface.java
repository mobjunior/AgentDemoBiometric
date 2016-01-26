package com.agentdemo.printer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle.Control;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.ctrl.gpio.Ioctl;

@SuppressWarnings("all")
public class JBInterface {

	public static final byte HT = 0x9; // 濮樻潙閽╅崚鎯般��
	public static final byte LF = 0x0A; // 閹垫挸宓冮獮鑸靛床鐞涳拷
	public static final byte CR = 0x0D; // 閹垫挸宓冮崶鐐舵簠
	public static final byte ESC = 0x1B;
	public static final byte DLE = 0x10;
	public static final byte GS = 0x1D;
	public static final byte FS = 0x1C;
	public static final byte STX = 0x02;
	public static final byte US = 0x1F;
	public static final byte CAN = 0x18;
	public static final byte CLR = 0x0C;
	public static final byte EOT = 0x04;

	/* 姒涙顓绘０婊嗗鐎涙ぞ缍嬮幐鍥︽姢 */
	public static final byte[] ESC_FONT_COLOR_DEFAULT = new byte[] { ESC, 'r',
			0x00 };
	/* 閺嶅洤鍣径褍鐨� */
	public static final byte[] FS_FONT_ALIGN = new byte[] { FS, 0x21, 1, ESC,
			0x21, 1 };
	/* 闂堢姴涔忛幍鎾冲祪閸涙垝鎶� */
	public static final byte[] ESC_ALIGN_LEFT = new byte[] { 0x1b, 'a', 0x00 };
	/* 鐏炲懍鑵戦幍鎾冲祪閸涙垝鎶� */
	public static final byte[] ESC_ALIGN_CENTER = new byte[] { 0x1b, 'a', 0x01 };
	/* 閸欐牗绉风�涙ぞ缍嬮崝鐘电煐 */
	public static final byte[] ESC_CANCEL_BOLD = new byte[] { ESC, 0x45, 0 };

	// 鏉╂稓鐒�
	public static final byte[] ESC_ENTER = new byte[] { 0x1B, 0x4A, 0x40 };
	public static final byte[] ENTER = new byte[] { 0x0D, 0x0A };

	// 閼奉亝顥�
	public static final byte[] PRINTE_TEST = new byte[] { 0x1D, 0x28, 0x41 };
	public static final byte[] SET_RIGHT = new byte[] { 0x1B, 0x61, 0x02 };
	public static final byte[] SET_LEFT = new byte[] { 0x1B, 0x61, 0x00 };

	// 濞村鐦潏鎾冲毉Unicode Pirit Message
	public static final byte[] UNICODE_TEXT = new byte[] { 0x00, 0x50, 0x00,
			0x72, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x74, 0x00, 0x20, 0x00, 0x20,
			0x00, 0x20, 0x00, 0x4D, 0x00, 0x65, 0x00, 0x73, 0x00, 0x73, 0x00,
			0x61, 0x00, 0x67, 0x00, 0x65 };
	public static final byte[] huidu = new byte[] { 0x1B, 0x6D, 0x04 };

	public static final DateFormat formatw = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * print test 閹垫挸宓冮張楦垮殰濡拷/
	 * 
	 */
	public static void printTest() {
		try {
			// print(ESC_ALIGN_CENTER);
			if (allowTowrite())
				C.printSerialPortTools.write(PRINTE_TEST);
			// Thread.sleep(2000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ENTER(2);

		writeEnterLine(2);
	}

	/** 鎵撳嵃鏂囧瓧GB2312 */
	public static void printText(String text) {
		Log.i("info", "text == " + text);
		PrintTools_58mm.printText(text);
	}

	public static boolean getState() {

		return C.printSerialPortTools.getState();
	}

	/** print text 閹垫挸宓冮弬鍥х摟 */
	public static void printText(String text, boolean test, String Persian) {
		// ENTER(4);
		writeEnterLine(2);
		print_Unicode(text, test, Persian);
		// ENTER(4);
		writeEnterLine(2);

	}

	public static void setBold() {
		print(huidu);
	}

	public static void setRight() {
		print(SET_RIGHT);
	}
	public static void setLeft() {
		print(SET_LEFT);
	}

	public static void ENTER(int k) {
		for (int i = 0; i < k; i++) {
			print(ENTER);
		}
	}

	/**
	 * print photo with path 閺嶈宓侀崶鍓у鐠侯垰绶為幍鎾冲祪閸ュ墽澧�
	 * 
	 * @param 閸ュ墽澧栭崷
	 *            鈯緿閸椔ょ熅瀵板嫸绱濇俊锟絧hoto/pic.bmp
	 * */
	public static void printPhotoWithPath(String filePath) {

		String SDPath = Environment.getExternalStorageDirectory() + "/";
		String path = SDPath + filePath;

		// 閺嶈宓佺捄顖氱窞閼惧嘲褰囬崶鍓у
		File mfile = new File(path);
		if (mfile.exists()) {// 閼汇儴顕氶弬鍥︽鐎涙ê婀�
			Bitmap bmp = BitmapFactory.decodeFile(path);
			byte[] command = decodeBitmap(bmp);
			printPhoto(command);
		} else {
			testPrinter();
			Log.e("PrintTools_58mm", "the file isn't exists");
		}
	}

	/**
	 * print photo in assets 閹垫挸宓僡ssets闁插瞼娈戦崶鍓у
	 * 
	 * @param 閸ュ墽澧栭崷
	 *            鈺漵sets閻╊喖缍嶉敍灞筋渾:pic.bmp
	 * */
	public static void printPhotoInAssets(Context context, String fileName) {

		AssetManager asm = context.getResources().getAssets();
		InputStream is;
		try {
			is = asm.open(fileName);
			Bitmap bmp = BitmapFactory.decodeStream(is);
			is.close();
			if (bmp != null) {
				byte[] command = decodeBitmap(bmp);
				printPhoto(command);
			} else {
				Log.e("PrintTools", "the file isn't exists");
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("PrintTools", "the file isn't exists");
		}
	}

	/**
	 * decode bitmap to bytes 鐟欙絿鐖淏itmap娑撹桨缍呴崶鎯х摟閼哄倹绁�
	 * */
	public static byte[] decodeBitmap(Bitmap bmp) {
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();

		List<String> list = new ArrayList<String>(); // binaryString list
		StringBuffer sb;

		// 濮ｅ繗顢戠�涙濡弫锟介梽銈勪簰8閿涘奔绗夌搾瀹犓�0)
		int bitLen = bmpWidth / 8;
		int zeroCount = bmpWidth % 8;
		// 濮ｅ繗顢戦棁锟筋渽鐞涖儱鍘栭惃锟�
		String zeroStr = "";
		if (zeroCount > 0) {
			bitLen = bmpWidth / 8 + 1;
			for (int i = 0; i < (8 - zeroCount); i++) {
				zeroStr = zeroStr + "0";
			}
		}
		// 闁劒閲滅拠璇插絿閸嶅繒绀屾０婊嗗閿涘苯鐨㈤棃鐐垫閼瑰弶鏁兼稉娲拨閼癸拷
		for (int i = 0; i < bmpHeight; i++) {
			sb = new StringBuffer();
			for (int j = 0; j < bmpWidth; j++) {
				int color = bmp.getPixel(j, i); // 閼惧嘲绶盉itmap
												// 閸ュ墽澧栨稉顓熺槨娑擄拷閲滈悙鍦畱color妫版粏澹婇崐锟�
				// 妫版粏澹婇崐鑲╂畱R G B
				int r = (color >> 16) & 0xff;
				int g = (color >> 8) & 0xff;
				int b = color & 0xff;

				// if color close to white閿涘異it='0', else bit='1'
				if (r > 160 && g > 160 && b > 160)
					sb.append("0");
				else
					sb.append("1");
			}
			// 濮ｅ繋绔寸悰宀�绮ㄩ弶鐔告閿涘矁藟閸忓懎澧挎担娆戞畱0
			if (zeroCount > 0) {
				sb.append(zeroStr);
			}
			list.add(sb.toString());
		}
		// binaryStr濮ｏ拷娴ｅ秷鐨熼悽銊ょ濞喡ゆ祮閹广垺鏌熷▔鏇礉閸愬秵瀚鹃崥锟�
		List<String> bmpHexList = ConvertUtil.binaryListToHexStringList(list);
		String commandHexString = "1D763000";
		// 鐎硅棄瀹抽幐鍥︽姢
		String widthHexString = Integer
				.toHexString(bmpWidth % 8 == 0 ? bmpWidth / 8
						: (bmpWidth / 8 + 1));
		if (widthHexString.length() > 2) {
			Log.e("decodeBitmap error", "鐎硅棄瀹崇搾鍛毉 width is too large");
			return null;
		} else if (widthHexString.length() == 1) {
			widthHexString = "0" + widthHexString;
		}
		widthHexString = widthHexString + "00";

		// 妤傛ê瀹抽幐鍥︽姢
		String heightHexString = Integer.toHexString(bmpHeight);
		if (heightHexString.length() > 2) {
			Log.e("decodeBitmap error", "妤傛ê瀹崇搾鍛毉 height is too large");
			return null;
		} else if (heightHexString.length() == 1) {
			heightHexString = "0" + heightHexString;
		}
		heightHexString = heightHexString + "00";

		List<String> commandList = new ArrayList<String>();
		commandList.add(commandHexString + widthHexString + heightHexString);
		commandList.addAll(bmpHexList);

		return ConvertUtil.hexList2Byte(commandList);
	}

	/**
	 * print photo with bytes 閺嶈宓侀幐鍥︽姢閹垫挸宓冮崶鍓у
	 * */
	public static void printPhoto(byte[] bytes) {
		print(ESC_ALIGN_CENTER);
		writeEnterLine(1);
		print(bytes);
		writeEnterLine(3);
	}

	public static void printEndLine() {
		String msg = "\n\n\n\n\n";
		print(msg);
	}


	/** reset 闁插秶鐤嗛弽鐓庣础 */
	public static void resetPrint() {

		print(ESC_FONT_COLOR_DEFAULT);
		print(FS_FONT_ALIGN);
		print(ESC_ALIGN_LEFT);
		print(ESC_CANCEL_BOLD);
		print(LF);
	}

	/** 娑撴彃褰涢弰顖氭儊鐏忚京鍗� */
	public static boolean allowTowrite() {
		return C.printSerialPortTools != null;
	}

	/**
	 * 鏉堟挸鍤�
	 * 
	 * @param String閸愬懎
	 *            顔�
	 * */
	public static void print(String msg) {
		if (allowTowrite())
			C.printSerialPortTools.write(msg);
	}

	public static void print_unicode(String msg) {
		if (allowTowrite())
			C.printSerialPortTools.write_unicode(msg);
	}

	public static void print_Unicode(String msg, boolean test, String Persian) {
		if (allowTowrite())
			System.out.println("msg == " + msg);
		System.out.println("Persian == " + Persian);
		C.printSerialPortTools.write_Unicode(msg, Persian);
	}

	/**
	 * 鏉堟挸鍤�
	 * 
	 * @param byte[]閹稿洣鎶�
	 * */
	public static void print(byte[] b) {
		if (allowTowrite())
			C.printSerialPortTools.write(b);
//		try {
//			Thread.sleep(80);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	/**
	 * 鏉堟挸鍤�
	 * 
	 * @param int閹稿洣鎶�
	 * */
	public static void print(int oneByte) {
		if (allowTowrite())
			C.printSerialPortTools.write(oneByte);
	}

	/**
	 * EnterLine 鏉╂稓鐒�
	 * 
	 * @param 鏉╂稓鐒婄悰灞炬殶
	 * */
	public static void writeEnterLine(int count) {
		for (int i = 0; i < count; i++) {
			print(ESC_ENTER);
		}
	}

	// public static void setEnter(int count){
	// for(int )
	// }

	public static String getEnterLine(int count) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(ESC_ENTER);
		return sBuilder.toString();
	}

	public static void initPrinter() {
		openPrinter();
		convertPrinterControl();
		setBold();
	}

	public static boolean openPrinter() {
		// int result = GpioControl.activate(GpioControl.printer_o, true);
		int result = Ioctl.activate(20, 1);
		// Log.i("info", "open printer == "+Ioctl.get_status(8192));
		if (result == 1)
			return true;
		else
			return false;
	}

	public static boolean closePrinter() {
		int result = Ioctl.activate(20, 0);
		// Log.i("info", "close printer == "+Ioctl.get_status(8192));
		C.printSerialPortTools.closeSerialPort();
		if (result == 0)
			return true;
		else
			return false;
	}

	public static boolean convertPrinterControl() {
		int result = Ioctl.convertPrinter();

		C.printSerialPortTools = new SerialPortTools(C.printPort_58mm,
				C.printBaudrate_58mm);
		if (result == 0)
			return true;
		else
			return false;
	}

	/** 鑷 */
	public static void testPrinter() {
		JBInterface.printTest();
	}

	/* 浼犲叆鍥剧墖璺緞锛屾墦鍗颁簩缁寸爜鍥剧墖 */
	public static void printQRCodeWithPath(String qrcodeImagePath) {
		JBInterface.printPhotoWithPath(qrcodeImagePath);
	}

	/* 浼犲叆鍥剧墖璺緞锛屾墦鍗板浘鐗� */
	public static void printImageWithPath(String iamgePath) {
		JBInterface.printPhotoWithPath(iamgePath);
	}

	/* 浼犲叆Bitmap瀵硅薄锛屾墦鍗颁簩缁寸爜鍥剧墖 */
	public static void printQRCode(Bitmap bitmap) {
		byte[] command = JBInterface.decodeBitmap(bitmap);
		JBInterface.printPhoto(command);
	}

	/* 浼犲叆Bitmap瀵硅薄锛屾墦鍗板浘鐗� */
	public static void printImage(Bitmap bitmap) {
		byte[] command = JBInterface.decodeBitmap(bitmap);
		JBInterface.printPhoto(command);
	}

	/* 浼犲叆Assets鏂囦欢澶归噷闈㈢殑鍥剧墖鏂囦欢鍚嶏紝鎵撳嵃浜岀淮鐮佸浘鐗� */
	public static void printQRCodeImageInAssets(Context context, String fileName) {
		JBInterface.printPhotoInAssets(context, fileName);
	}

	/* 浼犲叆Assets鏂囦欢澶归噷闈㈢殑鍥剧墖鏂囦欢鍚嶏紝鎵撳嵃鍥剧墖 */
	public static void printImageInAssets(Context context, String fileName) {
		JBInterface.printPhotoInAssets(context, fileName);
	}

	// 瀹屾暣鐨勫垽鏂腑鏂囨眽瀛楀拰绗﹀彿
	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	// 鏍规嵁Unicode缂栫爜瀹岀編鐨勫垽鏂腑鏂囨眽瀛楀拰绗﹀彿
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	public static String stringToUnicode(String s) {

		String str = "";

		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			if (ch > 255)
				str += Integer.toHexString(ch);
			else
				str += Integer.toHexString(ch);
		}
		Log.i("info", "str ==  " + str);
		return str;

	}

	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	/**
	 * 灏嗘寚瀹氬瓧绗︿覆src锛屼互姣忎袱涓瓧绗﹀垎鍓茶浆鎹负16杩涘埗褰㈠紡 濡傦細"2B44EFD9" 鈥�> byte[]{0x2B, 0脳44, 0xEF,
	 * 0xD9}
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

	public static byte[] getStringToHexBytes(String str) {
		return HexString2Bytes(stringToUnicode(str));
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

}
