package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.kauailabs.navx.frc.AHRS;

import org.usfirst.frc.team4485.robot.Subsystems.PIDController.*;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class UniformDrive extends Subsystem {
	public AHRS ahrs;
	private PIDController pid;
	
	private double pastTurnError;
	private double currentTurnError;
	
	@Override
	protected void initSystem() {
		rightMotorMaster = new CANTalon(id.rightDriveMotorMaster);
		rightMotorSlave = new CANTalon(id.rightDriveMotorSlave);
		leftMotorMaster = new CANTalon(id.leftDriveMotorMaster);
		leftMotorSlave = new CANTalon(id.leftDriveMotorSlave);
		
		
		
		
		setMotorsForNoPID();
		setBraking(false);
		
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
		// TODO Auto-generated method stub
		publishEncoderVals();
		publishGyroVals();
	}

	@Override
	protected void killSystem() {
		drive4Motors(0.0,0.0);
		leftRotations = -1;
		rightRotations = -1;
	}

	@Override
	protected void errorHandler() {
		if (majorError) killSystem();
	}
	
	// Objects for drive
	// Robot Objects (Motors, etc.)
	private CANTalon rightMotorMaster;
	private CANTalon rightMotorSlave;
	private CANTalon leftMotorMaster;
	private CANTalon leftMotorSlave;
	
	
	// Time stuff
	double motorUpdateStart = -1;
	double waitForMotorUpdateDuration= -1;
	
	private boolean yawZeroed = false;
	private double maxTurnSpeed = 0.3;
	private double yawTollerance = 1;
		
	// Distance stuff
	private double leftStartTicks = 0;
	private double rightStartTicks = 0;
	private double leftRotations = -1;
	private double rightRotations = -1;
	//
	
	private double avgCurrentDraw = 0.0;
	
	//// Drive functions
	// Private functions
	private void controlDriveSide(CANTalon master, CANTalon slave, double value) {
		master.set(value);
		slave.set(value);
	}
	
	private double checkOnTargetPID(double target, double errorTollerance, double pastError) {
		double yawReport = ahrs.getYaw();
		
		double error = pid.update(target - yawReport, yawReport);
		if (error < errorTollerance && error > -errorTollerance) return 0;
		return error;
	}
	
	private double checkOnTarget(double target, double tollerance) {
		double yawReported = ahrs.getYaw();
		//if (id.flipGyro) yawReported *= -1; // Flip the yaw
		//System.out.print("YAW:" + yawReported);
		//System.out.println("AHRS YAW: " + yawReported + " Zeroed: " + yawZeroed);
		if (target == yawReported || Math.abs(target - yawReported) < tollerance) return 0;
		double returnVal = (target - yawReported)/(Math.abs(target - yawReported));
		//if (returnVal < yawTarget) returnVal *= -1;
		//else if (returnVal < 180) returnVal *= -1;
		
		return returnVal;
	}
	private void turn(double rawMove, double turnMultiplier) {
		double driveLeft, driveRight;
		driveLeft = turnMultiplier;
		driveRight = -turnMultiplier;
		
		if (driveLeft > 1) driveLeft = 1;
		if (driveLeft < -1) driveLeft = -1;
		if (driveRight > 1) driveRight = 1;
		if (driveRight < -1) driveRight = -1;
		
		drive4Motors(driveLeft * maxTurnSpeed,driveRight * maxTurnSpeed);
		//System.out.println("Drive: " + driveLeft + "," + driveRight);
	}
	
	private void publishGyroVals() {
		SmartDashboard.putNumber("AHRS Yaw", ahrs.getYaw());
		SmartDashboard.putNumber("AHRS Raw X", ahrs.getRawGyroX());
		SmartDashboard.putNumber("AHRS Raw Y", ahrs.getRawGyroY());
		SmartDashboard.putNumber("AHRS Raw Z", ahrs.getRawGyroZ());
	}
	private void publishEncoderVals() {
		SmartDashboard.putNumber("Right Encoder Velocity Value", rightMotorMaster.getEncVelocity());
		SmartDashboard.putNumber("Left Encoder Velocity Value", leftMotorMaster.getEncVelocity());
	}
	/*
	private void getDashboardTurnPIDVals() {
		//double pGain, iGain, dGain;
		SPID tmpSPID = pid.getSPID();
		SmartDashboard.getNumber("Turn P", tmpSPID.pGain);
		SmartDashboard.getNumber("Turn I", tmpSPID.iGain);
		SmartDashboard.getNumber("Turn D", tmpSPID.dGain);
		pid.setSPID(tmpSPID);
	}
	private void publishTurnPIDVals() {
		SPID tmpSPID = pid.getSPID();
		SmartDashboard.putNumber("Turn P", tmpSPID.pGain);
		SmartDashboard.putNumber("Turn I", tmpSPID.iGain);
		SmartDashboard.putNumber("Turn D", tmpSPID.dGain);
	}
	*/
	//
	
	//Public functions
	public void drive4Motors(double left, double right) {
		// check left and right against min and max numbers
		if (left > 1.0) left = 1.0;
		if (left < -1.0) left = -1.0;
		if (right > 1.0) right = 1.0;
		if (right < -1.0) right = -1.0;
		
		// check the time (wait for motors to update without stopping code)
		if (waitForMotorUpdateDuration < 0 || waitForMotorUpdateDuration >= 15 || motorUpdateStart < 0) {
			motorUpdateStart = System.currentTimeMillis();
		} else {
			waitForMotorUpdateDuration = System.currentTimeMillis() - motorUpdateStart;
			return;
		}
		
		// drive with given left and right
		controlDriveSide(rightMotorMaster, rightMotorSlave, right);
		controlDriveSide(leftMotorMaster, leftMotorSlave, -left);
	}	
	public void drive4MotorsPID(double left, double right) {
		boolean reverseLeft = -(left/Math.abs(left)) < 0;
		boolean reverseRight = (right/Math.abs(right)) < 0;
		
		leftMotorMaster.reverseOutput(reverseLeft);
		leftMotorMaster.set(left);
		rightMotorMaster.reverseOutput(reverseRight);
		rightMotorMaster.set(right);
	}
	
	public boolean turnToAnglePID(double turnToAngle) {
		if (!yawZeroed) zeroYaw();
		
		pastTurnError = currentTurnError;
		currentTurnError = checkOnTargetPID(turnToAngle, 0.02, pastTurnError);
		
		SmartDashboard.putNumber("Current Turn Error", currentTurnError);
		SmartDashboard.putNumber("Last Turn Error", pastTurnError);
		
		if (currentTurnError> 0) {
			turn(currentTurnError, -1);
		} else if (currentTurnError < 0) {
			turn(currentTurnError, 1);
		} else {
			drive4Motors(0,0);
			yawZeroed = false;
			return false;
		}
		return true;
	}
	
	@Deprecated
	public boolean turnToAngle(double turnToAngle) {
		if (!yawZeroed) zeroYaw();
		// Turn with gyro
		double targetError;
		targetError = checkOnTarget(turnToAngle, yawTollerance);
		//System.out.println(targetError);
		if (targetError > 0) {
			// Turn left??
			turn(targetError, -0.3);
		} else if (targetError < 0) {
			// Turn right??
			turn(targetError, 0.3);
		} else {
			drive4Motors(0, 0);
			yawZeroed = false;
			/*Timer.delay(0.020);
			ahrs.zeroYaw();
			ahrs.zeroYaw();
			Timer.delay(0.020);*/
			return false;
		}
		return true;
	}
	
	public boolean centerToAngle(double turnToAngle) {
		// Turn with gyro
		if (!yawZeroed) zeroYaw();
		double targetError;
		targetError = checkOnTarget(turnToAngle, yawTollerance);
		//System.out.println(targetError);
		if (targetError > 0) {
			// Turn left??
			turn(targetError, -1);
		} else if (targetError < 0) {
			// Turn right??
			turn(targetError, 1);
		} else {
				
			//yawZeroed = false;
			/*Timer.delay(0.020);
			ahrs.zeroYaw();
			ahrs.zeroYaw();
			Timer.delay(0.020);*/
			return false;
		}
		return true;
	}
	public boolean driveDistance(double left, double right, double distIn) {
		//setMotorsForNoPID();
		
		double leftCurrentTicks = leftMotorMaster.getEncPosition();
		double rightCurrentTicks = rightMotorMaster.getEncPosition();
		if (leftRotations < 0) {
			leftStartTicks = leftCurrentTicks;
		}
		if (rightRotations < 0) {
			rightStartTicks = rightCurrentTicks;
		}
		
		leftRotations = Math.abs(leftCurrentTicks - leftStartTicks) / 4096;
		rightRotations = Math.abs(rightCurrentTicks - rightStartTicks) / 4096;
		
		//System.out.println("Rotations: " + leftRotations + "," + rightRotations);
		
		double leftDistance = leftRotations * 4;
		double rightDistance = rightRotations * 4;
		
		System.out.println("Start Ticks: " + leftStartTicks + "," + rightStartTicks + " Current Ticks: " + leftCurrentTicks + "," + rightCurrentTicks);
		System.out.println("Distance: " + leftDistance + "," + rightDistance + " Rotations: " + leftRotations + "," + rightRotations);
		
		// Add leftDistance back later
		if (leftDistance >= distIn || rightDistance >= distIn) {
			drive4Motors(0,0);
			leftRotations = -1;
			rightRotations = -1;
			return true;
		} else {
			drive4Motors(left, right);
		}
		
		//System.out.println("Left: " + leftRotations + " Right: " + rightRotations);
		
		return false;
	}
	public void setBraking(boolean brake) {
		leftMotorMaster.enableBrakeMode(brake);
		leftMotorSlave.enableBrakeMode(brake);
		rightMotorMaster.enableBrakeMode(brake);
		rightMotorSlave.enableBrakeMode(brake);
		//System.out.println("Warning: Set brake to: " + brake);
	}
	public void zeroYaw() {
		Timer.delay(0.020);
		ahrs.zeroYaw();
		yawZeroed = true;
		Timer.delay(0.020);
	}
	public void setMotorsForPID() {
		leftMotorMaster.changeControlMode(CANTalon.TalonControlMode.Speed);
		leftMotorMaster.setFeedbackDevice(FeedbackDevice.EncRising);
		leftMotorMaster.reverseOutput(false);
		//leftMotorSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		//leftMotorSlave.set(leftMotorMaster.getDeviceID());
		
		rightMotorMaster.changeControlMode(CANTalon.TalonControlMode.Speed);
		rightMotorMaster.setFeedbackDevice(FeedbackDevice.EncRising);
		rightMotorMaster.reverseOutput(false);
		//rightMotorMaster.changeControlMode(CANTalon.TalonControlMode.Follower);
		//rightMotorMaster.set(rightMotorMaster.getDeviceID());
		
		leftMotorMaster.setPID(id.drivePIDVals.P, id.drivePIDVals.I, id.drivePIDVals.D);
		rightMotorMaster.setPID(id.drivePIDVals.P, id.drivePIDVals.I, id.drivePIDVals.D);
		
	}
	public void setMotorsForNoPID() {
		leftMotorMaster.setFeedbackDevice(FeedbackDevice.EncRising);
		rightMotorMaster.setFeedbackDevice(FeedbackDevice.EncRising);
		leftMotorMaster.changeControlMode(TalonControlMode.PercentVbus);
		rightMotorMaster.changeControlMode(TalonControlMode.PercentVbus);
	}
	//
	////
}