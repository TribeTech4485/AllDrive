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
		
	}

	@Override
	protected void run() {
		SmartDashboard.putString("FMS Message", gameSpecificMessage);
		subsystems.driveSystem.setBraking(true);
		subsystems.shifterPneumaticSystem.shiftDown();
		if (gameSpecificMessage.charAt(0) == 'R') {
			auto_complete = driveToRight();
		} else if (gameSpecificMessage.charAt(0) == 'L') {
			auto_complete = driveToLeft();
		}
		subsystems.driveSystem.update();
		subsystems.shifterPneumaticSystem.update();
		subsystems.collectorSystem.update();
	}
	
	double startTime = -1;
	
	private boolean driveToLeft() {
		switch (step) {
		case 0:
			if (subsystems.driveSystem.driveToDistance(-91.44) < 1) step++;
			break;
		case 1:
			if (subsystems.driveSystem.driveToAngle(72) < 1) step++;
			break;
		case 2:
			if (subsystems.driveSystem.driveToDistance(-248.92) < 1) step++;
			break;
		case 3:
			if (subsystems.driveSystem.driveToAngle(-2) < 1) step++;
			break;
		case 4:
			// -121.92 cm
			if (subsystems.driveSystem.driveToDistance(-91.92) < 1) step++;
			break;
		case 5:
			subsystems.collectorSystem.setExpel(true);
			if (startTime < 0) startTime = System.currentTimeMillis();
			if (System.currentTimeMillis() - startTime > 250) {
				SmartDashboard.putNumber("Time", System.currentTimeMillis() - startTime);
				subsystems.collectorSystem.setExpel(false);
				startTime = -1;
				step++;
			}
		case 6:
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
			if (subsystems.driveSystem.driveToDistance(-91.44) < 1) step ++;
			break;
		case 1:
			// Turn to -40 degrees
			if (subsystems.driveSystem.driveToAngle(-30) < 1) step ++;
			break;
		case 2:
			//-96.68
			if (subsystems.driveSystem.driveToDistance(-96.68) < 1) step ++;
			break;
		case 3:
			// Turn to 0 degrees
			if (subsystems.driveSystem.driveToAngle(2) < 1) step++;
			break;
		case 4:
			// Drive forward 60.96 cm (3 ft)
			if (subsystems.driveSystem.driveToDistance(-91.44) < 1) step++;
			break;
		case 5:
			subsystems.collectorSystem.setExpel(true);
			if (startTime < 0) startTime = System.currentTimeMillis();
			if (System.currentTimeMillis() - startTime > 250) {
				subsystems.collectorSystem.setExpel(false);
				startTime = -1;
				step++;
			}
		case 6:
			subsystems.driveSystem.setBraking(false);
			subsystems.shifterPneumaticSystem.shiftDown();			
			return true;
		}
		return false;
	}
	
}
