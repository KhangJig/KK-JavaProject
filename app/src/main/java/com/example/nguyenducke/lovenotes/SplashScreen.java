package com.example.nguyenducke.lovenotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    private ImageView imageView1, imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView2 = (ImageView) findViewById(R.id.image1);
        imageView1 = (ImageView) findViewById(R.id.image2);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        imageView2.startAnimation(animation1);
        imageView1.startAnimation(animation2);

        final Intent intent = new Intent(this, LoginActivity.class);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
}
