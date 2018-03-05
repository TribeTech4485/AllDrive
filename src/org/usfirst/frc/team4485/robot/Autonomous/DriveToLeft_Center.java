package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Robot;
import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class DriveToLeft_Center extends AutoProgram {

	public DriveToLeft_Center(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		Robot.sensorController.zeroAHRSYaw();
	}

	@Override
	protected void run() {
		// TODO Auto-generated method stub
		subsystems.driveSystem.setBraking(true);
		subsystems.shifterPneumaticSystem.shiftDown();
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
			if (subsystems.driveSystem.driveToDistance(-121.92) < 1) step++;
			break;
		case 5:
			subsystems.driveSystem.setBraking(false);
			subsystems.shifterPneumaticSystem.shiftDown();
			auto_complete = true;
			break;
		}
		subsystems.driveSystem.update();
		subsystems.shifterPneumaticSystem.update();
	}

}
