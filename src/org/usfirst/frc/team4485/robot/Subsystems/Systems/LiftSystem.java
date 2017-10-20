package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

// TODO: Comments????

public class LiftSystem extends Subsystem {

	// The lift motors
	private CANTalon liftMotorMaster, liftMotorSlave;
	
	// Control Variables
	private boolean brake = false;
	private boolean lift = false;
	private double power = 0.0;
	
	@Override
	protected void initSystem() {
		liftMotorMaster = new CANTalon(id.liftMotorMaster);
		liftMotorSlave = new CANTalon(id.liftMotorSlave);
		
		liftMotorMaster.reverseOutput(false);
		liftMotorSlave.changeControlMode(TalonControlMode.Follower);
		liftMotorSlave.set(liftMotorMaster.getDeviceID());
		liftMotorSlave.reverseOutput(false);
		
		// Set braking
		liftMotorMaster.enableBrakeMode(brake);
		liftMotorSlave.enableBrakeMode(brake);
	}

	@Override
	protected void updateSystem() {
		if (lift) liftMotorMaster.set(power);
		else liftMotorMaster.set(0);
	}

	@Override
	protected void killSystem() {
		setLift(false);
		update();
	}

	@Override
	protected void errorHandler() {
		// TODO Auto-generated method stub
		
	}
	
	// Control functions
	public void setLift(boolean _lift) { lift = _lift; }
	public void setPower(double _power) { power = -Math.abs(_power); }
	
}