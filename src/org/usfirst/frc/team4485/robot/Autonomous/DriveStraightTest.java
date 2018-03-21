package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class DriveStraightTest extends AutoProgram {

	public DriveStraightTest(SubsystemsControl _subsystems) {
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
		subsystems.driveSystem.setBraking(true);
		if (subsystems.driveSystem.driveToDistanceStraight(-800) < 1) auto_complete = true;
		subsystems.driveSystem.update();
	}

}
