package com.example.fortunecookie;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefs";
    public static final String KEY_LOGGED_IN = "isLoggedIn";
    EditText usernameEditText;
    EditText passwordEditText;
    private boolean doubleBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        LinearLayout login = findViewById(R.id.fragment_container);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_LOGGED_IN, false);
        if (isLoggedIn){
            Intent intent = new Intent(MainActivity.this , HomeActivity.class);
            startActivity(intent);
        }

        FrameLayout logoContainer = findViewById(R.id.logoContainer);

        logoContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int initialY = -logoContainer.getHeight() - 300;// Hide the logo off-screen
                logoContainer.setTranslationY(initialY);

                // Bounce Animation
                ObjectAnimator translateYAnim = ObjectAnimator.ofFloat(logoContainer, "translationY", initialY, 0);
                translateYAnim.setDuration(1500);
                translateYAnim.setInterpolator(new BounceInterpolato());

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(translateYAnim);
                animatorSet.start();

                // Remove the listener to avoid multiple callbacks
                logoContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if(isValidCredentials(name , password)) {
                    boolean loggedIn = createUserAccount(name, password);
                    if (loggedIn) {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(KEY_LOGGED_IN, true);
                        editor.apply();
                    }
                }
                else{
                    if (name.length() < 6) {
                        usernameEditText.setError("Username must be at least 6 characters");
                    }
                    if (password.length() < 8) {
                        passwordEditText.setError("Password must be at least 8 characters");
                    }
                }
            }
        });
    }

    private boolean isValidCredentials(String username, String password) {
        return username.length() >= 6 && password.length() >= 8;
    }
    private boolean createUserAccount(String name ,String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", password);
        editor.putString("name", name);
        editor.apply();
        return true;
    }
    public void onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed(); // Exit the app
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