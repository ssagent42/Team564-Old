/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
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
    Solenoid tfor;//t rex arm 1
    Solenoid tback;//t rex arm 2
    Solenoid rup;//Third leg arm up
    Solenoid rfor;//third leg arm side;
    Joystick left;
    Joystick right;
    Joystick arc;
    Joystick makeymakey;
    Servo swerve;
    Jaguar jag1;          //driving jaguars
    Jaguar jag2;
    Jaguar jag3; //winch jags
    Jaguar jag4;
    Relay spikeforward;
    Relay spikebackward;
    Relay compresspower;
    Compressor comp;
    AnalogChannel accel;
    DigitalInput lim;
    DigitalInput lim2;
    DigitalInput lim3;
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
    AxisCamera camera;
    CriteriaCollection cc; // our attempt at setting up a pattern for the camera.
    boolean conveyors;
    boolean flag;
    boolean autoFirstRun;
    boolean OperatorControl;
    
    public void robotInit() {
        jag1 = new Jaguar(1); 
        jag2 = new Jaguar(2); 
        jag3 = new Jaguar(3); 
        jag4 = new Jaguar(4); 
        spikeforward = new Relay(1,1, Relay.Direction.kForward);
        spikebackward = new Relay(2,1,Relay.Direction.kReverse);
        compresspower = new Relay(2, Relay.Direction.kForward);
        arc = new Joystick(1);
        left = new Joystick(2);
        right = new Joystick(3);
        makeymakey = new Joystick(4);
        tfor = new Solenoid(1); //trex
        tback = new Solenoid(2);
        rup = new Solenoid(3);//raptor
        rfor = new Solenoid(4);
        gyro = new Gyro(1);
        //gyro.reset();   //if you wanna recalibrate, allow this line of code, then compile make sure that the robot does not move during calibration
        sonar = new AnalogChannel(6);
        pot = new AnalogChannel(3);
        accel = new AnalogChannel(1);
        reed = new DigitalInput(5);
        lim = new DigitalInput(3);
        lim2 = new DigitalInput(4);
        lim3 = new DigitalInput(5);
        sonar = new AnalogChannel(2);// range finder in analog port 2
        flag = false;
        awesome = 0;
        limstick1 = 0.0;
        limstick2 = 0.0;
        swerve = new Servo(1);
        comp = new Compressor(1,1);
            /*camera = AxisCamera.getInstance();  // CAMERA CODE ONLY USE WHEN CAMERA IS PLUGGED In 
            camera.writeResolution(AxisCamera.ResolutionT.k320x240); // CAMERA CODE ONLY USE WHEN CAMERA IS PLUGGED IN
            */
            cc = new CriteriaCollection();      // create the criteria for the particle filter
            cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
            cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
            conveyors = true;
           autoFirstRun = true;
           OperatorControl = false;
            
        

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        if(autoFirstRun){
            while(((sonar.getValue()/1024.0)*5.0/0.01)>=10){
            jag1.set(0.5);
            jag2.set(0.5);
        }
            jag1.set(0.0);
            jag2.set(0.0);
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit(){
        flag = false;
        OperatorControl = true;
        compresspower.set(Relay.Value.kOn);
        comp.start();
        int i = 0;
        while(OperatorControl){
           System.out.println(i);
            Timer.delay(1.0);
            i++;
        }
    }
    
    public void teleopPeriodic() {
       System.out.println("Gyro:\t"+gyro.getAngle());//Gets angle for gyro
       System.out.println("Limit Switch:\t" + lim.get());
       System.out.println("Reed:\t"+reed.get());
       System.out.println("Solenoid 1,2,3,4:\t"+tfor+" "+tback+" "+rup+" "+rfor);
       System.out.println("Rangefinder:\t"+(sonar.getValue()/1024.0)*5.0/0.01+" inches");
       System.out.println("Accelerometer:" +accel.getValue());
       
       jag1.set(left.getY());
       jag2.set(right.getY());
       
       /**
         * DRIVE SYSTEM:
         *
        if(left.getY() > 0){ // power left motor 
            jag1.set(0.5);
            jag2.set(0.0);
        }
        else if(left.getY() < 0){ //reverse left motor power
            jag1.set(-0.5);
            jag2.set(0.0);
        }
        else if(left.getY() == 0 && right.getY() == 0){//stop
            jag1.set(0.0);
            jag2.set(0.0);
        }
        if(right.getY() > 0){ // power right motor
            jag1.set(0.0);
            jag2.set(0.5);
        }
        else if(right.getY() < 0){ // reverse right motor power
            jag1.set(0.0);
            jag2.set(-0.5);
        }*/
       
       if(makeymakey.getRawButton(1)){//t-rex arm up
            if(((gyro.getAngle()>=355 && gyro.getAngle()<=5)||(gyro.getAngle()>=85 && gyro.getAngle()<=95)||
                (gyro.getAngle()>=175 && gyro.getAngle()<=185)||(gyro.getAngle()>=265 && gyro.getAngle()<=275))&& ((sonar.getValue()/1024.0)*5.0/0.01)<10){//whilst the gyro returns certain values and rangefinder returns less than 10 inches, then u can move the arm initially
          // jag3.set(0.5);
                spikeforward.set(Relay.Value.kOn);
            }
       }
       if(makeymakey.getRawButton(2)){//t-rex arm down
          // jag3.set(-0.5);
           spikebackward.set(Relay.Value.kReverse);
           spikebackward.set(Relay.Value.kOn);
       }
       if(makeymakey.getRawButton(3)){//solenoidz for t-rex arm
           
           tfor.set(true);//makes trex arm go forward
           Timer.delay(1.5);
           tfor.set(false);
           
       }
       if(makeymakey.getRawButton(4)){ //try if all else works
           
           
          
           rup.set(true);//raptor up
           rfor.set(true);//raptor forward
            Timer.delay(2.0);
           
          /* rup.set(false);
           rfor.set(false);
           jag3.set(-0.5);//winch system jags on
           jag4.set(-0.5);
           Timer.delay(2.0);
           
           jag3.set(0.0);
           jag4.set(0.0);                                               
           tback.set(true); 
           spikeforward.set(Relay.Value.kOn);//trex arm up
           Timer.delay(1.5);
      
           tback.set(false);
           spikebackward.set(Relay.Value.kReverse);//trex arm pulls dawwnnn, 2nd bar
           spikebackward.set(Relay.Value.kOn);
            Timer.delay(1.5);
           
           */
           }
       /*if(lim.get()==false){//top, cannot go further 
               spike.set(Relay.Value.kOff);
           
       }
       if(lim2.get()==false){//bottom, cannot go farther down
               spike.set(Relay.Value.kOff);
           
       }
       if(left.getRawButton(3)||right.getRawButton(3)){
        
         lim.set(false);
         lim2.set(false);
        
        } 
       */
       
       }
      
       
    
    
    /*
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
         
    }
    
}
