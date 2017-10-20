package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class DriveToZone extends AutoProgram {

	public DriveToZone(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		auto_timeLimit = 2500;
	}

	@Override
	protected void run() {
		subsystems.driveSystem.centerToAnglePID(0);
		subsystems.driveSystem.update();
		subsystems.driveSystem.drive4Motors(0.5, 0.5);
		subsystems.driveSystem.update();
		//auto_complete = subsystems.driveSystem.driveDistance(1.0, 1.0, 120);
		subsystems.driveSystem.centerToAnglePID(0);
		subsystems.driveSystem.update();
	}
	
}