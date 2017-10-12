package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class BoxPneumaticSystem extends Subsystem {
	
	// Objects for the box pneumatics
	private Solenoid guideSolenoid1_in, guideSolenoid1_out;
	private Solenoid guideSolenoid2_in, guideSolenoid2_out;
	
	private Solenoid doorSolenoid_in, doorSolenoid_out;
	private Solenoid pusherSolenoid_in, pusherSolenoid_out;
	
	// Control variables
	private boolean guideOut = false, doorOut = false, pusherOut = false;
	private boolean closeDoor = false, expelGear = false;
	
	private double closeDoorStartTime = -1;
	private double closeDoorDuration = 0;
	
	// Timing for gear expulsion
	private double expelStartTime = -1;
	
	@Override
	protected void initSystem() {		
		guideSolenoid1_in = new Solenoid(id.boxGuideSolenoid1_in);
		guideSolenoid1_out = new Solenoid(id.boxGuideSolenoid1_out);
		guideSolenoid2_in = new Solenoid(id.boxGuideSolenoid2_in);
		guideSolenoid2_out = new Solenoid(id.boxGuideSolenoid2_out);
		
		doorSolenoid_in = new Solenoid(id.doorSolenoid_in);
		doorSolenoid_out = new Solenoid(id.doorSolenoid_out);
		
		pusherSolenoid_in = new Solenoid(id.pusherSolenoid_in);
		pusherSolenoid_out = new Solenoid(id.pusherSolenoid_out);
		
		update();
	}

	@Override
	protected void updateSystem() {
		expelGearProcess();
		closeDoorProcess();
		updateDashboardValues();
		guideSolenoid1_in.set(guideOut);
		guideSolenoid1_out.set(!guideOut);
		guideSolenoid2_in.set(guideOut);
		guideSolenoid2_out.set(!guideOut);
		
		doorSolenoid_in.set(doorOut);
		doorSolenoid_out.set(!doorOut);
		
		pusherSolenoid_in.set(!pusherOut);
		pusherSolenoid_out.set(pusherOut);
	}

	@Override
	protected void killSystem() {
		setGuideOut(false);
		setDoorOut(false);
		setPusherOut(false);
		updateSystem();
	}

	@Override
	protected void errorHandler() {
		// TODO Auto-generated method stub
		
	}
	
	// Function to close the door
	private void closeDoorProcess() {
		if (!closeDoor) return;
		
		doorOut = true;
		if (closeDoorStartTime < 0) closeDoorStartTime = System.currentTimeMillis();
		closeDoorDuration = System.currentTimeMillis() - closeDoorStartTime;
		if (closeDoorDuration >= 250) {
			doorOut = false;
			closeDoor = false;
		}
		
		pusherOut = false;
	}
	// Function to expel the gear
	private void expelGearProcess() {
		if (!expelGear) return;
		if (expelStartTime < 0) expelStartTime = System.currentTimeMillis();
		double duration = System.currentTimeMillis() - expelStartTime;
		
		if (duration < 500) {
			setDoorOut(true);
			return;
		} else if (duration < 800) {
			setPusherOut(true);
			return;
		}
		
		setDoorOut(false);
		expelGear = false;
		expelStartTime = -1;
	}
	private void updateDashboardValues() {
		if (guideOut) SmartDashboard.putString("Box Guide", "OUT");
		else SmartDashboard.putString("Box Guide", "IN");
		
		if (doorOut) SmartDashboard.putString("Box Door", "Open");
		else SmartDashboard.putString("Box Door", "Closed");
		
		if (pusherOut) SmartDashboard.putString("Box Gear Pusher", "OUT");
		else SmartDashboard.putString("Box Gear Pusher", "IN");
	}
	
	// Public control functions
	public void setGuideOut(boolean _out) { guideOut = _out; }
	
	// Set the door to open or close
	public void setDoorOut(boolean _out) { 
		if (!_out) closeDoor = true;	// If we want to close the door, set the close door function to run
		else doorOut = _out;			// If we don't then set the door to open
	}
	
	// Control the pusher
	public void setPusherOut(boolean _out) {
		if (doorOut && _out) pusherOut = _out;	// if the door is open, move the pusher
		else if (doorOut) pusherOut = false;	// if the door is not open, don't
	}
	
	// Control the expel gear function
	public void expel() {
		expelGear = true;
	}
	
	// Getters for pneumatic states
	public boolean getDoorState() { return doorOut; }
	
}