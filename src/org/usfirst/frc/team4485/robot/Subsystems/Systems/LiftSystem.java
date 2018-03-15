package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;
import org.usfirst.frc.team4485.robot.Subsystems.PIDController.SPID;
import org.usfirst.frc.team4485.robot.Subsystems.PIDController.PIDController;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiftSystem extends Subsystem{

	private double liftSpeed = 0;
	private double liftSetPosition = 0;
	private boolean reverseInput = true;
		
	private WPI_TalonSRX liftMotor;
	
	// Sensor values
	private boolean encodersInitialized = false;
	private double liftPosition = 0;
	
	// Lift PID Values
	private SPID spid;
	private PIDController pid;
	
	private boolean pidOverride = false;
	
	private boolean homing = false;	// if the lift is in the process of homing
	private int homingStep = 0;
	
	private int numLiftPresets = 7;
	private double[] liftPresetPositions = {0, -1000, -2000, -8300, -9300, -19400, -24700};
	
	private DigitalInput lowerLimitSwitch;
	
	@Override
	protected void initSystem() {
		liftMotor = new WPI_TalonSRX(id.liftMotor);
		
		lowerLimitSwitch = new DigitalInput(id.liftLowerLimitSwitch);
		
		pid = new PIDController();
		spid = new SPID();
		spid.pGain = 0.001;
		spid.iGain = 0.001;
		spid.dGain = 0.0;
		spid.iMax = 1.0;
		spid.iMin = -1.0;
		pid.setSPID(spid);
	}

	@Override
	protected void updateSystem() {
		updateSensors();
		iterativeHome();
		updateLiftPID();
		updateMotorControl();
	}

	@Override
	protected void killSystem() {
		setLiftPIDOverride(true);
		setLiftPosition_presetNum(0);
		setLift(0);
		update();
		encodersInitialized = false;
		setLiftPIDOverride(false);
	}

	@Override
	protected void errorHandler() {}
	
	//// Public control functions
	public void setLift(double lift) {
		if (!pidOverride) return;
		liftSpeed = lift;
		if (lift > 0.1) lift = 0.1;
		if (lift < -0.1) lift = -0.1;
	}
	public void setLiftPIDOverride(boolean override) {
		pidOverride = override;
	}
	public void setLiftPosition_encTicks(double pos) {
		liftSetPosition = pos;
	}
	public void setLiftPosition_presetNum(int presetNum) {
		if (presetNum >= numLiftPresets) return;
		if (presetNum < 0) return;
		liftSetPosition = liftPresetPositions[presetNum];
	}
	public boolean homeLift() {
		if (Math.abs(liftPosition) <= Math.abs(liftPresetPositions[0]) + 10) {
			liftSetPosition = liftPresetPositions[0];
			return true;
		} else if (!homing) homing = true;
		return false;
	}
	public double getPosition() {
		return liftPosition;
	}
	public double getLiftOffset() {
		if (pidOverride) return 0;
		return Math.abs(liftSetPosition - liftPosition);
	}
	////
	
	private void updateMotorControl() {
		//if (Math.abs(liftSpeed) > 0.3) liftSpeed = 0.3 * (Math.abs(liftSpeed) / liftSpeed);
		if (liftSpeed > 0.3) liftSpeed = 0.9;
		if (liftSpeed < -0.3) liftSpeed = -0.9;
		SmartDashboard.putNumber("Lift System - Lift Speed", liftSpeed);
		
		if (liftSpeed <-0.5) liftSpeed = -0.5;
		if (reverseInput) liftSpeed *= -1;		
		liftMotor.set(liftSpeed);
	}
	
	private void updateLiftPID() {
		if (pidOverride) return;
		double pidReturn = pid.update(liftPosition - liftSetPosition, liftPosition);
		SmartDashboard.putNumber("Lift System - Set Position", liftSetPosition);
		SmartDashboard.putNumber("Lift System - pidReturn", pidReturn);
		if (pidReturn > 1) pidReturn = 1;
		if (pidReturn < -1) pidReturn = -1;
		liftSpeed = pidReturn;
	}
	
	private boolean iterativeHome() {
		if (!homing) return true;
		double liftOffset = Math.abs(liftSetPosition - liftPosition);
		switch (homingStep) {
		case 0:
			setLiftPosition_presetNum(1);
			if (liftOffset <= 200) homingStep++;
			break;
		case 1:
			// Move the lift down
			setLiftPIDOverride(true);
			setLift(0);
			if (liftOffset <= 200) homingStep++;
			break;
		case 2:
			setLiftPosition_presetNum(0);
			setLiftPIDOverride(false);
			homingStep++;
		case 3:
			homing = false;	// DO NOT FORGET TO SET THIS TO FALSE, BAD THINGS WILL HAPPEN
			homingStep = 0;
		}
		return homing;
	}
	
	private void updateSensors() {
		try {
		if (!encodersInitialized) {
			// Initialize encoders
			liftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
			liftMotor.setSelectedSensorPosition(0,0,10);
			encodersInitialized = true;
		}
			liftPosition = liftMotor.getSelectedSensorPosition(0);
			
			// Check the lower limit switch
			boolean lowPosition = lowerLimitSwitch.get();
			SmartDashboard.putBoolean("Lift - Home Switch", lowPosition);
			if (lowPosition) {
				liftMotor.setSelectedSensorPosition(0, 0, 10);
				// reset the encoder to 0
			}
		} catch (Exception ex) {
			
		}
	}
	
}
