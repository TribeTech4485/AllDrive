package org.usfirst.frc.team4485.robot.Autonomous;

//import org.usfirst.frc.team4485.robot.Autonomous.Programs.*;
import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousControl {	
	SendableChooser<AutoProgram> autoChooser = new SendableChooser<>();
	
	// TODO:
	//	Replace old auto programs with new ones
	//  Don't run these programs, they do not exist
	
	AutoProgram example;
	AutoProgram driveDistance;	// This one exists, go ahead and run it
	AutoProgram turnToAngle;	// This one too
	AutoProgram testSerial;		// And this one
	AutoProgram ballChase;		// this one also
	AutoProgram driveToPeg;		// Just don't use example, it's the only one that doesn't exist
	
	AutoProgram driveToZone;
	AutoProgram placeGearForward;
	
	public AutonomousControl(SubsystemsControl subsystems) {	
		driveDistance = new DriveDistance(subsystems);
		turnToAngle = new TurnToAngle(subsystems);
		testSerial = new TestSerial(subsystems);
		ballChase = new BallChase(subsystems);
		driveToPeg = new DriveToPeg(subsystems);
		
		driveToZone = new DriveToZone(subsystems);
		placeGearForward = new PlaceGearForward(subsystems);
		
		autoChooser.addDefault("None", null);
		autoChooser.addObject("Example", example);
		autoChooser.addObject("Drive Distance", driveDistance);
		autoChooser.addObject("Turn To Angle", turnToAngle);
		autoChooser.addObject("Test Serial", testSerial);
		autoChooser.addObject("Ball Chase", ballChase);
		autoChooser.addObject("Drive To Peg", driveToPeg);
		autoChooser.addObject("DriveToZone", driveToZone);
		autoChooser.addObject("Place Gear Forward", placeGearForward);
		
		//autoChooser.addObject("Test Auto", testAuto);
		//autoChooser.addObject("Rotate to Angle", rotateToAngle);
		//autoChooser.addObject("Rotate Test", rotateTest);
		//autoChooser.addObject("Drive Straight", driveStraight);
		//autoChooser.addObject("Time Drive Gear", timeDriveGear);
		//autoChooser.addObject("Time Drive Line", timeDriveLine);
		
		SmartDashboard.putData("Auto Selector", autoChooser);
	}
	
	public void disableAuto() {
		if (autoChooser.getSelected() == null) return;
		autoChooser.getSelected().disable();
		System.out.println("Warning: Autonomous is disabled.");
	}
	
	public void runAuto() {
		if (autoChooser.getSelected() == null) return;
		autoChooser.getSelected().runIncomplete();
	}
}