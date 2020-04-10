package com.revolut.revolutrates.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.blongho.country_data.World;
import com.revolut.revolutrates.MainActivity;
import com.revolut.revolutrates.R;

public class SplashScreenActivity extends AppCompatActivity {

    Intent next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        World.init(getApplicationContext());

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(() -> {

            next = new Intent(this, MainActivity.class);

            runOnUiThread(() -> new Handler().postDelayed(() -> {
                startActivity(next);
                finish();
            }, 2000));
        }).start();
    }

}

