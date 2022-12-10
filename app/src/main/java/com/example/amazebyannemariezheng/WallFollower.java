package com.example.amazebyannemariezheng;


import generation.CardinalDirection;
import com.example.amazebyannemariezheng.Robot.Direction;
import com.example.amazebyannemariezheng.Robot.Turn;

// @Author: ANNEMARIE ZHENG
/*Responsibilities:
  * Drives robot to the exit by following one side of the wall
  *Keeps track of energy consumed 
  *
  * *  Collaborators:
  *  Floorplan.java 
  *  Maze.java
  *  Robot eg. UnreliableRobot.java or ReliableRobot.java
  */
public class WallFollower extends Wizard{
	int roomExitNum=0;
	@Override
	public boolean drive2Exit() throws Exception{
		if (maze==null) {
			throw new Exception("Maze does not exist so there is no Exit");
		}
		int[] curLoc=robot.getCurrentPosition();
		
		//from location check if left sensor has a wall and if not then turn such that left sensor has a wall

		while(robot.distanceToObstacle(Direction.LEFT)!=0) { //while there is no wall touching robot left sensor
			robot.rotate(Turn.RIGHT);//rotate till there is a wall touching leftsensor
		}
		

		while( robot.isAtExit()==false ) { //while location is not exit
			drive1Step2Exit();
			curLoc=robot.getCurrentPosition();
			if (robot.hasStopped()==true) {
				break;
			}
		}
		if (robot.isAtExit()==false) {//if not at exit
			return false;
		}
		//if at exit rotate such that you are facing the exit
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
	@Override
	public boolean drive1Step2Exit() throws Exception{
		int[]startPosition=robot.getCurrentPosition();
		CardinalDirection currentDirection=robot.getCurrentDirection();
		//check if wall exists on left side
		if(robot.distanceToObstacle(Direction.LEFT)==0) { //if left wall exists
			
			while (robot.distanceToObstacle(Direction.FORWARD)==0) { //while forward sensor has a wall infront
					robot.rotate(Turn.RIGHT);
					currentDirection=robot.getCurrentDirection();
				}
			
			robot.move(1);
		}
		
		else {// if left wall does not exist 
				robot.rotate(Turn.LEFT); //then turn to left side and then driveone step
				robot.move(1);

		}
		
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
	

}