package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class ClawSystem extends Subsystem{

	private double clawSpeed = 0;
		
	private WPI_TalonSRX clawMotorLeft;
	private WPI_TalonSRX clawMotorRight;
	
	@Override
	protected void initSystem() {
		clawMotorLeft = new WPI_TalonSRX(id.clawMotorLeft);
		clawMotorRight = new WPI_TalonSRX(id.clawMotorRight);
	}

	@Override
	protected void updateSystem() {
		updateMotorControl();
	}

	@Override
	protected void killSystem() {}

	@Override
	protected void errorHandler() {}
	
	public void moveTheClaw(double claw) {
		clawSpeed = claw;
	}
	
	private void updateMotorControl() {	
		clawMotorLeft.set(clawSpeed);
		clawMotorRight.set(-clawSpeed);
	}
}
