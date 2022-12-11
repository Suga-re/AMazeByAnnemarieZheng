package com.example.amazebyannemariezheng;
// @Author: ANNEMARIE ZHENG
/*Responsibilities:
  * Uses sensors and algorithm to move player out of maze
  *Keeps track of energy consumed 
  * *  Collaborators:
  *  Algorithm class eg. Wizard.java/WallFollower.java 
  *  Sensor Class eg. ReliableSensor.Java/UnreliableSensor.java
  *  Control -> StatePlaying.java
  */




import generation.CardinalDirection;
import generation.Maze;
import generation.Wallboard;
import com.example.amazebyannemariezheng.Constants.UserInput;
import com.example.amazebyannemariezheng.Robot.Direction;
import com.example.amazebyannemariezheng.Robot.Turn;

public class ReliableRobot implements Robot {
//	Control controller=new Control();
	StatePlaying control;
	Maze maze;
	float[] batteryLevel= {3500};
	int odometer=0;
	boolean hasStopped=false;

	DistanceSensor leftSensor= new ReliableSensor();
	DistanceSensor rightSensor= new ReliableSensor();
	DistanceSensor forwardSensor= new ReliableSensor();
	DistanceSensor backwardSensor= new ReliableSensor();




	
	///////////////////////////////////////////////////////////////////
	/////////////////// Initial configuration of a robot   ////////////
	///////////////////////////////////////////////////////////////////

//	/**
//	 * Provides the robot with a reference to the controller to cooperate with.
//	 * The robot memorizes the controller such that this method is most likely called only once
//	 * and for initialization purposes. The controller serves as the main source of information
//	 * for the robot about the current position, the presence of walls, the reaching of an exit.
//	 * The controller is assumed to be in the playing state.
//	 * @param controller is the communication partner for robot
//	 * @throws IllegalArgumentException if controller is null,
//	 * or if controller is not in playing state,
//	 * or if controller does not have a maze
//	 */
//	public void setController(Control controller) {
//		if (controller.getMaze()==null) {
//			throw new IllegalArgumentException();
//		}
//		if (!(controller.currentState instanceof StatePlaying)) {
//			throw new IllegalArgumentException();
//		}
//		this.controller=controller;
//	}
	public void setRobotMaze(StatePlaying control){
		this.maze = control.getMaze();
	}

//	public void setSensors(StatePlaying control){
//
//		backwardSensor.setMaze(control.getMaze());
//		forwardSensor.setMaze(control.getMaze());
//		leftSensor.setMaze(control.getMaze());
//		rightSensor.setMaze(control.getMaze());
//	}


	
	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		
		switch (mountedDirection) {
		case LEFT: {
			leftSensor=sensor;
			break;
		}
		case RIGHT:{
			rightSensor=sensor;
			break;
		}
		case FORWARD:{
			forwardSensor=sensor;
			break;
		}
		case BACKWARD:{
			backwardSensor=sensor;
			break;
		}
		}
		
	}



	
	///////////////////////////////////////////////////////////////////
	/////////////////// Current location in game   ////////////////////
	///////////////////////////////////////////////////////////////////
	/**
	 * Provides the current position as (x,y) coordinates for 
	 * the maze as an array of length 2 with [x,y].
	 * @return array of length 2, x = array[0], y = array[1]
	 * and ({@code 0 <= x < width, 0 <= y < height}) of the maze
	 * @throws Exception if position is outside of the maze
	 */
	public int[] getCurrentPosition() throws Exception{
		int [] Position = control.getCurrentPosition();
		
		int x= Position[0];
		int y=Position[1];
		int width= control.getMaze().getWidth();
		int height= control.getMaze().getHeight();
		
		if (x<0 || x>=width || y<0 ||y>=height) { // when Position is out of maze
			throw new Exception();
		}
		
		return Position;
	
	}
	
	/**
	 * Provides the robot's current direction.
	 * @return cardinal direction is the robot's current direction in absolute terms
	 */	
	public CardinalDirection getCurrentDirection() {
		return control.getCurrentDirection();
	}

	///////////////////////////////////////////////////////////////////
	/////////////////// Battery and Energy consumption ////////////////
	///////////////////////////////////////////////////////////////////
	/**
	 * Returns the current battery level.
	 * The robot has a given battery level (energy level) 
	 * that it draws energy from during operations. 
	 * The particular energy consumption is device dependent such that a call 
	 * for sensor distance2Obstacle may use less energy than a move forward operation.
	 * If battery {@code level <= 0} then robot stops to function and hasStopped() is true.
	 * @return current battery level, {@code level > 0} if operational. 
	 */
	public float getBatteryLevel() {
		return batteryLevel[0];
	}
	/**
	 * Sets the current battery level.
	 * The robot has a given battery level (energy level) 
	 * that it draws energy from during operations. 
	 * The particular energy consumption is device dependent such that a call 
	 * for distance2Obstacle may use less energy than a move forward operation.
	 * If battery {@code level <= 0} then robot stops to function and hasStopped() is true.
	 * @param level is the current battery level
	 * @throws IllegalArgumentException if level is negative 
	 */
	public void setBatteryLevel(float level) {
		this.batteryLevel[0]=level;
	}

	/**
	 * Gives the energy consumption for a full 360 degree rotation.
	 * Scaling by other degrees approximates the corresponding consumption. 
	 * @return energy for a full rotation
	 */
	public float getEnergyForFullRotation() {
		return 12;
	}
	/**
	 * Gives the energy consumption for moving forward for a distance of 1 step.
	 * For simplicity, we assume that this equals the energy necessary 
	 * to move 1 step and that for moving a distance of n steps 
	 * takes n times the energy for a single step.
	 * @return energy for a single step forward
	 */
	public float getEnergyForStepForward() {
		return 6;
	}
	///////////////////////////////////////////////////////////////////
	/////////////////// Odometer, distance traveled    ////////////////
	///////////////////////////////////////////////////////////////////
	/** 
	 * Gets the distance traveled by the robot.
	 * The robot has an odometer that calculates the distance the robot has moved.
	 * Whenever the robot moves forward, the distance 
	 * that it moves is added to the odometer counter.
	 * The odometer reading gives the path length if its setting is 0 at the start of the game.
	 * The counter can be reset to 0 with resetOdomoter().
	 * @return the distance traveled measured in single-cell steps forward
	 */
	public int getOdometerReading() {
		return odometer;
	}
	/** 
     * Resets the odometer counter to zero.
     * The robot has an odometer that calculates the distance the robot has moved.
     * Whenever the robot moves forward, the distance 
     * that it moves is added to the odometer counter.
     * The odometer reading gives the path length if its setting is 0 at the start of the game.
     */
	public void resetOdometer() {
		this.odometer=0;
	}
	///////////////////////////////////////////////////////////////////
	/////////////////// Actuators /////////////////////////////////////
	///////////////////////////////////////////////////////////////////

	@Override
	public void rotate(Robot.Turn turn) {
		switch (turn) {
		case LEFT: {
			
			if(batteryLevel[0]>3) {
				
				batteryLevel[0]=batteryLevel[0]-3;
				control.handleUserInput(UserInput.LEFT, 0);
			}
			break;
		}
		case RIGHT:{
			
			if(batteryLevel[0]>3) {
				
				batteryLevel[0]=batteryLevel[0]-3;
				control.handleUserInput(UserInput.RIGHT, 0);
			}
			break;
		}
		case AROUND:{
			
			if(batteryLevel[0]>6) {
				batteryLevel[0]=batteryLevel[0]-6;
				control.handleUserInput(UserInput.LEFT, 0);
				control.handleUserInput(UserInput.LEFT, 0);
			}
			break;
		}
		}
		
	}
	
	/**
	 * Moves robot forward a given number of steps. A step matches a single cell.
	 * If the robot runs out of energy somewhere on its way, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level. 
	 * If the robot hits an obstacle like a wall, it remains at the position in front 
	 * of the obstacle and also hasStopped() == true as this is not supposed to happen.
	 * This is also helpful to recognize if the robot implementation and the actual maze
	 * do not share a consistent view on where walls are and where not.
	 * @param distance is the number of cells to move in the robot's current forward direction 
	 * @throws IllegalArgumentException if distance not positive
	 */
	public void move(int distance) {
		if(distance<0) {
			throw new IllegalArgumentException();
		}
		else {
			for(int i=0;i<distance;i++) {
				if(distanceToObstacle(Direction.FORWARD)==0) {
					hasStopped=true;
				}
				batteryLevel[0]=batteryLevel[0]-getEnergyForStepForward();
				if(hasStopped()==false) {
					control.handleUserInput(UserInput.UP, 0);
				}
			}
			

			
		}
	}
	/**
	 * Makes robot move in a forward direction even if there is a wall
	 * in front of it. In this sense, the robot jumps over the wall
	 * if necessary. The distance is always 1 step and the direction
	 * is always forward.
	 * If the robot runs out of energy somewhere on its way, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level.
	 * If the robot tries to jump over an exterior wall and
	 * would land outside of the maze that way,  
	 * it remains at its current location and direction,
	 * hasStopped() == true as this is not supposed to happen.
	 */
	public void jump() {
		int[]Position=control.getCurrentPosition();
		int x=Position[0];
		int y=Position[1];
		CardinalDirection cd=getCurrentDirection();
		Wallboard wallboard= new Wallboard(x, y, cd);
		
		if (control.getMaze().hasWall(x, y, cd)) {
			if (control.getMaze().getFloorplan().isPartOfBorder(wallboard)!=true) {
				
				if(batteryLevel[0]>=40) {
					control.handleUserInput(UserInput.JUMP, 0);
					batteryLevel[0]=batteryLevel[0]-40;
				}
				else {
					hasStopped=true;//for lack of energy
				}
				
			}
			else {
				hasStopped=true; //for if it is trying to jump over an exterior wall
			}
		}
		else {
			move(1);
		}
		
	}
	///////////////////////////////////////////////////////////////////
	/////////////////// Sensors   /////////////////////////////////////
	///////////////////////////////////////////////////////////////////
	/**
	 * Tells if the current position is right at the exit but still inside the maze. 
	 * The exit can be in any direction. It is not guaranteed that 
	 * the robot is facing the exit in a forward direction.
	 * @return true if robot is at the exit, false otherwise
	 */
	public boolean isAtExit() {
		
		int []currentPos=control.getCurrentPosition();
		int dist= control.getMaze().getDistanceToExit(currentPos[0], currentPos[1]);
		if (dist==1) { //if at exit position, distance would be 1 to exit
			return true;
		}
		else {
			return false;
		}
		
	}
	/**
	 * Tells if current position is inside a room. 
	 * @return true if robot is inside a room, false otherwise
	 */	
	public boolean isInsideRoom() {
		
		int []currentPos=control.getCurrentPosition();
		return control.getMaze().isInRoom(currentPos[0], currentPos[1]);
		
		
	}
	/**
	 * Tells if the robot has stopped for reasons like lack of energy, 
	 * hitting an obstacle, etc.
	 * Once a robot is has stopped, it does not rotate or 
	 * move anymore.
	 * @return true if the robot has stopped, false otherwise
	 */
	public boolean hasStopped() {
		if (batteryLevel[0]<=0) {
			hasStopped=true;
		}
		return hasStopped;
	}
	/**
	 * Tells the distance to an obstacle (a wall) 
	 * in the given direction.
	 * The direction is relative to the robot's current forward direction.
	 * Distance is measured in the number of cells towards that obstacle, 
	 * e.g. 0 if the current cell has a wallboard in this direction, 
	 * 1 if it is one step forward before directly facing a wallboard,
	 * Integer.MaxValue if one looks through the exit into eternity.
	 * The robot uses its internal DistanceSensor objects for this and
	 * delegates the computation to the DistanceSensor which need
	 * to be installed by calling the addDistanceSensor() when configuring
	 * the robot.
	 * @param direction specifies the direction of interest
	 * @return number of steps towards obstacle if obstacle is visible 
	 * in a straight line of sight, Integer.MAX_VALUE otherwise
	 * @throws Exception 
	 */




	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
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
	 /**
     * helper function to calculate distance given sensor. This allows us to pass it into the cases in distanceToObstacle
     */
	protected int calculateDistanceBasedOnSensor(DistanceSensor sensor) throws Exception {
		if (((ReliableSensor)sensor).getMountedDirection()==null) {
			throw new UnsupportedOperationException();
		}
		else{
			return sensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), batteryLevel);
		}

	}
	
	
	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction)
			throws UnsupportedOperationException {
		try {
			if (distanceToObstacle(direction)==Integer.MAX_VALUE) {
				return true;
			}
			else {
				return false;
				}
		} catch (Exception e) {
			throw new UnsupportedOperationException();
		}
	}
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures,
			int meanTimeToRepair) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
		
	}
	
	@Override 
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
		
	}


  
}
