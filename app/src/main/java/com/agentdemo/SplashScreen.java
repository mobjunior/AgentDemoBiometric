package com.agentdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.agentdemo.util.Config;

public class SplashScreen extends Activity {
    Animation animationFadeIn;
    ImageView image;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;
    //preferencies
    SharedPreferences sh_Pref;
    String username, password;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        image = (ImageView) findViewById(R.id.imgLogo);
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        image.startAnimation(animationFadeIn);

        //////////////////////////
        sh_Pref = getSharedPreferences(("credentials"),Context.MODE_WORLD_READABLE);
        username = sh_Pref.getString(Config.USERNAME_KEY, "empty");
        password = sh_Pref.getString((Config.PASSWORD_KEY), "empty");

        /////////////////////

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
               if (username.equalsIgnoreCase("empty") && password.equalsIgnoreCase("empty")) {
                    i = new Intent(SplashScreen.this, FirstLogin.class);
                    startActivity(i);
                } else {
                    i = new Intent(SplashScreen.this, Login.class);
                    startActivity(i);
                }
                // This method will be executed once the timer is over
                // Start your app main activity


                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}