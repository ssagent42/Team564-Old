/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Compressor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    Joystick arc;
    Solenoid sol1;
    Solenoid sol2;
    Compressor comp;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        arc = new Joystick(2);
        sol1 = new Solenoid(1);
        sol2 = new Solenoid(2);
        comp = new Compressor(2,1);
        
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopInit()
    {
        comp.start();
    }
    
    public void teleopPeriodic() {
        System.out.println(comp.enabled());
      if(arc.getRawButton(2)){
          sol1.set(true);
          sol2.set(false);
      }
      else if(arc.getRawButton(3)){
          sol1.set(false);
          sol2.set(true);
      }
    }
    

    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
