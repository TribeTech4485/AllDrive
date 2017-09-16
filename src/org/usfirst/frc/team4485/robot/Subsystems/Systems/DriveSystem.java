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

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;


public class DriveSystem extends Subsystem {
	
	// Control Variables
	private boolean motorPIDEnabled = false;		// Is PID Enabled?? Here's a boolean
	private boolean motorPIDSetup = false;			// Is PID Setup?? Here's another boolean B)
	private double motorLeftDriveValue, motorRightDriveValue;	// The values for driving the left and right sides of the drive base

	// Objects for drive motors
	// Robot Objects (Motors, etc.)
	private CANTalon rightMotorMaster, rightMotorSlave;		// Right master and slave motor controllers
	private CANTalon leftMotorMaster, leftMotorSlave;		// Left master and slaver motor controllers
	
	// Time Stuff	-	it takes 15ms for the SRX Talons to update
	private double motorUpdateStartTime = -1;				// When we last updated the motors
	private double motorUpdateWaitTimeDuration = -1;		// How long is been since we updated the motors
	
	
	@Override
	protected void initSystem() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void updateSystem() {
		// Check the time (wait for motors to update without stopping code)
		if (motorUpdateWaitTimeDuration < 0 || motorUpdateWaitTimeDuration >= 15 || motorUpdateStartTime < 0) {
			motorUpdateStartTime = System.currentTimeMillis();
		} else {
			motorUpdateWaitTimeDuration = System.currentTimeMillis() - motorUpdateStartTime;
			return;
		}
		// All code that controls motors goes after checking for the update duration
		
		if (!motorPIDEnabled) drive4Motors(motorLeftDriveValue, motorRightDriveValue);		// If PID motor control is not enabled use percentage drive
		if (motorPIDEnabled) drive4MotorsPID(motorLeftDriveValue, motorRightDriveValue);	// If PID motor control is enabled use PID speed drive
	}

	@Override
	protected void killSystem() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void errorHandler() {
		// TODO Auto-generated method stub
		
	}
	
	
	// Unique Functions
	// Public Manipulator Functions (setters)
	public void setDriveValues(double left, double right) {
		motorLeftDriveValue = left;		// Set the left and right motor values
		motorRightDriveValue = right;
	}
	public void setPIDEnabled(boolean enabled) {
		motorPIDEnabled = enabled;		// Set the motorPID Enabled boolean
	}
	
	// Private Control functions
	private void drive4Motors(double left, double right) {
		// Check left and right against the max and min 
		if (left > 1.0) left = 1.0;		// Check the left against the max
		if (left < -1.0) left = -1.0;	// Check the left against the min
		if (right > 1.0) right = 1.0;	// Same thing except for the right side
		if (right < -1.0) right = -1.0;
		
		// Drive the given left and right
		controlTwoMotors(leftMotorMaster, leftMotorSlave, left);		// Control the left side of the drive base
		controlTwoMotors(rightMotorMaster, rightMotorSlave, right);		// Control the right side of the drive base
	}
	private void controlTwoMotors(CANTalon master, CANTalon slave, double power) {
		master.set(power);		// Set the master percentage to the passed in power percentage
		slave.set(power);		// Set the slave percentage to the same thing
		// TODO: Set slave to follower even if it isn't using PID because it should work the same here too
	}
	
	private void drive4MotorsPID(double left, double right) {
		// Don't check against min or max because we are using speed instead of percentage
		if (!motorPIDSetup) setupMotorPIDSpeed();		// If PID has not been setup, set it up
		controlMotorsPID(leftMotorMaster, left);	// Control the left side
		controlMotorsPID(rightMotorMaster, right);	// Control the right side
	}
	private void controlMotorsPID(CANTalon master, double value) {
		master.set(value);	// Set motor passed in to the value passed in
	}
	private void setupMotorPIDSpeed() {
		if (motorPIDSetup) return;
		leftMotorMaster.changeControlMode(CANTalon.TalonControlMode.Speed);		// Set the control mode to speed instead of percentage
		leftMotorMaster.setFeedbackDevice(FeedbackDevice.EncRising);			// Use the rising edge of the encoder
		leftMotorMaster.reverseOutput(false);									// Don't reverse the motor output
		
		leftMotorSlave.changeControlMode(CANTalon.TalonControlMode.Follower);	// Set the control mode to imitate the control of a master controller
		leftMotorSlave.set(leftMotorMaster.getDeviceID());						// Set the controller to imitate to the master
		
		// Do the same stuff but for the right side
		rightMotorMaster.changeControlMode(CANTalon.TalonControlMode.Speed);
		rightMotorMaster.setFeedbackDevice(FeedbackDevice.EncRising);
		rightMotorMaster.reverseOutput(false);
		
		rightMotorSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightMotorSlave.set(rightMotorMaster.getDeviceID());
	}
}