package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Robot;
import org.usfirst.frc.team4485.robot.Subsystems.SerialController;
import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

public class SerialSystem extends Subsystem {

	private SerialController serialController = Robot.serialController;
	
	// Text parsing
	String input = "";
	String parsedInput = "";
	String parsedIndex = "";
	
	@Override
	protected void initSystem() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void updateSystem() {
		// TODO Auto-generated method stub
		serialController.update();
		input = serialController.getInput();
		
		if (serialController.isError()) {
			createError(false, "SerialController has error.");
		}
		
		parsedInput = parseInput('a', 'e');
		if (parsedInput != "") serialController.clearSerialInput();
		//parsedIndex = parseInput('<', '>');
		
	}

	@Override
	protected void killSystem() {
		// TODO Auto-generated method stub
		serialController.close();
	}

	@Override
	protected void errorHandler() {
		// TODO Auto-generated method stub
		
	}
	
	public void clearSerial() {
		serialController.clearSerialInput();
	}
	
	public String parseInput(char beginFlag, char endFlag) {
		String parsed = "";
		int beginChar = -1, endChar = -1;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == beginFlag) beginChar = i;
			else if (input.charAt(i) == endFlag) endChar = i;
		}
		System.out.println("Begin: " + beginChar + " End: " + endChar);
		if (beginChar < 0 || endChar < 0) return "";
		for (int i = beginChar + 1; i < endChar; i++) {
			parsed += input.charAt(i);
		}
		
		return parsed;
	}
	
	// Getters
	public String getRawInput() {
		return input;
	}
	public String getParsedInput() {
		return parsedInput;
	}
	public String getParsedIndex() {
		return parsedIndex;
	}
}