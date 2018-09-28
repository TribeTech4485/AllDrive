package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import edu.wpi.first.wpilibj.Victor;

public class HopperSystem extends Subsystem {

	Victor hopperMotor;
	double setVal = 0;
	
	@Override
	protected void initSystem() {
		// TODO Auto-generated method stub
		hopperMotor = new Victor(id.hopperMotor);
	}

	@Override
	protected void updateSystem() {
		// TODO Auto-generated method stub
		hopperMotor.set(setVal);
	}

	@Override
	protected void killSystem() {
		// TODO Auto-generated method stub
		setVal = 0;
		hopperMotor.set(0);
	}

	@Override
	protected void errorHandler() {
		// TODO Auto-generated method stub
		
	}
	
	public void setHopper(double set) {
		setVal = set;
	}

}
