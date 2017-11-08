package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class DriveDistance extends AutoProgram {

	public DriveDistance(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		auto_timeLimit = -1;
	}

	@Override
	protected void run() {
		// TODO Auto-generated method stub
		subsystems.driveSystem.setBraking(true);
		
		//subsystems.driveSystem.drive4Motors(0.25, 0.25);
		if (subsystems.driveSystem.driveDistance(0.25,0.25,6)) {
			subsystems.driveSystem.drive4Motors(0,0);
			auto_complete = true;
			//subsystems.driveSystem.setBraking(false);
		}
		subsystems.driveSystem.update();
	}
	
}