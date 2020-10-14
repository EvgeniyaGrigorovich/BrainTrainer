package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView textViewTimer;
    private TextView textViewQuestion;
    private TextView textViewScore;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;

    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private int min = 5;
    private int max = 30;
    private int countOfQuestions = 0;
    private int countOfWriteAnswers = 0;
    private boolean gameOver = false;
    private int numberOfCorrectAnswersInARow = 0;
    private long globalCount;
    private long startTime = 15000;
    private int numberOfCorrectAnswersInARowResult = 0;

    private static final Random RANDOM = new Random();

    private ArrayList<Button> options;

    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTimer = findViewById(R.id.textViewCountDownTimer);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewScore = findViewById(R.id.textViewScore);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        options = new ArrayList<>();
        options.add(button0);
        options.add(button1);
        options.add(button2);
        options.add(button3);

        playNext();
        timer();


    }

    private void generateQuestion() {
        int a = RANDOM.nextInt((max - min + 1) + min);
        int b = RANDOM.nextInt((max - min + 1) + min);
        int mark = RANDOM.nextInt(2);
        isPositive = mark == 1;
        if (isPositive) {
            rightAnswer = a + b;
            question = String.format("%s + %s", a, b);
        } else {
            rightAnswer = a - b;
            question = String.format("%s - %s", a, b);
        }
        textViewQuestion.setText(question);
        rightAnswerPosition = (int) RANDOM.nextInt(4);

    }

    private int generateWrongAnswer() {
        int result;
        do {
            result = (int) RANDOM.nextInt(max * 2 + 1) - (max - min);
        } while (result == rightAnswer);
        return result;


    }

    private void playNext() {
        generateQuestion();
        for (int i = 0; i < options.size(); i++) {
            if (i == rightAnswerPosition) {
                options.get(i).setText(Integer.toString(rightAnswer));
            } else {
                options.get(i).setText(Integer.toString(generateWrongAnswer()));
            }
        }
        textViewScore.setText(String.format("%s/%s", countOfWriteAnswers, countOfQuestions));
    }

    public void onClickAnswer(View view) {
        if (!gameOver) {
            Button button = (Button) view;
            String answer = String.valueOf(button.getText());
            int chooseAnswer = Integer.parseInt(answer);
            if (chooseAnswer == rightAnswer) {
                Toast.makeText(this, "Верно!", Toast.LENGTH_SHORT).show();
                countOfWriteAnswers++;
                numberOfCorrectAnswersInARow++;
                numberOfCorrectAnswersInARowResult++;
            } else {
                Toast.makeText(this, "Ошибка!", Toast.LENGTH_SHORT).show();
                numberOfCorrectAnswersInARow = 0;
            }
            countOfQuestions++;
            if (numberOfCorrectAnswersInARow == 5) {
                timer.cancel();
                globalCount += 3000;
                startTime = globalCount;
                if (numberOfCorrectAnswersInARowResult < numberOfCorrectAnswersInARow) {
                    numberOfCorrectAnswersInARowResult = numberOfCorrectAnswersInARow;
                }
                numberOfCorrectAnswersInARow = 0;
                timer();
            }
            playNext();

        }
    }


    public static String getTime(long ms) {
        int seconds = (int) ms / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    private void timer() {

        timer = new CountDownTimer(startTime, 100) {


            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(getTime(millisUntilFinished));
                globalCount = millisUntilFinished;

                if (millisUntilFinished < 5000) {
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }

            }

            @Override
            public void onFinish() {
                gameOver = true;
                Intent i = new Intent(MainActivity.this, ScoreActivity.class);
                i.putExtra("result", countOfWriteAnswers);
                i.putExtra("numberOfCorrectAnswersInARowResult", numberOfCorrectAnswersInARowResult);
                startActivity(i);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = preferences.getInt("max", 0);
                if (countOfWriteAnswers >= max) {
                    preferences.edit().putInt("max", countOfWriteAnswers).apply();
                }
            }
        };
        timer.start();
    }


}