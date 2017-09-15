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
	private String parsedInput = "";
	private String lastGoodParse = "";
	
	@Override
	protected void run() {
		subsystems.serialSystem.update();
		rawInput = subsystems.serialSystem.getRawInput();
		parsedInput = subsystems.serialSystem.getParsedInput();
		
		if (parsedInput != "") {
			lastGoodParse = parsedInput;
			subsystems.serialSystem.clearSerial();
		}
		
		SmartDashboard.putString("Raw Serial", rawInput);
		SmartDashboard.putString("Parsed Serial", parsedInput);
		SmartDashboard.putString("Last Good Parse", lastGoodParse);
	}
	
}