package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class DriveForTimeGorilla extends AutoProgram{

	public DriveForTimeGorilla(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		auto_timeLimit = 3000;
	}

	@Override
	protected void run() {
		subsystems.driveSystem.drive4Motors(0.2, 0.2);
		subsystems.driveSystem.update();
	}

}
