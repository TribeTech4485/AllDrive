package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;

public class CollectorSystem extends Subsystem{

	private double armSpeed = 0;
	private double clawSpeed = 0;
	
	private WPI_TalonSRX clawMotorLeft, clawMotorRight;
	private WPI_TalonSRX armMotorLeft, armMotorRight;
	
	private Solenoid armSolenoidRight_in, armSolenoidRight_out;
	private Solenoid armSolenoidLeft_in, armSolenoidLeft_out;
	
	@Override
	protected void initSystem() {
		armMotorLeft = new WPI_TalonSRX(id.armMotorLeft);
		armMotorRight = new WPI_TalonSRX(id.armMotorRight);
		clawMotorLeft = new WPI_TalonSRX(id.clawMotorLeft);
		clawMotorRight = new WPI_TalonSRX(id.clawMotorRight);
		
		armSolenoidRight_in = new Solenoid(id.armSolenoidsModule, id.armSolenoidRight_in);
		armSolenoidRight_out = new Solenoid(id.armSolenoidsModule, id.armSolenoidRight_out);
		armSolenoidLeft_in = new Solenoid(id.armSolenoidsModule, id.armSolenoidLeft_in);
		armSolenoidLeft_out = new Solenoid(id.armSolenoidsModule, id.armSolenoidLeft_out);
	}

	@Override
	protected void updateSystem() {
		updateMotorControl();
	}

	@Override
	protected void killSystem() {}

	@Override
	protected void errorHandler() {}
	
	public void moveTheArm(double arm) {
		armSpeed = arm;
	}
	
	private void updateMotorControl() {	
		armMotorLeft.set(armSpeed);
		armMotorRight.set(-armSpeed);
		clawMotorLeft.set(clawSpeed);
		clawMotorLeft.set(-clawSpeed);
	}
}
