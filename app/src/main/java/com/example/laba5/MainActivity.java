package com.example.laba5;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import java.util.Locale;
import android.text.InputFilter;
import android.text.Spanned;
public class MainActivity extends AppCompatActivity {

    private TextView timerTextView;
    private Button toggleButton;
    private Button resetButton;
    private EditText inputMinutes;
    private EditText inputSeconds;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean timerRunning;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        toggleButton = findViewById(R.id.toggleButton);
        resetButton = findViewById(R.id.resetButton);
        inputMinutes = findViewById(R.id.inputMinutes);
        inputSeconds = findViewById(R.id.inputSeconds);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    private void startTimer() {
        String minutesText = inputMinutes.getText().toString();
        String secondsText = inputSeconds.getText().toString();
        inputMinutes.setFilters(new InputFilter[]{filter});
        inputSeconds.setFilters(new InputFilter[]{filter});
        if (minutesText.isEmpty() || secondsText.isEmpty()) {
            return;
        }

        int minutes = Integer.parseInt(minutesText);
        int seconds = Integer.parseInt(secondsText);

        timeLeftInMillis = (minutes * 60 + seconds) * 1000;

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                Animation scaleUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
                timerTextView.startAnimation(scaleUp);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                toggleButton.setText("Start");
                timeLeftInMillis = 0;
                updateCountDownText();
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alarm);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
        }.start();

        timerRunning = true;
        toggleButton.setText("Pause");

    }

    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        toggleButton.setText("Start");
    }

    private void resetTimer() {
        timeLeftInMillis = 0;
        updateCountDownText();
        if (timerRunning) {
            stopTimer();
            startTimer();
        }
    }
    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isLetterOrDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null; // Оставляем ввод без изменений
        }
    };

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }

}