package org.usfirst.frc.team4485.robot;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleOpControl {
	private RobotIndexing id;
	
	private UserControl userControl;
	private SubsystemsControl subsystems;
	
	public TeleOpControl(SubsystemsControl _subsystems, UserControl _userControl) {
		id = new RobotIndexing();
		
		subsystems = _subsystems;
		userControl = _userControl;
	}
	
	public void update() {
		// Update the user control system
		userControl.updateControls();

		if (userControl.getRawControlButton(id.c_liftButton)) {
			subsystems.liftSystem.setLift(userControl.getAxis(id.controlController, id.c_liftAxis)); //sets lift speed to a value between -1 to 1
		} else {
		
			// Drive using the drive subsystem
			//subsystems.driveSystem.drive4Motors(userControl.drive_leftStickY, userControl.drive_rightStickY);
			subsystems.driveSystem.drive4Motors(userControl.drive_leftStickY, userControl.drive_rightStickY); 	// Flip the input sticks for some robots
			if (userControl.getRawDriveButton(id.d_shiftDown)) subsystems.shifterPneumaticSystem.shiftDown();
			else if (userControl.getRawDriveButton(id.d_shiftUp)) subsystems.shifterPneumaticSystem.shiftUp();
		}
		// Drive Information on SmartDashboard
		SmartDashboard.putNumber("Right Drive", userControl.drive_rightStickY);
		SmartDashboard.putNumber("Left Drive", userControl.drive_leftStickY);
		// Control the collector
		subsystems.collectorSystem.setArms(userControl.getRawControlButton(id.c_collectorArmToggle));
		subsystems.collectorSystem.setExpel(userControl.getRawControlButton(id.c_collectorExpelButton));
		subsystems.collectorSystem.setIntake(userControl.getRawControlButton(id.c_collectorIntakeButton));
		subsystems.collectorSystem.update();
		
		//subsystems.driveSystem.setBraking(userControl.getRawDriveButton(id.d_brakeButton));
		// Update the drive system
		//subsystems.shifterPneumaticSystem.setAutoShift(true);
		subsystems.shifterPneumaticSystem.setAutoShift(true);
		subsystems.shifterPneumaticSystem.update();
		subsystems.driveSystem.update();
		subsystems.liftSystem.update();
		
		SmartDashboard.putNumber("Drive AVG Rpm", Robot.sensorController.getAverageRPM());
		
		// Make the drive controller rumble with drive amount
		//userControl.rumbleController(id.driveController, (Math.abs(userControl.drive_leftStickY) + Math.abs(userControl.drive_rightStickY)) / 2);
		//testPrint();
		

	}
}