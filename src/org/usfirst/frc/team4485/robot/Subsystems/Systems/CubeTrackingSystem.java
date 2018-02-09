package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.NetworkDevice;

public class CubeTrackingSystem extends NetworkDevice {
	
	private String data = "";
	private int messagesReceived = 0;
	
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
	public int getMessagesReceived() {
		return messagesReceived;
	}
	
}
