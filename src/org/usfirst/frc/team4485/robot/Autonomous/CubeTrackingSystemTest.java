package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class CubeTrackingSystemTest extends AutoProgram {

	double startAngle = 0.0;
	
	public CubeTrackingSystemTest(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void run() {
		// Set the start angle
		startAngle = subsystems.sensorController.getAHRSYaw();
		double angleOffset = subsystems.cubeTrackingSystem.getAngleToCube();
		double angleToMoveTo = startAngle + angleOffset;
		
		if (angleOffset <= 180) {	// Check if the offset is in bounds
			// Drive the Robot
			subsystems.driveSystem.driveToAngle(angleToMoveTo);
		}
		subsystems.shifterPneumaticSystem.shiftUp();
		subsystems.shifterPneumaticSystem.update();
		subsystems.driveSystem.update();
	}
	
}