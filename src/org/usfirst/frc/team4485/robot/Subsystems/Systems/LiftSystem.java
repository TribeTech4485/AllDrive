package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class LiftSystem extends Subsystem{

	private double liftSpeed;
	private boolean reverseInput = false;
		
	private WPI_TalonSRX liftMotor;
	
	@Override
	protected void initSystem() {
		liftMotor = new WPI_TalonSRX(id.liftMotor);
	}

	@Override
	protected void updateSystem() {
		updateMotorControl();
		
	}

	@Override
	protected void killSystem() {}

	@Override
	protected void errorHandler() {}
	
	public void moveTheLift(double lift) {
		liftSpeed = lift;
	}
	
	private void updateMotorControl() {
		if (reverseInput) liftSpeed *= -1;		
		liftMotor.set(liftSpeed);
		
	}
}
