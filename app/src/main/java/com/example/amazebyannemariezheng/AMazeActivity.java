package com.example.amazebyannemariezheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import generation.SingleRandom;


public class AMazeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private SeekBar skillLevel;
    private int Level=0;
    private TextView tvSkill;
    private ToggleButton toggleRooms;
    private boolean hasRooms=true;
    private Spinner mazeGeneration;
    private String generationAlgorithm="DFS";
    private static int seed;
    SingleRandom random;
    private Button revisit;
    private Button explore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);

        //set variable values
        skillLevel=(SeekBar)findViewById(R.id.seekBarSkillLevel);
        tvSkill=(TextView)findViewById(R.id.tvSkillLevel);
        toggleRooms=(ToggleButton)findViewById(R.id.toggleRooms);
        mazeGeneration=(Spinner)findViewById(R.id.spinnerMazeGeneration);
        toggleRooms.setText("Y");
        revisit=(Button) findViewById(R.id.btnRevisit);
        explore=(Button) findViewById(R.id.btnExplore);
        random = new SingleRandom();
        seed = random.nextInt();

        //updates skill level text based on the seekbar progress
        skillLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                tvSkill.setText("Tomb Level:"+progress);
                Level = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        toggleRooms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                hasRooms=b;
            }
        });
        //redirect buttons clicked to move to generating activity

        revisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revisit();
            }
        });
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                explore();
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
    public void explore() {
        Intent intent = new Intent(this, GeneratingActivity.class);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("generation", generationAlgorithm);
        editor.putInt("skill level", Level);
        editor.putBoolean("rooms", hasRooms);
        editor.putInt("seed", seed);
        editor.apply();

        intent.putExtra("generation algorithm", generationAlgorithm);
        intent.putExtra("skill level", Level);
        intent.putExtra("rooms", hasRooms);
        intent.putExtra("seed", seed);
        startActivity(intent);
    };

    public void revisit(){
        Intent intent = new Intent(this, GeneratingActivity.class);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        generationAlgorithm = preferences.getString("generation", generationAlgorithm);
        Level = preferences.getInt("skill level", Level);
        hasRooms = preferences.getBoolean("rooms", hasRooms);
        seed= preferences.getInt("seed", seed);

        intent.putExtra("generation algorithm", generationAlgorithm);
        intent.putExtra("skill level", Level);
        intent.putExtra("rooms", hasRooms);
        intent.putExtra("seed", seed);
        startActivity(intent);
    }

    //creates a toast popup based on item selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        String text = parent.getItemAtPosition(pos).toString();
        generationAlgorithm = text;
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