package com.example.foodapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button signUp,signIn;
    TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signUp = findViewById(R.id.signup);
        signIn = findViewById(R.id.signin);
        slogan = findViewById(R.id.slogan);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"font/NABILA.TTF");
        slogan.setTypeface(typeface);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = new Intent(MainActivity.this,signin.class);
                startActivity(signIn);

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signUp = new Intent(MainActivity.this,signUp.class);
                startActivity(signUp);
            }
        });

    }
}
