package com.example.jendi.arkanoid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game);
        layout = findViewById(R.id.layout);
        Intent intent = getIntent();
        int level = intent.getIntExtra("level", 0);
        GameView gameView = new GameView(GameActivity.this, level);
        layout.addView(gameView);
    }
}
