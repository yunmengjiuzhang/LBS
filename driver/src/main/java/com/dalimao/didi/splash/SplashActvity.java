package com.dalimao.didi.splash;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.dalimao.didi.R;
import com.dalimao.didi.main.ui.MainActivity;



public class SplashActvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_actvity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActvity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final AnimatedVectorDrawable anim1 = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.anim);
            final ImageView img1 = ((ImageView) findViewById(R.id.img1));
            img1.setImageDrawable(anim1);
            anim1.start();
        }


    }
}
