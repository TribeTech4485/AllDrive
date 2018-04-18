package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Robot;
import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


// Add comments


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
	
	private double ticksPerRev = 1340;
	
	
	//// Sensor Values
	private boolean encodersInitialized = false;
	private double wheelRadius_cm = 7.62;
	private double startPositionLeft_cm, startPositionRight_cm;
	private double leftOffset_cm, rightOffset_cm;
	//private double leftEncVelocity = 0.0, rightEncVelocity = 0.0;
	
	// Iterative Function Values
	// DRIVE DISTANCE SPEEDS ---------	(turn these up ++)
	private double driveToDistanceBaseSpeed = 0.1;//0.3;//0.7;//0.4;//0.25;		// This is the base speed used to adjust for drive distance
	private double driveToDistanceMinSpeed = 0.5;// 0.50;//1.0;//0.25;//0.10;		// The minimum speed to drive the motors while driving for distance
	private double driveToDistanceMaxSpeed = 1.0;		// The maximum speed to drive the motors while driving for distance
	// -------------------------------
	
	private double driveToDistanceStartLeft = 0.0;		// The starting position of the left wheels (in centimeters)
	private double driveToDistanceStartRight = 0.0;		// The starting position of the right wheels (in centimeters)
	private double driveToDistanceStartTime = -1;		// Start time used for timeout of drive to distance
	private double driveToDistanceStartAngle = 0;
	
	// TURN SPEEDS ----------	( Turn these up too XD ^_^)
	private double driveToAngleBaseSpeed = 0.6;//0.40;
	private double driveToAngleMinSpeed = 0.3;//0.4;//0.3;//0.2;	// Turn this up a lot
	private double driveToAngleMaxSpeed = 1.0;
	// ----------------------
	
	private double driveToAngleResetThreshold = 1.0;
	
	private double driveToDistanceResetThreshold_cm = 1.25;	// Threshold for resetting of driveToDistance (in centimeters)
	private boolean driveToDistanceInitialized = false;	// Boolean to control the iterative driveToDistance function
	
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
		resetDriveToDistance();
		update();
		encodersInitialized = false;
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
	public void setBraking(boolean _brake) {
		brake = _brake;
	}
	public void setPowerReductionLimiter(double limiter) {
		drivePowerReductionLimiter = limiter;
	}
	public void setUsePowerReductionLimiter(boolean use) {
		enablePowerReductionLimiter = use;
	}
	
	public double getLeftOffset_cm() {
		return leftOffset_cm;
	}
	public double getRightOffset_cm() {
		return rightOffset_cm;
	}
		
	// Function to call driveToDistance with timeout
	public double driveToDistance(double distance_cm, double timeout_ms) {
		double driveToDistanceReturn = driveToDistance(distance_cm);
		if (System.currentTimeMillis() - driveToDistanceStartTime > timeout_ms) {
			drive4Motors(0,0);
			return 0;
		}
		return driveToDistanceReturn;
	}
	public double driveToDistanceStraight(double distance_cm, double timeout_ms) {
		double driveToDistanceReturn = driveToDistanceStraight(distance_cm);
		if (System.currentTimeMillis() - driveToDistanceStartTime > timeout_ms) {
			drive4Motors(0,0);
			return 0;
		}
		return driveToDistanceReturn;
	}
	// Function to iteratively drive a distance
	public double driveToDistance(double distance_cm) {
		if (!driveToDistanceInitialized) {
			driveToDistanceStartTime = System.currentTimeMillis();
			driveToDistanceStartLeft = Robot.sensorController.getLeftOffset_cm();
			driveToDistanceStartRight = Robot.sensorController.getRightOffset_cm();
			driveToDistanceInitialized = true;
		}
		// Calculate offset from start
		double leftDistance = Robot.sensorController.getLeftOffset_cm() - driveToDistanceStartLeft;
		double rightDistance = Robot.sensorController.getRightOffset_cm() - driveToDistanceStartRight;
		
		double leftError = Math.abs(distance_cm) - Math.abs(leftDistance);
		double rightError = Math.abs(distance_cm) - Math.abs(rightDistance);
		
		double percentagePer_cm = driveToDistanceBaseSpeed / Math.abs(distance_cm);
		
		// Run the control on both sides
		double leftDriveMod = leftError * percentagePer_cm;
		double rightDriveMod = rightError * percentagePer_cm;
		
		leftDriveMod += driveToDistanceBaseSpeed;
		rightDriveMod += driveToDistanceBaseSpeed;
		
		SmartDashboard.putNumber("Left Drive Distance", leftDistance);
		SmartDashboard.putNumber("Right Drive Distance", rightDistance);
		
		SmartDashboard.putNumber("Left Drive Mod", leftDriveMod);
		SmartDashboard.putNumber("Right Drive Mod", rightDriveMod);
		
		SmartDashboard.putNumber("Left Drive Offset", leftError);
		SmartDashboard.putNumber("Right Drive Offset", rightError);
		
		if (leftDriveMod < driveToDistanceMinSpeed) leftDriveMod = driveToDistanceMinSpeed;
		else if (leftDriveMod > driveToDistanceMaxSpeed) leftDriveMod = driveToDistanceMaxSpeed;
		if (rightDriveMod < driveToDistanceMinSpeed) rightDriveMod = driveToDistanceMinSpeed;
		else if (rightDriveMod > driveToDistanceMaxSpeed) rightDriveMod = driveToDistanceMaxSpeed;
		
		// Correct for direction
		leftDriveMod *= Math.abs(distance_cm) / distance_cm;
		rightDriveMod *= Math.abs(distance_cm) / distance_cm;
				
		drive4Motors(leftDriveMod, rightDriveMod);
		
		// Check the reset threshold
		double averageError = (leftError + rightError) / 2;
		if (averageError < driveToDistanceResetThreshold_cm) {
			driveToDistanceInitialized = false;
			return 0;	// Return that the error is 0 centimeters if it is within the threshold
		}
		
		return (leftError + rightError) / 2;	// Otherwise return the actual error
	}
	// Function to iteratively drive a distance
	public double driveToDistanceStraight(double distance_cm) {
		if (!driveToDistanceInitialized) {
			driveToDistanceStartAngle = Robot.sensorController.getAHRSYaw();
			driveToDistanceStartTime = System.currentTimeMillis();
			driveToDistanceStartLeft = Robot.sensorController.getLeftOffset_cm();
			driveToDistanceStartRight = Robot.sensorController.getRightOffset_cm();
			driveToDistanceInitialized = true;
		}
		// Calculate offset from start
		double leftDistance = Robot.sensorController.getLeftOffset_cm() - driveToDistanceStartLeft;
		double rightDistance = Robot.sensorController.getRightOffset_cm() - driveToDistanceStartRight;
		
		double leftError = Math.abs(distance_cm) - Math.abs(leftDistance);
		double rightError = Math.abs(distance_cm) - Math.abs(rightDistance);
		
		double percentagePer_cm = driveToDistanceBaseSpeed / Math.abs(distance_cm);
		
		// Run the control on both sides
		double leftDriveMod = leftError * percentagePer_cm;
		double rightDriveMod = rightError * percentagePer_cm;
		
		leftDriveMod += driveToDistanceBaseSpeed;
		rightDriveMod += driveToDistanceBaseSpeed;
		
		SmartDashboard.putNumber("Left Drive Distance", leftDistance);
		SmartDashboard.putNumber("Right Drive Distance", rightDistance);
		
		SmartDashboard.putNumber("Left Drive Mod", leftDriveMod);
		SmartDashboard.putNumber("Right Drive Mod", rightDriveMod);
		
		SmartDashboard.putNumber("Left Drive Offset", leftError);
		SmartDashboard.putNumber("Right Drive Offset", rightError);
		
		if (leftDriveMod < driveToDistanceMinSpeed) leftDriveMod = driveToDistanceMinSpeed;
		else if (leftDriveMod > driveToDistanceMaxSpeed) leftDriveMod = driveToDistanceMaxSpeed;
		if (rightDriveMod < driveToDistanceMinSpeed) rightDriveMod = driveToDistanceMinSpeed;
		else if (rightDriveMod > driveToDistanceMaxSpeed) rightDriveMod = driveToDistanceMaxSpeed;
		
		// Calculate the angle offset and use the drive mod from the distance as a base speed for the turn
		double percentagePerDegree = driveToAngleBaseSpeed / 90;
		double angleOffset = Robot.sensorController.getAHRSYaw() - driveToDistanceStartAngle;
		
		double angleMod = Math.abs(angleOffset * percentagePerDegree);
		double leftAngleMod, rightAngleMod;
		if (angleOffset < 0) {
			leftAngleMod = angleMod * -1;	// Comment out for surge
			rightAngleMod = angleMod;// * -1;	// Uncomment for surge
		} else {
			leftAngleMod = angleMod;// * -1;	// Uncomment for surge
			rightAngleMod = angleMod * -1;	// Comment out for surge
		}
		
		// Calculate the final value with angle and distance offsets
		leftDriveMod += leftAngleMod;
		rightDriveMod += rightAngleMod;
		
		
		// Correct for direction
		leftDriveMod *= Math.abs(distance_cm) / distance_cm;
		rightDriveMod *= Math.abs(distance_cm) / distance_cm;
				
		drive4Motors(leftDriveMod, rightDriveMod);
		
		// Check the reset threshold
		double averageError = (leftError + rightError) / 2;
		if (averageError < driveToDistanceResetThreshold_cm) {
			driveToDistanceInitialized = false;
			return 0;	// Return that the error is 0 centimeters if it is within the threshold
		}
		
		return (leftError + rightError) / 2;	// Otherwise return the actual error
	}
	
	
	public void cancelDriveToDistance() {
		driveToDistanceInitialized = false;
	}
	
	public double driveToAngle(double angle) {
		angle = angle * -1;
		double percentagePerDegree = driveToAngleBaseSpeed / 180;
		double angleOffset = angle - Robot.sensorController.getAHRSYaw();
		double driveMod = Math.abs(angleOffset * percentagePerDegree);
		
		
		SmartDashboard.putNumber("Angle Degree", Robot.sensorController.getAHRSYaw());
		SmartDashboard.putNumber("Angle Offset", angleOffset);
		
		double leftDriveMod, rightDriveMod;
		if (angleOffset < 0) {
			leftDriveMod = driveMod * -1;	// Comment out for surge;
			rightDriveMod = driveMod;// * -1;	// Uncomment for surge
		} else {
			leftDriveMod = driveMod;// * -1;	// Uncomment for surge
			rightDriveMod = driveMod * -1;	// Comment out for surge
		}
		
		if (Math.abs(leftDriveMod) < driveToAngleMinSpeed) leftDriveMod = driveToAngleMinSpeed * (Math.abs(leftDriveMod) / leftDriveMod);
		else if (Math.abs(leftDriveMod) > driveToAngleMaxSpeed) leftDriveMod = driveToAngleMaxSpeed * (Math.abs(leftDriveMod) / leftDriveMod);
		if (Math.abs(rightDriveMod) < driveToAngleMinSpeed) rightDriveMod = driveToAngleMinSpeed * (Math.abs(rightDriveMod) / rightDriveMod);
		else if (Math.abs(rightDriveMod) > driveToAngleMaxSpeed) rightDriveMod = driveToAngleMaxSpeed * (Math.abs(rightDriveMod) / rightDriveMod);
		
		drive4Motors(leftDriveMod, rightDriveMod);
		
		SmartDashboard.putNumber("Left Drive Mod", leftDriveMod);
		SmartDashboard.putNumber("Right Drive Mod", rightDriveMod);
		
		if (Math.abs(angleOffset) < driveToAngleResetThreshold) angleOffset = 0;
		return Math.abs(angleOffset);
	}
	
	public void resetDriveToDistance() {
		driveToDistanceInitialized = false;
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
	private void updatePowerLimiter() {
		setPowerReductionLimiter(Robot.sensorController.getDrivePowerLimiter());
		//System.out.println("Drive Power Reduction: " + drivePowerReductionLimiter);
	}
	private void updateEncoderVals() {
		try {
			if (!encodersInitialized) {
				leftMotorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10); 	// (FeedbackDevice, id?, timeout)
				rightMotorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
				leftMotorMaster.setSelectedSensorPosition(0, 0, 10);
				rightMotorMaster.setSelectedSensorPosition(0, 0, 10);
				startPositionLeft_cm = ((leftMotorMaster.getSelectedSensorPosition(0) /  ticksPerRev) * (2 * Math.PI * wheelRadius_cm));
				startPositionRight_cm = ((rightMotorMaster.getSelectedSensorPosition(0)/ ticksPerRev) * (2 * Math.PI * wheelRadius_cm));
				encodersInitialized = true;
				
			}
			
			// Get the encoder values from the masters
			double leftRPM = leftMotorMaster.getSelectedSensorVelocity(0);
			double rightRPM = rightMotorMaster.getSelectedSensorVelocity(0);
			
			double leftPosition = leftMotorMaster.getSelectedSensorPosition(0);
			double rightPosition = rightMotorMaster.getSelectedSensorPosition(0);
			
			double leftPosition_cm = (leftPosition / ticksPerRev) * (2 * Math.PI * wheelRadius_cm);
			double rightPosition_cm = (rightPosition / ticksPerRev) * (2 * Math.PI * wheelRadius_cm);
			
			leftOffset_cm = leftPosition_cm - startPositionLeft_cm;
			rightOffset_cm = rightPosition_cm - startPositionRight_cm;
			
			Robot.sensorController.setRPMs(leftRPM, rightRPM);
			Robot.sensorController.setOffsets(leftOffset_cm, rightOffset_cm);
		} catch (Exception ex) {
			createError(false, ex.getMessage());
		}
	}
	////
	
}