package com.example.arielsurveysapp; // שים את שם החבילה שלך

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.arielsurveysapp.LoginActivity;
import com.example.arielsurveysapp.R;

public class Splash extends Activity {
    private ImageView myImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        myImageView = (ImageView) findViewById(R.id.imageViewSplash);
        Thread mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        Animation myFadeInAnimation = AnimationUtils.loadAnimation(Splash.this,R.anim.tween);
                        myImageView.startAnimation(myFadeInAnimation);
                        wait(3000);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        mSplashThread.start();
    }
}
