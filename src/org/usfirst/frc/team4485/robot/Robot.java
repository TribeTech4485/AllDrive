package org.usfirst.frc.team4485.robot;

//import edu.wpi.first.wpilibj.SampleRobot;

import org.usfirst.frc.team4485.robot.bots.MichaelBot;

import org.usfirst.frc.team4485.robot.Autonomous.AutonomousControl;
import org.usfirst.frc.team4485.robot.Subsystems.SerialController;
import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;
import org.usfirst.frc.team4485.robot.Subsystems.SensorController;


public class Robot extends MichaelBot {
	public static SerialController serialController;		// Class that controls basic serial
	public static SensorController sensorController;		// Class that controls sensors	-	This is not very well tested or implemented at this point
	
	UserControl userControl;				// Class for controlling get user input
	SubsystemsControl subsystems;			// Class that handles and updates all the subsytems
	
	TeleOpControl teleOpControl;			// Class for basic control using user input
	AutonomousControl autonomousControl;	// Class that handles all the autonomous programs and starts the selected one
	
	public Robot() {
	}

	@Override
	public void robotInit() {
		serialController = new SerialController();	// Initialize serialController as new SerialController
		sensorController = new SensorController();	// Again, this is not well implemented yet and might get removed. This static variable is referred to in the SubsystemsControl class
		
		userControl = new UserControl();		// Initialize userControl as new UserControl();
		subsystems = new SubsystemsControl();	// Initialize subsystems as new SubsystemsControl
		
		teleOpControl = new TeleOpControl(subsystems, userControl);		// Initialize teleOpControl as new TeleOpControl, using the subsystems class and the userControl class
		autonomousControl = new AutonomousControl(subsystems);			// Initialize autonomousControl as new AutonomousControl, using the subsystems class
	}

	@Override
	public void autonomous() {
		boolean autoStopped = false;		// true = don't run auto
		//autonomousControl = new AutonomousControl(subsystems);
		while(isEnabled() && isAutonomous() && !autoStopped) {
			autonomousControl.runAuto();	// Run autonomous iteratively
											// (The reason it's in a while loop)
											// Read more about running auto in the AutoProgram class
		}
		autonomousControl.disableAuto();	// Stop auto
		//subsystems.killAll;
	}

	@Override
	public void operatorControl() {		
		while(isEnabled() && isOperatorControl()) {		// Run while teleOp is enabled
			teleOpControl.update();			// Update teleOp
		}
	}
	
	@Override
	public void disabled() {
		subsystems.killAll();		// Kill all subsystems on disable
	}

	@Override
	public void test() {
		
	}
}
