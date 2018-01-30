package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CubeFollow extends AutoProgram {

	public CubeFollow(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}
	
	// Serial
	private String parsedInput = "";
	private String lastGoodParse = "!";
	
	// Angles and sizes
	private double angleOffset = 0.0;
	
	// Driving Speeds
	private double baseSpeed = 0.3;
	private double percentagePerDegree = baseSpeed / 90;
	
	@Override
	protected void run() {
		// TODO Auto-generated method stub
		subsystems.serialSystem.update();
		parsedInput = subsystems.serialSystem.getParsedAngleInput();
		
		SmartDashboard.putString("Last Good Parse", lastGoodParse);
		
		if (parsedInput != "") {
			lastGoodParse = parsedInput;
			subsystems.serialSystem.clearSerial();
		}
		
		if (!lastGoodParse.contains("!")) {
			try {
				if (!lastGoodParse.contains("!")) {
					angleOffset = Double.parseDouble(lastGoodParse);
					//System.out.println("Warning: Parse angle offset value is " + angleOffset + ". Target offset is " + absTargetOffset + ".");
				}
			} catch (Exception ex) {
				System.out.println("Error parsing serial input: " + ex.getMessage());
			}
		}
		
		if (!lastGoodParse.contains("!")) {
			// Do some math
			
			
			double driveMod = Math.abs(angleOffset * percentagePerDegree);
			double leftDriveMod, rightDriveMod;
			if (angleOffset < 0) {
				leftDriveMod = driveMod * -1;
				rightDriveMod = driveMod;
			} else {
				leftDriveMod = driveMod;
				rightDriveMod = driveMod * -1;
			}
			
			// Untested drive code for auto!!
			//subsystems.driveSystem.drive4Motors(-(leftDriveMod), -(rightDriveMod));
			subsystems.driveSystem.drive4Motors(-(baseSpeed + leftDriveMod), -(baseSpeed + rightDriveMod));
			SmartDashboard.putNumber("Drive Mod Left", (leftDriveMod));
			SmartDashboard.putNumber("Drive Mod Right", (rightDriveMod));
			SmartDashboard.putNumber("Drive Total Left", -(baseSpeed + leftDriveMod));
			SmartDashboard.putNumber("Drive Total Right", -(baseSpeed + rightDriveMod));	
			
		} else {
			subsystems.driveSystem.drive4Motors(0, 0);
		}
		subsystems.driveSystem.update();
	}
	
}
