package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class TestSerial extends AutoProgram {
	
	public TestSerial(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	private String rawInput = "";
	private String parsedAngleInput = "";
	private String lastGoodAngleParse = "";
	
	private String parsedDistanceInput = "";
	private String lastGoodDistanceParse = "";
	
	@Override
	protected void run() {
		subsystems.serialSystem.update();
		rawInput = subsystems.serialSystem.getRawInput();
		parsedAngleInput = subsystems.serialSystem.getParsedAngleInput();
		
		if (parsedAngleInput != "") {
			lastGoodAngleParse = parsedAngleInput;
			//subsystems.serialSystem.clearSerial();
		}
		
		parsedDistanceInput = subsystems.serialSystem.getParsedDistanceInput();
		
		if (parsedDistanceInput != "") {
			lastGoodDistanceParse = parsedDistanceInput;
		}
		
		SmartDashboard.putString("Raw Serial", rawInput);
		SmartDashboard.putString("Parsed Angle Serial", parsedAngleInput);
		SmartDashboard.putString("Last Good Angle Parse", lastGoodAngleParse);
		SmartDashboard.putString("Parsed Distance Serial", parsedDistanceInput);
		SmartDashboard.putString("Last Good Distance Parse", lastGoodDistanceParse);
	}
	
}