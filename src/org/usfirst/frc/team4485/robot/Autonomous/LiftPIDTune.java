package org.usfirst.frc.team4485.robot.Autonomous;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;
import org.usfirst.frc.team4485.robot.Subsystems.PIDController.PIDController;
import org.usfirst.frc.team4485.robot.Subsystems.PIDController.SPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiftPIDTune extends AutoProgram {

	private double setPoint = 0;
	private double currentPosition = 0;
	private SPID spid;
	
	private PIDController pid;
	
	public LiftPIDTune(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		pid = new PIDController();
		spid = new SPID();
		spid.pGain = 0;
		spid.iGain = 0;
		spid.dGain = 0;
		spid.iMax = 1;
		spid.iMin = -1;
		
		SmartDashboard.putNumber("Lift P",  spid.pGain);
		SmartDashboard.putNumber("Lift I", spid.iGain);
		SmartDashboard.putNumber("Lift D", spid.dGain);
		SmartDashboard.putNumber("Lift I MAX", spid.iMax);
		SmartDashboard.putNumber("Lift I MIN", spid.iMin);
		SmartDashboard.putNumber("Lift Set Point", setPoint);
		SmartDashboard.putNumber("Lift Position", currentPosition);
	}

	@Override
	protected void run() {
		// Get and update the PID values from the dash board
		spid.pGain = SmartDashboard.getNumber("Lift P", 0);
		spid.iGain = SmartDashboard.getNumber("Lift I", 0);
		spid.dGain = SmartDashboard.getNumber("Lift D", 0);
		spid.iMax = SmartDashboard.getNumber("Lift I MAX", 0);
		spid.iMin = SmartDashboard.getNumber("Lift I MIN", 0);
		setPoint = SmartDashboard.getNumber("Lift Set Point", 0);
		
		currentPosition = subsystems.liftSystem.getPosition();
		
		SmartDashboard.putNumber("Lift P",  spid.pGain);
		SmartDashboard.putNumber("Lift I", spid.iGain);
		SmartDashboard.putNumber("Lift D", spid.dGain);
		SmartDashboard.putNumber("Lift I MAX", spid.iMax);
		SmartDashboard.putNumber("Lift I MIN", spid.iMin);
		SmartDashboard.putNumber("Lift Set Point", setPoint);
		SmartDashboard.putNumber("Lift Position", currentPosition);
		
		pid.setSPID(spid);
		double setAmount = pid.update(currentPosition - setPoint, currentPosition);
		SmartDashboard.putNumber("Lift Error", setAmount);		
		if (setAmount > 1) setAmount = 1;
		if (setAmount < -1) setAmount = -1;
		
		// Control the lift
		subsystems.liftSystem.setLift(setAmount);
		subsystems.liftSystem.update();
	}

}
