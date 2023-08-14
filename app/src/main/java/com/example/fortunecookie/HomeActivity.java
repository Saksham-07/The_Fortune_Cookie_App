package com.example.fortunecookie;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private boolean doubleBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button generate = findViewById(R.id.fortuneButton);
        FrameLayout logoContainer = findViewById(R.id.logoContainer);

        logoContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int initialY = -logoContainer.getHeight() - 800;// Hide the logo off-screen
                logoContainer.setTranslationY(initialY);

                // Bounce Animation
                ObjectAnimator translateYAnim = ObjectAnimator.ofFloat(logoContainer, "translationY", initialY, 0);
                translateYAnim.setDuration(2900);
                translateYAnim.setInterpolator(new BounceInterpolato());

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(translateYAnim);
                animatorSet.start();

                // Remove the listener to avoid multiple callbacks
                logoContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this , FortuneActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            }
        });
    }

    public void onBackPressed() {
        if (doubleBackPressed) {
            finishAffinity(); // Exit the app
        } else {
            doubleBackPressed = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            int BACK_PRESS_DELAY = 2000;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackPressed = false;
                }
            }, BACK_PRESS_DELAY);
        }
    }
}