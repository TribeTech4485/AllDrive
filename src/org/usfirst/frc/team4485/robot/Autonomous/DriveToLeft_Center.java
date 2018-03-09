package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Robot;
import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

	double startTime = -1;
	
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
			auto_complete = true;
			break;
		}
		subsystems.driveSystem.update();
		subsystems.shifterPneumaticSystem.update();
		subsystems.collectorSystem.update();
	}

}
