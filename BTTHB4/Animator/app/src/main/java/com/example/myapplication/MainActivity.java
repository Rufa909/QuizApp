package com.example.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runAnimation();
            }
        });
    }
    private void runAnimation() {
        TextView textAnimation = (TextView) findViewById(R.id.text_animation);
        textAnimation.animate()
                .rotation(360f)
                .translationY(500f) //500 px
                .setDuration(2000)
                .start();

    }
}