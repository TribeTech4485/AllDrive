package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class DriveToPeg extends AutoProgram {

	public DriveToPeg(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	// Distance available in front of the robot
	private double cameraDistance_in = -1.0;
	
	// Serial
	private String parsedInput = "";
	private String lastGoodInput = "!";
	
	// Angles and sizes
	private double lastAngleOffset = -1.0;
	private double currentAngleOffset = 0.0;
	private double absTargetOffset = 5.0;
	
	private boolean turnState = true;	// False if the robot has completed current turn
	
	// Driving Speeds
	private double farFromSpeed = -0.25;	// Negative because the camera is mounted on the back right now
	private double closeToSpeed = -0.15;
	
	@Override
	protected void run() {
		// Update the serial system
		subsystems.serialSystem.update();
		parsedInput = subsystems.serialSystem.getParsedInput();
		
		//System.out.println("Last Good Parse: " + lastGoodInput);
		
		// Put this in the Serial Controller later
		if (parsedInput != "") {
			lastGoodInput = parsedInput;
			subsystems.serialSystem.clearSerial();
		}
		
		// Get the distance from the camera distance sensor
		// Scaling on this sensor with 5v is ~9.8mV/in
		// Analog is 0-1024 on the RoboRio so divide by 2
		cameraDistance_in = subsystems.sensorController.cameraDistanceSensor.getValue() / 2;
	
		System.out.println("Camera Distance: " + cameraDistance_in);
		
		if (cameraDistance_in <= 72) {
			if (cameraDistance_in >= 30) {
				subsystems.driveSystem.drive4Motors(closeToSpeed, closeToSpeed);
			} else {
				subsystems.driveSystem.drive4Motors(0, 0);
			}
		} else {
			if (!lastGoodInput.contains("!")) {
				try {
					currentAngleOffset = Double.parseDouble(lastGoodInput);
				} catch (Exception ex) {
					System.out.println("Error Parsing Serial!");
				}
				
				if (currentAngleOffset >= -absTargetOffset && currentAngleOffset <= absTargetOffset) {
					subsystems.driveSystem.drive4Motors(farFromSpeed, farFromSpeed);
				} else {
					if (turnState) turnState = subsystems.driveSystem.turnToAnglePID(currentAngleOffset);
				}
			} else {
				subsystems.driveSystem.drive4Motors(0, 0);
			}
			if (currentAngleOffset != lastAngleOffset) turnState = true;
		}
	}
	
}