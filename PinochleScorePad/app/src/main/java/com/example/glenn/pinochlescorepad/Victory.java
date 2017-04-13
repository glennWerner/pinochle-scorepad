package com.example.glenn.pinochlescorepad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import android.os.Handler;

public class Victory extends Activity {

    /**
     * String key for stored preferences
     */
    private static final String MyPREFERENCES = "MyPrefs" ;
    /**
     * String to store names of winning team
     */
    private String winningTeam;
    /**
     * Textview that displays the names
     */
    private TextView mTextView;
    /**
     * Textview that will be flashing
     */
    private TextView mTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
    }

    /**
     * Set the names of the winners, animate the "You Win!!" string, clear shared preferences, redirect to menu after ten seconds
     */
    private void init(){
        Intent intent = getIntent();
        winningTeam = intent.getStringExtra(Bidding.NAME_MESSAGE);

        mTextView = (TextView) findViewById(R.id.victoryHeader);
        mTextView2 = (TextView) findViewById(R.id.victoryH);

        String original = mTextView.getText().toString();
        original += winningTeam + "!";
        mTextView.setText(original);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(150); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(40);
        mTextView2.startAnimation(anim);

        this.getSharedPreferences(MyPREFERENCES, 0).edit().clear().apply();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent startActivity = new Intent(Victory.this, MenuScreen.class);
                startActivity(startActivity);
                finish();
            }
        }, 10000);
    }
}
