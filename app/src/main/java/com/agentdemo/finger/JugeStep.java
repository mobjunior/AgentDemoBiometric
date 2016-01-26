package com.agentdemo.finger;

import com.agentdemo.R;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;


public class JugeStep {

	public static int search(int doResult, TextView t, int i, Context context) {
		int page = -1;
		switch (doResult) {
		case 0:
			t.setText(context.getResources()
					.getString(R.string.Successful_match));
//			t.setText("\n");

			page = i;
			return page;

		case 1:
			t.setText(context.getResources().getString(
					R.string.data_packet_error));
//			t.append("\n");
			return 1;

		case 9:
//			t.append(context.getResources().getString(
//					R.string.No_matching_fingerprint_records));
//			t.append("\n");
			return 9;
		}

		return page;

	}

	public static boolean tezheng1(int doResult, int i,
			TextView t, Context context) {
		switch (doResult) {
		case 0:
			if (i == 2) {
				t.setText(context.getResources().getString(
						R.string.Synthetic_fingerprint_feature_two_successful));
				t.append("\n");
				t.append(context
						.getResources()
						.getString(
								R.string.Synthesizing_to_obtain_two_fingerprint_feature));
				t.append("\n");

				return false;
			}
			t.setText(context.getResources().getString(
					R.string.Synthetic_fingerprint_feature_one_successful) + " " + context.getResources().getString(
					R.string.Obtaining_the_fingerprint_image_again));
			t.append("\n");
			t.append(context.getResources().getString(
					R.string.Obtaining_the_fingerprint_image_again));
			t.append("\n");
			return false;
		case -1:
			if (i == 2) {
//				t.append("æ”¶åˆ°çš„ç‰¹å¾?äºŒæ•°æ?®åŒ…é”™è¯¯ï¼?");
//				t.append("\n");
				return true;
			}
//			t.append("ç”Ÿæˆ?ç‰¹å¾?ä¸?æ•°æ?®åŒ…é”™è¯¯ï¼?");
//			t.append("\n");
			return true;
		case 6:
			t.setText(context.getResources().getString(
					R.string.Fingerprint_image_is_too_messy));
			t.append("\n");
//			tx.setText(context.getResources().getString(
//					R.string.Fingerprint_image_is_too_messy));
			return true;
		case 7:
			t.setText(context
					.getResources()
					.getString(
							R.string.Fingerprint_image_is_normal_but_too_few_feature_points_is_born_not_feature));
			t.append("\n");
//			tx.setText(context
//					.getResources()
//					.getString(
//							R.string.Fingerprint_image_is_normal_but_too_few_feature_points_is_born_not_feature));
			return true;
		case 15:
			t.append(context
					.getResources()
					.getString(
							R.string.No_valid_source_image_buffer_born_not_figure_image));
//			tx.setText(context
//					.getResources()
//					.getString(
//							R.string.No_valid_source_image_buffer_born_not_figure_image));
			t.append("\n");
			return true;

		case -2:
//			t.append("æ²¡æœ‰ä¸²å?£æ•°æ?®ï¼?");
//			t.append("\n");
			return true;
		case -3:
			if (i == 2) {
//				t.append("ç‰¹å¾?äºŒï¼Œæ•°æ?®åŒ…ä¸ºç©ºï¼?");
//				t.append("\n");
				return true;
			}
//			t.append("ç‰¹å¾?ä¸?ï¼Œæ•°æ?®åŒ…ä¸ºç©ºï¼?");
//			t.append("\n");
			return true;
		}
		return true;

	}

	public static int regMsg(int doResult, TextView t,
			Context context) {
		switch (doResult) {
		case 0:
//			t.append(context.getResources().getString(
//					R.string.Enroll_twice_successful_merger));
			t.append("\n");
//			t.append(context.getResources().getString(
//					R.string.Being_stored_fingerprint));
			t.append("\n");

			return 0;

		case 1:
			t.append("æŒ‡çº¹å?ˆæˆ?æŽ¥æ”¶åŒ…é”™è¯¯ï¼?");
			t.append("\n");
			return 1;

		case 10:
//			t.append(context
//					.getResources()
//					.getString(
//							R.string.Synthetic_fingerprint_failed_Two_different_fingerprint_entry));
			t.append("\n");
//			tx.setText(context
//					.getResources()
//					.getString(
//							R.string.Synthetic_fingerprint_failed_Two_different_fingerprint_entry));
			return 10;
		}
		return -1;
	}

	public static void storeMsg(int doResult,TextView t,
			Context context) {
		switch (doResult) {
		case 0:
//			t.append(context.getResources().getString(
//					R.string.Fingerprint_save_success));
			t.append("\n");

			break;

		case 1:
			t.append("æŒ‡çº¹å‚¨å­˜æŽ¥æ”¶åŒ…é”™è¯¯ï¼?");
			t.append("\n");

			break;

		case 11:
//			t.append(context.getResources().getText(
//					R.string.Successful_match_Fingerprint_ID_is));
			t.append("\n");
			break;

		case 18:
			t.append("FLASH å‡ºé”™ï¼?");
			t.append("\n");
			break;

		case -3:
			t.append("æ²¡æœ‰æŽ¥å?—åˆ°æ•°æ?®ï¼?");
			t.append("\n");
			break;
		}
	}
}
