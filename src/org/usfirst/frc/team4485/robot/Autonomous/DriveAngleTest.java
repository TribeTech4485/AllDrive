package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class DriveAngleTest extends AutoProgram {

	public DriveAngleTest(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	int step = 0;
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		step = 0;
	}
	
	@Override
	protected void run() {
		// TODO Auto-generated method stub
		if (subsystems.driveSystem.driveToAngle(90) < 1) auto_complete = true;
		subsystems.driveSystem.update();
	}

}
