package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class DriveToRight_Center extends AutoProgram {

	public DriveToRight_Center(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
	}

	double startTime = -1;
	
	@Override
	protected void run() {
		subsystems.driveSystem.setBraking(true);
		subsystems.shifterPneumaticSystem.shiftDown();
		switch (step) {	// Step is protected by the abstract class
		case 0:
			// Drive forward 91.44 cm (3 ft)
			if (subsystems.driveSystem.driveToDistance(-91.44) < 1) step ++;
			break;
		case 1:
			// Turn to -40 degrees
			if (subsystems.driveSystem.driveToAngle(-30) < 1) step ++;
			break;
		case 2:
			//-96.68
			if (subsystems.driveSystem.driveToDistance(-96.68) < 1) step ++;
			break;
		case 3:
			// Turn to 0 degrees
			if (subsystems.driveSystem.driveToAngle(2) < 1) step++;
			break;
		case 4:
			// Drive forward 60.96 cm (3 ft)
			if (subsystems.driveSystem.driveToDistance(-91.44) < 1) step++;
			break;
		case 5:
			subsystems.collectorSystem.setExpel(true);
			if (startTime < 0) startTime = System.currentTimeMillis();
			if (System.currentTimeMillis() - startTime > 250) {
				subsystems.collectorSystem.setExpel(false);
				startTime = -1;
				step++;
			}
		case 6:
			subsystems.driveSystem.setBraking(false);
			subsystems.shifterPneumaticSystem.shiftDown();			
			auto_complete = true;
			break;
		}
		subsystems.driveSystem.update();
		subsystems.shifterPneumaticSystem.update();
	}

}
