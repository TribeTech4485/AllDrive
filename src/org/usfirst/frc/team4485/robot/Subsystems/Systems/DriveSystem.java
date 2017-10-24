package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Robot;
import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;
import org.usfirst.frc.team4485.robot.Subsystems.PIDController.PIDController;
import org.usfirst.frc.team4485.robot.Subsystems.PIDController.SPID;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


// TODO: Test all drive functions


public class DriveSystem extends Subsystem {

	// Robot Objects (Motors, sensors, etc.)
	private PIDController pid;
	private CANTalon rightMotorMaster, rightMotorSlave;
	private CANTalon leftMotorMaster, leftMotorSlave;
	
	// Control variables for the motors
	private double rightDriveSetVal = 0.0, leftDriveSetVal = 0.0;
	private double speedModPercent = 1.0;
	private boolean brakingEnabled = false;
	
	//Values for motor update delay
	double motorUpdateStart = -1, motorUpdateDelayDuration = -1;
	
	// enums for control types
	public enum ControlType {Percentage, Speed};
	private ControlType driveControlType = ControlType.Percentage;
	
	// Max Speed variables for driving with PID with a percentage input (-1.0 to 1.0)
	private double driveMaxSpeed = 500;	// Set the max speed to x number of RPMs
	private double driveBaseSpeed = 500;	// Set the base speed to 500 too. The base speed is used when using a percentage to drive the motors with PID. Example: baseSpeed * 0.1 = 50 RPMs
	
	// Encoder values
	private double leftEncVelocity = 0.0, rightEncVelocity = 0.0;
	private double leftLastEncTicks = 0.0, rightLastEncTicks = 0.0;
	private double leftRotations = 0.0, rightRotations = 0.0;
	private double leftEncTicks = 0.0, rightEncTicks = 0.0;
	private final double encTicksPerRotation = 4096;
	private final double wheelSize = 6.0;
	
	// GYRO turning values
	private boolean yawZeroed = false;
	private double yawReport = 0.0;
	private double currentTurnError = 0.0;
	private double turnErrorTolerance = 0.02;
	private double bangbangAngleTolerance = 1;
	
	// SmartDashboard control
	private final boolean publishEncoderVals = true, publishGYROVals = true;
	
	// Drive Control modifiers
	private final boolean reverseInput = false, flipLeftRight = false, reverseLeft = true, reverseRight = false;
	
	@Override
	protected void initSystem() {
		// TODO: Should this be in a try/catch statement? That may be handled somewhere else, let me check.
		
		// Set up the motors using the indexing class
		leftMotorMaster = new CANTalon(id.leftDriveMotorMaster);
		leftMotorSlave = new CANTalon(id.leftDriveMotorSlave);
		rightMotorMaster = new CANTalon(id.rightDriveMotorMaster);
		rightMotorSlave = new CANTalon(id.rightDriveMotorSlave);
		
		// Set the feedback devices for the masters (Encoders should be plugged into these controllers
		leftMotorMaster.setFeedbackDevice(FeedbackDevice.EncRising);
		rightMotorMaster.setFeedbackDevice(FeedbackDevice.EncRising);
		// Set the slave motors to follower so they copy the outputs of the master controllers
		leftMotorSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightMotorSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		
		pid = new PIDController();
		{
			SPID spid = new SPID();
			spid.dState = 0;
			spid.iState = 0;
			spid.iMax = 1.0;
			spid.iMin = -1.0;
			spid.iGain = 0.01;
			spid.pGain = 0.01;
			spid.dGain = 0.0;
			pid.setSPID(spid);
		}
	}

	@Override
	protected void updateSystem() {
		updateEncoderVals();			// Update the encoder values
		updateGYROVals();				// Update the GYRO values
		updateMotorsForDriveControl();	// Update the motor control mode
		updateMotors();					// Update the motors
		
		publishToSmartDashboard();		// Put data on the SmartDashboard
	}

	@Override
	protected void killSystem() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void errorHandler() {
		// TODO Auto-generated method stub
		
	}
	
	//// Private functions
	// Function to control the motors
	private void updateMotors() {
		// Check what control mode we are in so we can check the motor control values accordingly
		switch (driveControlType) {
		case Percentage:
			// Check if the drive set values are in the correct range for percentage drive mode
			// for the left
			if (leftDriveSetVal > 1.0) leftDriveSetVal = 1.0;
			else if (leftDriveSetVal < -1.0) leftDriveSetVal = -1.0;
			// for the right
			if (rightDriveSetVal > 1.0) rightDriveSetVal = 1.0;
			else if (rightDriveSetVal < -1.0) rightDriveSetVal = -1.0;
			break;
		case Speed:
			if (driveMaxSpeed == 0.0) break;	// If there is no max we don't check because there is no range
			// Check if the drive set values are in the correct range for the speed drive mode
			// for the left
			if (leftDriveSetVal > driveMaxSpeed) leftDriveSetVal = driveMaxSpeed;
			else if (leftDriveSetVal < -driveMaxSpeed) leftDriveSetVal = -driveMaxSpeed;
			// for the right
			if (rightDriveSetVal > driveMaxSpeed) rightDriveSetVal = driveMaxSpeed;
			else if (rightDriveSetVal < -driveMaxSpeed) rightDriveSetVal = driveMaxSpeed;
			break;
		}
		
		// Apply the drive control modifiers
		if (reverseInput) {		// Reverse both inputs
			leftDriveSetVal = -leftDriveSetVal;
			rightDriveSetVal = -rightDriveSetVal;
		}
		if (flipLeftRight) {	// Set the left input equal to the right and vice versa
			double newLeft = rightDriveSetVal;
			double newRight = leftDriveSetVal;
			leftDriveSetVal = newLeft;
			rightDriveSetVal = newRight;
		}
		if (reverseLeft) leftDriveSetVal = -leftDriveSetVal;	// Reverse the left input
		if (reverseRight) rightDriveSetVal = -rightDriveSetVal;	// Reverse the right input
		
		// Apply the speed modification percentage
		// Modification should be between 0.0 and 1.0
		leftDriveSetVal *= speedModPercent;
		rightDriveSetVal *= speedModPercent;
		
		// Actually set the motors now
		leftMotorMaster.set(leftDriveSetVal);
		rightMotorMaster.set(rightDriveSetVal);
		
		// Enable/disable braking
		leftMotorMaster.enableBrakeMode(brakingEnabled);
		rightMotorMaster.enableBrakeMode(brakingEnabled);
	}
	// Function to update the control mode
	private void updateMotorsForDriveControl() {
		switch (driveControlType) {
		case Percentage:
			// Set the control mode to percentage
			// Set the masters because the followers do what the master controllers do
			leftMotorMaster.changeControlMode(TalonControlMode.PercentVbus);	// PercentVbus is the default TalonControl mode
			rightMotorMaster.changeControlMode(TalonControlMode.PercentVbus);
			break;
		case Speed:
			// Set the control mode to speed
			// Set the masters because the followers do what the master controllers do
			leftMotorMaster.changeControlMode(TalonControlMode.Speed);		// (TODO: speed uses PID so set that up???)
			rightMotorMaster.changeControlMode(TalonControlMode.Speed);
			break;
		}
	}
	
	// Function to update encoder values
	private void updateEncoderVals() {
		//leftLastEncPosition = leftEncPosition;
		//rightLastEncPosition = rightEncPosition;
		//leftEncPosition = leftMotorMaster.getEncPosition();
		//rightEncPosition = rightMotorMaster.getEncPosition();
		
		try {
			leftEncVelocity = leftMotorMaster.getEncVelocity();		// Velocity makes more sense for our use because it is displacement in position not distance.
			rightEncVelocity = rightMotorMaster.getEncVelocity();
		} catch (Exception ex) {
			System.out.println("Warning (Drive System): Cannot get data from encoders!");
		}
		
		Robot.sensorController.setRPMs(leftEncVelocity, rightEncVelocity);
	}
	// Function to update GYRO values
	private void updateGYROVals() { 
		if (Robot.sensorController.isAHRSError()) {	// If there is an error initializing the AHRS in the sensor controller
			yawReport = 0;	// Set the yawReport to some number
			return;			// Stop the function
		}
		yawReport = Robot.sensorController.ahrs.getYaw(); 
	}
	
	// Function to publish all values to SmartDashboard
	private void publishToSmartDashboard() {
		if (publishEncoderVals) {	// If we need to put the encoder values on the SmartDashboard
			SmartDashboard.putNumber("Left Enc Velocity", leftEncVelocity);
			SmartDashboard.putNumber("Right Enc Velocity", rightEncVelocity);
		}
		if (publishGYROVals) {	// If we need to put the GYRO values on the SmartDashboard
			SmartDashboard.putNumber("GYRO Yaw", yawReport);
		}
	}
	
	// Function to turn to a given angle using PID
	private boolean baseTurnToAnglePID(double target, boolean stopWhenDone) {
		if (Robot.sensorController.isAHRSError()) return false;	// Say there is nothing left to do because we can't do anything
		if (!yawZeroed) {	// If we haven't zeroed the YAW
			Robot.sensorController.ahrs.zeroYaw();	// Zero the YAW
			yawZeroed = true;	// Set this to true so we don't zero the YAW again
		}
		
		double error = pid.update(target - yawReport, yawReport);	// Update the error with the PID Controller
		if (error < turnErrorTolerance && error > -turnErrorTolerance) error = 0;	// If the error is within the tolerance range, set error to 0
		
		currentTurnError = error;
		
		if (currentTurnError == 0) {	// If we are on target
			drive4Motors(0,0);	// Stop moving
			yawZeroed = false;	// Set this to false so next time we zero it
			return false;		// Return false when we are done. (TODO: Some of these functions return true when they are done, that makes more sense so update this)
		}
		return true;	// Return when there is still work to do (TODO: ^^^)
	}
	////
	
	//// Public control functions
	// Set the control type. This will be used in updateMotorsForDriveControl()
	public void setDriveControlType(ControlType _type) { driveControlType = _type; }
	
	// Set the motor control values with two percentages (-1.0 to 1.0)
	public void drive4Motors(double left, double right) {
		switch (driveControlType) {
		case Percentage:
			// If the control mode is percentage just use left and right
			leftDriveSetVal = left;
			rightDriveSetVal = right;
			break;
		case Speed:
			// If the control mode is speed use the product of the base speed and the left and right values
			leftDriveSetVal = driveBaseSpeed * left;
			rightDriveSetVal = driveBaseSpeed * right;
			break;
		}
	}
	
	// Function to call the base turn to angle with stop
	public boolean turnToAnglePID(double target) { return baseTurnToAnglePID(target, true); }
	// Function to call the base turn to angle without stop
	public boolean centerToAnglePID(double target) { return baseTurnToAnglePID(target, false); }
	
	// Function to center to angle using a bang-bang type method
	public boolean centerToAngleNoPID(double target) {
		// Make sure the GYRO works
		if (Robot.sensorController.isAHRSError()) return false;
		if (!yawZeroed) Robot.sensorController.ahrs.zeroYaw();	// Zero the YAW if it hasn't been zeroed
		
		// Check on the target
		double offset = 0;
		offset = target - yawReport;
		
		// Check if the offset is within the tolerance range
		if (target == yawReport || Math.abs(offset) < bangbangAngleTolerance) offset = 0;
		
		if (offset == 0) return false;	// Return false if we are done
		System.out.println("Offset:" + offset);
		
		// Turn
		if (offset > 0) {
			// turn left??
			turn(-1);
		} else if (offset < 0) {
			// turn right??
			turn(1);
		} else return false;
		
		return true;	// Return true if there is still work to do
	}
	
	// Set the motor braking to enabled or disabled
	public void setBraking(boolean brake) { brakingEnabled = true; }
	
	// Function to turn on the center of the robot
	public void turn(double speed) { drive4Motors(speed, -speed); }
	
	// Function to drive a given distance
	public boolean driveDistance(double left, double right, double distance) {
		leftEncTicks = leftMotorMaster.getPosition();		// Update the encoder ticks
		rightEncTicks = rightMotorMaster.getPosition();		//
		
		if (leftRotations < 0) leftLastEncTicks = leftEncTicks;		// If this is the first time we have updated, set the beginning tick values to the current tick values
		if (rightRotations < 0) rightLastEncTicks = rightEncTicks;
		
		leftRotations = Math.abs(leftEncTicks - leftLastEncTicks) / encTicksPerRotation;		// The number of rotations is equal to 
		rightRotations = Math.abs(rightEncTicks - rightLastEncTicks) / encTicksPerRotation;		// the total number of ticks divided by the number of ticks per rotation
		
		double leftDistance = leftRotations * wheelSize;		// Distance is equal to the product of the number of rotations and the wheel size
		double rightDistance = rightRotations * wheelSize;		// For example: if we have 6" wheels and we have gone 1 rotation, we have moved 6"
		
		System.out.println("Distance: " + leftDistance + "," + rightDistance);
		
		if (leftDistance >= distance || rightDistance >= distance) {	// If we have hit our target
			drive4Motors(0,0);	// Stop moving
			leftRotations = -1;	// Reset rotations to -1 so we know we need to start again next time this function is called
			rightRotations = -1;
			return true;	// Return true when we are done
		} else {
			drive4Motors(left, right);	// Move the robot if we haven't hit the target
		}
		
		return false;	// Return false when we need to do more work
	}
	////
	
}