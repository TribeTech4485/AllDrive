package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiftAutoTest extends AutoProgram {

	public LiftAutoTest(SubsystemsControl _subsystems) {
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
			// Move the lift to position 1
			subsystems.liftSystem.setLiftPosition_presetNum(2);
			SmartDashboard.putNumber("Lift Offset", subsystems.liftSystem.getLiftOffset());
			if (subsystems.liftSystem.getLiftOffset() < 200) step++;
			break;
		case 1:
			if (subsystems.liftSystem.homeLift()) step++;
			//if (subsystems.liftSystem.getLiftOffset() < 200) step++;
			break;
		case 2:
			auto_complete = true;
			break;
		}
		subsystems.liftSystem.update();
	}

}
