package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


/*
 * USING THE BASICS FOR NOW
 * TODO: Add more functionality for easy auto and teleOp usage
 * TODO: Add sensor usage (encoders, GYRO, distance)
 */


public class DriveSystem extends Subsystem {

	//// Motors
	private WPI_TalonSRX leftMotorMaster, leftMotorSlave;
	private WPI_TalonSRX rightMotorMaster, rightMotorSlave;
	
	//// Control Variables
	private boolean enableSensorUpdate = true;
	private double leftDriveSet = 0.0, rightDriveSet = 0.0;
	
	//// Sensor Values
	//private double leftEncVelocity = 0.0, rightEncVelocity = 0.0;
	
	
	@Override
	protected void initSystem() {
		leftMotorMaster = new WPI_TalonSRX(id.leftDriveMotorMaster);
		leftMotorSlave = new WPI_TalonSRX(id.leftDriveMotorSlave);
		rightMotorMaster = new WPI_TalonSRX(id.rightDriveMotorMaster);
		rightMotorSlave = new WPI_TalonSRX(id.rightDriveMotorSlave);
	}

	@Override
	protected void updateSystem() {
		updateMotorControl();
		if (enableSensorUpdate) updateSensors();
	}
	
	private void updateSensors() {
		updateEncoderVals();
	}

	@Override
	protected void killSystem() {
		drive4Motors(0,0);
		update();
	}

	@Override
	protected void errorHandler() {
		// TODO Auto-generated method stub
		
	}
	
	//// Public control interface functions
	public void drive4Motors(double left, double right) {
		leftDriveSet = left;
		rightDriveSet = right;
	}
	////
	
	//// Private hardware control
	private void updateMotorControl() {
		leftMotorMaster.set(leftDriveSet);
		leftMotorSlave.set(leftDriveSet);
		rightMotorMaster.set(rightDriveSet);
		rightMotorSlave.set(rightDriveSet);
	}
	private void updateEncoderVals() {
		try {
			
		} catch (Exception ex) {
			createError(false, ex.getMessage());
		}
	}
	////
	
}