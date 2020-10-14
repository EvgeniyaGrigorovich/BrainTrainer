package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        textView = findViewById(R.id.textViewScore);
        Intent intent = getIntent();
        if (intent!= null && intent.hasExtra("result")){
            int res = intent.getIntExtra("result", 0);
            int numberOfCorrectAnswersInARowResult  = intent.getIntExtra("numberOfCorrectAnswersInARowResult", 0);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            int max = sharedPreferences.getInt("max", 0);
            String result = String.format("Ваш результат: %s \nВаш рекорд: %s\n Количество верных ответов подряд: %s" , res, max,numberOfCorrectAnswersInARowResult );
            textView.setText(result);


        }
    }

    public void onClickStartNewGame(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}