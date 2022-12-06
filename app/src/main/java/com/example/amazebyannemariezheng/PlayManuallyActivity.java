package com.example.amazebyannemariezheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PlayManuallyActivity extends AppCompatActivity {
    private Button shortCut;
    private Button jump;
    private ImageButton forwardMove;
    private ImageButton leftTurn;
    private ImageButton rightTurn;
    private MazePanel mazePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        //initialize private variables to their view on xml
        shortCut=(Button)findViewById(R.id.btnShortcut);
        jump=(Button)findViewById(R.id.btnJump);
        forwardMove=(ImageButton)findViewById(R.id.imgbtnForward);
        leftTurn=(ImageButton)findViewById(R.id.imgbtnLeft);
        rightTurn=(ImageButton)findViewById(R.id.imgbtnRight);
        mazePanel=(MazePanel)findViewById(R.id.mazePanelManual);


    //setting onClick Listeners for each View
        shortCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayManuallyActivity.this, WinningActivity.class);
                startActivity(intent);
            }
        });

    }

    //private helper functions to draw and update Manual MazePanel View
    private void updatePanel(){

    }
}