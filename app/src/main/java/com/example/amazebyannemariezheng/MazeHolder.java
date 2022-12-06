package com.example.amazebyannemariezheng;

import generation.Maze;
import generation.Order;

public class MazeHolder {

	private static Maze maze;


	public Maze getMaze() {return maze;}

	public void setMaze(Maze maze) {this.maze = maze;}

	private static final MazeHolder holder = new MazeHolder();
	public static MazeHolder getInstance() {return holder;}
}
