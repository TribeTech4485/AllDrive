package org.usfirst.frc.team4485.robot.Subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;

public class SerialController {
	private SerialPort serial;
	private Port port = Port.kUSB1;
	
	private String serialInput = "";
	private boolean enabled = true;
	private boolean error = false;
	
	public SerialController() {
		init();
	}
	
	private void init() {
		try {
			error = false;
			serial = new SerialPort(9600, port);
			serial.setReadBufferSize(100);
			serial.enableTermination();
		} catch (Exception ex) {
			error = true;
			System.out.println("Warning: (SerialController) " + ex.getMessage());
		}
	}
	
	public void update() {
		//if (error || !enabled) return;
		if (!error && enabled) {
			if (serial.getBytesReceived() != 0) {
				serialInput += serial.readString();
			}
		}
	}
	
	public void close() {
		serial.free();
	}
	
	public void clearSerialInput() {
		serialInput = "";
	}
	
	public String getInput() {
		return serialInput;
	}
	
	public boolean isError() {
		return error;
	}
	
}