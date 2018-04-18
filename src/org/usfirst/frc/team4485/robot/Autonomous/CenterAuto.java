package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CenterAuto extends AutoProgram {
	
	public CenterAuto(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		subsystems.sensorController.zeroAHRSYaw();
	}

	@Override
	protected void run() {
		// For testing without lift use:
		subsystems.liftSystem.setLiftPIDOverride(false);
		// Otherwise don't set it, or set it to false
		
		SmartDashboard.putString("FMS Message", gameSpecificMessage);
		subsystems.driveSystem.setBraking(true);
		//subsystems.shifterPneumaticSystem.shiftDown();
		subsystems.shifterPneumaticSystem.shiftUp();
		if (gameSpecificMessage.charAt(0) == 'R') {
			auto_complete = driveToRight();
		} else if (gameSpecificMessage.charAt(0) == 'L') {
			auto_complete = driveToLeft();
		}
		
		SmartDashboard.putNumber("Yaw", subsystems.sensorController.getAHRSYaw());
		
		subsystems.driveSystem.update();
		subsystems.shifterPneumaticSystem.update();
		subsystems.collectorSystem.update();
		subsystems.liftSystem.update();
	}
	
	double startTime = -1;
	
	private boolean driveToLeft() {
		switch (step) {
		case 0:
			// Move the lift to the switch preset, drive forward
			subsystems.liftSystem.setLiftPosition_presetNum(3);
			if (subsystems.driveSystem.driveToDistance(-75/*-91.44*/) < 1) step++;
			break;
		case 1:
			// Turn the the left
			if (subsystems.driveSystem.driveToAngle(-70) < 1) step++;
			break;
		case 2:
			// Drive forward
			if (subsystems.driveSystem.driveToDistance(-218.44) < 1) step++;
			break;
		case 3:
			// Straighten back up
			if (subsystems.driveSystem.driveToAngle(0) < 1) step++;
			break;
		case 4:
			subsystems.driveSystem.drive4Motors(0, 0);
			step++;
			break;
		case 5:
			if (subsystems.liftSystem.getLiftOffset() > 200) break;	// check if the lift is in the correct position (to avoid damage)
			// Move forward to the switch with timeout so we can stop if we hit it early
			// -121.92 cm	
			if (subsystems.driveSystem.driveToDistance(-70/*-60.96*/,1000) < 1) step++;	// Drive to distance with a timeout of 5000 ms
			break;
		case 6:
			// Expel into the switch
			subsystems.collectorSystem.setExpel(true);
			if (startTime < 0) startTime = System.currentTimeMillis();
			SmartDashboard.putNumber("Time", System.currentTimeMillis() - startTime);
			if (System.currentTimeMillis() - startTime > 250) {
				subsystems.collectorSystem.setExpel(false);
				startTime = -1;
				step++;
			}
		case 7:
			// Backup
			if (subsystems.driveSystem.driveToDistance(91.92) < 1) step++;
			break;
		case 8:
			subsystems.driveSystem.drive4Motors(0, 0);
			// Home the lift
			if (subsystems.liftSystem.homeLift()) step++;
			break;
		case 9:
			// Stop
			subsystems.driveSystem.setBraking(false);
			subsystems.shifterPneumaticSystem.shiftDown();
			return true;
		}
		return false;
	}
	
	private boolean driveToRight() {
		switch (step) {	// Step is protected by the abstract class
		case 0:
			// Drive forward 91.44 cm (3 ft)
			subsystems.liftSystem.setLiftPosition_presetNum(3);
			if (subsystems.driveSystem.driveToDistance(-91.44) < 1) step ++;
			break;
		case 1:
			// Turn to -40 degrees
			if (subsystems.driveSystem.driveToAngle(30) < 1) step ++;
			break;
		case 2:
			//-96.68
			if (subsystems.driveSystem.driveToDistance(-96.68) < 1) step ++;
			break;
		case 3:
			// Turn to 0 degrees
			if (subsystems.driveSystem.driveToAngle(0) < 1) step++;
			break;
		case 4:
			subsystems.driveSystem.drive4Motors(0, 0);
			step++;
			break;
		case 5:
			// Drive forward 60.96 cm (3 ft)
			if (subsystems.liftSystem.getLiftOffset() > 200) break;	// check if the lift is in the correct position (to avoid damage)
			if (subsystems.driveSystem.driveToDistance(-70/*-60.96*/, 1000) < 1) step++;
			break;
		case 6:
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
		case 7:
			// Backup
			if (subsystems.driveSystem.driveToDistance(91.44) < 1) step++;
			break;
		case 8:
			subsystems.driveSystem.drive4Motors(0, 0);
			// Home the lift
			if (subsystems.liftSystem.homeLift()) step++;
			break;
		case 9:
			// Stop
			subsystems.driveSystem.setBraking(false);
			subsystems.shifterPneumaticSystem.shiftDown();			
			return true;
		}
		return false;
	}
	
}
