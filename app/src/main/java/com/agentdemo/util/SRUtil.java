package com.agentdemo.util;

import java.text.DecimalFormat;

public class SRUtil {
	public static String doubleFormat(double d) {
		String str;
		DecimalFormat df2 = new DecimalFormat("#.00");
		str = df2.format(d);
		return str;
	}
}
