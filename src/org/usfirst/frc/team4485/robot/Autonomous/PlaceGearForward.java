package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Robot;
import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

public class PlaceGearForward extends AutoProgram {

	public PlaceGearForward(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		Robot.sensorController.ahrs.zeroYaw();
		auto_timeLimit = 3950;
	}
	
	@Override
	protected void run() {
		//if (auto_duration < 3000) {
		//	subsystems.driveSystem.centerToAngle(0);
		//	subsystems.driveSystem.drive4Motors(0.5, 0.5);
		//	subsystems.driveSystem.centerToAngle(0);
		//} else {
		//	subsystems.driveSystem.drive4Motors(0.3, 0.3);
		//}
		double startTime = System.currentTimeMillis();
		while((System.currentTimeMillis() - startTime) < 3000) {
			subsystems.driveSystem.centerToAnglePID(0);
			subsystems.driveSystem.update();
			subsystems.driveSystem.drive4Motors(0.5, 0.5);
			subsystems.driveSystem.update();
			subsystems.driveSystem.centerToAnglePID(0);
			subsystems.driveSystem.update();
		}
		subsystems.driveSystem.drive4Motors(0, 0);
		
		// Expell the gear
		startTime = System.currentTimeMillis();
		subsystems.boxPneumaticSystem.setDoorOut(true);
		subsystems.boxPneumaticSystem.update();
		while((System.currentTimeMillis() - startTime) < 500);
		startTime = System.currentTimeMillis();
		subsystems.boxPneumaticSystem.setPusherOut(true);
		subsystems.boxPneumaticSystem.update();
		while((System.currentTimeMillis() - startTime) < 300);
		startTime = System.currentTimeMillis();
		while((System.currentTimeMillis() - startTime) < 1500) {
			subsystems.driveSystem.drive4Motors(-0.3, -0.3);
			subsystems.driveSystem.update();
		}
		subsystems.boxPneumaticSystem.setDoorOut(false);
		subsystems.boxPneumaticSystem.update();
		subsystems.driveSystem.drive4Motors(0, 0);
		subsystems.driveSystem.update();
		auto_complete = true;
		
		/*subsystems.driveSystem.centerToAngle(0);
		auto_complete = subsystems.driveSystem.driveDistance(0.5, 0.5, 96);
		subsystems.driveSystem.driveDistance(0.5,0.5,96);
		subsystems.driveSystem.centerToAngle(0);
		
		auto_complete = (subsystems.sensorManager.getAnalogSensorValue(subsystems.sensorManager.gearOpticalSensor) >= 30) || auto_complete;
		//System.out.println(subsystems.sensorManager.getAnalogSensorValue(subsystems.sensorManager.gearOpticalSensor));
		//System.out.println("cm: " + subsystems.sensorManager.getOpticalSensorDistance_cm(subsystems.sensorManager.gearOpticalSensor));
		Timer.delay(0.5);*/
	}
	
}