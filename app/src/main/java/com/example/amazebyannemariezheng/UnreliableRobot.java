package com.example.amazebyannemariezheng;

import generation.CardinalDirection;
import com.example.amazebyannemariezheng.Robot.Direction;

// @Author: ANNEMARIE ZHENG
/*Responsibilities:
  * Uses sensors and algorithm to move player out of maze
  *Keeps track of energy consumed 
  *carries at least one unreliable sensor that robot has to account for when sensing
  *
  * *  Collaborators:
  *  Algorithm class eg. Wizard.java/WallFollower.java 
  *  Sensor Class eg. ReliableSensor.Java/UnreliableSensor.java
  *  Control -> StatePlaying.java
  */
public class UnreliableRobot extends ReliableRobot{
	String robotConfig;
	public void setConfig(String robotConfig){
		this.robotConfig=robotConfig;
	}



	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		

		if(isOperational(direction)) {//check if operational
			try {
				return getSensor(direction).distanceToObstacle(getCurrentPosition(), getCurrentDirection(), batteryLevel);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}
		else {//if not operational check if neighbors are operational
			Direction neighborSensorDirection=closestOperationalNeighborSensor(direction);
			
			//case A when there is an neighbor sensor that is operational
			if (neighborSensorDirection!=null) {
				return distanceToObstacleUsingOperationalNeighborSensor(direction); 
			}
			//case B when no neighbor sensors are operational so you have to wait till sensor repairs itself
			else {
				while (isOperational(direction)==false) {
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return distanceToObstacleWhenOperational(direction);
				
				
			}
			
		}

	}
	/**
	 * gets corresponding Sensor given a Direction{LEFT, RIGHT, FORWARD, BACKWARD}
	 * @param direction
	 * @return Sensor
	 */
	protected DistanceSensor getSensor(Direction direction) {
		switch (direction) {
		case LEFT: {
			return leftSensor;
		}
		case RIGHT:{
			return rightSensor;
		}
		case FORWARD:{
			return forwardSensor;
		}
		case BACKWARD:{
			return backwardSensor;
		}
		}
		return null;
	}
	/**
	 * Calculates distance to obstacle using a Operational working sensor
	 * @param direction
	 * @return distance to object from direction
	 */
	protected int distanceToObstacleUsingOperationalNeighborSensor(Direction direction) {
		int distance = 0;
		if(isOperational(leftDirection(direction))) {
			rotate(Turn.RIGHT);
			distance=distanceToObstacleWhenOperational(leftDirection(direction));
			rotate(Turn.LEFT);
		}
		else if (isOperational(rightDirection(direction))) {
			rotate(Turn.LEFT);
			distance=distanceToObstacleWhenOperational(rightDirection(direction));
			rotate(Turn.RIGHT);
		}
		else if (isOperational(oppositeDirection(direction))) {
			rotate(Turn.AROUND);
			distance=distanceToObstacleWhenOperational(rightDirection(direction));
			rotate(Turn.AROUND);
		}
		
		return distance;
		
	}
	/**
	 * gives the closest operational neighbor sensor given the direction
	 * @param direction
	 * @return
	 */
	protected Direction closestOperationalNeighborSensor(Direction direction) {
		if(isOperational(leftDirection(direction))) {
			return leftDirection(direction);
		}
		else if (isOperational(rightDirection(direction))) {
			return rightDirection(direction);
		}
		else if (isOperational(oppositeDirection(direction))) {
			return oppositeDirection(direction);
		}
		
		return null;
		
	}
	/**
	 * gives the left of given direction
	 * @param direction
	 * @return
	 */
	protected Direction leftDirection(Direction direction) {
		switch (direction) {
		case LEFT: {
			return Direction.BACKWARD;
		}
		case RIGHT:{
			return Direction.FORWARD;
		}
		case FORWARD:{
			return Direction.LEFT;
		}
		case BACKWARD:{
			return Direction.RIGHT;
		}
		}
		return null;
		
	}
	/**
	 * gives the right of given direction
	 * @param direction
	 * @return
	 */
	protected Direction rightDirection(Direction direction) {
		switch (direction) {
		case LEFT: {
			return Direction.FORWARD;
		}
		case RIGHT:{
			return Direction.BACKWARD;
		}
		case FORWARD:{
			return Direction.RIGHT;
		}
		case BACKWARD:{
			return Direction.LEFT;
		}
		}
		return null;
		
	}
	/**
	 * gives the opposite of given direction
	 * @param direction
	 * @return
	 */
	protected Direction oppositeDirection(Direction direction) {
		switch (direction) {
		case LEFT: {
			return Direction.RIGHT;
		}
		case RIGHT:{
			return Direction.LEFT;
		}
		case FORWARD:{
			return Direction.BACKWARD;
		}
		case BACKWARD:{
			return Direction.FORWARD;
		}
		}
		return null;
		
	}
	
	
	/**
	 * checks if sensor for direction is operational or not
	 * @param direction
	 * @return true for is operational anf false if it is not operational or if sensor is null.
	 */
	protected boolean isOperational ( Direction direction ){
		boolean operational = false;
		DistanceSensor sensor = null;
		switch (direction) {
		case FORWARD:
			sensor=forwardSensor;
			break;
		case LEFT:
			sensor=leftSensor;
			break;
		case RIGHT:
			sensor=rightSensor;
			break;
		case BACKWARD:
			sensor=backwardSensor;
			break;

		default:
			break;
		}
		if (sensor==null) { //check sensor does not exist
			return false;
		}
		if(sensor instanceof UnreliableSensor) { //check sensor is part of unreliable class
			operational = ((UnreliableSensor) sensor).isSensorOperational();
			return operational;
			
		}
		else { //check sensor is part of reliable class
			return true;
		}
		
	}

	
	/**
	 * calculate distance to obstacle using the given operational sensor
	 * @param direction
	 * @return
	 * @throws UnsupportedOperationException
	 */
	protected int distanceToObstacleWhenOperational(Direction direction) throws UnsupportedOperationException {
		switch (direction) {
		case LEFT:
			try {
				return calculateDistanceBasedOnSensor(leftSensor);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

		case RIGHT:
			try {
				return calculateDistanceBasedOnSensor(rightSensor);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		case FORWARD:
			try {
				return calculateDistanceBasedOnSensor(forwardSensor);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		case BACKWARD:
			try {
				return calculateDistanceBasedOnSensor(backwardSensor);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		default:
			return 0;
		}
	}
	
	
	
	
	
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures,
			int meanTimeToRepair) throws UnsupportedOperationException {
		switch (direction) {
		case LEFT: {
			leftSensor.startFailureAndRepairProcess(0, 0);
			break;
		}
		case RIGHT:{
			rightSensor.startFailureAndRepairProcess(0, 0);
			break;
		}
		case FORWARD:{
			forwardSensor.startFailureAndRepairProcess(0, 0);
			break;
		}
		case BACKWARD:{
			backwardSensor.startFailureAndRepairProcess(0, 0);
			break;
		}
		}
		//calls the sensor start failure and repair process to start thread for said direction
		
	}
		

	@Override 
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		switch (direction) {
		case LEFT: {
			leftSensor.stopFailureAndRepairProcess();
			break;
		}
		case RIGHT:{
			rightSensor.stopFailureAndRepairProcess();
			break;
		}
		case FORWARD:{
			forwardSensor.stopFailureAndRepairProcess();
			break;
		}
		case BACKWARD:{
			backwardSensor.stopFailureAndRepairProcess();
			break;
		}
		}
		
		
	
		
	}

}