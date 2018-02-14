package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Robot;
import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


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
	private double startingPositionCm;
	
	// Drive limiters
	private boolean enablePowerReductionLimiter = true;
	private double drivePowerReductionLimiter = 1.0;		// Limit the drive capacity based on power usage. 1.0 = full capacity 0.0 = no capacity
	private double driveControlLimiter = 1.0;				// Limit the speed of the drive motors based on preference
	
	
	//// Sensor Values
	private boolean encodersInitialized = false;
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
		updatePowerLimiter();
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
	
	public double getDriveDistance() {
		double position = rightMotorMaster.getSelectedSensorPosition(0);
		System.out.println("getSelectedSensorPosition: " + position);
		double positionCm = (position / 1440 /*4096*/) * (2 * Math.PI * 3);
		return positionCm-startingPositionCm;
	}
	
	
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
	private void updatePowerLimiter() {
		setPowerReductionLimiter(Robot.sensorController.getDrivePowerLimiter());
		System.out.println("Drive Power Reduction: " + drivePowerReductionLimiter);
	}
	private void updateEncoderVals() {
		try {
			if (!encodersInitialized) {
				leftMotorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10); 	// (FeedbackDevice, id?, timeout)
				rightMotorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
				startingPositionCm = ((rightMotorMaster.getSelectedSensorPosition(0) / 1440/*4096*/) * (2 * Math.PI * 3));
				encodersInitialized = true;
			}
			
			// Get the encoder values from the masters
			double leftRPM = leftMotorMaster.getSelectedSensorVelocity(0);
			double rightRPM = rightMotorMaster.getSelectedSensorVelocity(0);
			
			Robot.sensorController.setRPMs(leftRPM, rightRPM);
		} catch (Exception ex) {
			createError(false, ex.getMessage());
		}
	}
	////
	
}