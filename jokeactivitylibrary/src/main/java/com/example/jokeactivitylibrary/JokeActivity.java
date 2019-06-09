package com.example.jokeactivitylibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    public static final String EXTRA_JOKE = "extra_joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        TextView jokeTextView = findViewById(R.id.tv_joke);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_JOKE)){
            jokeTextView.setText(intent.getStringExtra(EXTRA_JOKE));
        }

    }
}
