package org.usfirst.frc.team4485.robot.bots;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class MichaelBot extends RobotBase {

  private boolean m_robotMainOverridden;

  public MichaelBot() {
    super();
    m_robotMainOverridden = true;
  }

  protected void robotInit() {
    System.out.println("Default robotInit() method running, consider providing your own");
  }

  protected void disabled() {
    System.out.println("Default disabled() method running, consider providing your own");
  }

  public void autonomous() {
    System.out.println("Default autonomous() method running, consider providing your own");
  }

  public void operatorControl() {
    System.out.println("Default operatorControl() method running, consider providing your own");
  }

  public void test() {
    System.out.println("Default test() method running, consider providing your own");
  }

  public void robotMain() {
    m_robotMainOverridden = false;
  }

  public void startCompetition() {
    HAL.report(tResourceType.kResourceType_Framework,
                                   tInstances.kFramework_Simple);

    robotInit();

    // Tell the DS that the robot is ready to be enabled
    HAL.observeUserProgramStarting();

    robotMain();
    if (!m_robotMainOverridden) {
      // first and one-time initialization
      LiveWindow.setEnabled(false);

      while (true) {
        if (isDisabled()) {
          m_ds.InDisabled(true);
          disabled();
          m_ds.InDisabled(false);
          while (isDisabled()) {
            Timer.delay(0.01);
          }
        } else if (isAutonomous()) {
          m_ds.InAutonomous(true);
          autonomous();
          m_ds.InAutonomous(false);
          while (isAutonomous() && !isDisabled()) {
            Timer.delay(0.01);
          }
        } else if (isTest()) {
          LiveWindow.setEnabled(true);
          m_ds.InTest(true);
          test();
          m_ds.InTest(false);
          while (isTest() && isEnabled()) {
            Timer.delay(0.01);
          }
          LiveWindow.setEnabled(false);
        } else {
          m_ds.InOperatorControl(true);
          operatorControl();
          m_ds.InOperatorControl(false);
          while (isOperatorControl() && !isDisabled()) {
            Timer.delay(0.01);
          }
        }
      } /* while loop */
    }
  }
}
