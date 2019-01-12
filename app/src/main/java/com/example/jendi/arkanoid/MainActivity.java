package com.example.jendi.arkanoid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    int level = 0;
    ConstraintLayout layout;
    Button easy, medium, hard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.layout);

        easy = findViewById(R.id.button);
        medium = findViewById(R.id.button2);
        hard = findViewById(R.id.button3);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easy.setVisibility(View.INVISIBLE);
                medium.setVisibility(View.INVISIBLE);
                hard.setVisibility(View.INVISIBLE);
                level = 1;
                Intent intentStartGame = new Intent(getApplicationContext(), GameActivity.class);
                intentStartGame.putExtra("level", level);
                startActivityForResult(intentStartGame, 100);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easy.setVisibility(View.INVISIBLE);
                medium.setVisibility(View.INVISIBLE);
                hard.setVisibility(View.INVISIBLE);
                level = 2;
                Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
                startGame.putExtra("level", level);
                startActivityForResult(startGame, 100);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easy.setVisibility(View.INVISIBLE);
                medium.setVisibility(View.INVISIBLE);
                hard.setVisibility(View.INVISIBLE);
                level = 3;
                Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
                startGame.putExtra("level", level);
                startActivityForResult(startGame, 100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easy.setVisibility(View.VISIBLE);
        medium.setVisibility(View.VISIBLE);
        hard.setVisibility(View.VISIBLE);
    }
}