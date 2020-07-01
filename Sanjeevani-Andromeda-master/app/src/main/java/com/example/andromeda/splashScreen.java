package com.example.andromeda;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splashScreen extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences settings = getSharedPreferences("prefs",0);
        boolean firstRun = settings.getBoolean("firstRun",false);

        if(!firstRun) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstRun", true);
            editor.apply();
            start();
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            },SPLASH_SCREEN_TIME);
        }

    }
    private void start(){
        Intent i = new Intent(splashScreen.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
