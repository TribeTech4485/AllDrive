package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.NetworkController;
import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

public class NetworkDeviceSystem extends Subsystem {

	private Thread nconThread;
	private NetworkController ncon;
	private int port;
	
	private String deviceData = "";
	private String lastDeviceData = "";
	private String parsedData = "";
	private int timesUpdated = 0;
	
	private boolean parseData = false;
	
	@Override
	protected void initSystem() {
		port = 5800;
		ncon = new NetworkController();
		ncon.setPort(port);
		nconThread = new Thread(ncon);
		nconThread.start();
	}

	@Override
	protected void updateSystem() {
		deviceData = ncon.getRcvd();
		timesUpdated = ncon.getNumRcvd();
		
		if (deviceData != lastDeviceData) {
			parseData = true;
			lastDeviceData = deviceData;
		} else parseData = false;
		parsedData = parseInput(deviceData, 'a', 'e');
	}

	@Override
	protected void killSystem() {
		
	}

	@Override
	protected void errorHandler() {
		
	}
	
	private String parseInput(String input, char beginFlag, char endFlag) {
		String parsed = "";
		int beginChar = -1, endChar = -1;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == beginFlag) beginChar = i;
			else if (input.charAt(i) == endFlag) endChar = i;
		}
		//System.out.println("Begin: " + beginChar + " End: " + endChar);
		if (beginChar < 0 || endChar < 0) return "";
		for (int i = beginChar + 1; i < endChar; i++) {
			parsed += input.charAt(i);
		}
		
		return parsed;
	}
	
	public int getTimesUpdated() {
		return timesUpdated;
	}
	
	public String getParsedData() {
		return parsedData;
	}
	public String getRawData() {
		return deviceData;
	}

}
