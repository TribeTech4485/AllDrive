package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BallChase extends AutoProgram {

	public BallChase(SubsystemsControl _subsystems) {
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
	private double lastAngleOffset = -1.0;
	private double angleOffset = 0.0;
	private double absTargetOffset = 7.0;
	
	private boolean turnState = true;
	
	// Driving Speeds
	double maxspeed = -0.3;

	@Override
	protected void run() {
		// TODO Auto-generated method stub
		subsystems.serialSystem.update();
		parsedInput = subsystems.serialSystem.getParsedInput();
		
		SmartDashboard.putString("Last Good Parse", lastGoodParse);
		
		if (parsedInput != "") {
			lastGoodParse = parsedInput;
			subsystems.serialSystem.clearSerial();
		}
		
		if (!lastGoodParse.contains("!")) {
			try {
				if (!lastGoodParse.contains("!")) {
					angleOffset = Double.parseDouble(lastGoodParse);
					System.out.println("Warning: Parse angle offset value is " + angleOffset + ". Target offset is " + absTargetOffset + ".");
				}
			} catch (Exception ex) {
				System.out.println("Error parsing serial input: " + ex.getMessage());
			}
		}
		
		if (!lastGoodParse.contains("!")) {
			if (angleOffset >= -absTargetOffset && angleOffset <= absTargetOffset) {
					subsystems.driveSystem.drive4Motors(maxspeed, maxspeed);
			} else {
				if (turnState) {
					turnState = subsystems.driveSystem.turnToAnglePID(angleOffset);
				}
				if (angleOffset != lastAngleOffset) {
					turnState = true;
				}
			}
		} else {
			subsystems.driveSystem.drive4Motors(0, 0);
		}
	}
	
}