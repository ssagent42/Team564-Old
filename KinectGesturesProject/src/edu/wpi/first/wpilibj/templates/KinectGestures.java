/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2011. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author koconnor
 * This sample program implements the same gestures embedded in the Kinect Server and available
 * through the KinectStick class, but using robot side skeleton processing.
 * It is designed as an example for working with the raw Kinect skeleton data.
 * Details on the gestures, including the button mapping can be found in the "Getting Started
 * with the Microsoft Kinect for FRC" document.
 *
 * This code should be used as an example for modifying the default gestures or creating your own.
 * Without modification this code will provide the same functionality as the KinectStick example,
 * but with increased processing load.
 *
 * Note: The array index for the button array in this code begins at 0, the map in the document
 * and used with the KinectSticks begins at 1.
 *
 */
public class KinectGestures extends IterativeRobot {

    //Constants which define the valid arm positions
    static final double ARM_MAX_ANGLE = 178.8658868;
    static final int ARM_MIN_ANGLE = -90;
    static final double Z_PLANE_TOLERANCE = 0.3;    /* In meters */

    //Constants which define the "trigger" angles for the various buttons
    static final int LEG_FORWARD = -110;
    static final int LEG_BACKWARD = -80;
    static final int LEG_OUT = -75;
    static final int HEAD_LEFT = 98;
    static final int HEAD_RIGHT = 82;

    Kinect kinect;
    static final int UNINITIALIZED_DRIVE = 0;
    static final int ARCADE_DRIVE = 1;
    static final int TANK_DRIVE = 2;
    int drivemode = UNINITIALIZED_DRIVE;
    RobotDrive drive;
    
    Jaguar jag1;          //driving jaguars
    Jaguar jag2;
    Jaguar jag3;
    Jaguar jag4;
    Jaguar jag5;  // arm
    DigitalInput left;
    DigitalInput middle;
    DigitalInput right;
    Joystick rightStick;  // joystickChannel
    Joystick leftStick;   // joystick
    Joystick other;
    Gyro gyro;//initializes gyro
    AnalogChannel marijuana;//potentiometer lol
    AxisCamera cam;//activates the camera


    /**
     * Constructor for the sample program.
     * Gets an instance of the Kinect and
     * sets up a basic RobotDrive
     */

     public void robotInit() 
    {
        jag1 = new Jaguar(1); // MOTHER FUCKING JAGUAR IS BETTER THAN HONEY BADGER
        jag2 = new Jaguar(2);
        jag3 = new Jaguar(3);
        jag4 = new Jaguar(4);
        jag5 = new Jaguar(5);
        gyro = new Gyro(1);
       // gyro.reset();   if you wanna recalibrate, allow this line of code, then compile make sure that the robot does not move during calibration
        left = new DigitalInput(9);
        middle = new DigitalInput(2);
        right = new DigitalInput(3);
        rightStick = new Joystick(3);
        leftStick = new Joystick(2);
        other = new Joystick(1);
        marijuana = new AnalogChannel(3); // pot
        kinect = Kinect.getInstance();
       
       // cam = AxisCamera.getInstance();  // CAMERA CODE ONLY USE WHEN CAMERA IS PLUGGED IN
        //cam.writeResolution(AxisCamera.ResolutionT.k160x120); // CAMERA CODE ONLY USE WHEN CAMERA IS PLUGGED IN
        //cam.writeColorLevel(1); // CAMERA CODE ONLY USE WHEN CAMERA IS PLUGGED IN
        //cam.writeBrightness(0); // CAMERA CODE ONLY USE WHEN CAMERA IS PLUGGED IN
        
        //hmmm...I wonder what's for dinner... MAH BOI
        
    }

    /* The sample code runs during the autonomous period. */
    public void autonomous() {
        double leftAxis = 0;
        double rightAxis = 0;
        double leftAngle, rightAngle, headAngle, rightLegAngle, leftLegAngle, rightLegYZ, leftLegYZ;
        boolean dataWithinExpectedRange;
        boolean[] buttons = new boolean[8];

        /* Run while the robot is enabled */
        while(isEnabled()) {

        /* Only process data if skeleton is tracked */
        if (kinect.getSkeleton().GetTrackState() == Skeleton.tTrackState.kTracked) {

                /* Determine angle of each arm and map to range -1,1 */
                leftAngle = AngleXY(kinect.getSkeleton().GetShoulderLeft(), kinect.getSkeleton().GetWristLeft(), true);
                rightAngle = AngleXY(kinect.getSkeleton().GetShoulderRight(), kinect.getSkeleton().GetWristRight(), false);
                leftAxis = CoerceToRange(leftAngle, -70, 70, -1, 1);
                rightAxis = CoerceToRange(rightAngle, -70, 70, -1, 1);

                /* Check if arms are within valid range and at approximately the same z-value */
                dataWithinExpectedRange = leftAngle < ARM_MAX_ANGLE && leftAngle > ARM_MIN_ANGLE &&
                                      rightAngle < ARM_MAX_ANGLE && rightAngle > ARM_MIN_ANGLE;
                dataWithinExpectedRange = dataWithinExpectedRange &&
                                      InSameZPlane(kinect.getSkeleton().GetShoulderLeft(),
                                                   kinect.getSkeleton().GetWristLeft(),
                                                   Z_PLANE_TOLERANCE) &&
                                      InSameZPlane(kinect.getSkeleton().GetShoulderRight(),
                                                   kinect.getSkeleton().GetWristRight(),
                                                   Z_PLANE_TOLERANCE);

                /* Determine the head angle and use it to set the Head buttons */
                headAngle = AngleXY(kinect.getSkeleton().GetShoulderCenter(), kinect.getSkeleton().GetHead(), false);
                buttons[0] = headAngle > HEAD_LEFT;
                buttons[1] = headAngle < HEAD_RIGHT;

                /* Calculate the leg angles in the XY plane and use them to set the Leg Out buttons */
                leftLegAngle = AngleXY(kinect.getSkeleton().GetHipLeft(), kinect.getSkeleton().GetAnkleLeft(), true);
                rightLegAngle = AngleXY(kinect.getSkeleton().GetHipRight(), kinect.getSkeleton().GetAnkleRight(), false);
                buttons[2] = leftLegAngle > LEG_OUT;
                buttons[3] = rightLegAngle > LEG_OUT;

                /* Calculate the leg angle in the YZ plane and use them to set the Leg Forward and Leg Back buttons */
                leftLegYZ = AngleYZ(kinect.getSkeleton().GetHipLeft(), kinect.getSkeleton().GetAnkleLeft(), false);
                rightLegYZ = AngleYZ(kinect.getSkeleton().GetHipRight(), kinect.getSkeleton().GetAnkleRight(), false);
                buttons[4] = rightLegYZ < LEG_FORWARD;
                buttons[5] = rightLegYZ > LEG_BACKWARD;
                buttons[6] = leftLegYZ < LEG_FORWARD;
                buttons[7] = leftLegYZ > LEG_BACKWARD;

                if (dataWithinExpectedRange){
                    /**
                     * Drives using the Kinect axes scaled to 1/3 power
                     * Axes are inverted so arms up == joystick pushed away from you
                     */
                    drive.tankDrive(-leftAxis*.3, -rightAxis*.3);

                    /**
                     * Do something with boolean "buttons" here
                     */

                    /* Optional SmartDashboard display of Kinect values */
                    SmartDashboard.putDouble("Left Arm", -leftAxis);
                    SmartDashboard.putDouble("Right Arm", -rightAxis);
                    SmartDashboard.putBoolean("Head Left", buttons[0]);
                    SmartDashboard.putBoolean("Head Right", buttons[1]);
                    //...etc...
                }
                else{
                    /* Arms are outside valid range */

                    drive.tankDrive(0, 0);

                    /**
                     * Do default behavior with boolean "buttons" here
                     */

                    /* Optional SmartDashboard display of Kinect values */
                    //SmartDashboard.putDouble("Left Arm", 0);
                    //SmartDashboard.putDouble("Right Arm", 0);
                    //SmartDashboard.putBoolean("Head Left", false);
                    //SmartDashboard.putBoolean("Head Right", false);
                    //...etc...
                }
            }
            else{
                /* Skeleton not tracked */
System.out.println("WHYYYY You no recognize me?! :O");
                

                /**
                * Do default behavior with boolean "buttons" here
                */

                 //Optional SmartDashboard display of Kinect values */
                SmartDashboard.putDouble("Left Arm", 0);
                SmartDashboard.putDouble("Right Arm", 0);
                SmartDashboard.putBoolean("Head Left", false);
                SmartDashboard.putBoolean("Head Right", false);
                //...etc...
            }
            Timer.delay(.01);   /* Delay 10ms to reduce proceessing load*/
        }
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        // supply your own teleop code here
        System.out.println(gyro.getAngle());
        
        DriverStation.getInstance().setDigitalOut(1, left.get());
        DriverStation.getInstance().setDigitalOut(2, middle.get());
        DriverStation.getInstance().setDigitalOut(3, right.get());
        if(other.getZ() < 0) // REPLICATES arcadeDrive() METHOD
        {
        // local variables to hold the computed PWM values for the motors
        double moveValue = (other.getX());
        double rotateValue = (other.getY());
        boolean squaredInputs = true;
        double leftMotorSpeed = 0.0;
        double rightMotorSpeed = 0.0;
        
        if (moveValue > 1.0) { // REPLICATES RobotDrive.limit(num) METHOD
            moveValue = 1.0;//drugs are bad, mmkay..so if you do drugs, you're bad, mmkay...
        }
        if (moveValue < -1.0) {
            moveValue = -1.0;
        }
        if (rotateValue > 1.0) {
            rotateValue = 1.0;
        }
        if (rotateValue < -1.0) {
            rotateValue = -1.0;
        }

        if (squaredInputs) {
            // square the inputs (while preserving the sign) to increase fine control while permitting full power
            if (moveValue >= 0.0) {
                moveValue = (moveValue * moveValue);
            } else {
                moveValue = -(moveValue * moveValue);
            }
            if (rotateValue >= 0.0) {
                rotateValue = (rotateValue * rotateValue);
            } else {
                rotateValue = -(rotateValue * rotateValue);
            }
        }

        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }

        jag1.set(leftMotorSpeed);
        jag2.set(rightMotorSpeed);
        jag3.set(leftMotorSpeed);
        jag4.set(rightMotorSpeed);
        
        if(other.getRawButton(8))
        {
            jag5.set(0.22);//arm down
        }
        else if(other.getRawButton(9)){
            jag5.set(-0.65);//arm up
        }
        else
        {
            jag5.set(0.0);//arm stop
        }
        
        }
        else
        {
        jag1.set(-leftStick.getY());
        jag2.set(rightStick.getY());
        jag3.set(-leftStick.getY());
        jag4.set(rightStick.getY());
        if(leftStick.getRawButton(8))
        {
            jag5.set(0.22);//arm down
        }
        else if(leftStick.getRawButton(9)){
            jag5.set(-0.65);//arm up
        }
        else
        {
            jag5.set(0.0);//arm stop
        }
        }
    }


    /**
     * This method returns the angle (in degrees) of the vector pointing from Origin to Measured
     * projected to the XY plane. If the mirrored parameter is true the vector is flipped about the Y-axis.
     * Mirroring is used to avoid the region where the atan2 function is discontinuous
     * @param origin The Skeleton Joint to use as the origin point
     * @param measured The Skeleton Joint to use as the endpoint of the vector
     * @param mirrored Whether to mirror the X coordinate of the joint about the Y-axis
     * @return The angle in degrees
     */
    public double AngleXY(Skeleton.Joint origin, Skeleton.Joint measured, boolean mirrored){
        return Math.toDegrees(MathUtils.atan2(measured.getY()- origin.getY(),
                (mirrored) ? (origin.getX() - measured.getX()) : (measured.getX() - origin.getX())));
    }

     /**
     * This method returns the angle (in degrees) of the vector pointing from Origin to Measured
     * projected to the YZ plane. If the mirrored parameter is true the vector is flipped about the Y-axis.
     * Mirroring is used to avoid the region where the atan2 function is discontinuous
     * @param origin The Skeleton Joint to use as the origin point
     * @param measured The Skeleton Joint to use as the endpoint of the vector
     * @param mirrored Whether to mirror the Z coordinate of the joint about the Y-axis
     * @return The angle in degrees
     */
    public double AngleYZ(Skeleton.Joint origin, Skeleton.Joint measured, boolean mirrored){
        return Math.toDegrees(MathUtils.atan2(measured.getY()- origin.getY(),
                (mirrored) ? (origin.getZ() - measured.getZ()) : (measured.getZ() - origin.getZ())));
    }

    /**
     * This method checks if two Joints have z-coordinates within a given tolerance
     * @param origin
     * @param measured
     * @param tolerance
     * @return True if the z-coordinates are within tolerance
     */
    public boolean InSameZPlane(Skeleton.Joint origin, Skeleton.Joint measured, double tolerance)
        {
            return Math.abs(measured.getZ() - origin.getZ()) < tolerance;
        }

    /**
     * This method takes an input, an input range, and an output range,
     * and uses them to scale and constrain the input to the output range
     * @param input The input value to be manipulated
     * @param inputMin The minimum value of the input range
     * @param inputMax The maximum value of the input range
     * @param outputMin The minimum value of the output range
     * @param outputMax The maximum value of the output range
     * @return The output value scaled and constrained to the output range
     */
    public double CoerceToRange(double input, double inputMin, double inputMax, double outputMin, double outputMax)
        {
            /* Determine the center of the input range and output range */
            double inputCenter = Math.abs(inputMax - inputMin) / 2 + inputMin;
            double outputCenter = Math.abs(outputMax - outputMin) / 2 + outputMin;

            /* Scale the input range to the output range */
            double scale = (outputMax - outputMin) / (inputMax - inputMin);

            /* Apply the transformation */
            double result = (input + -inputCenter) * scale + outputCenter;

            /* Constrain to the output range */
            return Math.max(Math.min(result, outputMax), outputMin);
        }
}
