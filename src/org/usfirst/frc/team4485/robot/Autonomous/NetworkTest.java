package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4485.robot.Subsystems.NetworkController;

public class NetworkTest extends AutoProgram {

	NetworkController ncon;
	
	public NetworkTest(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		ncon = new NetworkController();
		Thread nconThread = new Thread(ncon);
		nconThread.start();
		//SmartDashboard.putString("Network RCVD", "");
	}

	@Override
	protected void run() {
		// TODO Auto-generated method stub
		//System.out.println("RCVD: " + ncon.getRcvd());
		SmartDashboard.putNumber("rcvd", ncon.getNumRcvd());
		SmartDashboard.putString("rcvd data", ncon.getRcvd());
	}

}
