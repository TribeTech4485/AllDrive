// Class for getting user input from the drive and control controllers
package org.usfirst.frc.team4485.robot;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class UserControl {
	private RobotIndexing id;
	
	public UserControl() {
		id = new RobotIndexing();
		
		driveController = new Joystick(id.driveController);
		controlController = new Joystick(id.controlController);
		
		for (int i = 0; i < 9; i++) {
			lastControlButtonState[i] = false;
		}
	}
	
	private Joystick driveController;
	private Joystick controlController;
	private static double kDeadBand = 0.13;
	
	// Boolean array for debounce
	private boolean[] lastControlButtonState = new boolean[9];
	//private boolean[] lastDriveButtonState = new boolean[9];
	
	//// Public controller values
	// Drive Controller
	public double drive_rightStickY;
	public double drive_leftStickY;
	
	// Private rumble values
	private int rumbleStickID;
	private int rumbleType;
	private double rumbleVal = 0.0;
	private static int kRumbleSolid = 1, kRumbleDash = 300, kRumbleBeep = 1000;
	// Timing for rumble patterns
	private double rumbleStartTime = -1;
	
	////Public functions
	public void updateControls() {
		updateAxis();
		publishControls();
	}
	public boolean getRawControlButton(int button) {
		if (button < 1) return false;
		return controlController.getRawButton(button);
	}
	public boolean getRawDriveButton(int button) {
		if (button < 1) return false;
		return driveController.getRawButton(button);
	}
	// Debounce function for control buttons
	public boolean getDebounceControlButton(int button) {
		boolean currentButtonState = getRawControlButton(button);
		boolean registerDebouncePress = false;
		if (currentButtonState != lastControlButtonState[button - 1] && currentButtonState) {
			registerDebouncePress = true;
		}
		lastControlButtonState[button - 1] = currentButtonState;
		return registerDebouncePress;
	}
	// Debounce function for drive buttons
	public boolean getDebounceDriveButton(int button) {
		return false;	// Don't use right now
		/*boolean currentButtonState = getRawControlButton(button);
		boolean registerDebouncePress = false;
		if (currentButtonState != lastDriveButtonState[button - 1] && currentButtonState) {
			registerDebouncePress = true;
		}
		lastDriveButtonState[button - 1] = currentButtonState;
		return registerDebouncePress;*/
	}
	public double getAxis(int joystickID, int axisID) {
		Joystick tempStick = new Joystick(joystickID);
		double axisV = tempStick.getRawAxis(axisID);
		
		// Adjust for dead band
		if (axisV > -kDeadBand && axisV < kDeadBand) axisV = 0.0;
		return axisV;
	}
	public double getPOV(int joystickID) {
		Joystick tempStick = new Joystick(joystickID);
		double pov = tempStick.getPOV();
		return pov;
	}
	public void rumbleController(int joystickID, double rumble) {
		Joystick tempStick = new Joystick(joystickID);
		rumble = Math.abs(rumble);
		tempStick.setRumble(RumbleType.kLeftRumble, rumble);
		tempStick.setRumble(RumbleType.kRightRumble, rumble);
	}
	////
	
	////Private functions
	private void updateAxis() {
		drive_rightStickY = driveController.getRawAxis(5);
		drive_leftStickY = driveController.getRawAxis(1);
		
		// Adjust for dead band
		if (drive_rightStickY > -kDeadBand && drive_rightStickY < kDeadBand) drive_rightStickY = 0.0;
		if (drive_leftStickY > -kDeadBand && drive_leftStickY < kDeadBand) drive_leftStickY = 0.0;
	}
	private void updateRumble() {
		Joystick tempStick = new Joystick(rumbleStickID);
		if (rumbleStartTime < 0) rumbleStartTime = System.currentTimeMillis();
		
		double delayTime = -1;
		
		if (rumbleType == kRumbleDash) delayTime = kRumbleDash;
		if (delayTime < 0) return;
		if (System.currentTimeMillis() - rumbleStartTime >= delayTime) {
			rumbleStartTime = -1;
			if (rumbleVal == 0.0) rumbleVal = 1.0;
			else if (rumbleVal > 0) rumbleVal = 0.0;
		}
	}
	private void publishControls() {
		SmartDashboard.putNumber("RightStick Y", drive_rightStickY);
		SmartDashboard.putNumber("LeftStick Y", drive_leftStickY);
	}
	////
}