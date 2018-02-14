package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class DriveForTimeGorilla extends AutoProgram{

	double driveDistanceTicks = 0.0, driveDistanceCm;
	
	public DriveForTimeGorilla(SubsystemsControl _subsystems) {
		super(_subsystems);
	}

	@Override
	protected void init() {
		auto_timeLimit = 3000;
	}

	@Override
	protected void run() {

		if(System.currentTimeMillis()-auto_startTime<1000) {
			subsystems.driveSystem.drive4Motors(0.2, 0.2);
		}else if(System.currentTimeMillis()-auto_startTime>1000) {
			subsystems.driveSystem.drive4Motors(.4, .4);
		}

		subsystems.driveSystem.update();
		driveDistanceCm = subsystems.driveSystem.getDriveDistance();
		System.out.println("DRIVE DISTANCE in CM: " + driveDistanceCm);
	}
}