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
	
	// Button indexes
	public int c_collectorIntakeButton = 1;
	public int c_collectorExpelButton = 2;
	public int c_collectorArmToggle = 4;
	
	public int c_hopperButton = 0;	// 0 is unassigned
	public int c_liftAxis = 1;
	public int c_liftButton = 3;
	
	public int d_turnToAngleButton = 6;
	public int d_brakeButton = 0;//5;	// Left Shoulder Button (Unassigned)
	public int d_shiftUp = 6;		// Right Shoulder Button
	public int d_shiftDown = 5;		// Left Shoulder Button
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
	////
	
	// PID speed vals
	public double driveMaxRPM = 1500;
	////
	
	//// PIDValues
	public SPID liftSPID;
	////////
}