package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveDistanceTest extends AutoProgram {

	public DriveDistanceTest(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void run() {
		// TODO Auto-generated method stub
		double distanceOffset = subsystems.driveSystem.driveToDistance(-254);
		if (distanceOffset < 1) auto_complete = true;
		SmartDashboard.putNumber("Distance Offset", distanceOffset);
		subsystems.driveSystem.update();
	}

}
