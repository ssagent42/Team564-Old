/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.Accelerometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class ArcadeMANNNN extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    DoubleSolenoid Tsolenoids = new DoubleSolenoid(3, 2);
    DoubleSolenoid swagger = new DoubleSolenoid(4, 5);
   Solenoid trexforward;//t rex arm 1
    Solenoid raptorup1;//raptor arm 2 extends
    Solenoid raptorup2;//raptor arm 1 extend
    Solenoid raptorforward;//dual piston raptor arm;
    Solenoid raptorupretract1;//raptor up retract
    Solenoid raptorupretract2;//raptor up retract
    Solenoid raptorretract;//raptor forward retract
    Solenoid trexretract;//trex retract
    char Plan = 'a';
    Joystick left;
    Joystick right;
    Joystick arc;
    Joystick makeymakey;
    Servo swerve;
    Jaguar jag1;          //driving jaguars
    Jaguar jag2;
    Jaguar winch1; //winch jags
    Jaguar winch2;
    Relay trexup;
    Relay trexdown;
    DigitalInput lim_arm;
    DigitalInput lim2;
    DigitalInput lim3;
    //DigitalInput reed;
    AnalogChannel pot;
    AnalogChannel sonar;
    DigitalInput pressureSwitch;
    Gyro gyro;
    ADXL345_I2C accel;
    int autoPeriodicLoops;
    int disabledPeriodicLoops;
    int telePeriodicLoops;
    int driveMode;
    int dsPacketsReceivedInCurrentSecond;
    double limstick1 = 0.0;
    double limstick2 = 0.0;
    public int awesome;
    static final int UNINITIALIZED_DRIVE = 0;
    static final int ARCADE_DRIVE = 1;
    static final int TANK_DRIVE = 2;
    static final double DRIVE_LIMIT = 0.30;
    final static double DRIVE_OFFSET = 0.0;
    int drivemode = UNINITIALIZED_DRIVE;
    int counter;
    AxisCamera camera;
    CriteriaCollection cc; // our attempt at setting up a pattern for the camera.
    //boolean conveyors;
   // boolean flag;
    boolean button1;
    boolean button2; 
    boolean button3;
    boolean button4;
    boolean autoFirstRun;
    boolean OperatorControl;
    //Relay compresspower;
    Compressor comp;
    Wumbo wum;
    public ArcadeMANNNN(){
        jag1 = new Jaguar(1); 
        jag2 = new Jaguar(2); 
        winch1 = new Jaguar(3);
        winch2 = new Jaguar(4); 
        trexup = new Relay(5, Relay.Direction.kBoth);
        //trexdown = new Relay(1,6 ,Relay.Direction.kReverse);
        comp = new Compressor(5,2);
        arc = new Joystick(1);
        left = new Joystick(2);
        right = new Joystick(3);
        makeymakey = new Joystick(4);
     /*   trexforward = new Solenoid(5); //trex
        raptorup1 = new Solenoid(7);
        raptorup2 = new Solenoid(3);//raptor
        raptorforward = new Solenoid(1);
        raptorretract = new Solenoid(2);
        trexretract = new Solenoid(6);
        raptorupretract1 = new Solenoid(8);
        raptorupretract2 = new Solenoid(4); */
        gyro = new Gyro(1);
        //gyro.reset();   //if you wanna recalibrate, allow this line of code, then compile make sure that the robot does not move during calibration
        //sonar = new AnalogChannel(6);
        pot = new AnalogChannel(3);
        //reed = new DigitalInput(5);
        lim_arm = new DigitalInput(1); // old reed 
        accel = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G);
        
        // sonar = new AnalogChannel(2);
        // range finder in analog port 2
       // flag = false;
        awesome = 0; 
        button1 = false;
        button2 = false;
        wum = new Wumbo();
    }
    public void robotInit() {
       
        // swerve = new Servo();
            camera = AxisCamera.getInstance();  // CAMERA CODE ONLY USE WHEN CAMERA IS PLUGGED In 
            camera.writeResolution(AxisCamera.ResolutionT.k320x240); // CAMERA CODE ONLY USE WHEN CAMERA IS PLUGGED IN
            /*
            cc = new CriteriaCollection();      // create the criteria for the particle filter
            cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
            cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
            * */
           // conveyors = true;
            Tsolenoids.set(DoubleSolenoid.Value.kOff);
            swagger.set(DoubleSolenoid.Value.kOff);
           autoFirstRun = true;
           OperatorControl = false;
           trexforward.set(false);
           raptorup1.set(false);
           raptorup2.set(false);
           raptorforward.set(false);
           raptorretract.set(false);
           raptorupretract1.set(false);
           raptorupretract2.set(false);
           trexretract.set(false);
          //compresspower.set(Relay.Value.kOn);
          comp.start();
          System.out.println(wum.Wumbology);
           
           
            
        

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousContinuous() {
        
        switch(Plan)
        {
            case 'a': //Start Climb
            {
                jag1.set(0.35);//move forward a little
                jag2.set(0.35);
                Timer.delay(2.5);
                trexforward.set(true);//moves trex arm forward
                trexretract.set(false);
                Timer.delay(5.0);
                trexup.set(Relay.Value.kReverse);//pulls down and brings robot up
                Timer.delay(7.5);
                break;
            }
            case 'b': //Bottom Left
            {
                
                break;
            }
            case 'c': //Top Right
            {
                
                break;
            }
            case 'd': //Top Left
            {
                
                break;
            }
            default: break;
        }
        
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit(){
//        flag = false;
        OperatorControl = false;
        int i = 0;
        /*while(OperatorControl){  // Dammit Sukh
           System.out.println(i);
            Timer.delay(1.0);
            i++;
        }*/
        comp.start();
        //comp.setRelayValue(Relay.Value.kOn);
    }
    
    public void disabledInit()
    {
       // comp.stop();
        //comp.setRelayValue(Relay.Value.kOff);
        System.out.println("Robot Disabled");
    }
    
    public String teleopDiagnostic()
    {
        return(""
                + "Gyro:\t\t\t"+gyro.getAngle() + "\n"
                + "Accelerometer :\t\t" + accel.getAcceleration(ADXL345_I2C.Axes.kX) + "\n"
                + "Limit Switch:\t\t" + lim_arm.get() + "\n"
                //+ "Compressor\t\t" + comp.enabled() + "\n"
                + "T rex forward \t\t" + trexforward.get() + "\n"
                + "raptor up 1 \t\t" + raptorup1.get() + "\n"
                + "raptor up 2 \t\t" + raptorup2.get() + "\n"
                + "raptor forward \t\t" + raptorforward.get() + "\n"
                + "raptorupretract1 \t"+ raptorupretract1.get() + "\n"
                + "raptorupretract2 \t"+ raptorupretract2.get() + "\n"
                + "trexforwardretract \t"+ trexretract.get() + "\n"
                + "Spike City\t\t " + trexup.get().value + "\n"
                + "Current Voltage: \t" + DriverStation.getInstance().getBatteryVoltage()
                + ""
                + ""
                + ""
                
                
                );
    } 
    
    
    public void teleopPeriodic() {
       
       System.out.println(teleopDiagnostic());
       manageDriveMotors(right.getY(), left.getY());
       
       /**
         // DRIVE SYSTEM:
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
       if(arc.getRawButton(2)){//trex forward
          Tsolenoids.set(DoubleSolenoid.Value.kForward); 
          // trexforward.set(true);
           
       }
       
       else if(arc.getRawButton(1))
       {
           Tsolenoids.set(DoubleSolenoid.Value.kReverse); 
          /* trexforward.set(false);
           trexretract.set(true);
       
           */
       }
       
 
       if(arc.getRawButton(6))//winch up
       {
           winch1.set(0.55);
           winch2.set(0.55);
           
       }
       else if(arc.getRawButton(7))//winch dawnnn
       {
           winch1.set(-0.55);
           winch2.set(-0.55);
       }
       else
       {
           winch1.stopMotor();
           winch2.stopMotor();
       }
   //    if(arc.getRawButton(3)&& !raptorforward.get()){//raptor forward solenoids
      
           
       /*    raptorforward.set(true);
           raptorretract.set(false);
       */
     //      }
       if(arc.getRawButton(3)){//uses boolean to stop it every other button press
           swagger.set(DoubleSolenoid.Value.kForward); 
           /*
           raptorforward.set(false);
           raptorretract.set(false);
       */
           }
       
       
       if(arc.getRawButton(4)){ //try if all else works
           swagger.set(DoubleSolenoid.Value.kReverse);
           /*  raptorup1.set(true) ;//raptor left
           raptorup2.set(true);//raptor right
           raptorupretract1.set(false);
           raptorupretract2.set(false);
           button4 = false;
          //  Timer.delay(2.0);
           
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
   /*    else if(arc.getRawButton(4) && button4 == false){////uses boolean to stop it every other button press
           raptorup1.set(false) ;//raptor left
           raptorup2.set(false);//raptor right
           raptorupretract1.set(false);
           raptorupretract2.set(false);
           button4 = true;
       }
       
       if(arc.getRawButton(11))
       {
           trexup.set(Relay.Value.kReverse);
       }
       else if(arc.getRawButton(10))
       {
           trexup.set(Relay.Value.kForward);
       }
       else
       {
           trexup.set(Relay.Value.kOff);
       }
     */  
       if(lim_arm.get() == true){//if the arm is all the way down, stops the winch from going down
          if(arc.getRawButton(7)){
              winch1.stopMotor();
              winch2.stopMotor();
          }
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
       System.out.println("Gyro:\t"+gyro.getAngle());//Gets angle for gyro
       System.out.println("Limit Switch:\t" + lim_arm.get());
       //System.out.println("Reed:\t"+reed.get());
       System.out.println("Solenoid 1" + trexforward.get());
       System.out.println("Solenoid 2" + raptorup1.get());
       System.out.println("Solenoid 3" + raptorup2.get());
       System.out.println("Solenoid 4" + raptorforward.get());
       //System.out.println("Rangefinder:\t"+getSonarValue()+" inches");
     //  System.out.println("Acclerometer:"+accel.getValue());
    }
    
    /*public double getSonarValue()
    {
        return((sonar.getValue()/1024.0)*5.0/0.01);
    }
    */
    public boolean checkAscend()
    {
        return (((gyro.getAngle()>=355 && gyro.getAngle()<=5)||(gyro.getAngle()>=85 && gyro.getAngle()<=95)||
                (gyro.getAngle()>=175 && gyro.getAngle()<=185)||(gyro.getAngle()>=265 && gyro.getAngle()<=275)));//&& (getSonarValue())<10);
    }
    
    public void manageDriveMotors(double left, double right)
    {
        double change1 =  left * -0.95 - limstick1;
        double change2 =  right * -0.95 - limstick2;
        if(change1 > DRIVE_LIMIT)
        {
            change1 = DRIVE_LIMIT;
        }
        else if(change1 < -DRIVE_LIMIT)
        {
            change1 = -DRIVE_LIMIT;
        }
        limstick1 += change1;
        
        if(change2 > DRIVE_LIMIT)
        {
            change2 = DRIVE_LIMIT;
        }
        else if(change2 < -DRIVE_LIMIT)
        {
            change2 = -DRIVE_LIMIT;
        }
        limstick2 += change2;
        
        jag2.set(limstick1*0.95);
        jag1.set(-limstick2*0.95);
    }
    
    
}

class Wumbo extends Wumbology
{
    Wumbo()
    {
        super();
        Wumbology = "First grade, Spongebob!";
    }
}
abstract class Wumbology
        {
    String Wumbology;
}
