package com.example.amazebyannemariezheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WinningActivity extends AppCompatActivity {
    private Button playAgain;
    private TextView pathLength;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);

        playAgain = (Button) findViewById(R.id.btnNewGame);
        pathLength = (TextView) findViewById(R.id.tvPathLength);
        Intent intent = getIntent();

        pathLength.setText("Path Length :"+intent.getIntExtra("path length",0));

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