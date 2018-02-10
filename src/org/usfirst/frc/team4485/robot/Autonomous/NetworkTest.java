package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NetworkTest extends AutoProgram {
	
	public NetworkTest(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void run() {
		subsystems.cubeTrackingSystem.update();
		SmartDashboard.putString("Network Data", subsystems.cubeTrackingSystem.getData());
	}

}
