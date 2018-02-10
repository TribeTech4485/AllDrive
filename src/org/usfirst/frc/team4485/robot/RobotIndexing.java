package org.usfirst.frc.team4485.robot;

public class RobotIndexing {
	public RobotIndexing() {
		initPIDVals();
	}
	
	//// GYRO Properties
	public boolean flipGyro = false;
	////
	
	//// Motor indexes
	// Drive motors
	public int rightDriveMotorMaster = 1;
	public int rightDriveMotorSlave = 2;
	public int leftDriveMotorMaster = 3;
	public int leftDriveMotorSlave = 4;
	// Lift Motors
	public int liftMotor = 5;
	// Arm Motors
	public int armMotorLeft = 6;
	public int armMotorRight = 7;
	// Claw Motors
	public int clawMotorLeft = 8;
	public int clawMotorRight = 9;
	
	// PDP power channels for the motors
	public int rightDriveMotorMasterPDP = 0;
	public int rightDriveMotorSlavePDP = 1;
	public int leftDriveMotorMasterPDP = 2;
	public int leftDriveMotorSlavePDP = 3;
	public int liftMotorPDP = 4;
	
	//// Controller indexes
	public int driveController = 1;
	public int controlController = 0;
	
	// Button indexes
	public int c_shootButton = 0;	// 0 is unassigned
	public int c_collectButton = 0;	// 0 is unassigned
	public int c_guideInButton = 3;
	public int c_guideOutButton = 4;
	public int c_doorOutButton = 1;
	public int c_doorInButton = 2;
	public int c_pusherOutButton = 5;
	public int c_expelGearButton = 10;
	
	public int c_hopperButton = 0;	// 0 is unassigned
	public int c_liftAxis = 1;
	public int c_liftButton = 6;
	
	public int d_turnToAngleButton = 6;
	public int d_brakeButton = 0;//5;	// Left Shoulder Button (Unassigned)
	public int d_shiftUp = 6;		// Right Shoulder Button
	public int d_shiftDown = 5;		// Left Shoulder Button
	////
	
	//// Sensor Indexes
	// Analog inputs
	public int cameraDistanceSensor = 0;
	////
	
	//// Pnewmatic indexes	
	// Shifting Gear Box Solenoids & Modules
	public int shifterSolenoidsModule = 0;
	public int shifterSolenoid_in = 0;
	public int shifterSolenoid_out = 1;
	// Arm Solenoids and Modules
	public int armSolenoidsModule = 0;
	public int armSolenoidLeft_in = 2;
	public int armSolenoidLeft_out = 3;
	public int armSolenoidRight_in = 4;
	public int armSolenoidRight_out = 5;
	////
	
	// PID speed vals
	public double driveMaxRPM = 1500;
	////
	
	//////// PID
	//// Class for PID values
	public class PIDVals {
		public double F = 0.0;
		public double P = 0.0;
		public double I = 0.0;
		public double D = 0.0;
		public void setVals(double f, double p, double i, double d) { F=f; P=p; I=i; D=d; }
	}
	////
	
	//// PIDValues
	public PIDVals shooterPIDVals;
	public PIDVals drivePIDVals;
	////
	
	//// Private Init Functions
	private void initPIDVals() {
		initShooterPIDVals();
		initDrivePIDVals();
	}
	private void initShooterPIDVals() {
		shooterPIDVals = new PIDVals();
		shooterPIDVals.setVals(0.0, 0.0, 0.0, 0.0);
	}
	private void initDrivePIDVals() {
		drivePIDVals = new PIDVals();
		drivePIDVals.setVals(0.0,1.5,0.0001,4.5);
	}
	////
	////////
	
	//////// Subsystem ID
	//// For Error Identification
	private int systemIDit = 0;
	public int getNextSystemID() {
		return systemIDit++;
	}
	////
	////////
}