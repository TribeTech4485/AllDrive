package org.usfirst.frc.team4485.robot.Subsystems;

import org.usfirst.frc.team4485.robot.RobotIndexing;
import org.usfirst.frc.team4485.robot.Subsystems.Systems.PowerHandlerSystem;

import edu.wpi.first.wpilibj.AnalogInput;


public class SensorController {
	
	RobotIndexing id;
	
	// Create your sensor objects here
	public AnalogInput cameraDistanceSensor;
	public PowerHandlerSystem powerHandlerSystem;
	
	// Create the value objects here. You will update these with update()
	//private double gearOpticalSensorVal;
	
	public SensorController() {
		id = new RobotIndexing();
		init();
	}
	
	private void init() {
		// Here is where you initialize sensors
		cameraDistanceSensor = new AnalogInput(id.cameraDistanceSensor);
		powerHandlerSystem = new PowerHandlerSystem();
		powerHandlerSystem.setID(100);
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
}