package com.globex.triviagame.game;

/**
 * PointHelper: Used for updating how many points a user has.
 * @author sterling
 *
 */
public class PointHelper {
	
	
	private static int points = 0;
	
	// Number of points to be added/subtracted when the user gets a question right/wrong
	private static final int ADD_POINTS = 10;
	private static final int SUB_POINTS = 5;
			
	/**
	 * addPoints: Adds points to the round score when the user answers a question correctly.
	 */
	public static void addPoints() {
		points += ADD_POINTS;		
	}

	/**
	 * subPoints: Subtracts points from the round score when the user answers a question incorrectly.
	 */
	public static void subPoints() {
		points -= SUB_POINTS;
	}	
	
	/**
	 * resetPoints: Resets the amount of points the user has got to 0.
	 */
	public static void resetPoints() {
		points = 0;
	}

	/**
	 * getPoints: Gets the number of points the user has.
	 * @return
	 */
	public static int getPoints() {
		return points;
	}
			
}
