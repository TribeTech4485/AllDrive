package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class I2CTestAuto extends AutoProgram {

	I2C port;
	
	public I2CTestAuto(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		port = new I2C(I2C.Port.kOnboard, 8);
	}

	@Override
	protected void run() {
		
	}

}
