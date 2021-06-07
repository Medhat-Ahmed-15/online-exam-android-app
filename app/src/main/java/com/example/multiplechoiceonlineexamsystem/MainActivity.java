package com.example.multiplechoiceonlineexamsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=10000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();

                //Call this when your activity is done and should be closed.
                // The ActivityResult is propagated back to whoever launched you via onActivityResult().
                finish();

            }
        },SPLASH_SCREEN);


    }


    public void nextActivity(){
        Intent intent=new Intent(this, HomePage.class);
        startActivity(intent);
    }


}




