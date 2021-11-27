package com.vanth.trackingvehicleuser.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vanth.trackingvehicleuser.R;

public class WelcomeActivity extends AppCompatActivity {
    Animation slideUp, slideDown;
    ImageView imageLogo;
    TextView slogan;

    static int SPLASH_TIME_OUT = 3200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hide titile_bar
        setContentView(R.layout.welcome_layout);

        //load animation
        slideUp = AnimationUtils.loadAnimation(this,R.anim.silde_up_anim);
        slideDown = AnimationUtils.loadAnimation(this,R.anim.slide_down_anim);

        // Mapping
        imageLogo = findViewById(R.id.imageLogo);
        imageLogo.setAnimation(slideDown);
        slogan = findViewById(R.id.tv_Slogan);
        slogan.setAnimation(slideUp);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
