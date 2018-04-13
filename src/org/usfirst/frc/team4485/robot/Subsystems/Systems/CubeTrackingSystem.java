package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.NetworkDevice;

public class CubeTrackingSystem extends NetworkDevice {
	
	private String data = "";
	private String parsedData = "";
	private int messagesReceived = 0;
	
	private char parseStartChar = 'A';
	private char parseEndChar = 'E';
	
	@Override
	protected void onInitSystem() {
		port = 5801;	// Set the port for connection (teams are allowed to use ports 5800 to 5810)
	}

	@Override
	protected void onDeviceUpdate() {
		// Parse the data and do stuff
		data = getDeviceData();
		messagesReceived = getDeviceMessagesReceived();
	}

	@Override
	protected void errorHandler() {

	}

	public String getData() {
		return data;
	}
	public String getParsedData() {
		int startIndex = -1;
		int endIndex = -1;
		for (int i = 0; i < data.length(); i++) {
			if (data.charAt(i) == parseStartChar) {
				startIndex = i;
			} else if (data.charAt(i) == parseEndChar) {
				
			}
		}
		
		// check the start and end
		if (startIndex < 0 || endIndex < 0) return "";
		else if (startIndex + 1 > endIndex) return "";
		
		parsedData = "";
		
		for (int i = startIndex + 1; i < endIndex; i++) {
			parsedData += data.charAt(i);
		}
		
		return parsedData;
	}
	public double getAngleToCube() {
		String angleData = getParsedData();
		if (angleData == "") return 400;	// Return out of bounds number
		
		double returnAngle = 410.0;	// if try fails return out of bounds
		try {
			returnAngle = Double.parseDouble(angleData);
		} catch (Exception ex) {}
		return returnAngle;
	}
	public int getMessagesReceived() {
		return messagesReceived;
	}
	
}
