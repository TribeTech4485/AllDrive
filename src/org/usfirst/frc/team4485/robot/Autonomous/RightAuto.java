package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RightAuto extends AutoProgram {

	public RightAuto(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		subsystems.sensorController.zeroAHRSYaw();
	}

	@Override
	protected void run() {
		subsystems.driveSystem.setBraking(true);
		subsystems.shifterPneumaticSystem.shiftDown();
		subsystems.liftSystem.setLiftPIDOverride(true);	// Set this to true if disabling the lift
		
		if (gameSpecificMessage.charAt(1) == 'R') {
			auto_complete = driveToScale();
		} else if (gameSpecificMessage.charAt(0) == 'R') {
			auto_complete = driveToSwitch();
		} else {
			auto_complete = driveBaseLine();
		}
		
		// Update the systems
		subsystems.driveSystem.update();
		subsystems.shifterPneumaticSystem.update();
		subsystems.liftSystem.update();
	}
	
	double startTime = -1;
	
	private boolean driveToScale() {
		// Drive to the scale
		switch(step) {
		case 0:			
			// Drive the distance to the scale
			if (subsystems.driveSystem.driveToDistance(-838.2, 20000) < 1) step++;
			break;
		case 1:
			// Turn 90 degrees clockwise
			if (subsystems.driveSystem.driveToAngle(-90) < 1) step++;
			break;
		case 2:
			subsystems.liftSystem.setLiftPosition_presetNum(6);
			if (subsystems.liftSystem.getLiftOffset() < 200) step++;
		case 3:
			// Expel the block
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
			// Lower the lift
			if (subsystems.liftSystem.homeLift()) step++;
			break;
		case 5:
			return true;
		}
		return false;
	}
	
	private boolean driveToSwitch() {
		// Drive to the switch
		switch (step) {
		case 0:
			subsystems.liftSystem.setLiftPosition_presetNum(3);
			if (subsystems.driveSystem.driveToDistance(-365.76, 10000) < 1) step++;
			break;
		case 1:
			if (subsystems.driveSystem.driveToAngle(-90) < 1) step++;
			break;
		case 2:
			if (subsystems.driveSystem.driveToDistance(-121.96, 2000) < 1) step++;
			break;
		case 3:
			// Expel into the switch
			subsystems.collectorSystem.setExpel(true);
			if (startTime < 0) startTime = System.currentTimeMillis();
			SmartDashboard.putNumber("Time", System.currentTimeMillis() - startTime);
			if (System.currentTimeMillis() - startTime > 250) {
				subsystems.collectorSystem.setExpel(false);
				startTime = -1;
				step++;
			}
		case 4:
			// Backup
			if (subsystems.driveSystem.driveToDistance(91.92) < 1) step++;
			break;
		case 5:
			// Home the lift
			if (subsystems.liftSystem.homeLift())step++;
			subsystems.shifterPneumaticSystem.shiftDown();
			return true;
		}
		return true;
	}
	
	private boolean driveBaseLine() {
		// Drive across the base line
		if (subsystems.driveSystem.driveToDistance(-365.76, 30000) < 1) return true;
		return false;
	}

}
