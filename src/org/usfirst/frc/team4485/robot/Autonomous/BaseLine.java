package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class BaseLine extends AutoProgram {

	public BaseLine(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void run() {
		subsystems.shifterPneumaticSystem.shiftDown();
		subsystems.driveSystem.setBraking(true);
		if (subsystems.driveSystem.driveToDistance(-365.76, 30000) < 1) auto_complete = true;
		subsystems.driveSystem.setBraking(false);
		subsystems.driveSystem.update();
		subsystems.shifterPneumaticSystem.update();
	}

}
