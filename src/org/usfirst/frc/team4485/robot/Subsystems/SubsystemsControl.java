package org.usfirst.frc.team4485.robot.Subsystems;

import org.usfirst.frc.team4485.robot.Subsystems.Systems.SerialSystem;
import org.usfirst.frc.team4485.robot.Subsystems.Systems.UniformDrive;


import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;

// Master control for all subsystems

public class SubsystemsControl {
	// Subsystems
	public UniformDrive driveSystem;
	public SerialSystem serialSystem;
	
	public SubsystemsControl() {
		createAll();
		
		try {
			driveSystem.ahrs = new AHRS(SPI.Port.kMXP);
		} catch (RuntimeException ex) {
			DriverStation.reportError("Error AHRS: " + ex.getMessage(), true);
		}
	}
	
	public void killAll() {
		// Kill each subsystem 
		driveSystem.killAll();
		
		//System.out.println("WARNING: Subsystems - killAll() was called!");
	}
	
	private void createAll() {
		driveSystem = new UniformDrive();
		driveSystem.setID(0);
		
		serialSystem = new SerialSystem();
		serialSystem.setID(1);
	}
}