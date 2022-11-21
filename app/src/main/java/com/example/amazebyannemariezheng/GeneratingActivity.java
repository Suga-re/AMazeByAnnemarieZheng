package com.example.amazebyannemariezheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class GeneratingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ProgressBar mazeProgress;

    private Spinner robotConfig;
    private TextView tvMazePercentage;
    int counter=0;
    Handler handler= new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);
        robotConfig=(Spinner)findViewById(R.id.spinnerRobotConfig);
        tvMazePercentage=(TextView) findViewById(R.id.tvMazeProgress);
        mazeProgress=(ProgressBar) findViewById(R.id.progressBarMaze);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.mazeGeneration,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        robotConfig.setAdapter(adapter);
        robotConfig.setOnItemSelectedListener(this);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startProgress();
            }
        });
        thread.start();
    }
    public void startProgress(){
        for(counter=0; counter<100;counter++) {
            try {
                Thread.sleep(50);
                mazeProgress.setProgress(counter);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tvMazePercentage.setText("Maze Progress\n"+String.valueOf(counter)+"%");
                }
            });


        }
    }
//    public void prog()
//    {
//
//        mazeProgress=(ProgressBar)findViewById(R.id.progressBarMaze);
//        Timer t= new Timer();
//        TimerTask tt= new TimerTask() {
//            @Override
//            public void run() {
//                counter++;
//                mazeProgress.setProgress(counter);
//
//                if(counter==100){
//                    t.cancel();
//                }
//            }
//        };
//        t.schedule(tt,0,100);
//    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, AMazeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        String text = parent.getItemAtPosition(pos).toString();
        Toast.makeText(parent.getContext(),text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}