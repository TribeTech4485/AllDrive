package org.usfirst.frc.team4485.robot;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleOpControl {
	private RobotIndexing id;
	
	private UserControl userControl;
	private SubsystemsControl subsystems;
	
	private boolean useBoard = true;
	
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
		//if (userControl.getRawDriveButton(id.d_shiftDown)) subsystems.shifterPneumaticSystem.shiftDown();
		//else if (userControl.getRawDriveButton(id.d_shiftUp)) subsystems.shifterPneumaticSystem.shiftUp();
		
		// Drive Information on SmartDashboard
		SmartDashboard.putNumber("Right Drive", userControl.drive_rightStickY);
		SmartDashboard.putNumber("Left Drive", userControl.drive_leftStickY);
		if (!useBoard) {
			// Control the collector
			subsystems.collectorSystem.setArms(userControl.getRawControlButton(id.c_collectorArmToggle));
			subsystems.collectorSystem.setExpel(userControl.getRawControlButton(id.c_collectorExpelButton));
			subsystems.collectorSystem.setIntake(userControl.getRawControlButton(id.c_collectorIntakeButton));
			subsystems.collectorSystem.setSpin(userControl.getRawControlButton(id.c_collectorSpinButton));
			
			if (userControl.getRawControlButton(id.c_liftHomeButton)) subsystems.liftSystem.homeLift();
			if (userControl.getRawControlButton(id.c_liftPos2Button)) subsystems.liftSystem.setLiftPosition_presetNum(3);
			if (userControl.getRawControlButton(id.c_liftPos3Button)) subsystems.liftSystem.setLiftPosition_presetNum(6);
			
			subsystems.liftSystem.setLiftPIDOverride(false);	//Set this to true if PID is no wanted
			subsystems.liftSystem.setLift(userControl.getAxis(id.controlController, id.c_liftAxis));
		} else {
			// Collector Control
			subsystems.collectorSystem.setArms(userControl.getRawBoardButton(id.b_armOutButton));
			subsystems.collectorSystem.setExpel(userControl.getRawBoardButton(id.b_collectorExpelButton));
			subsystems.collectorSystem.setIntake(userControl.getRawBoardButton(id.b_collectorIntakeButton));
			if (userControl.getRawBoardButton(id.b_collectorExpelButton) && userControl.getRawBoardButton(id.b_collectorIntakeButton)) subsystems.collectorSystem.setSpin(true);
			else subsystems.collectorSystem.setSpin(false);
			
			subsystems.liftSystem.setLiftPIDOverride(false);
			if (userControl.getRawBoardButton(id.b_liftHomeButton)) subsystems.liftSystem.homeLift();
			else if (userControl.getRawBoardButton(id.b_liftPos1Button)) subsystems.liftSystem.setLiftPosition_presetNum(3);
			else if (userControl.getRawBoardButton(id.b_liftPos2Button)) subsystems.liftSystem.setLiftPosition_presetNum(4);
			else if (userControl.getRawBoardButton(id.b_liftPos3Button)) subsystems.liftSystem.setLiftPosition_presetNum(5);
			else if (userControl.getRawBoardButton(id.b_liftPos4Button)) subsystems.liftSystem.setLiftPosition_presetNum(6);
			//else if (userControl.getRawBoardButton(id.b_liftPos5Button)) subsystems.liftSystem.setLiftPosition_presetNum(7);
			else {
				subsystems.liftSystem.setLiftPIDOverride(true);
				subsystems.liftSystem.setLift(0);
			}
		}
		
		
		// Update systems
		//subsystems.driveSystem.setBraking(userControl.getRawDriveButton(id.d_brakeButton));
		// Update the drive system
		//subsystems.shifterPneumaticSystem.setAutoShift(true);
		subsystems.shifterPneumaticSystem.setAutoShift(false);
		if (userControl.getRawDriveButton(id.d_shiftUp)) subsystems.shifterPneumaticSystem.shiftUp();
		else if (userControl.getRawDriveButton(id.d_shiftDown)) subsystems.shifterPneumaticSystem.shiftDown();
		subsystems.shifterPneumaticSystem.update();
		subsystems.driveSystem.update();
		
		subsystems.collectorSystem.update();
		subsystems.liftSystem.update();
		
		SmartDashboard.putNumber("Lift Offset", subsystems.liftSystem.getLiftOffset());
		SmartDashboard.putNumber("Lift Position", subsystems.liftSystem.getPosition());
		
		SmartDashboard.putNumber("Drive AVG Rpm", Robot.sensorController.getAverageRPM());
		
		// Make the drive controller rumble with drive amount
		//userControl.rumbleController(id.driveController, (Math.abs(userControl.drive_leftStickY) + Math.abs(userControl.drive_rightStickY)) / 2);
		//testPrint();
		

	}
}