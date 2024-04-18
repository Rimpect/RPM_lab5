package com.example.laba5;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ToggleButton toggleButton;
    private EditText editTextMinutes;
    private EditText editTextSeconds;
    private ImageView imageView;
    private CountDownTimer countDownTimer;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton = findViewById(R.id.toggleButton);
        editTextMinutes = findViewById(R.id.editTextMinutes);
        editTextSeconds = findViewById(R.id.editTextSeconds);
        imageView = findViewById(R.id.imageView);

        // Инициализируем MediaPlayer с звуком, который будет воспроизводиться по окончании отсчета
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButton.isChecked()) {
                    startTimer();
                } else {
                    stopTimer();
                }
            }
        });
    }

    private void startTimer() {
        int minutes = Integer.parseInt(editTextMinutes.getText().toString());
        int seconds = Integer.parseInt(editTextSeconds.getText().toString());
        long totalTime = (minutes * 60 + seconds) * 1000; // В миллисекундах

        countDownTimer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                long minutes = secondsRemaining / 60;
                secondsRemaining %= 60;

                // Обновляем текст в EditText
                editTextMinutes.setText(String.valueOf(minutes));
                editTextSeconds.setText(String.valueOf(secondsRemaining));

                // Запускаем анимацию
                imageView.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.circle));
            }

            @Override
            public void onFinish() {
                // Останавливаем таймер
                toggleButton.setChecked(false);

                // Воспроизводим звук
                mediaPlayer.start();
            }
        };

        countDownTimer.start();
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}