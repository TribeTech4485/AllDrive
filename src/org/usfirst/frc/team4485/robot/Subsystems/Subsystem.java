package org.usfirst.frc.team4485.robot.Subsystems;

import org.usfirst.frc.team4485.robot.RobotIndexing;

// TODO: Put a whole lot of comments in here

public abstract class Subsystem extends Thread {
	protected RobotIndexing id;
	
	protected boolean enabled = true;
	
	// Error values
	protected boolean error = false;
	protected boolean majorError = false;
	protected String lastErrorMessage = " ";
	protected String errorMessage = "";  
	//
	
	// Class ID Values
	private int systemid = -1;
	private boolean idSet = false;
	//
	
	// Time values
	private double time_start = -1;
	private double time_duration = 0;
	private double time_wait = -1;
	//
	
	public Subsystem() {
		id = new RobotIndexing();
		
		time_start = -1;
		time_duration = -1;
		
		//setID(id.getNextSystemID());
		try {
			initSystem();
		} catch (Exception ex) {
			createError(false, ex.getMessage());
		}
	}
	public Subsystem(int setsystemid) {
		setID(setsystemid);
		time_start = -1;
		time_duration = -1;
		try {
			initSystem();
		} catch (Exception ex) {
			createError(false, ex.getMessage());
		}
	}
	
	public void update() {
		if (!checkTime() || !enabled) return;
		checkSubsystem();
		try {
			updateSystem();
		} catch (Exception ex) {
			createError(false, ex.getMessage());
		}
	}
	public void killAll() {
		try {
			killSystem();
		} catch (Exception ex) {
			createError(false, ex.getMessage());
		}
	}
	public void setID(int setsystemid) {
		idSet = true;
		systemid = setsystemid;
		createMessage("Created Subsystem with system ID:" + systemid);
	}
	public boolean error() {
		//if (lastErrorMessage == errorMessage) return error;
		//if (error && majorError) System.out.println("Error (System ID:" + systemid + "): " + errorMessage);
		//else if (error) System.out.println("Warning (System ID:" + systemid + "): " + errorMessage);
		lastErrorMessage = errorMessage;
		return error;
	}
	
	private boolean checkTime() {
		if (time_wait < 0) return true;
		if (time_start < 0) time_start = System.currentTimeMillis();
		else time_duration = System.currentTimeMillis() - time_start;
		
		if (time_duration >= time_wait) {
			time_start = -1;
			return true;
		} 
		
		return false;
	}
	private void checkSubsystem() {
		// Check the system id
		if (systemid < 0 && idSet) createError(false, "System id is less than 0. System id is " + systemid + ".");
		else if (!idSet) createError(false, "System id has not been set. System default id is " + systemid + ".");
		error();
	}
	
	protected void createError(boolean major, String message) {
		if (lastErrorMessage != errorMessage) return;
		error = true;
		majorError = major;
		errorMessage = message;
	}
	protected void createMessage(String message) {
		//System.out.println("(System ID:" + systemid + "):" + message);
	}
	
	protected abstract void initSystem();// throws Exception;
	protected abstract void updateSystem();// throws Exception;
	protected abstract void killSystem();// throws Exception;
	protected abstract void errorHandler();// throws Exception;
}