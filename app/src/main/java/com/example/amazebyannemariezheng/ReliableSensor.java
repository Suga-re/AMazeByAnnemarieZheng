package com.example.amazebyannemariezheng;


import generation.CardinalDirection;
import generation.Maze;
import com.example.amazebyannemariezheng.Robot.Direction;

// @Author: ANNEMARIE ZHENG
/*Responsibilities:
  * Tells how many steps till it gets to a wall given relative position
  * Translate relative directions to cardinal directions
  *
  * Collaborators
  * Maze.java
  * Floorplan.java
  * CardinalDirection.java
  */
public class ReliableSensor implements DistanceSensor{
	//has Operational boolean that determines if sensor is working or not
	private boolean Operational=true;
	
	public boolean isSensorOperational() {
		return Operational;
	}
	protected Direction mountedDirection=null;
	
	protected Maze maze=null;
	
	public Direction getMountedDirection() {
		return mountedDirection;
	}
	public Maze getMaze() {
		return maze;
	}
	/**
	 * given currentDirection and the mountedDirection, it gives the cardinal direction the sensor is pointing towards
	 * @param currentDirection is the direction robot is currently facing
	 */
	protected CardinalDirection relativeDirectionBasedOnSensor(CardinalDirection currentDirection) {
		switch (mountedDirection) {
		case FORWARD: {
			break; //keep currentDirection the same
		}
		case BACKWARD:{
			currentDirection=currentDirection.oppositeDirection(); 
			break;
		}
		case RIGHT:{
			currentDirection=currentDirection.oppositeDirection(); 
			currentDirection=currentDirection.rotateClockwise();
			break;
		}
		case LEFT:{
			currentDirection=currentDirection.rotateClockwise();
			break;
		}
	}
		return currentDirection;
	}
	/**
	 * Updates position one step towards specified cardinal direction.
	 * @param Position of current location, must be inside maze and does not have wall towards direction (helper to distance to object)
	 */
	 protected int[] updatePositionTowardsDirectionby1(int[] Position, CardinalDirection Direction){
		 
		 switch (Direction) {
		 	case North:
		 		Position[1]-= 1; //decrement y by 1
		 		break;
		 	case East:
		 		Position[0] += 1; //increment x by 1
		 		break;
		 	case South:
		 		Position[1] += 1; //increment y by 1
		 		break;
		 	case West:
		 		Position[0] -=1 ; //subtract x by 1
		 		break;
		  }
		 return Position;
	 }
	
	
	 	/**
	     * gets the direction you need to face when you are at exit position such that you are facing the exit
	     */
	protected CardinalDirection getExitDirection(){
		int []location = maze.getExitPosition(); //gets the location of exit. 
		int x=location[0];
		int y=location[1];
		
		//
		//checks if exit location border wall torn down was one on the left border
	  if (x==0) {
		  if (y==0) { //this is the top left corner point 0,0 which has exit either on the north or west border wall
			  if (maze.hasWall(x, y, CardinalDirection.North)==false) { //if border wall does not exist, it is the exit wall torn down
				return CardinalDirection.North;
			}
			  else { 
				return CardinalDirection.West;
			}
			  
		  }
		  else if (y==maze.getHeight()-1) { //this is the bottom left corner which either has exit at the south or the west
			  if (maze.hasWall(x, y, CardinalDirection.South)==false) {
					return CardinalDirection.South;
				}
				  else {
					return CardinalDirection.West;
				}
		}
		  else { // position has a border wall only in the west that could be torn down as the exit
			return CardinalDirection.West; 
		}
		  
	  }
	  
	// 
	//checks if exit location border wall torn down was one on the Right border
	  else if (x==maze.getWidth()-1) {
		if (y==0) { //top right corner which can only have exit either on north wall or east wall
			if (maze.hasWall(x, y, CardinalDirection.North)==false) { //if border wall does not exist, it is the exit wall torn down
				return CardinalDirection.North;
			}
			else { 
				return CardinalDirection.East;
			}
			
		}
		else if (y==maze.getHeight()-1) { //this is the bottom right corner which either has exit at the south or the east
			  if (maze.hasWall(x, y, CardinalDirection.South)==false) {
					return CardinalDirection.South;
				}
			  else {
					return CardinalDirection.East;
				}
		}
		else { // position has a border wall only in the east that could be torn down as the exit
			return CardinalDirection.East; 
		}
	}
	
	else if (y==0) {
		return CardinalDirection.North;
	}
	  
	else{
		return CardinalDirection.South;
	}
 }
	 
	
	/**
	 * Provides the maze information that is necessary to make
	 * a DistanceSensor able to calculate distances.
	 * @param maze the maze for this game
	 * @throws IllegalArgumentException if parameter is null
	 * or if it does not contain a floor plan
	 */
	public void setMaze(Maze maze) {
		this.maze=maze;
	}
	
	
	/**
	 * Returns the amount of energy this sensor uses for 
	 * calculating the distance to an obstacle exactly once.
	 * This amount is a fixed constant for a sensor.
	 * @return the amount of energy used for using the sensor once
	 */
	public float getEnergyConsumptionForSensing() {
		return 1;
	}
	
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		
		//Possible exception error handling
		if (mountedDirection==null || maze==null) {
			throw new Exception("SensorFailure");
		}
		if (powersupply[0]<getEnergyConsumptionForSensing()) {
			throw new Exception("PowerFailure");
		}
		if(currentPosition==null || currentDirection==null) {
			throw new IllegalArgumentException("one of the parameters is null");
		}
		if(currentPosition[0] < 0 || currentPosition[0] >= maze.getWidth()|| currentPosition[1] < 0 || currentPosition[1] >= maze.getHeight()) {
			throw new IllegalArgumentException("currentPosition is outside of legal range");
		}

		//if none of the exception occurs, calculates distance to obstacle
		int x=currentPosition[0];
		int y=currentPosition[1];
		
		currentDirection=relativeDirectionBasedOnSensor(currentDirection);
		
		//counts distance to obstacle from current location towards direction
		int counter=0;
		
		int xPosExit=maze.getExitPosition()[0];
		int yPosExit=maze.getExitPosition()[1];
		
		while (maze.hasWall(x, y, currentDirection)==false) {
			if(xPosExit==x && yPosExit==y) {
				if(getExitDirection()==currentDirection) {
					return Integer.MAX_VALUE;
				}
					
			}
			counter+=1;
			currentPosition=updatePositionTowardsDirectionby1(currentPosition, currentDirection);
			x=currentPosition[0];
			y=currentPosition[1];
			
		}
		return counter;
	}


	@Override
	public void setSensorDirection(Direction mountedDirection) {
		this.mountedDirection=mountedDirection;
		
	}

	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
		
	}
	
	



}

