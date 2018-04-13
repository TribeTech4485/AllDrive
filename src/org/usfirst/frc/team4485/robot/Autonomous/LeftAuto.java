package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LeftAuto extends AutoProgram {

	public LeftAuto(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		//step = 4;
	}

	@Override
	protected void run() {
		subsystems.driveSystem.setBraking(true);
		subsystems.shifterPneumaticSystem.shiftUp();
		subsystems.liftSystem.setLiftPIDOverride(false);	// Set this to true if disabling the lift
		
		if (gameSpecificMessage.charAt(0) == 'L') {
			auto_complete = driveToSwitch();
		} else {
			auto_complete = driveBaseLine();
		}
		
		// Update the systems
		subsystems.driveSystem.update();
		subsystems.shifterPneumaticSystem.update();
		subsystems.collectorSystem.update();
		subsystems.liftSystem.update();
		subsystems.shifterPneumaticSystem.shiftDown();
	}
	
	double startTime = -1;
	double startWaitTime = -1;
	
	@SuppressWarnings("unused")
	private boolean driveToScale() {
		// Drive to the scale
		switch(step) {
		case 0:			
			// Drive the distance to the scale
			if (subsystems.driveSystem.driveToDistanceStraight(-800) < 1) step++;
			break;
		case 1:
			// Turn 90 degrees clockwise
			if (subsystems.driveSystem.driveToAngle(105) < 1) {
				subsystems.driveSystem.drive4Motors(0, 0);
				step++;
			}
			subsystems.liftSystem.setLiftPosition_presetNum(6);
			break;
		case 2:
			if (subsystems.liftSystem.getLiftOffset() < 200) step++;
			break;
		case 3:
			if (subsystems.driveSystem.driveToDistanceStraight(-20) < 1) step++;
			break;
		case 4:
			// Expel the block
			subsystems.collectorSystem.setExpel(true);
			if (startTime < 0) startTime = System.currentTimeMillis();
			SmartDashboard.putNumber("Time", System.currentTimeMillis() - startTime);
			if (System.currentTimeMillis() - startTime > 1000) {
				subsystems.collectorSystem.setExpel(false);
				startTime = -1;
				step++;
			}
			break;
		case 5:
			// Lower the lift
			if (subsystems.liftSystem.homeLift()) step++;
			break;
		}
		if (step >= 6) return true;
		return false;
	}
	
	private boolean driveToSwitch() {
		// Drive to the switch
		switch (step) {
		case 0:
			subsystems.liftSystem.setLiftPosition_presetNum(3);
			if (subsystems.driveSystem.driveToDistanceStraight(-335.25/*-365.76*/) < 1) step++;
			break;
		case 1:
			if (subsystems.driveSystem.driveToAngle(100) < 1) step++;
			break;
		case 2:
			if (subsystems.liftSystem.getLiftOffset() > 200) break;	// check if the lift is in the correct position (to avoid damage)
			if (subsystems.driveSystem.driveToDistanceStraight(-121.96, 500) < 1) step++;
			break;
		case 3:
			// Expel the block into the switch
			subsystems.collectorSystem.setExpel(true);
			if (startTime < 0) startTime = System.currentTimeMillis();
			SmartDashboard.putNumber("Time", System.currentTimeMillis() - startTime);
			if (System.currentTimeMillis() - startTime > 250) {
				subsystems.collectorSystem.setExpel(false);
				startTime = -1;
				step++;
			}
			break;
		case 4:
			// Backup
			subsystems.driveSystem.drive4Motors(0.5, 0.5);
			if (startTime < 0) startTime = System.currentTimeMillis();
			SmartDashboard.putNumber("Time", System.currentTimeMillis() - startTime);
			if (System.currentTimeMillis() - startTime > 800) {
				subsystems.driveSystem.drive4Motors(0,0);
				startTime = -1;
				step++;
			}
			break;
			/*if (subsystems.driveSystem.driveToDistance(30) < 1) step++;
			break;*/
			/*double offset = subsystems.driveSystem.driveToDistanceStraight(25);
			SmartDashboard.putNumber("Auto - Offset", offset);
			if (offset < 1) step++;
			break;*/
		case 5:
			// Home the lift
			subsystems.driveSystem.drive4Motors(0, 0);
			subsystems.driveSystem.setBraking(false);
			subsystems.shifterPneumaticSystem.shiftDown();
			if (subsystems.liftSystem.homeLift())step++;
			break;
		}
		if (step >= 6) return true;
		return false;
	}
	
	private boolean driveBaseLine() {
		// Drive across the base line
		if (subsystems.driveSystem.driveToDistanceStraight(-365.76, 30000) < 1) return true;
		return false;
	}

}
