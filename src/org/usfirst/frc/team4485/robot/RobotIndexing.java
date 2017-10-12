package org.usfirst.frc.team4485.robot;

public class RobotIndexing {
	public RobotIndexing() {
		initPIDVals();
	}
	
	//// Gyro Properties
	public boolean flipGyro = false;
	////
	
	//// Motor indexes
	// Drive motors
	public int rightDriveMotorMaster = 7;
	public int rightDriveMotorSlave = 6;
	public int leftDriveMotorMaster = 5;	// Changed from 9
	public int leftDriveMotorSlave = 8;
	
	// Shooter motors	-	DON'T USE
	public int shooterMotorMaster = 9;
	public int shooterMotorSlave = 4;
	
	// Collector motors
	public int collectorMotor = 3;
	
	// Lift motors
	public int liftMotorMaster = 1;
	public int liftMotorSlave = 2;
	
	// Hopper motors
	public int hopperMotor = 10;
	////
	
	//// Controller indexes
	public int driveController = 0;
	public int controlController = 1;
	
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
	public int d_brakeButton = 5;	// Left Shoulder Button
	////
	
	//// Sensor Indexes
	// Analog inputs
	public int gearOpticalSensor = 3;
	public int cameraDistanceSensor = 0;
	////
	
	//// Pnewmatic indexes
	// Box Guide Solenoids & Modules
	public int guideSolenoidsModule = 0;
	public int boxGuideSolenoid1_out = 1;
	public int boxGuideSolenoid1_in = 0;
	public int boxGuideSolenoid2_out = 4;
	public int boxGuideSolenoid2_in = 5;
	
	// Box Pusher Solenoids & Modules
	public int pusherSolenoidsModule = 0;
	public int pusherSolenoid_out = 3;
	public int pusherSolenoid_in = 2;
	public int doorSolenoid_out = 7;
	public int doorSolenoid_in = 6;
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