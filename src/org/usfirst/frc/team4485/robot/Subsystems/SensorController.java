package org.usfirst.frc.team4485.robot.Subsystems;

import org.usfirst.frc.team4485.robot.RobotIndexing;
import org.usfirst.frc.team4485.robot.Subsystems.Systems.PowerHandlerSystem;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/*
 * TODO:
 * ------ COMMENTS --------
 */

/*
 * 
 * SYSTEM USAGE --
 * This system is used for general sensors (AHRS, Power Sensors, etc)
 * For subsystem specific sensors, use the subsystem class
 * Drive System might need to use general sensors??
 * 
 */


public class SensorController {
	
	RobotIndexing id;
	
	// Network Controller
	public NetworkController networkController;
	
	// Create your sensor objects here
	public AHRS ahrs;
	public PowerHandlerSystem powerHandlerSystem;
	
	// Create the value objects here. You will update these with update()
	//private double gearOpticalSensorVal;
	private boolean ahrsError = false;
	public boolean ahrsYawZeroed = false;	// I don't think this is ever used, check that for me?
	public double ahrsYawMultiplier = -1;	// Invert the YAW when the GYRO is upside down
	private double ahrsZeroValue = 0;
	
	private double leftDriveRPM = 0.0, rightDriveRPM = 0.0;
	private double leftDriveOffset_cm = 0.0, rightDriveOffset_cm = 0.0;
	private double networkNumRcvd = 0;
	
	// Current limits for systems
	private double driveSystemVoltageLowLimit = 9, driveSystemVoltageHighLimit = 10.5, reductionLimit = 0.5; //redLimit was 0.0
	private double voltageTotal = 0;

	// Number of times we have taken the voltage reading for smoothing of the motor limits
	int voltageReadings = 0;
	
	public SensorController() {
		id = new RobotIndexing();
		init();
	}
	
	private void init() {
		// Here is where you initialize sensors
		powerHandlerSystem = new PowerHandlerSystem();
		powerHandlerSystem.setID(100);
		
		// Initialize the AHRS
		try {
			ahrs = new AHRS(SPI.Port.kMXP);// SPI Port
			//ahrs = new AHRS(SerialPort.Port.kMXP);
		} catch (Exception ex) {
			ahrsError = true;
			System.out.println("Warning: AHRS Error: " + ex.getMessage());
		}
		
		CameraServer.getInstance().startAutomaticCapture(0);
	}
	
	public void update() {
		powerHandlerSystem.update();
	}
	
	//// Getters and setters for motors --------
	public void setRPMs(double left, double right) {
		leftDriveRPM = Math.abs(left);
		rightDriveRPM = Math.abs(right);
	}
	public void setOffsets(double left, double right) {
		leftDriveOffset_cm = left;
		rightDriveOffset_cm = right;
	}
	public double getAverageRPM() {
		return (leftDriveRPM + rightDriveRPM) / 2;
	}
	public double getLeftOffset_cm() {
		return leftDriveOffset_cm;
	}
	public double getRightOffset_cm() {
		return rightDriveOffset_cm;
	}
	////------
	
	//// GYRO stuff -----

	@SuppressWarnings("unused")
	//@Deprecated
	private boolean isAHRSYawZeroed() {
		if (Math.abs(ahrs.getYaw()) < 1) return true;
		return false;
	}
	public boolean isAHRSError() { return ahrsError; }
	public boolean zeroAHRSYaw() {
		for (int i = 0; i < 20; i++) {
			ahrs.zeroYaw();
			if (isAHRSYawZeroed()) return true;
		}
		return false;
		//ahrsZeroValue = getRawAHRSYaw();
		//return true;
	}
	public double getRawAHRSYaw() { if (!isAHRSError()) return ahrs.getYaw() * ahrsYawMultiplier; else return 0;}
	public double getAHRSYaw() {
		//SmartDashboard.putNumber("Yaw", getRawAHRSYaw());
		//return getRawAHRSYaw();
		/*
		double rawYaw = getRawAHRSYaw();
		double yaw = 0;
		if (rawYaw < 0 && ahrsZeroValue < 0) yaw = rawYaw - ahrsZeroValue;
		else if (rawYaw < 0 && ahrsZeroValue >= 0) yaw = Math.abs(rawYaw) + ahrsZeroValue;
		else if (rawYaw >= 0 && ahrsZeroValue < 0) yaw = rawYaw + ahrsZeroValue;
		else if (rawYaw >= 0 && ahrsZeroValue >= 0) yaw = rawYaw - ahrsZeroValue;
		
		SmartDashboard.putNumber("Zero Yaw Val", ahrsZeroValue);
		if (yaw > 180) yaw = 180 - yaw;
		else if (yaw < -180) yaw = -180 - yaw;
			
				*/
		//ahrsZeroValue = -80;
		
		double rawYaw = getRawAHRSYaw();//-90;//getRawAHRSYaw();
		double zero360d = ahrsZeroValue + 180;
		double yaw = rawYaw + 180;
		
		yaw -= zero360d;
		SmartDashboard.putNumber("Yaw Tmp", yaw);
		while(yaw < 0) {
			yaw += 360;
		}
		while (yaw > 360) {
			yaw -= 360;
		}
		
		SmartDashboard.putNumber("Zero Yaw Val", zero360d);
		SmartDashboard.putNumber("Raw Yaw 360", rawYaw + 180);
		SmartDashboard.putNumber("Yaw 360", yaw);
		
		if (yaw > 180) yaw -= 360;
		
		return yaw;
	}
	//// ------
	
	//// Power limit control functions ------
	public double getDrivePowerLimiter() {
		if (powerHandlerSystem.getPDPTotalVoltage() > driveSystemVoltageHighLimit) return 1.0;
		
		voltageTotal += powerHandlerSystem.getPDPTotalVoltage();
		voltageReadings++;
		
		double voltageAvg = voltageTotal /voltageReadings;
	
		if(voltageReadings == 200) {
			voltageReadings = 0;
			voltageTotal = 0;
		}

		if (voltageAvg < driveSystemVoltageHighLimit) {
			double reduction = 1 - driveSystemVoltageLowLimit / voltageAvg;
			if (reduction < reductionLimit) reduction = reductionLimit;
			return reduction;
		}
		return 1;	// Returns a multiplier, so return 1 to have no affect.
	}
	//// ------
	
	@Deprecated
	public void setNetworkNumRcvd(double num) {
		networkNumRcvd = num;
	}
	@Deprecated
	public double getNetworkNumRcvd() {
		return networkNumRcvd;
	}
	
	//// General IO functions ------
	// Function to get the value from a given analog port
	public double getAnalogInput(int sensorID) {
		AnalogInput analogInput = new AnalogInput(sensorID);
		return analogInput.getValue();
	}
	// Function to get the value from a given digital port
	public boolean getDigitalInput(int sensorID) {
		DigitalInput digitalInput = new DigitalInput(sensorID);
		return digitalInput.get();
	}
	public double getDistanceSensorDistance(AnalogInput sensor, double mV_in) {
		// TODO: Figure out scaling
		
		// For now just divide by 2
		if (sensor == null) return -1;
		return sensor.getValue() / 2;
	}
	//// ------
}