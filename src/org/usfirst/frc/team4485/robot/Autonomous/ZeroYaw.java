package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Robot;
import org.usfirst.frc.team4485.robot.Autonomous.AutoProgram;
import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class ZeroYaw extends AutoProgram {

	boolean yawZeroed = false;
	
	public ZeroYaw(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void run() {
		SmartDashboard.putNumber("GYRO Yaw", Robot.sensorController.ahrs.getYaw());
		yawZeroed = Robot.sensorController.zeroAHRSYaw();
		if (yawZeroed) {
			SmartDashboard.putString("YawZeroed", "yes");
			auto_complete = true;
		} else {
			SmartDashboard.putString("YawZeroed", "no");
		}
	}
	
}