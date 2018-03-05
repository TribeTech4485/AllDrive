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
	AutoProgram driveToRight_center;
	AutoProgram driveToLeft_center;
	
	public AutonomousControl(SubsystemsControl subsystems) {
				
		cubeFollow = new CubeFollow(subsystems);
		gyroTest = new GyroTest(subsystems);
		zeroYaw = new ZeroYaw(subsystems);
		networkTest = new NetworkTest(subsystems);
		driveForTimeGorilla = new DriveForTimeGorilla(subsystems);
		sensorsTest = new SensorsTest(subsystems);
		liftPIDTune = new LiftPIDTune(subsystems);
		driveDistanceTest = new DriveDistanceTest(subsystems);
		driveAngleTest = new DriveAngleTest(subsystems);
		driveToRight_center = new DriveToRight_Center(subsystems);
		driveToLeft_center = new DriveToLeft_Center(subsystems);
		
		autoChooser.addDefault("None", null);
		autoChooser.addObject("Example", example);
		
		autoChooser.addObject("Gyro Test", gyroTest);
		autoChooser.addObject("Cube Follow", cubeFollow);
		autoChooser.addObject("Test Serial", testSerial);
		autoChooser.addObject("Zero Yaw", zeroYaw);
		autoChooser.addObject("NetworkTest", networkTest);
		autoChooser.addObject("Auto Gorilla", driveForTimeGorilla);
		autoChooser.addObject("Sensors Test", sensorsTest);
		autoChooser.addObject("Lift PID Tune", liftPIDTune);
		autoChooser.addObject("Drive Distance Test", driveDistanceTest);
		autoChooser.addObject("Drive Angle Test", driveAngleTest);
		autoChooser.addObject("Drive To Right - Center", driveToRight_center);
		autoChooser.addObject("Drive To Left - Center", driveToLeft_center);
		
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