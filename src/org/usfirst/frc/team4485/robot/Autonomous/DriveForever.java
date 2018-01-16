package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveForever extends AutoProgram {

	public DriveForever(SubsystemsControl _subsystems) {
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
		subsystems.serialSystem.update();
		subsystems.driveSystem.drive4Motors(1, 1);
	}
	
}