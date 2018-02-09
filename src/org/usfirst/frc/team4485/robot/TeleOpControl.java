package org.usfirst.frc.team4485.robot;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

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

		subsystems.liftSystem.moveTheLift(userControl.getAxis(id.controlController, id.c_liftAxis)); //sets lift speed to a value between -1 to 1

		// Drive using the drive subsystem
		//subsystems.driveSystem.drive4Motors(userControl.drive_leftStickY, userControl.drive_rightStickY);
		subsystems.driveSystem.drive4Motors(userControl.drive_leftStickY, userControl.drive_rightStickY); 	// Flip the input sticks for some robots
		if (userControl.getRawDriveButton(id.d_shiftDown)) subsystems.shifterPneumaticSystem.shiftDown();
		else if (userControl.getRawDriveButton(id.d_shiftUp)) subsystems.shifterPneumaticSystem.shiftUp();
		
		//subsystems.driveSystem.setBraking(userControl.getRawDriveButton(id.d_brakeButton));
		// Update the drive system
		//subsystems.shifterPneumaticSystem.setAutoShift(true);
		subsystems.shifterPneumaticSystem.setAutoShift(false);
		subsystems.shifterPneumaticSystem.update();
		subsystems.driveSystem.update();
		subsystems.liftSystem.update();
		
		// Make the drive controller rumble with drive amount
		//userControl.rumbleController(id.driveController, (Math.abs(userControl.drive_leftStickY) + Math.abs(userControl.drive_rightStickY)) / 2);
		//testPrint();
		

	}
}