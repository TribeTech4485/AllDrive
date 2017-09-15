package org.usfirst.frc.team4485.robot.Tests;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;

public class RotateToAngleAuto {
	SubsystemsControl subsystems;
	AHRS ahrs;
	
	public RotateToAngleAuto(SubsystemsControl _subsystems) {
		subsystems = _subsystems;
		
		try {
			ahrs = new AHRS(SPI.Port.kMXP);
			Timer.delay(0.020);
			ahrs.zeroYaw();
		} catch(RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX MXP: " + ex.getMessage(), true);
		}
	}
	
	//// Public functions
	public void run() {
		Timer.delay(0.020);
		double yaw = ahrs.getYaw();
		System.out.println("YAW: " + yaw);
	}
	////
}