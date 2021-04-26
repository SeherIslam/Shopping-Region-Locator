package com.seher.shoppingregionlocator;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SplashScreenActivity extends AppCompatActivity
{
    private final String TAG = SplashScreenActivity.class.getSimpleName();
    private static int SPLASH_SCREEN=2500;
    Animation top,bottom;

    TextView text;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        View decorView = getWindow().getDecorView();

        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);


        top= AnimationUtils.loadAnimation(this,R.anim.splash_top_animation);
        bottom= AnimationUtils.loadAnimation(this,R.anim.splash_bottom_animation);

        text=findViewById(R.id.shopping);
        image=findViewById(R.id.splashLogo);

        text.setAnimation(top);
        image.setAnimation(bottom);


        makeActivityAppearOnLockScreen();
        redirect_home();
    }

    /** Called when the activity is about to become visible. */
    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d("Inside : ", "onStart() event");
    }

    /** Called when the activity has become visible. */
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("Inside : ", "onResume() event");
    }

    /** Called when another activity is taking focus. */
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("Inside : ", "onPause() event");
    }

    /** Called when the activity is no longer visible. */
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d("Inside : ", "onStop() event");
    }

    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("Inside : ", "onDestroy() event");
    }

    @Override
    public void onBackPressed()
    {

    }


    private void makeActivityAppearOnLockScreen()
    {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }


    private void redirect_home()
    {
        new Handler().postDelayed(new Runnable() {

            /**
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run()
            {
                if(!isFinishing())
                {
                    startHomeActivity();
                }
            }
        }, 3000);
    }


    private void startHomeActivity()
    {
        Pair[] pair=new Pair[2];

        pair[0]=new Pair<View,String>(text,"s_text");
        pair[1]=new Pair<View,String>(image,"s_logo");

        ActivityOptions op=ActivityOptions.makeSceneTransitionAnimation(this,pair);

        Intent i = new Intent(SplashScreenActivity.this, Login.class);
        //i.putExtra("IS_OFFLINE", is_offline);
        startActivity(i,op.toBundle());

        finish();
    }
}