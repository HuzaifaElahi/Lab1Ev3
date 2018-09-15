package ca.mcgill.ecse211.wallfollowing;

import lejos.hardware.motor.*;

public class BangBangController implements UltrasonicController {
	
	
	private static final int FILTER_OUT = 10;
	  private int filterControl;


  private final int bandCenter;
  private final int bandwidth;
  private final int motorLow;
  private final int motorHigh;
  private int distance;

  public BangBangController(int bandCenter, int bandwidth, int motorLow, int motorHigh) {
    // Default Constructor
    this.bandCenter = bandCenter;
    this.bandwidth = bandwidth;
    this.motorLow = motorLow;
    this.motorHigh = motorHigh;
    WallFollowingLab.leftMotor.setSpeed(motorHigh); // Start robot moving forward
    WallFollowingLab.rightMotor.setSpeed(motorHigh);
    WallFollowingLab.leftMotor.forward();
    WallFollowingLab.rightMotor.forward();
  }

  @Override
  public void processUSData(int distance) {
    //this.distance = distance;
    // TODO: process a movement based on the us distance passed in (BANG-BANG style)
    // compute the error from the band center
    
    
    
    if (distance >= 255 && filterControl < FILTER_OUT) {
        // bad value, do not set the distance var, however do increment the
        // filter value
        filterControl++;
      } else if (distance >= 255) {
        // We have repeated large values, so there must actually be nothing
        // there: leave the distance alone
        this.distance = distance;
      } else {
        // distance went below 255: reset filter and leave
        // distance alone.
        filterControl = 0;
        this.distance = distance;
      }
    
    
    float error = bandCenter - distance;
	if (error <= bandwidth) {
		// if the error is outside the band width on the right
		// turn left
		WallFollowingLab.leftMotor.setSpeed(motorLow+10);
		WallFollowingLab.rightMotor.setSpeed(motorHigh);
	} else if (error >= -bandwidth) {
		// if it's outside the band width on the left
		// turn right
		WallFollowingLab.leftMotor.setSpeed(motorHigh);
		WallFollowingLab.rightMotor.setSpeed(motorLow-60);
	} else {
		// if it's inside the acceptable band width, go forward
		WallFollowingLab.leftMotor.setSpeed(motorHigh);
		WallFollowingLab.rightMotor.setSpeed(motorHigh);
	}
    /*
	int error = bandCenter - distance;
	if (error <= bandwidth) {
		WallFollowingLab.leftMotor.setSpeed(motorHigh);
		WallFollowingLab.rightMotor.setSpeed(motorHigh);
		WallFollowingLab.leftMotor.forward();
		WallFollowingLab.rightMotor.forward();
	} 
	else if(error > 0) { //Turn right
		WallFollowingLab.leftMotor.setSpeed(motorHigh);
		WallFollowingLab.rightMotor.setSpeed(motorLow);
		WallFollowingLab.leftMotor.forward();
		WallFollowingLab.rightMotor.forward();
	}
	else if(error < 0){  //Turn left
		WallFollowingLab.leftMotor.setSpeed(motorLow);
		WallFollowingLab.rightMotor.setSpeed(motorHigh);
		WallFollowingLab.leftMotor.forward();
		WallFollowingLab.rightMotor.forward();
	}	*/
    /*
    //Turns LEFT
    WallFollowingLab.leftMotor.setSpeed(motorLow);
	WallFollowingLab.rightMotor.setSpeed(motorHigh);
	WallFollowingLab.leftMotor.forward();
	WallFollowingLab.rightMotor.forward();
    */
  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}
