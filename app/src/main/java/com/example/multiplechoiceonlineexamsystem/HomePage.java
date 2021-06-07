package com.example.multiplechoiceonlineexamsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HomePage extends AppCompatActivity {

    ImageView start_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        start_button = (ImageView) findViewById(R.id.start_button);




        start_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                OpenQuizActivity();

            }
        });



    }

    public void OpenQuizActivity(){
        Intent intent=new Intent(this, QuizActivity.class);
        startActivity(intent);
    }

}