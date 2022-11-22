package com.example.amazebyannemariezheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayAnimationActivity extends AppCompatActivity {
    Button win;
    Button lose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        win=(Button) findViewById(R.id.btnWin);
        lose=(Button) findViewById(R.id.btnLose);

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
}