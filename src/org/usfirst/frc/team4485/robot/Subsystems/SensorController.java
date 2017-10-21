package org.usfirst.frc.team4485.robot.Subsystems;

import org.usfirst.frc.team4485.robot.RobotIndexing;
import org.usfirst.frc.team4485.robot.Subsystems.Systems.PowerHandlerSystem;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SPI;


/*
 * TODO:
 * ------ COMMENTS --------
 */


public class SensorController {
	
	RobotIndexing id;
	
	// Create your sensor objects here
	public AnalogInput cameraDistanceSensor;
	public AHRS ahrs;
	public PowerHandlerSystem powerHandlerSystem;
	
	// Create the value objects here. You will update these with update()
	//private double gearOpticalSensorVal;
	private boolean ahrsError = false;
	public boolean ahrsYawZeroed = false;	// I don't think this is ever used, check that for me?
	
	private double leftDriveRPM = 0.0, rightDriveRPM = 0.0;
	
	public SensorController() {
		id = new RobotIndexing();
		init();
	}
	
	private void init() {
		// Here is where you initialize sensors
		cameraDistanceSensor = new AnalogInput(id.cameraDistanceSensor);
		
		powerHandlerSystem = new PowerHandlerSystem();
		powerHandlerSystem.setID(100);
		
		// Initialize the AHRS
		try {
			ahrs = new AHRS(SPI.Port.kMXP);
		} catch (Exception ex) {
			ahrsError = true;
			System.out.println("Warning: AHRS Error: " + ex.getMessage());
		}
	}
	
	public void update() {
		powerHandlerSystem.update();
	}
	
	public double getDistanceSensorDistance(AnalogInput sensor, double mV_in) {
		// TODO: Figure out scaling
		
		// For now just divide by 2
		if (sensor == null) return -1;
		return sensor.getValue() / 2;
	}
	
	public void setRPMs(double left, double right) {
		leftDriveRPM = left;
		rightDriveRPM = right;
	}
	
	public double getAverageRPM() {
		return (leftDriveRPM + rightDriveRPM) / 2;
	}
	
	public boolean isAHRSError() { return ahrsError; }
}