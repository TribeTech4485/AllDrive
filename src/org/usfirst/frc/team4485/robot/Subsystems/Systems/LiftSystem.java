package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Robot;
import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class LiftSystem extends Subsystem{

	private double liftSpeed = 0;
	private boolean reverseInput = false;
		
	private WPI_TalonSRX liftMotor;
	
	// Sensor values
	private boolean encodersInitialized = false;
	private double liftPosition = 0;
	
	@Override
	protected void initSystem() {
		liftMotor = new WPI_TalonSRX(id.liftMotor);
	}

	@Override
	protected void updateSystem() {
		updateSensors();
		updateMotorControl();
	}

	@Override
	protected void killSystem() {
		setLift(0);
		encodersInitialized = false;
		update();
	}

	@Override
	protected void errorHandler() {}
	
	//// Public control functions
	public void setLift(double lift) {
		liftSpeed = lift;
		if (lift > 0.1) lift = 0.1;
		if (lift < -0.1) lift = -0.1;
	}
	public double getPosition() {
		return liftPosition;
	}
	////
	
	private void updateMotorControl() {
		if (liftSpeed <-0.5) liftSpeed = -0.5;
		if (reverseInput) liftSpeed *= -1;		
		liftMotor.set(liftSpeed);
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
			boolean lowPosition = Robot.sensorController.getDigitalInput(id.liftLowerLimitSwitch);
			if (lowPosition) {
				liftMotor.setSelectedSensorPosition(0, 0, 10);
				// reset the encoder to 0
			}
		} catch (Exception ex) {
			
		}
	}
	
}
