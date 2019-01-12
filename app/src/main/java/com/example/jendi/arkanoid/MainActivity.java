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
    Button easy, medium, hard, createLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.layout);

        easy = findViewById(R.id.button);
        medium = findViewById(R.id.button2);
        hard = findViewById(R.id.button3);
        createLevel = findViewById(R.id.button4);
        final boolean isCustomLevel = false;

        createLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateLevelActivity.class);
                startActivity(intent);
            }
        });

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level = 1;
                Intent intentStartGame = new Intent(getApplicationContext(), GameActivity.class);
                intentStartGame.putExtra("level", level);
                intentStartGame.putExtra("custom", isCustomLevel);
                startActivityForResult(intentStartGame, 100);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level = 2;
                Intent intentStartGame = new Intent(getApplicationContext(), GameActivity.class);
                intentStartGame.putExtra("level", level);
                intentStartGame.putExtra("custom", isCustomLevel);
                startActivityForResult(intentStartGame, 100);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level = 3;
                Intent intentStartGame = new Intent(getApplicationContext(), GameActivity.class);
                intentStartGame.putExtra("level", level);
                intentStartGame.putExtra("custom", isCustomLevel);
                startActivityForResult(intentStartGame, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}