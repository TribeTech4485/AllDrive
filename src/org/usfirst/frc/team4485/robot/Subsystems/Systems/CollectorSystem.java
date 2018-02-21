package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;

public class CollectorSystem extends Subsystem{

	private double intakeSpeed = 1.0;
	private double expelSpeed = -1.0;
	private double setSpeed = 0;
	private boolean intake = false, expel = false;
	
	private WPI_TalonSRX armMotorLeft, armMotorRight;
	
	private Solenoid armSolenoidRight_in, armSolenoidRight_out;
	private Solenoid armSolenoidLeft_in, armSolenoidLeft_out;
	
	@Override
	protected void initSystem() {
		armMotorLeft = new WPI_TalonSRX(id.armMotorLeft);
		armMotorRight = new WPI_TalonSRX(id.armMotorRight);
		
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
	
	public void setIntake(boolean run) {
		intake = run;
	}
	public void setExpel(boolean run) {
		expel = run;
	}
	public void setIntakeSpeed(double speed) {
		intakeSpeed = speed;
	}
	public void setExpelSpeed(double speed) {
		expelSpeed = speed;
	}
	
	private void updateMotorControl() {
		if (intake) setSpeed = intakeSpeed;
		else if (expel) setSpeed = expelSpeed;
		else setSpeed = 0;
		armMotorLeft.set(setSpeed);
		armMotorRight.set(-setSpeed);
	}
}
