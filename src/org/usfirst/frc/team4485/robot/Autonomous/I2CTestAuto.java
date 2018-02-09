package org.usfirst.frc.team4485.robot.Autonomous;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.usfirst.frc.team4485.robot.Subsystems.SubsystemsControl;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class I2CTestAuto extends AutoProgram {

	I2C i2c = new I2C(I2C.Port.kOnboard, 8);
	byte[] byteToSend= new byte[4];
	
	public I2CTestAuto(SubsystemsControl _subsystems) {
		super(_subsystems);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 4; i++) {
			byteToSend[i] = 0;
		}
	}

	@Override
	protected void run() {
		byte[] byteRcvd = new byte[4];
		for (int i = 0; i < 4; i++) {
			byteRcvd[i] = 0;
		}
		byteRcvd[0] = 0;
		i2c.transaction(byteToSend, 4, byteRcvd, 4);
		//i2c.readOnly(byteRcvd, 4);
		
		
		String rcvdString = "";
		for (int i = 0; i < 4; i++) {
			rcvdString += (char)byteRcvd[i];
		}
		
		float fRcvd = ByteBuffer.wrap(byteRcvd).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		
		SmartDashboard.putString("Bytes RCVD", rcvdString);
		SmartDashboard.putNumber("Float RCVD", fRcvd);
	}
}
