package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

// TODO: Gosh darn it! Start commenting on stuff

public class PowerHandlerSystem extends Subsystem {
	
	// The actual power board
	private PowerDistributionPanel pdp;
	
	// If there is major system error
	private boolean error = false;
	
	// The information we get from the board
	private double totalCurrent = 0.0, totalVoltage = 0.0, temperature = 0.0;
	private double currentPerChannel[];
	
	@Override
	protected void initSystem() {
		currentPerChannel = new double[16];
		try {
			pdp = new PowerDistributionPanel(1);
		} catch (Exception ex) {
			System.out.println("Warning (PowerHandlerSystem): Could not initilize PDP!");
			error = true;
		}
		update();
	}

	@Override
	protected void updateSystem() {
		 if (error) return;
		
		pdp.clearStickyFaults();
		for (int i = 0; i < 16; i++) {
			currentPerChannel[i] = pdp.getCurrent(i);
		}
		totalCurrent = pdp.getTotalCurrent();
		totalVoltage = pdp.getVoltage();
		temperature = pdp.getTemperature();
	}

	@Override
	protected void killSystem() {
		
	}

	@Override
	protected void errorHandler() {
		
	}
	
	
	// Getters for the info
	public double getPDPTotalCurrent() { return totalCurrent; }
	public double getPDPTotalVoltage() { return totalVoltage; }
	public double getPDPTemperature() { return temperature; }
	public double getChannelCurrent(int channel) { 
		try {
			return currentPerChannel[channel];
		} catch (Exception ex) {
			createError(false, ex.getMessage());
		}
		return 0.0;
	}
	
	// Getter for if there is error
	public boolean isError() { return error; }
}