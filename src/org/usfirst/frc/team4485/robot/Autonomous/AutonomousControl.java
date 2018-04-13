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
	
	/*
	AutoProgram cubeFollow;
	AutoProgram testSerial;
	AutoProgram gyroTest;
	AutoProgram zeroYaw;
	AutoProgram networkTest;
	AutoProgram driveForTimeGorilla;
	AutoProgram sensorsTest;
	AutoProgram liftPIDTune;
	AutoProgram driveDistanceTest;
	AutoProgram driveAngleTest;
	AutoProgram liftAutoTest;
	AutoProgram driveToRight_center;
	AutoProgram driveToLeft_center;
	AutoProgram baseLine;
	AutoProgram driveStraightTest;
	*/
	
	AutoProgram cubeTrackingSystemTest;
	
	AutoProgram centerAuto;
	AutoProgram rightAuto;
	AutoProgram leftAuto;
	

	public AutonomousControl(SubsystemsControl subsystems) {
		
		/*
		cubeFollow = new CubeFollow(subsystems);
		gyroTest = new GyroTest(subsystems);
		zeroYaw = new ZeroYaw(subsystems);
		networkTest = new NetworkTest(subsystems);
		driveForTimeGorilla = new DriveForTimeGorilla(subsystems);
		sensorsTest = new SensorsTest(subsystems);
		liftPIDTune = new LiftPIDTune(subsystems);
		driveDistanceTest = new DriveDistanceTest(subsystems);
		driveAngleTest = new DriveAngleTest(subsystems);
		liftAutoTest = new LiftAutoTest(subsystems);
		driveToRight_center = new DriveToRight_Center(subsystems);
		driveToLeft_center = new DriveToLeft_Center(subsystems);
		baseLine = new BaseLine(subsystems);
		driveStraightTest = new DriveStraightTest(subsystems);
		*/
		
		
		cubeTrackingSystemTest = new CubeTrackingSystemTest(subsystems);
		
		centerAuto = new CenterAuto(subsystems);
		rightAuto = new RightAuto(subsystems);
		leftAuto = new LeftAuto(subsystems);
		
		autoChooser.addDefault("None", null);
		autoChooser.addObject("Example", example);
		
		/*
		autoChooser.addObject("Gyro Test", gyroTest);
		autoChooser.addObject("Cube Follow", cubeFollow);
		autoChooser.addObject("BaseLine", baseLine);
		//autoChooser.addObject("Test Serial", testSerial);
		//autoChooser.addObject("Zero Yaw", zeroYaw);
		//autoChooser.addObject("NetworkTest", networkTest);
		//autoChooser.addObject("Auto Gorilla", driveForTimeGorilla);
		autoChooser.addObject("Sensors Test", sensorsTest);
		autoChooser.addObject("Lift PID Tune", liftPIDTune);
		//autoChooser.addObject("Drive Distance Test", driveDistanceTest);
		//autoChooser.addObject("Drive Angle Test", driveAngleTest);
		autoChooser.addObject("Lift Auto Test", liftAutoTest);
		autoChooser.addObject("Drive To Right - Center", driveToRight_center);
		autoChooser.addObject("Drive To Left - Center", driveToLeft_center);
		autoChooser.addObject("Drive Straight Test", driveStraightTest);
		*/
		
		autoChooser.addObject("System Test - Cube Tracking", cubeTrackingSystemTest);
		
		autoChooser.addObject("Center Auto", centerAuto);
		autoChooser.addObject("Left Auto", leftAuto);
		autoChooser.addObject("Right Auto", rightAuto);
		
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
		autoChooser.getSelected().reset();
		System.out.println("Warning: Autonomous is disabled.");
	}
	
	public void runAuto() {
		if (autoChooser.getSelected() == null) return;
		autoChooser.getSelected().runIncomplete();
	}
	
}