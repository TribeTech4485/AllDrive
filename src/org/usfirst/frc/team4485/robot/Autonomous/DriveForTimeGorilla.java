package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Robot;
import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveForTimeGorilla extends AutoProgram{

	double driveDistanceCm;
	
	public DriveForTimeGorilla(SubsystemsControl _subsystems) {
		super(_subsystems);
	}

	@Override
	protected void init() {
		//auto_timeLimit = 3000;
	}

	double distanceCm = 914.4;
	
	@Override
	protected void run() {
		driveDistanceCm = Robot.sensorController.getLeftOffset_cm();
		
		subsystems.driveSystem.setBraking(true);
		
		if(driveDistanceCm < distanceCm) {
			//subsystems.driveSystem.drive4Motors(1, 1);
		} else if(driveDistanceCm >= distanceCm) {
			subsystems.driveSystem.drive4Motors(0.0, 0.0);
		}
		
		SmartDashboard.putNumber("Distance", driveDistanceCm);
		//System.out.println("DRIVE DISTANCE in CM: " + driveDistanceCm);
		subsystems.driveSystem.update();
	}
}