package com.example.amazebyannemariezheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import generation.Distance;
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
    private ProgressBar energy;
    private TextView energyText;

    private RadioButton front;
    private RadioButton back;
    private RadioButton left;
    private RadioButton right;

    //for RObot and drivers
    Maze maze;
    Intent intent;
    private UnreliableRobot robot;
    private String robotStr;
    private RobotDriver driver;
    private String driverStr;
    DistanceSensor leftSensor;
    DistanceSensor rightSensor;
    DistanceSensor forwardSensor;
    DistanceSensor backwardSensor;

    //for repeating task
    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    private boolean started=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        // initiate values for variables

        //set maze
        maze = MazeHolder.getInstance().getMaze();
        mazePanel=(MazePanel) findViewById(R.id.mazePanelAnimation);


        myStatePlaying= new StatePlaying();
        myStatePlaying.setMaze(maze);
        myStatePlaying.playAnimation=this;

        //set buttons and toggles
        zoomIn =(Button)findViewById(R.id.btnZoomInAnimation);
        zoomOut=(Button)findViewById(R.id.btnZoomOutAnimation);
        pause=(Button)findViewById(R.id.btnPause);
        win=(Button) findViewById(R.id.btnWin);
        lose=(Button) findViewById(R.id.btnLose);

        right=(RadioButton) findViewById(R.id.rbRightSensor);
        left=(RadioButton) findViewById(R.id.rbLeftSensor);
        front=(RadioButton) findViewById(R.id.rbFrontSensor);
        back=(RadioButton) findViewById(R.id.rbBackSensor);


        toggleWalls=(Switch) findViewById(R.id.switchWallsAnimation); //the 2d walls ontop view
        toggleMap=(Switch) findViewById(R.id.switchMapAnimation);
        toggleSolution=(Switch) findViewById(R.id.switchSolutionAnimation);

        speed=(SeekBar) findViewById(R.id.seekBarSpeed);
        energy=(ProgressBar) findViewById(R.id.progressBarEnergyLeft);
        energyText=(TextView) findViewById(R.id.tvEnergyLeftAnimation);


//set robot
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

        checkSensorStatuses(robot);

        robot.setBatteryLevel(100);

        driverStr=intent.getStringExtra("driver");
        setDriver(driverStr);
        driver.setMaze(maze);
        driver.setRobot(robot);




       robot.control.setPlayAnimationActivity(this);
       robot.control.start(mazePanel);

        mHandler=new Handler();
        startRepeatingTask();








//set listners etc.

        speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mInterval=1000/(i+1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
                checkSensorStatuses(robot);
                boolean atExit=driver.drive1Step2Exit();
                mazePanel.commit();


                energy.setProgress((int) robot.getBatteryLevel());
                energyText.setText("Energy:"+ Integer.toString((int) robot.getBatteryLevel()));

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
                mHandler.postDelayed(this, mInterval);
            } catch (Exception e) {
                playingToLosing();

            }
//            Toast.makeText(PlayAnimationActivity.this, "testing repeated activity",Toast.LENGTH_SHORT).show();

        }
    };

    public void playingToWinning(){
        Intent intent = new Intent(PlayAnimationActivity.this, WinningActivity.class);
        intent.putExtra("path length", driver.getPathLength());
        startActivity(intent);
    }
    public void playingToLosing(){
        stopRepeatingTask();
        Intent intent = new Intent(PlayAnimationActivity.this, LosingActivity.class);
        intent.putExtra("path length", driver.getPathLength());
        startActivity(intent);
    }

    void startRepeatingTask() {
        automaticPlayer.run();
//       mHandler.postDelayed(automaticPlayer, 3000);
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(automaticPlayer);
    }

    private void check1SensorStatus(DistanceSensor sensor, RadioButton button){
        if (sensor instanceof UnreliableSensor){
            button.setChecked(((UnreliableSensor)sensor).isSensorOperational());
        }
        else{
            button.setChecked(true);
        }
    }
    private void checkSensorStatuses(UnreliableRobot robot){
        check1SensorStatus(robot.getSensor(Robot.Direction.FORWARD), front);
        check1SensorStatus(robot.getSensor(Robot.Direction.BACKWARD), back);
        check1SensorStatus(robot.getSensor(Robot.Direction.LEFT), left);
        check1SensorStatus(robot.getSensor(Robot.Direction.RIGHT), right);

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
//        startRepairProcessSensor(backwardSensor);
        forwardSensor.setSensorDirection(Robot.Direction.FORWARD);
//        startRepairProcessSensor(forwardSensor);
        leftSensor.setSensorDirection(Robot.Direction.LEFT);
//        startRepairProcessSensor(leftSensor);
        rightSensor.setSensorDirection(Robot.Direction.RIGHT);
//        startRepairProcessSensor(rightSensor);

    }
    private void startRepairProcessSensor(DistanceSensor sensor){
        sensor.startFailureAndRepairProcess(0,0);
        try {
            Thread.sleep(1300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}