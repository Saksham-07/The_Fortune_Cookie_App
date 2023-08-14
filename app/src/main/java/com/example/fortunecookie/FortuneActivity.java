package com.example.fortunecookie;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class FortuneActivity extends AppCompatActivity {
    private ImageView fortuneCookieImageView, crackedCookieImageView, brokenCookieImageView;
    private TextView fortuneText;
    private Vibrator vibrator;
    private TextView click;
    private TextView fortune;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fortune);

        fortuneCookieImageView = findViewById(R.id.fortuneCookieImageView);
        crackedCookieImageView = findViewById(R.id.crackedCookieImageView);
        brokenCookieImageView = findViewById(R.id.brokenCookieImageView);
        fortuneText = findViewById(R.id.fortuneText);
        click = findViewById(R.id.click);
        fortune = findViewById(R.id.fortune);

        Resources res = getResources();
        String[] fortuneTexts = res.getStringArray(R.array.fortune_text);

        // Convert array to Set to remove duplicates
        Set<String> uniqueSet = new HashSet<>(Arrays.asList(fortuneTexts));

        // Convert Set back to array
        String[] uniqueFortune = uniqueSet.toArray(new String[0]);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        fortuneCookieImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrator != null) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                }
                fortuneCookieImageView.setVisibility(View.GONE);
                crackedCookieImageView.setVisibility(View.VISIBLE);

                crackedCookieImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (vibrator != null) {
                            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                        }
                        crackedCookieImageView.setVisibility(View.GONE);
                        brokenCookieImageView.setVisibility(View.VISIBLE);
                        click.setVisibility(View.GONE);
                        fortune.setVisibility(View.VISIBLE);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                brokenCookieImageView.setVisibility(View.GONE);
                                Random random = new Random();
                                int randomIndex = random.nextInt(uniqueFortune.length);

                                animateWriting(fortuneText , uniqueFortune[randomIndex]);

                            }
                        }, 1100);
                    }
                });
            }
        });

        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FortuneActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });

    }
    private void animateWriting(final TextView textView, final String textToWrite) {
        textView.setText("");
        textView.setVisibility(View.VISIBLE);

        final int[] index = {0}; // Create an array to hold the index value for reference in the lambda expression

        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index[0] < textToWrite.length()) {
                    textView.append(Character.toString(textToWrite.charAt(index[0])));
                    index[0]++;
                    textView.postDelayed(this, 50); // Adjust the delay for typing speed
                }
            }
        }, 50); // Delay before starting the animation
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(FortuneActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    private boolean isSameDay(long timestamp1, long timestamp2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(timestamp1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(timestamp2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}