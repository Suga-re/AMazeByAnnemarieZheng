package com.example.amazebyannemariezheng;
// @Author: ANNEMARIE ZHENG
/*Responsibilities:
  * Drives robot to the exit using shortest path from maze
  * 
  *  Collaborators:
  *  Maze.java 
  *  	-neighboreCloserToExit function shortest path
  *  Floorplan.java
  *  ReliableRobot.java
  */



import generation.CardinalDirection;
import generation.Maze;
import com.example.amazebyannemariezheng.Robot.Direction;
import com.example.amazebyannemariezheng.Robot.Turn;

////handleUserInput(UserInput userInput, int value)

public class Wizard implements RobotDriver{
	Maze maze=null;
	Robot robot;
	int pathLength=0;
	
	/**
	 * Assigns a robot platform to wizard. 
	 * wizard uses a robot to perform, this method provides it with this necessary information.
	 * @param r robot to operate
	 */
	public void setRobot(Robot r) {
		this.robot = r;

	}
	/**
	 * Provides wizard with the maze information.
	 * @param maze represents the maze, must be non-null and a fully functional maze object.
	 */
	public void setMaze(Maze maze) {
		this.maze= maze;
	}
	
	public Maze getMaze() {
		return maze;
	}
	
	
	/**
	 * Drives the robot towards the exit
	 * by using maze.getNeighborClosestToExit. 
	 * Given the exit exists and  
	 * given the robot's energy supply lasts long enough. 
	 * //exit will exist if a proper maze is generated and given to wizard robot
	 * When the robot reached the exit position and its forward
	 * direction points to the exit the search terminates and 
	 * the method returns true.
	 * If the robot failed due to lack of energy or crashed, the method
	 * throws an exception. 
	 * //implemented in Drive1Step2Exit
	 * 
	 * If the method determines that it is not capable of finding the
	 * exit it returns false, for instance, if it determines it runs
	 * in a cycle and can't resolve this.
	 * @return true if driver successfully reaches the exit, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	public boolean drive2Exit() throws Exception{
		if (maze==null) {
			throw new Exception("Maze does not exist so there is no Exit");
		}
		int[] exitPosition=maze.getExitPosition();
		int[] curLoc=robot.getCurrentPosition();
		
		
		while( curLoc[0]!=exitPosition[0] || curLoc[1]!=exitPosition[1] ) { // drives all the way to exit position
			drive1Step2Exit();
			curLoc=robot.getCurrentPosition();
			if (robot.hasStopped()==true) {
				break;
			}
		}
		if (curLoc[0]!=exitPosition[0] || curLoc[1]!=exitPosition[1]) {
			return false;
		}
		
		if (robot.canSeeThroughTheExitIntoEternity(Direction.LEFT)==true) {
			robot.rotate(Turn.LEFT);
		}
		else if (robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT)==true) {
			robot.rotate(Turn.RIGHT);
		}
		else if (robot.canSeeThroughTheExitIntoEternity(Direction.BACKWARD)==true) {
			robot.rotate(Turn.AROUND);
		}
		return true;
		
	}
	
	/**
	 * Drives the robot one step towards the exit following
	 * its solution strategy and given the exists and 
	 * given the robot's energy supply lasts long enough.
	 * It returns true if the driver successfully moved
	 * the robot from its current location to an adjacent
	 * location.
	 * At the exit position, it rotates the robot 
	 * such that if faces the exit in its forward direction
	 * and returns false. 
	 * If the robot failed due to lack of energy or crashed, the method
	 * throws an exception. 
	 * @return true if it moved the robot to an adjacent cell, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	public boolean drive1Step2Exit() throws Exception{
		
		int[]startPosition=robot.getCurrentPosition();
		
		int[]nextPosition=maze.getNeighborCloserToExit(startPosition[0], startPosition[1]);
				
		
		
		if (startPosition[0]-1==nextPosition[0]) {
			turnToCardinalDirection(CardinalDirection.West);
			//direction is west
			
		}
		else if(startPosition[0]+1==nextPosition[0]) {
			turnToCardinalDirection(CardinalDirection.East);
			//direction is east
		}
		else if(startPosition[1]-1==nextPosition[1]) {
			turnToCardinalDirection(CardinalDirection.North);
			//direction is North
		}
		else if(startPosition[1]+1==nextPosition[1]) {
			turnToCardinalDirection(CardinalDirection.South);
			
			//direction is South
		}
		
		robot.move(1);// move towards next position
		
		if (robot.hasStopped()==true) {
			throw new Exception("Robot has Stopped");
		}
		
		
		int[] adjPosition=robot.getCurrentPosition();
		if (startPosition[0]==adjPosition[0] && startPosition[1]==adjPosition[1]) {
			return false;
		}
		else {
			pathLength++; //updates pathlength after moving one step
			return true;
		}
	}
	 /**
     * turns the robot to face the cardinal direction given
     * @param direction you want robot to face
     */
	protected void turnToCardinalDirection(CardinalDirection direction) {
		CardinalDirection curDirection=robot.getCurrentDirection();
		if (direction==curDirection.oppositeDirection()) {
			robot.rotate(Turn.AROUND);
			
		}
		else if (direction==curDirection.oppositeDirection().rotateClockwise()) {
			robot.rotate(Turn.RIGHT);
			
		}
		else if (direction==curDirection.rotateClockwise()) {
			robot.rotate(Turn.LEFT);
			
		}
	}
	
	/**
	 * Returns the total energy consumption of the journey, i.e.,
	 * the difference between the robot's initial energy level at
	 * the starting position and its energy level at the exit position. 
	 * This is used as a measure of efficiency for a robot driver.
	 * @return the total energy consumption of the journey
	 */
public float getEnergyConsumption(){ 
	//3500 is the inital energy level so by looking at the difference with current level, you can get energy Consumed
	return 3500-robot.getBatteryLevel();
}
	
	/**
	 * Returns the total length of the journey in number of cells traversed. 
	 * Being at the initial position counts as 0. 
	 * This is used as a measure of efficiency for a robot driver.
	 * @return the total length of the journey in number of cells traversed
	 */
	public int getPathLength() {
		//update a variable each time curloc is not exit position in drive to exit
		return pathLength;
		
	}

	
	
	

}
