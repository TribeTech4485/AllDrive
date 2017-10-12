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
	private double slowDistMin_in = 80;
	private double slowDistMax_in = 200;
	
	// Serial
	private String parsedAngleInput = "";
	private String lastGoodAngleInput = "!";
	
	private String parsedDistanceInput = "";
	private String lastGoodDistanceInput = "-1.00";
	
	// Angles and sizes
	private double lastAngleOffset = -1.0;
	private double currentAngleOffset = 0.0;
	private double absTargetOffset = 1.5;
	
	private boolean turnState = true;	// False if the robot has completed current turn
	
	// Driving Speeds
	private double farFromSpeed = 0.25; //0.50;	// Negative because the camera is mounted on the back right now
	private double closeToSpeed = 0.15; //0.30;	// Not negative anymore because the camera is on the front now
	
	@Override
	protected void run() {
		// Update the serial system
		subsystems.serialSystem.update();
		parsedAngleInput = subsystems.serialSystem.getParsedAngleInput();
		
		//System.out.println("Last Good Parse: " + lastGoodAngleInput);
		
		// Put this in the Serial Controller later
		if (parsedAngleInput != "") {
			lastGoodAngleInput = parsedAngleInput;
			//subsystems.serialSystem.clearSerial();
		}
		
		parsedDistanceInput = subsystems.serialSystem.getParsedDistanceInput();
		if (parsedDistanceInput != "") {
			lastGoodDistanceInput = parsedDistanceInput;
		}
		
		// Get the distance from the camera distance sensor
		// Scaling on this sensor with 5v is ~9.8mV/in
		// Analog is 0-1024 on the RoboRio so divide by 2
		
		// Don't use the sensor right now
		//cameraDistance_in = subsystems.sensorController.cameraDistanceSensor.getValue() / 2;
		
		// Get the distance over Serial for now
		try {
			cameraDistance_in = Double.parseDouble(lastGoodDistanceInput);
		} catch (Exception ex) {
			System.out.println("Error Parsing Serial!");
		}
		if (cameraDistance_in < 0) {
			cameraDistance_in = slowDistMax_in + 1;
		}
	
		System.out.println("Camera Distance: " + cameraDistance_in);
		
		if (cameraDistance_in <= slowDistMax_in) {
			if (cameraDistance_in >= slowDistMin_in) {
				subsystems.driveSystem.drive4Motors(closeToSpeed, closeToSpeed);
			} else {
				subsystems.driveSystem.drive4Motors(0, 0);
			}
		} else {
			if (!lastGoodAngleInput.contains("!")) {
				try {
					currentAngleOffset = Double.parseDouble(lastGoodAngleInput);
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