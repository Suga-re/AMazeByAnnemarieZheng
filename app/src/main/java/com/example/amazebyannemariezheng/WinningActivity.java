package com.example.amazebyannemariezheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WinningActivity extends AppCompatActivity {
    private Button playAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);

        playAgain = (Button) findViewById(R.id.btnNewGame);

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