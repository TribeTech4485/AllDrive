package org.usfirst.frc.team4485.robot.Subsystems.Systems;

import org.usfirst.frc.team4485.robot.Subsystems.Subsystem;

import edu.wpi.first.wpilibj.Solenoid;

public class BlastOffSystem extends Subsystem {

	private Solenoid blastOff_in, blastOff_out;
	private boolean out = false;
	
	@Override
	protected void initSystem() {
		// TODO Auto-generated method stub
		blastOff_in = new Solenoid(id.pneumaticBlastOffModule, id.pneumaticBlastOff_in);
		blastOff_out = new Solenoid(id.pneumaticBlastOffModule, id.pneumaticBlastOff_out);
	}

	@Override
	protected void updateSystem() {
		// TODO Auto-generated method stub
		controlSolenoids();
	}

	@Override
	protected void killSystem() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void errorHandler() {
		// TODO Auto-generated method stub
		
	}
	
	private void controlSolenoids() {
		blastOff_in.set(!out);
		blastOff_out.set(out);
	}
	
	public void setOut(boolean _out) {
		out = _out;
	}

}
