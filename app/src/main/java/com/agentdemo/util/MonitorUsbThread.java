package com.agentdemo.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.PendingIntent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

public class MonitorUsbThread implements Runnable {
	private UsbManager usbManger = null;
	private PendingIntent mPermissionIntent = null;
	//private static final String TAG = "com.cytmxk.usbutils.utils.MonitorUsbThread";
	private boolean isRun = false;
	private Map<Integer,Integer> usbDeviceTem = new HashMap<Integer,Integer>();
	
	

	public MonitorUsbThread() {
		super();
		// TODO Auto-generated constructor stub
	}



	public MonitorUsbThread(UsbManager usbManger,PendingIntent mPermissionIntent) {
		super();
		this.usbManger = usbManger;
		this.mPermissionIntent = mPermissionIntent;
		usbDeviceTem.put(2009, 7638);
		usbDeviceTem.put(8201, 30264);
		usbDeviceTem.put(2109, 7638);
		usbDeviceTem.put(8457, 30264);
		usbDeviceTem.put(2109, 7368);
		usbDeviceTem.put(8457, 29544);
		usbDeviceTem.put(0453, 9005);
		usbDeviceTem.put(1107, 36869);

	}

	public boolean isRun() {
		return isRun;
	}



	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		android.util.Log.i("chenyang",Thread.currentThread().getId() + "");
		isRun = true;
		while(isRun){
			HashMap<String, UsbDevice> usbDevices = usbManger.getDeviceList();
			//android.util.Log.i("chenyang","usbDevices.size() = " + usbDevices.size());
			Iterator<UsbDevice> it = usbDevices.values().iterator();
			while(it.hasNext()){
				UsbDevice usbDevice = it.next();
				int productId = usbDevice.getProductId();
				int vendorId = usbDevice.getVendorId();
				if(usbDeviceTem.containsKey(vendorId) && (usbDeviceTem.get(vendorId) == productId)){
					android.util.Log.i("chenyang","usbManger.requestPermission begin");
					usbManger.requestPermission(usbDevice, mPermissionIntent);
					android.util.Log.i("chenyang","usbManger.requestPermission end");
					isRun = false;
					break;
				}
				//android.util.Log.i("chenyang","productId = " + productId);
			}			
		}

	}

}
