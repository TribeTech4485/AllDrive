package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SensorsTest extends AutoProgram {

	public SensorsTest(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void run() {
		subsystems.liftSystem.setLiftPIDOverride(true);
		subsystems.liftSystem.update();
		SmartDashboard.putNumber("Lift Position", subsystems.liftSystem.getPosition());
	}

}
