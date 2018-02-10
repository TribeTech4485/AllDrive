package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Robot;
import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


/*
 * USING THE BASICS FOR NOW
 * TODO: Add more functionality for easy auto and teleOp usage
 * TODO: Add sensor usage (encoders, GYRO, distance)
 */


public class DriveSystem extends Subsystem {

	//// Motors
	private WPI_TalonSRX leftMotorMaster, leftMotorSlave1, leftMotorSlave2;
	private WPI_TalonSRX rightMotorMaster, rightMotorSlave1, rightMotorSlave2;
	
	//// Control Variables
	// Sensor Control
	private boolean enableSensorUpdate = true;
	
	// Movement control
	private double leftDriveSet = 0.0, rightDriveSet = 0.0;
	private boolean brake = false;
	
	// Drive limiters
	private boolean enablePowerReductionLimiter = true;
	private double drivePowerReductionLimiter = 1.0;		// Limit the drive capacity based on power usage. 1.0 = full capacity 0.0 = no capacity
	private double driveControlLimiter = 1.0;				// Limit the speed of the drive motors based on preference
	
	
	//// Sensor Values
	//private double leftEncVelocity = 0.0, rightEncVelocity = 0.0;
	
	// Drive Control modifiers
	private final boolean reverseInput = false, flipLeftRight = false, reverseLeft = false, reverseRight = true;
	
	
	@Override
	protected void initSystem() {
		leftMotorMaster = new WPI_TalonSRX(id.leftDriveMotorMaster);
		leftMotorSlave1 = new WPI_TalonSRX(id.leftDriveMotorSlave1);
		leftMotorSlave2 = new WPI_TalonSRX(id.leftDriveMotorSlave2);
		rightMotorMaster = new WPI_TalonSRX(id.rightDriveMotorMaster);
		rightMotorSlave1 = new WPI_TalonSRX(id.rightDriveMotorSlave1);
		rightMotorSlave2 = new WPI_TalonSRX(id.rightDriveMotorSlave2);
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
	public void setPowerReductionLimiter(double limiter) {
		drivePowerReductionLimiter = limiter;
	}
	public void setUsePowerReductionLimiter(boolean use) {
		enablePowerReductionLimiter = use;
	}
	////
	
	//// Private hardware control
	private void updateMotorControl() {
		Robot.sensorController.update();
		//updatePowerLimits();
		
		// Use drive modifiers
		if (reverseInput) {
			leftDriveSet *= -1;
			rightDriveSet *= -1;
		}
		if (flipLeftRight) {
			double left = leftDriveSet;
			leftDriveSet = rightDriveSet;
			rightDriveSet = left;
		}
		if (reverseLeft) leftDriveSet *= -1;
		if (reverseRight) rightDriveSet *= -1;
		
		
		// Calculate the drive set amounts based on the limiters
		// Power usage reduction calculation
		if (enablePowerReductionLimiter) {
			leftDriveSet *= drivePowerReductionLimiter;
			rightDriveSet *= drivePowerReductionLimiter;
		}
		
		// Control limiter calculation
		leftDriveSet *= driveControlLimiter;
		rightDriveSet *= driveControlLimiter;
		
		// Set the motor drive		
		leftMotorMaster.set(leftDriveSet);
		leftMotorSlave1.set(leftDriveSet);
		leftMotorSlave2.set(leftDriveSet);
		rightMotorMaster.set(rightDriveSet);
		rightMotorSlave1.set(rightDriveSet);
		rightMotorSlave2.set(rightDriveSet);
		
		// Set the Neutral Mode of the drive motors
		if (brake) {
			leftMotorMaster.setNeutralMode(NeutralMode.Brake);
			leftMotorSlave1.setNeutralMode(NeutralMode.Brake);
			leftMotorSlave2.setNeutralMode(NeutralMode.Brake);
			rightMotorMaster.setNeutralMode(NeutralMode.Brake);
			rightMotorSlave1.setNeutralMode(NeutralMode.Brake);
			rightMotorSlave2.setNeutralMode(NeutralMode.Brake);
		} else {
			leftMotorMaster.setNeutralMode(NeutralMode.Coast);
			leftMotorSlave1.setNeutralMode(NeutralMode.Coast);
			leftMotorSlave2.setNeutralMode(NeutralMode.Coast);
			rightMotorMaster.setNeutralMode(NeutralMode.Coast);
			rightMotorSlave1.setNeutralMode(NeutralMode.Coast);
			rightMotorSlave2.setNeutralMode(NeutralMode.Coast);
		}
	}
	private void updatePowerLimits() {
		setPowerReductionLimiter(Robot.sensorController.getDrivePowerLimiter());
		System.out.println("Drive Power Reduction: " + drivePowerReductionLimiter);
	}
	private void updateEncoderVals() {
		try {
			
		} catch (Exception ex) {
			createError(false, ex.getMessage());
		}
	}
	////
	
}