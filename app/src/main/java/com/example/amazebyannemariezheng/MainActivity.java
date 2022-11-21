package com.example.amazebyannemariezheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView greeting;
    private Button click;
    private Button ToGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        greeting =(TextView)findViewById(R.id.tvGreeting);
        click =(Button) findViewById (R.id.btnClick);
        ToGame=(Button) findViewById(R.id.btnToGame);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                greeting.setVisibility(View.VISIBLE);
            }
        });
        ToGame.setOnClickListener(new View.OnClickListener() {
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
}