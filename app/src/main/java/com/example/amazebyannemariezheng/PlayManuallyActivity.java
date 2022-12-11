package com.example.amazebyannemariezheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;

import java.util.logging.Logger;

import generation.CardinalDirection;
import generation.Maze;

public class PlayManuallyActivity extends AppCompatActivity {
    private Button shortCut;
    private Button jump;
    private ImageButton forwardMove;
    private ImageButton leftTurn;
    private ImageButton rightTurn;
    private Button zoomIn;
    private Button zoomOut;
    private Switch toggleWalls;
    private Switch toggleMap;
    private Switch toggleSolution;  // toggle switch to show solution in overall maze on screen
    private StatePlaying myStatePlaying;
    private int pathLength=0;
    //PlayManuallyActivity connects to a StatePlaying object

    private MazePanel panel;
    /**
     * Maze holds the main information on where walls are.
     */
    Maze maze;
    private boolean showMaze;           // toggle switch to show overall maze on screen
    private boolean showSolution;
    private boolean mapMode; // true: display map of maze, false: do not display map of maze
    private String playingState="Manual:";
    int px = 0;// current position on maze grid (x,y)
    int py = 0;
    CardinalDirection cd = CardinalDirection.East;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);
        maze = MazeHolder.getInstance().getMaze();

        //initialize private variables to their view on xml
        shortCut=(Button)findViewById(R.id.btnShortcut);
        jump=(Button)findViewById(R.id.btnJump);
        forwardMove=(ImageButton)findViewById(R.id.imgbtnForward);
        leftTurn=(ImageButton)findViewById(R.id.imgbtnLeft);
        rightTurn=(ImageButton)findViewById(R.id.imgbtnRight);

        panel=(MazePanel)findViewById(R.id.mazePanelManual);
        zoomIn =(Button)findViewById(R.id.btnZoomInManual);
        zoomOut=(Button)findViewById(R.id.btnZoomOutManual);

        toggleWalls=(Switch) findViewById(R.id.switchWallsManual); //the 2d walls ontop view
        toggleMap=(Switch) findViewById(R.id.switchMapManual);
        toggleSolution=(Switch) findViewById(R.id.switchSolutionManual);


        myStatePlaying= new StatePlaying();
        myStatePlaying.setMaze(maze);
        myStatePlaying.playManual=this;

        myStatePlaying.setPlayManualActivity(this);
        myStatePlaying.start(panel);


    //setting onClick Listeners for each View
        forwardMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(playingState,"Moving One Step Forward");
                myStatePlaying.handleUserInput(Constants.UserInput.UP,0);
                panel.commit();
                pathLength++;

            }
        });
        leftTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(playingState,"rotated Left");
                myStatePlaying.handleUserInput(Constants.UserInput.LEFT,0);
                panel.commit();
            }
        });
        rightTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(playingState,"rotated Right");
                myStatePlaying.handleUserInput(Constants.UserInput.RIGHT,0);
                panel.commit();
            }
        });
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(playingState,"Jumping one step forward");
                myStatePlaying.handleUserInput(Constants.UserInput.JUMP,0);
                panel.commit();
                pathLength++;
            }
        });

        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(playingState,"Zooming In Map");
                myStatePlaying.handleUserInput(Constants.UserInput.ZOOMIN,0);
                panel.commit();
            }
        });

        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(playingState,"Zooming Out Map");
                myStatePlaying.handleUserInput(Constants.UserInput.ZOOMOUT,0);
                panel.commit();
            }
        });
        toggleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(playingState,"Toggling Map");
                myStatePlaying.handleUserInput(Constants.UserInput.TOGGLEFULLMAP,0);
                panel.commit();

            }
        });
        toggleWalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(playingState,"Toggle Walls");
                myStatePlaying.handleUserInput(Constants.UserInput.TOGGLELOCALMAP,0);
                panel.commit();

            }
        });
        toggleSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(playingState,"Toggling Solution");
                myStatePlaying.handleUserInput(Constants.UserInput.TOGGLESOLUTION,0);
                panel.commit();
            }
        });


        shortCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playingToWinning();
            }
        });

    }
    public void playingToWinning(){
        Intent intent = new Intent(PlayManuallyActivity.this, WinningActivity.class);
        intent.putExtra("path length", pathLength);
//        MazeHolder.getInstance().setMaze(maze);
        startActivity(intent);
    }


}