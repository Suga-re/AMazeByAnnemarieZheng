package com.example.amazebyannemariezheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class AMazeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private SeekBar skillLevel;
    private TextView tvSkill;
    private ToggleButton toggleRooms;
    private Spinner mazeGeneration;
    private Button start;
    private Button revisit;
    private Button explore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);





        Log.v("Play","Message here");
        //set variable values
        skillLevel=(SeekBar)findViewById(R.id.seekBarSkillLevel);
        tvSkill=(TextView)findViewById(R.id.tvSkillLevel);
        toggleRooms=(ToggleButton)findViewById(R.id.toggleRooms);
        mazeGeneration=(Spinner)findViewById(R.id.spinnerMazeGeneration);
        toggleRooms.setText("Y");
        start=(Button) findViewById(R.id.btnStart);
        revisit=(Button) findViewById(R.id.btnRevisit);
        explore=(Button) findViewById(R.id.btnExplore);

        //updates skill level text based on the seekbar progress
        skillLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                tvSkill.setText("Tomb Level:"+progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //redirect buttons clicked to move to generating activity
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
        });

        revisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
        });
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
        });

        //maze generation values set and given to spinner to display
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.mazeGeneration,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mazeGeneration.setAdapter(adapter);
        mazeGeneration.setOnItemSelectedListener(this);

    }
    // extra (overridden) functions

    //opens GeneratingActivity page
    public void openActivity() {
        Intent intent = new Intent(this, GeneratingActivity.class);
        startActivity(intent);
    };

    //creates a toast popup based on item selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        String text = parent.getItemAtPosition(pos).toString();
        Toast.makeText(parent.getContext(),text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    @Override
    public void onBackPressed(){
//        super.onBackPressed();
    }

}