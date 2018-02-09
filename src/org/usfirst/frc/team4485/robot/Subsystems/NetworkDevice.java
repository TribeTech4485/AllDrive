package org.usfirst.frc.team4485.robot.Subsystems;

public abstract class NetworkDevice extends Subsystem {
	private final boolean oneTimeInit = true;	// If enabled this system's objects are only initialized once and will stay initialized until the program is restarted
	private boolean ranInit = false;
	
	private NetworkController networkController;
	private Thread networkThread;
	
	// Can be set in child class
	protected int port = 0;				// The port the network controller will run on
	
	private String deviceAddress = "";	// This will be received with the packets
	private String deviceData = "";		// The data that was received from the device
	private int deviceMessagesReceived = 0;	// The number of messages that have been received
	
	@Override
	protected void initSystem() {
		if (oneTimeInit && ranInit) return;
		else if (oneTimeInit) ranInit = true;
		onInitSystem();
		
		// Initialize network controller
		networkController = new NetworkController();
		networkController.setPort(port);
		
		// Start the network controller
		networkThread = new Thread(networkController);
		networkThread.start();
	}
	
	@Override 
	protected void updateSystem() {
		deviceAddress = networkController.getSenderAddress();
		deviceData = networkController.getRcvd();
		deviceMessagesReceived = networkController.getNumRcvd();
		
		onDeviceUpdate();
	}
	
	@Override
	protected void killSystem() {
		if (!oneTimeInit) return;
		networkController.stop();
	}
	
	protected String getDeciveAddress() {
		return deviceAddress;
	}
	protected String getDeviceData() {
		return deviceData;
	}
	protected int getDeviceMessagesReceived() {
		return deviceMessagesReceived;
	}
	
	protected abstract void onInitSystem();
	protected abstract void onDeviceUpdate();
}
