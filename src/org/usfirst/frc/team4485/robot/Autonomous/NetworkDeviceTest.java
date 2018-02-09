package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NetworkDeviceTest extends AutoProgram {
	
	public NetworkDeviceTest(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void run() {
		// Put the data from the network device on the smart dashboard
		subsystems.cameraSystem.update();
		SmartDashboard.putString("Network Device Parsed Data", subsystems.cameraSystem.getParsedData());
		SmartDashboard.putString("Network Device Raw Data", subsystems.cameraSystem.getRawData());
	}

}
