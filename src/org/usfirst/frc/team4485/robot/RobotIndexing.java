package org.usfirst.frc.team4485.robot;

import org.usfirst.frc.team4485.robot.Subsystems.PIDController.SPID;

public class RobotIndexing {
	//// GYRO Properties
	public boolean flipGyro = false;
	////
	
	//// Motor indexes
	// Drive motors	
	public int leftDriveMotorMaster = 1;
	public int leftDriveMotorSlave1 = 2;
	public int leftDriveMotorSlave2 = 3;
	public int rightDriveMotorMaster = 4;
	public int rightDriveMotorSlave1 = 5;
	public int rightDriveMotorSlave2 = 6;
	
	public int leftDriveMotorMaster_victor = 7;
	public int leftDriveMotorSlave_victor = 6;
	public int rightDriveMotorMaster_victor = 9;
	public int rightDriveMotorSlave_victor = 8;

	// Hopper motor
	public int hopperMotor = 5;
	// Lift Motors
	public int liftMotor = 7;
	// Collector Motors
	public int collectorMotorLeft = 9;
	public int collectorMotorRight = 8;
	// Victors for collector (control them if they are present)
	public int collectorVictorLeft = 8;
	public int collectorVictorRight = 9;
	
	// PDP power channels for the motors
	public int rightDriveMotorMasterPDP = 0;
	public int rightDriveMotorSlave1PDP = 15;
	public int rightDriveMotorSlave2PDP = 13;
	public int leftDriveMotorMasterPDP = 15;
	public int leftDriveMotorSlave1PDP = 1;
	public int leftDriveMotorSlave2PDP = 2;
	public int liftMotorPDP = 8;
	public int armMotorLeftPDP = -1;		// -1 is unassigned
	public int armMotorRightPDP = -1;		// -1 is unassigned
	public int clawMotorRightPDP = -1;		// -1 is unassigned
	public int clawMotorLeftPDP = -1;		// -1 is unassigned
	
	//// Controller indexes
	public int driveController = 0;
	public int controlController = 0;
	public int buttonBoard = 1;
	
	// Button indexes
	public int c_collectorIntakeButton = 1;
	public int c_collectorExpelButton = 2;
	public int c_collectorSpinButton = 3;
	public int c_collectorArmToggle = 4;
	public int c_liftHomeButton = 5;
	public int c_liftPos2Button = 6;
	public int c_liftPos3Button = 9;
	
	public int c_liftAxis = 0;
	
	public int b_collectorIntakeButton = 2;
	public int b_collectorExpelButton = 3;
	public int b_armOutButton = 4;
	
	public int b_liftHomeButton = 11;
	public int b_liftPos1Button = 10;
	public int b_liftPos2Button = 9;
	public int b_liftPos3Button = 8;
	public int b_liftPos4Button = 7;
	public int b_liftPos5Button = 6;
	
	public int b_liftPositionNum = 6;
	public int[] b_liftPositionButtons = {10,9,8,7,6,5};	// Lift position buttons in order from home to top
	
	public int d_turnToAngleButton = 6;
	public int d_brakeButton = 0;//5;	// Left Shoulder Button (Unassigned)
	public int d_alignButton = 1;	// A button
	public int d_shiftUp = 6;		// Right Shoulder Button
	public int d_shiftDown = 5;		// Left Shoulder Button
	public int d_hopperButton = 4; // Y button
	////
	
	//// Sensor Indexes
	// Analog inputs
	public int clawOpticalDistanceSensor_left = 0;
	public int clawOpticalDistanceSensor_right = 1;
	public int armOpticalDistanceSensor = 2;
	public int armUltrasonicDistanceSensor = 3;
	// Digital inputs
	public int liftLowerLimitSwitch = 0;
	public int liftUpperLimitSwitch = 1;
	////
	
	//// Pneumatic indexes	
	// Shifting Gear Box Solenoids & Modules
	public int shifterSolenoidsModule = 0;
	public int shifterSolenoidLeft_in = 2;
	public int shifterSolenoidLeft_out = 3;
	public int shifterSolenoidRight_in = 0;
	public int shifterSolenoidRight_out = 1;
	// Arm Solenoids and Modules
	public int armSolenoidsModule = 0;
	public int armSolenoid_in = 4;
	public int armSolenoid_out = 5;
	public int armSolenoidRight_in = 6;
	public int armSolenoidRight_out = 7;
	// Pneumatic Blast-Off Solenoids and Modules
	public int pneumaticBlastOffModule = 1;
	public int pneumaticBlastOff_in = 1;
	public int pneumaticBlastOff_out = 0;
	////
	
	// PID speed vals
	public double driveMaxRPM = 1500;
	////
	
	//// PIDValues
	public SPID liftSPID;
	////////
}