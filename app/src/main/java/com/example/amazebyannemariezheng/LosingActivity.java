package com.example.amazebyannemariezheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LosingActivity extends AppCompatActivity {
    private Button playAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);

        playAgain=(Button) findViewById(R.id.btnNewGameLosing);

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
        });
    }
    public void openActivity(){
        Intent intent = new Intent(this,AMazeActivity.class);
        startActivity(intent);

    };
    @Override
    public void onBackPressed(){
        openActivity();
    }
}