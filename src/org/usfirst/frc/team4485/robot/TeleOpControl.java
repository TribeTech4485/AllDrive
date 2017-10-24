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
				
		// Drive using the drive subsystem
		//subsystems.driveSystem.drive4Motors(userControl.drive_leftStickY, userControl.drive_rightStickY);
		subsystems.driveSystem.drive4Motors(userControl.drive_leftStickY, userControl.drive_rightStickY); 	// Flip the input sticks for some robots
		if (userControl.getRawDriveButton(id.d_shiftDown)) subsystems.shifterPneumaticSystem.shiftDown();
		else if (userControl.getRawDriveButton(id.d_shiftUp)) subsystems.shifterPneumaticSystem.shiftUp();
		//subsystems.driveSystem.setBraking(userControl.getRawDriveButton(id.d_brakeButton));
		// Update the drive system
		//subsystems.shifterPneumaticSystem.setAutoShift(true);
		subsystems.shifterPneumaticSystem.update();
		subsystems.driveSystem.update();
		
		// Make the drive controller rumble with drive amount
		//userControl.rumbleController(id.driveController, (Math.abs(userControl.drive_leftStickY) + Math.abs(userControl.drive_rightStickY)) / 2);
		
		// Control the box pneumatics
		// Cntrol the guide
		if (userControl.getRawControlButton(id.c_guideOutButton)) subsystems.boxPneumaticSystem.setGuideOut(true);
		else if (userControl.getRawControlButton(id.c_guideInButton)) subsystems.boxPneumaticSystem.setGuideOut(false);
		
		// Control the door
		if (userControl.getRawControlButton(id.c_doorOutButton)) subsystems.boxPneumaticSystem.setDoorOut(true);
		else if (userControl.getRawControlButton(id.c_doorInButton)) subsystems.boxPneumaticSystem.setDoorOut(false);
		
		// Control the pusher
		// Only set it out here because the subsystem will bring it in when its clear
		if (userControl.getRawControlButton(id.c_pusherOutButton)) subsystems.boxPneumaticSystem.setPusherOut(true);
		
		// Expel the gear
		if (userControl.getRawControlButton(id.c_expelGearButton)) subsystems.boxPneumaticSystem.expel();
		// Update the box pneumatic system
		subsystems.boxPneumaticSystem.update();
		
		
		// Control the lifter
		subsystems.liftSystem.setLift(userControl.getRawControlButton(id.c_liftButton));
		subsystems.liftSystem.setPower(userControl.getAxis(id.controlController, id.c_liftAxis));
		subsystems.liftSystem.update();
		
		// Rumble the controllers depending on the state of the robot
		//if (subsystems.boxPneumaticSystem.getDoorState()) userControl.rumbleController(id.controlController, 0.25);
		//else userControl.rumbleController(id.controlController, 0);
		if (subsystems.boxPneumaticSystem.getDoorState()) userControl.rumbleController(userControl.kRumbleDash, id.controlController);
		else userControl.rumbleController(userControl.kRumbleNone, id.controlController);
		
		testPrint();
	}
	
	private void testPrint() {
		//System.out.println("TeleOpControl Test Print");
	}
}