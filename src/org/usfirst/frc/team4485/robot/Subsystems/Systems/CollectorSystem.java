package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

public class CollectorSystem extends Subsystem{
	// Motor Control Values
	private double intakeSpeed = 1.0;
	private double expelSpeed = -1.0;
	private double setSpeed = 0;
	private boolean intake = false, expel = false;
	
	// Pneumatic Control Values
	private boolean armsOut = false;
	private boolean rightArmOut = false;
	
	private WPI_TalonSRX collectorMotorLeft, collectorMotorRight;
	
	// Victors (control them if they are present)
	private Victor collectorVictorLeft, collectorVictorRight;
	
	private Solenoid armSolenoidRight_in, armSolenoidRight_out;
	private Solenoid armSolenoid_in, armSolenoid_out;
	
	@Override
	protected void initSystem() {
		// Initialize Talons
		collectorMotorLeft = new WPI_TalonSRX(id.collectorMotorLeft);							// (Talon CAN ID)
		collectorMotorRight = new WPI_TalonSRX(id.collectorMotorRight);
		
		// Initialize PWM for Victors
		collectorVictorLeft = new Victor(id.collectorVictorLeft);								// (PWM pin)
		collectorVictorRight = new Victor(id.collectorVictorRight);
		
		// Initialize Pneumatics
		// TODO: replace with raise lower solenoids
		armSolenoidRight_in = new Solenoid(id.armSolenoidsModule, id.armSolenoidRight_in);		// (module, channel on module)
		armSolenoidRight_out = new Solenoid(id.armSolenoidsModule, id.armSolenoidRight_out);
		
		armSolenoid_in = new Solenoid(id.armSolenoidsModule, id.armSolenoid_in);
		armSolenoid_out = new Solenoid(id.armSolenoidsModule, id.armSolenoid_out);
	}

	@Override
	protected void updateSystem() {
		updateMotorControl();
		updatePneumaticControl();
	}

	@Override
	protected void killSystem() {}

	@Override
	protected void errorHandler() {}
	
	//// Public control functions ----
	// Motor Control Interface Functions
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
	
	// Pneumatic Control Interface Functions
	public void setArms(boolean out) {
		armsOut = out;
	}
	//// ----
	
	private void updateMotorControl() {
		if (intake) setSpeed = intakeSpeed;
		else if (expel) setSpeed = expelSpeed;
		else setSpeed = 0;
		collectorMotorLeft.set(setSpeed);
		collectorMotorRight.set(-setSpeed);
		
		// Update Victors
		collectorVictorLeft.set(setSpeed);
		collectorVictorRight.set(-setSpeed);
	}
	private void updatePneumaticControl() {
		armSolenoid_in.set(!armsOut);
		armSolenoid_out.set(armsOut);
	}
}
