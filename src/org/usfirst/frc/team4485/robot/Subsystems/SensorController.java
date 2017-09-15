package org.usfirst.frc.team4485.robot.Subsystems;

import org.usfirst.frc.team4485.robot.RobotIndexing;
import edu.wpi.first.wpilibj.AnalogInput;


public class SensorController {
	
	RobotIndexing id;
	
	// Create your sensor objects here
	AnalogInput gearOpticalSensor;
	
	// Create the value objects here. You will update these with update()
	//private double gearOpticalSensorVal;
	
	public SensorController() {
		id = new RobotIndexing();
		init();
	}
	
	private void init() {
		// Here is where you initialize sensors
		gearOpticalSensor = new AnalogInput(id.gearOpticalSensor);
	}
	
	public void update() {
		
	}
}