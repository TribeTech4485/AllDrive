package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import edu.wpi.first.wpilibj.PowerDistributionPanel;


public class PowerHandlerSystem extends Subsystem {
	
	// The actual power board
	private PowerDistributionPanel pdp;
	
	// The information we get from the board
	private double totalCurrent = 0.0, temperature = 0.0;
	private double currentPerChannel[];
	
	@Override
	protected void initSystem() {
		currentPerChannel = new double[16];
		pdp = new PowerDistributionPanel(0);
		update();
	}

	@Override
	protected void updateSystem() {
		pdp.clearStickyFaults();
		for (int i = 0; i < 16; i++) {
			currentPerChannel[i] = pdp.getCurrent(i);
		}
		totalCurrent = pdp.getTotalCurrent();
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
	public double getPDPTemperature() { return temperature; }
	public double getChannelCurrent(int channel) { 
		try {
			return currentPerChannel[channel];
		} catch (Exception ex) {
			createError(false, ex.getMessage());
		}
		return 0.0;
	}
}