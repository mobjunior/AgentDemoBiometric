package com.agentdemo.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

public class NFCUtil {
	
	private static final String TAG = "NFCUtil";
	
	public NfcAdapter nfcAdapter;
	public PendingIntent pendingIntent;
	public IntentFilter ndefFilter;
	public IntentFilter[] mFilters;
	public String[][] mTechLists;
	
	public Context mContext;
	
	public NFCUtil(Context context) {
		mContext = context;
		initNfc();
	}
	
	public void initNfc() {
		
		Log.d(TAG, "initNfc into...");
		
		// NFC adapter, all about the NFC operating from the adapter
		nfcAdapter = NfcAdapter.getDefaultAdapter(mContext);
		
		// Is called the Intent, Used to repeat Intent trigger
		pendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext,
				mContext.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		// Set to filter the label of the action, here only receive ACTION_NDEF_DISCOVERED type.
		ndefFilter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		ndefFilter.addCategory("*/*");

		// The filter
		mFilters = new IntentFilter[] { ndefFilter };

		// Allow scan label type
		mTechLists = new String[][] {
				new String[] { MifareClassic.class.getName() },
				new String[] { NfcA.class.getName() },
				new String[] { NfcF.class.getName() },
				new String[] { NfcB.class.getName() },
				new String[] { NfcV.class.getName() } };
		
		Log.d(TAG, "initNfc finish...");
	}
	
	/**
	 * Write NFC Tag
	 * @param intent
	 * @param info
	 * @return
	 */
	public boolean writeNfcTag(Intent intent, String info) {
		
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);		
		Ndef ndef = Ndef.get(tag);
		
		NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				"text/plain".getBytes(), new byte[] {}, info.getBytes());
		NdefRecord[] records = { ndefRecord };
		
		NdefMessage ndefMessage = new NdefMessage(records);

		int size = ndefMessage.toByteArray().length;
		Log.d(TAG, "ndefMessage size : " + size);
				

		try {
			if (ndef != null) {
				ndef.connect();
				ndef.writeNdefMessage(ndefMessage);

				showToast("Write data succeed!");
				return true;
			} else {
				NdefFormatable format = NdefFormatable.get(tag);

				if (format != null) {
					format.connect();
					format.format(ndefMessage);
					
					showToast("Formate tag succeed!");
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			showToast("IOException! Write tag failed!");
		} catch (FormatException e) {
			e.printStackTrace();
			showToast("NDEF is not supported by the tag!");
		}

		return false;	
	}
	
	/**
	 * Read NFC Tag
	 * @param intent
	 * @return
	 */
	public String readNfcTag(Intent intent) {
		
		Log.d(TAG, "Read nfc tag start!");
		
		String readResult = null;
		
		Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		
		if (rawArray != null) {
			NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
			NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
			
			try {
				if (mNdefRecord != null) {
					readResult = new String(mNdefRecord.getPayload(), "UTF-8");
					Log.d(TAG, "Read tag info :" + readResult);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				showToast("Unsupported data formate!");
			}		
		}
		return readResult;
	}
	
	/**
	 * Check the NFC device
	 * @return
	 */
	public Boolean isNFCUse() {	
		
		if (nfcAdapter == null) {
			showToast("The device does not support NFC！");
			return false;
		}
		if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
			showToast("Please enable NFC functionality in the system settings！");
			return false;
		}
		return true;
	}
	
	/**
	 * Show toast
	 * @param context
	 * @param text
	 */
	public void showToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

}
