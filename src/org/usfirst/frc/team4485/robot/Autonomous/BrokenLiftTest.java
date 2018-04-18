package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class BrokenLiftTest extends AutoProgram {

	public BrokenLiftTest(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void run() {
		switch (step) {
		case 0:
			subsystems.liftSystem.setLiftPosition_presetNum(3);
			step++;
			break;
		case 1:
			break;
		case 2:
			subsystems.liftSystem.homeLift();
			step++;
			break;
		}
		
		subsystems.liftSystem.update();
		
	}

}
