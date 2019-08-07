package com.mlkt.development.drawingpanel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawingPanel dp = findViewById(R.id.dp);
        findViewById(R.id.undo).setOnClickListener((v) -> dp.undo());
    }

}
