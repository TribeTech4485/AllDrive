package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GyroTest extends AutoProgram {

	public GyroTest(SubsystemsControl _subsystems) {
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
		
		//subsystems.sensorController.zeroAHRSYaw();
		// TODO Auto-generated method stub
		subsystems.driveSystem.setBraking(false);
		subsystems.shifterPneumaticSystem.shiftUp();
		subsystems.driveSystem.update();
		subsystems.shifterPneumaticSystem.update();
		SmartDashboard.putNumber("Raw Yaw", subsystems.sensorController.getRawAHRSYaw());
		SmartDashboard.putNumber("Yaw", subsystems.sensorController.getAHRSYaw());
	}

}
