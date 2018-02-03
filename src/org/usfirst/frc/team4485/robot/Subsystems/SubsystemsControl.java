package org.usfirst.frc.team4485.robot.Subsystems;

import org.usfirst.frc.team4485.robot.Robot;
//import org.usfirst.frc.team4485.robot.Subsystems.Systems.PowerHandlerSystem;
import org.usfirst.frc.team4485.robot.Subsystems.Systems.SerialSystem;
import org.usfirst.frc.team4485.robot.Subsystems.Systems.ShifterPneumaticSystem;
import org.usfirst.frc.team4485.robot.Subsystems.Systems.DriveSystem;
import org.usfirst.frc.team4485.robot.Subsystems.Systems.LiftSystem;
//import org.usfirst.frc.team4485.robot.Subsystems.Systems.UniformDrive;


//import com.kauailabs.navx.frc.AHRS;

//import edu.wpi.first.wpilibj.DriverStation;
//import edu.wpi.first.wpilibj.SPI;

// Master control for all subsystems

public class SubsystemsControl {
	// Subsystems
	//public UniformDrive driveSystem;
	public DriveSystem driveSystem;
	public ShifterPneumaticSystem shifterPneumaticSystem;
	public SerialSystem serialSystem;
	public LiftSystem liftSystem;
	//public PowerHandlerSystem powerHandlerSystem;
	
	// This System is weird. To use this one it needs to be declared only once so we will just make this a reference to a static SensorController in Robot.java
	public SensorController sensorController;
	
	public SubsystemsControl() {
		createAll();
		
		//try {
		//	driveSystem.ahrs = new AHRS(SPI.Port.kMXP);
		//} catch (RuntimeException ex) {
		//	DriverStation.reportError("Error AHRS: " + ex.getMessage(), true);
		//}
	}
	
	public void killAll() {
		// Kill each subsystem 
		driveSystem.killAll();
		
		//System.out.println("WARNING: Subsystems - killAll() was called!");
	}
	
	private void createAll() {
		sensorController = Robot.sensorController;	// This is where we refer to Robot
		
		// Create the other systems like normal
		//driveSystem = new UniformDrive();
		driveSystem = new DriveSystem();
		driveSystem.setID(0);	// Set the ID so the system doesn't complain and fill up the log.
								// I'll probably remove that or implement it in a better way
		
		
		
		shifterPneumaticSystem = new ShifterPneumaticSystem();
		shifterPneumaticSystem.setID(2);
		
		serialSystem = new SerialSystem();
		serialSystem.setID(4);
		
		liftSystem = new LiftSystem();
		liftSystem.setID(420);
		
		//powerHandlerSystem = new PowerHandlerSystem();
		//powerHandlerSystem.setID(5);
	}
}