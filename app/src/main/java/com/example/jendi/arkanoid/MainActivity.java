package com.example.jendi.arkanoid;

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
                level = 1;
                GameView gameView = new GameView(MainActivity.this, level);
                layout.addView(gameView);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        easy.setVisibility(View.INVISIBLE);
                        medium.setVisibility(View.INVISIBLE);
                        hard.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level = 2;
                GameView gameView = new GameView(MainActivity.this, level);
                layout.addView(gameView);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        easy.setVisibility(View.INVISIBLE);
                        medium.setVisibility(View.INVISIBLE);
                        hard.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level = 3;
                GameView gameView = new GameView(MainActivity.this, level);
                layout.addView(gameView);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        easy.setVisibility(View.INVISIBLE);
                        medium.setVisibility(View.INVISIBLE);
                        hard.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }
}