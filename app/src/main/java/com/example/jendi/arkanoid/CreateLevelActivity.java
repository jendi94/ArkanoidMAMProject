package com.example.jendi.arkanoid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateLevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_create_level);
        TextView textView = findViewById(R.id.textView4);
        String info = "Welcome!\nHere you can create your own custom Arkanoid level!\n" +
                "To do so, please type in block numbers\nseparated by comma in the below field.\n" +
                "0 - empty space(required for block aligning)\n" +
                "1 - single block\n" +
                "2 - double block\n" +
                "3 - prize block\n";
        textView.setText(info);

        Button play = findViewById(R.id.button5);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.editText);
                if (editText != null) {
                    Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                    intent.putExtra("array", editText.getText().toString());
                    intent.putExtra("custom", true);
                    startActivity(intent);
                }
            }
        });
    }
}
