package com.example.amazebyannemariezheng;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import generation.DefaultOrder;
import generation.Factory;
import generation.Floorplan;
import generation.Maze;
import generation.MazeBuilder;
import generation.MazeFactory;
import generation.Order;
import generation.SingleRandom;

public class GeneratingActivity extends AppCompatActivity implements Order {
    private ProgressBar mazeProgress;
    private Spinner robotConfig;
    private TextView tvMazePercentage;
    private RadioGroup driverGroup;
    private RadioButton manualButton;
    private RadioButton buttonSelected;
    int progress=0;
    Handler handler= new Handler();

    private String filename;
    String driverSelected;

    protected Factory factory;
    private boolean started=false;
    private static int skilllevel;
    private static Builder builder;
    private String checkAlgorithm;
    private static boolean perfectMaze;
    private static int seed;
    SingleRandom random;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

        //initialize variable values
        robotConfig=(Spinner)findViewById(R.id.spinnerRobotConfig);
        tvMazePercentage=(TextView) findViewById(R.id.tvMazeProgress);
        mazeProgress=(ProgressBar) findViewById(R.id.progressBarMaze);
        driverGroup=(RadioGroup) findViewById(R.id.rgDriver);
        manualButton=(RadioButton) findViewById(R.id.rbManual);
        buttonSelected=null;

        //initialize maze generation parameter values
        Intent intent = getIntent();
        skilllevel = intent.getIntExtra("skill level",0);
        perfectMaze =!intent.getBooleanExtra("rooms",true);
        checkAlgorithm= intent.getStringExtra("generation algorithm");


        if (checkAlgorithm.equals("DFS")){

            builder = Builder.DFS;
        }
        else if (checkAlgorithm.equals("Prim")){
            builder= Builder.Prim;
        }
        else if (checkAlgorithm.equals("Boruvka")){
            builder= Builder.Boruvka;
        }
        random = new SingleRandom();
        seed = random.nextInt();

        buildMaze();
//        factory.waitTillDelivered();





        driverGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedDriver) {
                buttonSelected=(RadioButton)findViewById(checkedDriver);

                if (progress!=100){
                    AlertDialog.Builder alertDialog2= new AlertDialog.Builder(GeneratingActivity.this);
                    alertDialog2.setMessage("Game Will Start Shortly");
                    alertDialog2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alertDialog2.show();
                }
                else{
                   switchToPlaying(buttonSelected); //navigate based on button selected
                }
            }
        });
        //robot values set and given to spinner to display
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.robotConfig,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        robotConfig.setAdapter(adapter);

        robotConfig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                String text = parent.getItemAtPosition(pos).toString();
                Toast.makeText(parent.getContext(),text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //thread that generates maze in background and updates UI to show progress

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                startProgress();
//            }
//        });
//        thread.start();
    }




    //Functions
    private void buildMaze(){
        factory = new MazeFactory();
        factory.order(this);
    }


    private void switchToPlaying(RadioButton buttonSelected){
        Intent intent;
        if (buttonSelected==manualButton){
            intent = new Intent(this, PlayManuallyActivity.class);
        }
        else{
            intent = new Intent(this, PlayAnimationActivity.class);

        }
        startActivity(intent);
        //check if driver instance value is some value
        //check if maze is 100 or more
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, AMazeActivity.class);
        startActivity(intent);
    }



    @Override
    public int getSkillLevel() {
        return skilllevel;
    }

    @Override
    public Builder getBuilder() {
        return builder;
    }

    @Override
    public boolean isPerfect() {
        return perfectMaze;
    }

    @Override
    public int getSeed() {
        return seed;
    }

    @Override
    public void deliver(Maze mazeConfig) {
        MazeHolder.getInstance().setMaze(mazeConfig);
    }

    @Override
    public void updateProgress(int percentage) {
        progress=percentage;
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (progress==100){
                    mazeProgress.setProgress(progress);
                    tvMazePercentage.setText("Maze Progress\n" + String.valueOf(progress) + "%");

                    if(buttonSelected==null){
                        AlertDialog.Builder alertDialog= new AlertDialog.Builder(GeneratingActivity.this);
                        alertDialog.setMessage("Select Driver");
                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialog.show();
                    }
                    else{
                        switchToPlaying(buttonSelected);
                    }

                }
                else{
                    mazeProgress.setProgress(progress);
                    tvMazePercentage.setText("Maze Progress\n" + String.valueOf(progress) + "%");
                }
            }
        });
    }



}