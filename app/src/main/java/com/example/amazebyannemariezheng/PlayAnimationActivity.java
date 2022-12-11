package com.example.amazebyannemariezheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import generation.Maze;

public class PlayAnimationActivity extends AppCompatActivity {
    Button win;
    Button lose;
    MazePanel mazePanel;
    ProgressBar energyBar;
    private StatePlaying myStatePlaying;
    private String playingState="Animation:";
    private Switch toggleWalls;
    private Switch toggleMap;
    private Switch toggleSolution;  // toggle switch to show solution in overall maze on screen
    private Button zoomIn;
    private Button zoomOut;
    private Button pause;
    private SeekBar speed;

    Maze maze;
    Intent intent;
    private UnreliableRobot robot;
    private String robotStr;
    private RobotDriver driver;
    private String driverStr;

    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    private boolean started=false;

    DistanceSensor leftSensor;
    DistanceSensor rightSensor;
    DistanceSensor forwardSensor;
    DistanceSensor backwardSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        // initiate values for variables

        maze = MazeHolder.getInstance().getMaze();
        mazePanel=(MazePanel) findViewById(R.id.mazePanelAnimation);

        myStatePlaying= new StatePlaying();
        myStatePlaying.setMaze(maze);
        myStatePlaying.playAnimation=this;

        zoomIn =(Button)findViewById(R.id.btnZoomInAnimation);
        zoomOut=(Button)findViewById(R.id.btnZoomOutAnimation);
        pause=(Button)findViewById(R.id.btnPause);

        toggleWalls=(Switch) findViewById(R.id.switchWallsAnimation); //the 2d walls ontop view
        toggleMap=(Switch) findViewById(R.id.switchMapAnimation);
        toggleSolution=(Switch) findViewById(R.id.switchSolutionAnimation);



        intent=getIntent();
        robot= new UnreliableRobot();
        robot.control = myStatePlaying;
        robotStr=intent.getStringExtra("robot");
        robot.setConfig(robotStr);
        robot.setRobotMaze(myStatePlaying);
        setSensors(myStatePlaying);
        robot.addDistanceSensor(leftSensor, Robot.Direction.LEFT);
        robot.addDistanceSensor(rightSensor, Robot.Direction.RIGHT);
        robot.addDistanceSensor(forwardSensor, Robot.Direction.FORWARD);
        robot.addDistanceSensor(backwardSensor, Robot.Direction.BACKWARD);


        robot.setBatteryLevel(3500);

        driverStr=intent.getStringExtra("driver");
        setDriver(driverStr);
        driver.setMaze(maze);
        driver.setRobot(robot);


        win=(Button) findViewById(R.id.btnWin);
        lose=(Button) findViewById(R.id.btnLose);

       robot.control.setPlayAnimationActivity(this);
       robot.control.start(mazePanel);

        mHandler=new Handler();
        startRepeatingTask();








//set listners etc.

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pause.getText().equals("Pause")){
                    pause.setText("Unpause");
                    stopRepeatingTask();
                }
                else{
                    pause.setText("Pause");
                    startRepeatingTask();
                }
            }
        });
        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(playingState,"Zooming In Map");
                myStatePlaying.handleUserInput(Constants.UserInput.ZOOMIN,0);
                mazePanel.commit();
            }
        });

        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(playingState,"Zooming Out Map");
                myStatePlaying.handleUserInput(Constants.UserInput.ZOOMOUT,0);
                mazePanel.commit();
            }
        });
        toggleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(playingState,"Toggling Map");
                myStatePlaying.handleUserInput(Constants.UserInput.TOGGLEFULLMAP,0);
                mazePanel.commit();

            }
        });
        toggleWalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(playingState,"Toggle Walls");
                myStatePlaying.handleUserInput(Constants.UserInput.TOGGLELOCALMAP,0);
                mazePanel.commit();

            }
        });
        toggleSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(playingState,"Toggling Solution");
                myStatePlaying.handleUserInput(Constants.UserInput.TOGGLESOLUTION,0);
                mazePanel.commit();
            }
        });

        win.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayAnimationActivity.this, WinningActivity.class);
                startActivity(intent);
            }
        });
        lose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayAnimationActivity.this, LosingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }


    private void setDriver(String driver){
        if (driver.equals("Wizard")){
            this.driver= new Wizard();
        }
        else if (driver.equals("WallFollower")){
            this.driver = new WallFollower();

        }
    }

    Runnable automaticPlayer = new Runnable(){

        @Override
        public void run() {
            try {
                boolean atExit=driver.drive1Step2Exit();
                mazePanel.commit();

                if(robot.isAtExit()){
                    if (robot.canSeeThroughTheExitIntoEternity(Robot.Direction.LEFT)==true) {
                        robot.rotate(Robot.Turn.LEFT);
                    }
                    else if (robot.canSeeThroughTheExitIntoEternity(Robot.Direction.RIGHT)==true) {
                        robot.rotate(Robot.Turn.RIGHT);
                    }
                    else if (robot.canSeeThroughTheExitIntoEternity(Robot.Direction.BACKWARD)==true) {
                        robot.rotate(Robot.Turn.AROUND);
                    }
                    robot.move(1);
                }
            } catch (Exception e) {
                playingToLosing();
            }
//            Toast.makeText(PlayAnimationActivity.this, "testing repeated activity",Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(this, 500);
        }
    };

    public void playingToWinning(){
        Intent intent = new Intent(PlayAnimationActivity.this, WinningActivity.class);
        intent.putExtra("path length", driver.getPathLength());
        startActivity(intent);
    }
    public void playingToLosing(){
        Intent intent = new Intent(PlayAnimationActivity.this, LosingActivity.class);
        intent.putExtra("path length", driver.getPathLength());
        stopRepeatingTask();
        startActivity(intent);
    }

    void startRepeatingTask() {
        started=true;
        automaticPlayer.run();
//       mHandler.postDelayed(automaticPlayer, 3000);
    }

    void stopRepeatingTask() {
        started=false;
        mHandler.removeCallbacks(automaticPlayer);
    }


    public void setSensors(StatePlaying control){
        if(robotStr.equals("Premium")){
            leftSensor= new ReliableSensor();
            rightSensor= new ReliableSensor();
            forwardSensor= new ReliableSensor();
            backwardSensor= new ReliableSensor();

        }
        else if (robotStr.equals("Mediocre")){
            leftSensor= new UnreliableSensor();
            rightSensor= new UnreliableSensor();
            forwardSensor= new ReliableSensor();
            backwardSensor= new ReliableSensor();

        }
        else if (robotStr.equals("So-So")){
            leftSensor= new ReliableSensor();
            rightSensor= new ReliableSensor();
            forwardSensor= new UnreliableSensor();
            backwardSensor= new UnreliableSensor();

        }
        else if (robotStr.equals("Shaky")){
            leftSensor= new UnreliableSensor();
            rightSensor= new UnreliableSensor();
            forwardSensor= new UnreliableSensor();
            backwardSensor= new UnreliableSensor();

        }
        backwardSensor.setMaze(control.getMaze());
        forwardSensor.setMaze(control.getMaze());
        leftSensor.setMaze(control.getMaze());
        rightSensor.setMaze(control.getMaze());

        backwardSensor.setSensorDirection(Robot.Direction.BACKWARD);
        forwardSensor.setSensorDirection(Robot.Direction.FORWARD);
        leftSensor.setSensorDirection(Robot.Direction.LEFT);
        rightSensor.setSensorDirection(Robot.Direction.RIGHT);

    }
}