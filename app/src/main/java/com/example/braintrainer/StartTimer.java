package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StartTimer extends AppCompatActivity {
    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;
    private TextView textViewTimerStartTimerActivity;
    private final long timerCount = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_timer);

        progressBar = findViewById(R.id.progressBar);
        textViewTimerStartTimerActivity = findViewById(R.id.textViewCountDownTimer);

        countDownTimer = new CountDownTimer(timerCount, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimerStartTimerActivity.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                textViewTimerStartTimerActivity.setText("ПОГНАЛИ!");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        };
        countDownTimer.start();
    }
}