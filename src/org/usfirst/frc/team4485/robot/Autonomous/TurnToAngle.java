package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class TurnToAngle extends AutoProgram {

	public TurnToAngle(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		//auto_timeLimit = 30000; // Stop after 30 seconds no matter what
	}

	@Override
	protected void run() {
		// TODO Auto-generated method stub
		//if (!subsystems.driveSystem.turnToAngle(60)) {
		//	auto_complete = true;
		//}
		
		if (!subsystems.driveSystem.turnToAnglePID(90)) {
			auto_complete = true;
		}
		subsystems.driveSystem.update();
	}
	
}