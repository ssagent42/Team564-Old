/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
        Solenoid s1;//t rex arm 1
        Solenoid s2;//t rex arm 2
        Solenoid s3;//Third leg arm up
        Solenoid s4;//third leg arm side;
        Joystick left;
    Joystick right;
    Joystick arc;
    Joystick makeymakey;
    Servo servup;
    Servo servleft;
    Jaguar jag1;          //driving jaguars
    Jaguar jag2;
    Jaguar jag3;
    Jaguar jag4;
    Jaguar jag5;
    Jaguar jag6;
    DigitalInput phototop;
    DigitalInput photobottom;
    DigitalInput lim;
    DigitalInput lim2;
    DigitalInput reed;
    AnalogChannel pot;
    AnalogChannel sonar;
    Gyro gyro;
    int autoPeriodicLoops;
    int disabledPeriodicLoops;
    int telePeriodicLoops;
    int driveMode;
    int dsPacketsReceivedInCurrentSecond;
    double limstick1;
    double limstick2;
    public int awesome;
    static final int UNINITIALIZED_DRIVE = 0;
    static final int ARCADE_DRIVE = 1;
    static final int TANK_DRIVE = 2;
    static final double DRIVE_LIMIT = 0.30;    
    static final double CAM_SERVO_LEFT = 0.2008;
    static final double CAM_SERVO_UP = 0.2345;
    int drivemode = UNINITIALIZED_DRIVE;
    int counter;
    //AxisCamera camera;
    CriteriaCollection cc; // our attempt at setting up a pattern for the camera.
    boolean conveyors;
    boolean flag;
    boolean autoFirstRun;
    public void robotInit() {
        jag1 = new Jaguar(1); // Conveyor belt
        jag2 = new Jaguar(2); // Left drive
        jag3 = new Jaguar(3); // Right drive
        jag4 = new Jaguar(4); // Conveyor belt
        jag5 = new Jaguar(5); // Shooter
        jag6 = new Jaguar(6); // Arm
        arc = new Joystick(1);
        left = new Joystick(2);
        right = new Joystick(3);
        makeymakey = new Joystick(4);
        gyro = new Gyro(1);
        gyro.reset();   //if you wanna recalibrate, allow this line of code, then compile make sure that the robot does not move during calibration
        sonar = new AnalogChannel(6);
        pot = new AnalogChannel(3);
        phototop = new DigitalInput(1);
        photobottom = new DigitalInput(2);
        reed = new DigitalInput(5);
        lim = new DigitalInput(3);
        lim2 = new DigitalInput(4);
        sonar = new AnalogChannel(2);// range finder in analog port 2
        flag = false;
        awesome = 0;
        limstick1 = 0.0;
        limstick2 = 0.0;
        servup = new Servo(7);
        servleft = new Servo(8);
            /*camera = AxisCamera.getInstance();  // CAMERA CODE ONLY USE WHEN CAMERA IS PLUGGED In 
            camera.writeResolution(AxisCamera.ResolutionT.k320x240); // CAMERA CODE ONLY USE WHEN CAMERA IS PLUGGED IN
            camera.writeColorLevel(0); // CAMERA CODE ONLY USE WHEN CAMERA IS PLUGGED IN
            camera.writeBrightness(0); // CAMERA CODE ONLY USE WHEN CAMERA IS PLUGGED IN
            */
            cc = new CriteriaCollection();      // create the criteria for the particle filter
            cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
            cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
            conveyors = true;
           autoFirstRun = true;
            
        

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

        
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit(){
        flag = false;
    }
    
    public void teleopPeriodic() {
       System.out.println("Gyro"+gyro.getAngle());//Gets angle for gyro
       jag1.set(left.getY());
       jag2.set(right.getY());
       
       if(makeymakey.getRawButton(1)){//t rex arm up
           jag3.set(0.5);
       }
       if(makeymakey.getRawButton(2)){//t rex arm down
           jag3.set(-0.5);
       }
       if(makeymakey.getRawButton(3)){
           s1.set(true);
           s2.set(true);
           Timer.delay(.5);
           s1.set(false);
           s2.set(false);
       }
       if(makeymakey.getRawButton(4)){
           s3.set(true);
           s4.set(true);
       }
      
       
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
