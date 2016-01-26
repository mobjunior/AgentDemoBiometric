package com.agentdemo.finger;

public class C {

	public static SerialPortTools ledSerialPortTools;
	public static SerialPortTools kitchenSerialPortTools;
	public static SerialPortTools fingerSerialPortToools;
	
	public static String ledPort = "/dev/ttyS6";
	public static int ledBaudrate = 9600;
	
	public static String kitchenPort = "/dev/ttyS6"; // ��ӡ����
	public static int kitchenBaudrate = 115200; // ��ӡ������
	
	public static String fingerPort = "/dev/ttyS2";
	public static int fingerBaudrate = 57600;
	
	
}
