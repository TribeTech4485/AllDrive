package org.usfirst.frc.team4485.robot.Subsystems;

import org.usfirst.frc.team4485.robot.RobotIndexing;
import org.usfirst.frc.team4485.robot.Subsystems.Systems.PowerHandlerSystem;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;


/*
 * TODO:
 * ------ COMMENTS --------
 */


public class SensorController {
	
	RobotIndexing id;
	
	// Network Controller
	public NetworkController networkController;
	
	// Create your sensor objects here
	public AHRS ahrs;
	public PowerHandlerSystem powerHandlerSystem;
	
	private DigitalInput armLimitSwitchSensor_left, armLimitSwitchSensor_right;
	private AnalogInput armOpticalDistanceSensor, armUltrasonicDistanceSensor;
	private DigitalInput clawLimitSwitchSensor_left, clawLimitSwitchSensor_right;
	private AnalogInput clawOpticalDistanceSensor_left, clawOpticalDistanceSensor_right;
	
	
	// Create the value objects here. You will update these with update()
	//private double gearOpticalSensorVal;
	private boolean ahrsError = false;
	public boolean ahrsYawZeroed = false;	// I don't think this is ever used, check that for me?
	public double ahrsYawMultiplier = -1;	// Invert the YAW when the GYRO is upside down
	
	private double leftDriveRPM = 0.0, rightDriveRPM = 0.0;
	private double networkNumRcvd = 0;
	
	// Current limits for systems
	private double driveSystemVoltageLowLimit = 10.0, driveSystemVoltageHighLimit = 11.0, reductionLimit = 0.0;
	
	public SensorController() {
		id = new RobotIndexing();
		init();
	}
	
	private void init() {
		// Here is where you initialize sensors
		powerHandlerSystem = new PowerHandlerSystem();
		powerHandlerSystem.setID(100);
		
		armLimitSwitchSensor_left = new DigitalInput(id.armLimitSwitchSensor_left);
		armLimitSwitchSensor_right = new DigitalInput(id.armLimitSwitchSensor_right);
		armOpticalDistanceSensor = new AnalogInput(id.armOpticalDistanceSensor);
		armUltrasonicDistanceSensor = new AnalogInput(id.armUltrasonicDistanceSensor);
		
		clawLimitSwitchSensor_left = new DigitalInput(id.clawLimitSwitchSensor_left);
		clawLimitSwitchSensor_right = new DigitalInput(id.clawLimitSwitchSensor_right);
		clawOpticalDistanceSensor_left = new AnalogInput(id.clawOpticalDistanceSensor_left);
		clawOpticalDistanceSensor_right = new AnalogInput(id.clawOpticalDistanceSensor_right);
		
		// Initialize the AHRS
		/*
		try {
			//ahrs = new AHRS(SPI.Port.kMXP);
			ahrs = new AHRS(SerialPort.Port.kMXP);
		} catch (Exception ex) {
			ahrsError = true;
			System.out.println("Warning: AHRS Error: " + ex.getMessage());
		}
		*/
	}
	private boolean isAHRSYawZeroed() {
		if (Math.abs(ahrs.getYaw()) < 1) return true;
		return false;
	}
	
	public void update() {
		powerHandlerSystem.update();
	}
	
	public double getDistanceSensorDistance(AnalogInput sensor, double mV_in) {
		// TODO: Figure out scaling
		
		// For now just divide by 2
		if (sensor == null) return -1;
		return sensor.getValue() / 2;
	}
	
	public void setRPMs(double left, double right) {
		leftDriveRPM = left;
		rightDriveRPM = right;
	}
	
	public double getAverageRPM() {
		return (leftDriveRPM + rightDriveRPM) / 2;
	}
	
	public boolean isAHRSError() { return ahrsError; }
	public boolean zeroAHRSYaw() {
		for (int i = 0; i < 20; i++) {
			ahrs.zeroYaw();
			if (isAHRSYawZeroed()) return true;
		}
		return false;
	}
	public double getAHRSYaw() { if (!isAHRSError()) return ahrs.getYaw() * ahrsYawMultiplier; else return 0;}
	
	// Power limit control functions
	public double getDrivePowerLimiter() {
		if (powerHandlerSystem.getPDPTotalVoltage() > driveSystemVoltageHighLimit) return 0.0;
		
		System.out.println(powerHandlerSystem.getPDPTotalVoltage());
		double reduction = driveSystemVoltageLowLimit / powerHandlerSystem.getPDPTotalVoltage();
		if (reduction < reductionLimit) reduction = reductionLimit;
		return reduction;
	}
	
	public void setNetworkNumRcvd(double num) {
		networkNumRcvd = num;
	}
	public double getNetworkNumRcvd() {
		return networkNumRcvd;
	}
	
	public double getAnalogInput(int sensorID) {
		AnalogInput analogInput = new AnalogInput(sensorID);
		return analogInput.getValue();
	}
	public boolean getDigitalInput(int sensorID) {
		DigitalInput digitalInput = new DigitalInput(sensorID);
		return digitalInput.get();
	}
}