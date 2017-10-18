package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Robot;
import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import edu.wpi.first.wpilibj.Solenoid;

public class ShifterPneumaticSystem extends Subsystem {

	private Solenoid shifterSolenoid_out, shifterSolenoid_in;
	private boolean shifterOut = false;
	
	private boolean autoShiftEnabled = false;
	
	// Since we have a shifting gear box we can implement AUTO shifting
	// so we need a couple ways to know when to shift.
	// One is current draw, so we have the current draw average.
	private double motorCurrentAvg = 0.0;
	
	@Override
	protected void initSystem() {
		shifterSolenoid_out = new Solenoid(id.shifterSolenoidsModule, id.shifterSolenoid_out);
		shifterSolenoid_in = new Solenoid(id.shifterSolenoidsModule, id.shifterSolenoid_in);
		shiftDown();
		update();
	}

	@Override
	protected void updateSystem() {
		shifterSolenoid_out.set(shifterOut);
		shifterSolenoid_in.set(!shifterOut);
		updateCurrentAvg();
		autoShift();
	}

	@Override
	protected void killSystem() {
		shiftDown();
		update();
	}

	@Override
	protected void errorHandler() {
		// TODO Auto-generated method stub
		
	}
	
	private void updateCurrentAvg() {
		Robot.sensorController.update();
		motorCurrentAvg += Robot.sensorController.powerHandlerSystem.getChannelCurrent(id.leftDriveMotorMasterPDP);
		motorCurrentAvg += Robot.sensorController.powerHandlerSystem.getChannelCurrent(id.leftDriveMotorSlavePDP);
		motorCurrentAvg += Robot.sensorController.powerHandlerSystem.getChannelCurrent(id.rightDriveMotorMasterPDP);
		motorCurrentAvg += Robot.sensorController.powerHandlerSystem.getChannelCurrent(id.rightDriveMotorSlavePDP);
		motorCurrentAvg /= 4;
	}
	
	private void autoShift() {
		if (!autoShiftEnabled) return;
		System.out.println("MotorAvgCurrent: " + motorCurrentAvg);
	}
	
	// Functions to control the shifter
	public void shiftUp() { shifterOut = false; }
	public void shiftDown() {shifterOut = true; }
	public void setShifter(boolean _out) { shifterOut = _out; }
	
	public void setAutoShift(boolean _shift) {autoShiftEnabled = _shift;}
	
}