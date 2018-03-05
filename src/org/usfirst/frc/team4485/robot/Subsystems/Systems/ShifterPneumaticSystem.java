package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Robot;
import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import edu.wpi.first.wpilibj.Solenoid;

// TODO: Add comments

public class ShifterPneumaticSystem extends Subsystem {

	// Two solenoids for shifter in and out
	private Solenoid shifterSolenoidRight_out, shifterSolenoidRight_in;
	private Solenoid shifterSolenoidLeft_out, shifterSolenoidLeft_in;
	private boolean shifterOut = true, lowGear = true;
	
	private boolean autoShiftEnabled = true;
	// Current limits for shifting up and down
	//private final double lowGearShiftUpCurrentLimit = -1.0;
	private final double highGearShiftDownCurrentLimit = 80.0;
	
	// Since we have a shifting gear box we can implement AUTO shifting,
	// so we need a couple ways to know when to shift.
	// One is current draw and voltage drops, so we have the current draw average and voltage.
	private double motorCurrentAvg = 0.0, totalVoltage = 0.0, initVoltage = -1.0;
	private double lowGearCurrentHigh = 0.0, highGearCurrentHigh = 0.0;
	private double voltageDropCheckPercent = 0.20;
	// The other is RPM
	private double avgDriveRPM = 0.0;
	private double lowGearShiftUpRPMLimit = 400.0;
	private double highGearShiftDownRPMLimit = 350.0;
	
	@Override
	protected void initSystem() {
		shifterSolenoidLeft_out = new Solenoid(id.shifterSolenoidsModule, id.shifterSolenoidLeft_out);
		shifterSolenoidLeft_in = new Solenoid(id.shifterSolenoidsModule, id.shifterSolenoidLeft_in);
		shifterSolenoidRight_out = new Solenoid(id.shifterSolenoidsModule, id.shifterSolenoidRight_out);
		shifterSolenoidRight_in = new Solenoid(id.shifterSolenoidsModule, id.shifterSolenoidRight_in);
		
		shiftDown();
		update();
	}

	@Override
	protected void updateSystem() {
		lowGear = shifterOut;	//We are in low gear if the shifter is out
		shifterSolenoidLeft_out.set(shifterOut);
		shifterSolenoidLeft_in.set(!shifterOut);
		shifterSolenoidRight_out.set(shifterOut);
		shifterSolenoidRight_in.set(!shifterOut);
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
		motorCurrentAvg += Robot.sensorController.powerHandlerSystem.getChannelCurrent(id.leftDriveMotorSlave1PDP);
		motorCurrentAvg += Robot.sensorController.powerHandlerSystem.getChannelCurrent(id.rightDriveMotorMasterPDP);
		motorCurrentAvg += Robot.sensorController.powerHandlerSystem.getChannelCurrent(id.rightDriveMotorSlave1PDP);
		motorCurrentAvg /= 4;
		
		if (initVoltage < 0) initVoltage = Robot.sensorController.powerHandlerSystem.getPDPTotalVoltage();
		totalVoltage = Robot.sensorController.powerHandlerSystem.getPDPTotalVoltage();
		
		if (lowGear && motorCurrentAvg > lowGearCurrentHigh) lowGearCurrentHigh = motorCurrentAvg;
		else if (!lowGear && motorCurrentAvg > highGearCurrentHigh) highGearCurrentHigh = motorCurrentAvg;
	}
	
	private void autoShift() {
		if (!autoShiftEnabled) return;
		//System.out.println("MotorAvgCurrent: " + motorCurrentAvg + "  lowGearCurrentHigh: " + lowGearCurrentHigh + "  highGearCurrentHigh: " + highGearCurrentHigh);
		//System.out.println("TotalVoltage: " + totalVoltage);
		//System.out.println("TotalVoltage: " + totalVoltage + "  InitVoltage: " + initVoltage + "  VoltageDrop:" + (initVoltage * voltageDropCheckPercent));
		
		// Check for a voltage drop
		if (totalVoltage <= initVoltage - (initVoltage * voltageDropCheckPercent) && motorCurrentAvg >= highGearShiftDownCurrentLimit) {
			// There is a drop, so shift into low gear if our motor draw is above its limit
			shiftDown();
			
			//System.out.println("MotorAvgCurrent: " + motorCurrentAvg + "  lowGearCurrentHigh: " + lowGearCurrentHigh + "  highGearCurrentHigh: " + highGearCurrentHigh);
			//System.out.println("TotalVoltage: " + totalVoltage + "  Voltage Drop Check Limit: " + (initVoltage - (initVoltage * voltageDropCheckPercent)));
			return;
		}
		
		// Now we check the average RPM 
		//avgDriveRPM = Robot.sensorController.getAverageRPM();
		//System.out.println("AVG RPM: " + avgDriveRPM);
		if (avgDriveRPM >= lowGearShiftUpRPMLimit) {
			shiftUp();
			return;
		}
		
		if (avgDriveRPM <= highGearShiftDownRPMLimit) {
			shiftDown();
			return;
		}
		
	}
	
	// Functions to control the shifter
	public void shiftUp() { shifterOut = false; }
	public void shiftDown() {shifterOut = true; }
	public void setShifter(boolean _out) { shifterOut = _out; }
	
	public void setAutoShift(boolean _shift) {autoShiftEnabled = _shift;}
	
}