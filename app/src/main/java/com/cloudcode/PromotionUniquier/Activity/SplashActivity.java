package com.cloudcode.PromotionUniquier.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cloudcode.PromotionUniquier.R;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    ImageView centerImage;
    RippleBackground rippleBackgroundcontent;
    RelativeLayout layoutRipple;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Handler handler = new Handler();
        layoutRipple = findViewById(R.id.layoutRipple);

        rippleBackgroundcontent = findViewById(R.id.contentripple);
        centerImage = (ImageView) findViewById(R.id.centerImage);
        //    loadAllBanner();
        rippleBackgroundcontent.startRippleAnimation();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                foundDevice();
            }
        }, 3000);
    }

    private void foundDevice() {


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList = new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(centerImage, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(centerImage, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        rippleBackgroundcontent.stopRippleAnimation();
        rippleBackgroundcontent.setVisibility(View.GONE);


        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();




    }
}
