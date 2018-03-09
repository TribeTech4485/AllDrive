package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.DriverStation;

// TODO: Comment here too

public abstract class AutoProgram {
	SubsystemsControl subsystems;
	protected boolean auto_complete = false;
	private boolean instanced = false;
	private boolean auto_started = false;
	
	// Timer values
	protected double auto_startTime = 0;
	protected double auto_duration = 0;
	protected double auto_timeLimit = 0; // Limit of < 1 is unlimited
	//
	
	// Iterative values
	protected int step = 0;	// Step number for iterative auto programs
	//
	
	protected String gameSpecificMessage;
	
	public AutoProgram(SubsystemsControl _subsystems) {
		subsystems = _subsystems;		
		///auto_startTime = System.currentTimeMillis();
		auto_startTime = -1;
		auto_duration = 0;
		instanced = true;
		auto_complete = false;
		step = 0;
		
		// Don't init here
		// Init here will cause timer issues
	}
	
	//// Private functions
	protected boolean updateDuration() {
		if (auto_startTime < 0) auto_startTime = System.currentTimeMillis();
		// Update auto_duration and auto_complete
		auto_duration = System.currentTimeMillis() - auto_startTime;
		if (auto_duration >= auto_timeLimit && auto_timeLimit > 0) auto_complete = true;
		return auto_complete;
	}
	////
	
	//// Public functions
	public boolean runIncomplete() {
		// Init if auto isn't started
		if (!auto_started && !auto_complete) {
			//subsystems.driveSystem.zeroYaw();
			init();
			gameSpecificMessage = DriverStation.getInstance().getGameSpecificMessage();
			auto_started = true;
		}
		
		// Run if the auto program has not been completed
		if (!auto_complete) run();
		else stop();
		return updateDuration();
	}
	public void reset() {
		auto_started = false;
		auto_complete = false;
		auto_startTime = -1;
	}
	public void stop() {
		// Stop the robot from moving
		subsystems.killAll();
		auto_started = false;
		auto_startTime = -1;
	}
	public void disable() {
		auto_started = false;
		auto_complete = false;
		step = 0;
		subsystems.killAll();
	}
	public boolean isInstanced() {
		return instanced;
	}
	public boolean isComplete() {
		return auto_complete;
	}
	////
	
	//////// Abstract Functions
	//// Protected Functions
	protected abstract void init();
	protected abstract void run();
	////
	////////
	
}