package com.example.amazebyannemariezheng;


import generation.CardinalDirection;

// @Author: ANNEMARIE ZHENG 
/*Responsibilities:
  * Alternates between time periods of either operational or not (Manually able to change operation state) and when unoperational
  * initialize repair of sensor
  * 
  *inherits ReliableSensor Responsibilities:
  *  	Tells how many steps till it gets to a wall given relative position
  *		Translate relative directions to cardinal directions
  *
  * Collaborators
  * Floorplan
  * thread
  * 
  */
public class UnreliableSensor extends ReliableSensor implements Runnable,DistanceSensor{
	//has Operational boolean that determines if sensor is working or not
	private boolean Operational=true;
	
	//thread object for the sensor
	 private Thread sensorThread;
	/**
	 * Check if sensor is operational
	 * returns true for is operational and false for not operational
	 */
	public boolean isSensorOperational() {
		return Operational;
	}
	/**
	 * check if thread is null
	 * returns true for if it is and false if not
	 */
	public boolean isThreadNull() {
		if (sensorThread==null) {
			return true;
		}
		else {
			return false;
		}
	}
	@Override
	public void run() {// run infinitely till stop failure and repair process stops it
		while(true) {
			
			try {
				Operational=true;
				Thread.sleep(4000);
				// sets Operational to true
				//sleep for 4 sec
				
				Operational=false;
				Thread.sleep(2000);
				//set operational to false
				//sleep for 2 sec
				
			} 
			catch (InterruptedException e) {
				return;
			}
		
		}
				
		
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
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair) throws UnsupportedOperationException {
		sensorThread = new Thread(this);
		sensorThread.start();		
		//starts new thread of the unreliable sensor
		
	}

	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		sensorThread.interrupt();
		Operational=true;//stop the thread and then set operational to true
		

}


}