package org.usfirst.frc.team4485.robot;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class TeleOpControl {
	//private RobotIndexing id;
	
	private UserControl userControl;
	private SubsystemsControl subsystems;
	
	public TeleOpControl(SubsystemsControl _subsystems, UserControl _userControl) {
		//id = new RobotIndexing();
		
		subsystems = _subsystems;
		userControl = _userControl;
	}
	
	public void update() {
		// Update the user control system
		userControl.updateControls();
				
		// Drive using the drive subsystem
		subsystems.driveSystem.drive4Motors(userControl.drive_leftStickY, userControl.drive_rightStickY);
		// Update the drive system
		subsystems.driveSystem.update();
		
		
		testPrint();
	}
	
	private void testPrint() {
		//System.out.println("TeleOpControl Test Print");
	}
}