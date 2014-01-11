/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;



import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*;
import edu.wpi.first.wpilibj.*;

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
    Joystick left;
    Joystick right;
    Joystick arc;
    Joystick maKeyMaKey;
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
        maKeyMaKey = new Joystick(4);
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
    
    public void teleopInit()
    {
        flag = false;
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
        if(maKeyMaKey.getRawButton(1))
        {
            System.out.println("Spin wheel up!");
            jag5.set(-1.0);
        }
        else if(maKeyMaKey.getRawButton(2))
        {
            System.out.println("Spin wheel down!");
            jag5.set(0.0);
        }
        if(maKeyMaKey.getRawButton(3))
            {
                jag4.set(-0.95);
                jag1.set(-1.0);
            }
        else if(maKeyMaKey.getRawButton(4))
            {
                jag4.set(0.95);
                jag1.set(1.0);
            }
            else
            {
                jag4.set(0.0);
                jag1.set(0.0);
            }
        if(maKeyMaKey.getRawButton(5))
        {
                /*if(pot.getValue() >= 320)
                {
                     jag6.set(-0.40);
                }
                else
                {
                jag6.set(0.0);
                }*/
            if(lim2.get())
                jag6.set(-0.5);//was at .75 but needs more power
        }
        else if(maKeyMaKey.getRawButton(6))
        {
            /*if(pot.getValue() < 745)
            {
                jag6.set(0.350);
            }
            else
            {
         bbnhnh       jag6.set(0.0);
            
            }*/
                jag6.set(0.5);//was at .625 before
         }
         else
         {
             jag6.set(0.0);
         }
        if(maKeyMaKey.getX()== 1.0)
           
        
        {
            System.out.println("turn right");
            jag2.set(.5);
            jag3.set(0.0);
        }
        if(maKeyMaKey.getX() == -1.0)
        {
            System.out.println("Turn left");
            jag2.set(0.0);
            jag3.set(.5);
        }
        if(maKeyMaKey.getY() == 1 )
        {
            System.out.println("go forward");
            jag2.set(.5);
            jag3.set(.5);
            
        }
        if(maKeyMaKey.getY() == -1.0)
        {
            System.out.println("go backward");
            jag2.set(-.5);
            jag3.set(-.5);
        }
        if(maKeyMaKey.getX() == 0 && maKeyMaKey.getY() == 0 ){
            System.out.println("Stop");
            jag2.set(0);
            jag3.set(0);
        }
        
      //  System.out.println("2: " + photobottom.get());
        System.out.println("Pot! "+ pot.getValue());
        System.out.println("3: " + lim.get());
        System.out.println("4: " + lim2.get());
        System.out.println("5: " + reed.get());
        if(!reed.get()){
            flag = true;
        }
        else{
            flag = false;
        }
        System.out.println("flag " + flag);
        }
    }
    
