/* 
 * TODO:Add Normal Drive Function that works with update()
 * 		Add PID Turn
 * 			Write separate class for gyro handling?
 * 		Add Drive Distance	
*/
/*
 * ADDED:
 * 		Speed Control with PID
 * 		Normal motor control
 */
package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Robot;
import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;
import org.usfirst.frc.team4485.robot.Subsystems.PIDController.PIDController;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;


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
	private double currentTurnError = 0.0;
	private double turnErrorTollerance = 0.02;
	
	@Override
	protected void initSystem() {
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
	}

	@Override
	protected void updateSystem() {
		updateEncoderVals();
		
		updateMotorsForDriveControl();
		updateMotors();
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
		
		// Apply the speed modification percentage
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
			leftMotorMaster.changeControlMode(TalonControlMode.PercentVbus);
			rightMotorMaster.changeControlMode(TalonControlMode.PercentVbus);
			break;
		case Speed:
			// Set the control mode to speed
			// Set the masters because the followers do what the master controllers do
			leftMotorMaster.changeControlMode(TalonControlMode.Speed);
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
		
		leftEncVelocity = leftMotorMaster.getSpeed();
		rightEncVelocity = rightMotorMaster.getSpeed();
		
		Robot.sensorController.setRPMs(leftEncVelocity, rightEncVelocity);
	}
	////
	
	//// Public control functions
	// Set the control type. This will be used in updateMotorsForDriveControl()
	public void setDriveControlType(ControlType _type) { driveControlType = _type; }
	
	// Set the motor control values with two percentages (-1.0 to 1.0)
	public void drive4Motors(double left, double right) {
		left = -left;
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
	
	// Set the motor braking to enabled or disabled
	public void setBraking(boolean brake) { brakingEnabled = true; }
	
	// Function to turn on the center of the robot
	public void turn(double speed) { drive4Motors(speed, -speed); }
	
	// Function to turn to a given angle using PID
	public boolean turnToAnglePID(double target) {
		if (!yawZeroed) {
			Robot.sensorController.ahrs.zeroYaw();
			yawZeroed = true;
		}
		
		double yawReport = Robot.sensorController.ahrs.getYaw();
		double error = pid.update(target - yawReport, yawReport);
		if (error < turnErrorTollerance && error > -turnErrorTollerance) error = 0;
		
		currentTurnError = error;
		
		if (currentTurnError == 0) {
			drive4Motors(0,0);
			yawZeroed = false;
			return false;
		}
		return true;
	}
	
	// Function to drive a given distance
	public boolean driveDistance(double left, double right, double distance) {
		leftEncTicks = leftMotorMaster.getPosition();
		rightEncTicks = rightMotorMaster.getPosition();
		
		if (leftRotations < 0) leftLastEncTicks = leftEncTicks;
		if (rightRotations < 0) rightLastEncTicks = rightEncTicks;
		
		leftRotations = Math.abs(leftEncTicks - leftLastEncTicks) / encTicksPerRotation;
		rightRotations = Math.abs(rightEncTicks - rightLastEncTicks) / encTicksPerRotation;
		
		double leftDistance = leftRotations * wheelSize;
		double rightDistance = rightRotations * wheelSize;
		
		System.out.println("Distance: " + leftDistance + "," + rightDistance);
		
		if (leftDistance >= distance || rightDistance >= distance) {
			drive4Motors(0,0);
			leftRotations = -1;
			rightRotations = -1;
			return true;
		} else {
			drive4Motors(left, right);
		}
		
		return false;
	}
	////
	
}