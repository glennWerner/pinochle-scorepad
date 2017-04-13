package com.example.glenn.pinochlescorepad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.WindowManager;

public class shoot_moon extends Activity {

    /**
     * Name team 1
     */
    private String name_team1;
    /**
     * Name team 2
     */
    private String name_team2;
    /**
     * To tell who won the bid
     */
    private String bid_winner;
    /**
     * To tell what trump is
     */
    private String trump;
    /**
     * For accessing variables in shared preferences
     */
    private SharedPreferences sharedPreferences;
    /**
     * Strings for storing scores for teams in shared preferences
     */
    private final static String teamOneScore = "team1Score";
    private final static String teamTwoScore = "team2Score";
    /**
     * Score for team 1
     */
    private int score_team1;
    /**
     * Score for team 2
     */
    private int score_team2;
    /**
     * String key for stored preferences
     */
    private static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot_moon);

        //getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
    }

    /**
     * Initialize variables from sharedpreferences and intent
     */
    private void init(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        score_team1 = sharedPreferences.getInt("team1Score", 0);
        score_team2 = sharedPreferences.getInt("team2Score", 0);

        Intent intent = getIntent();

        name_team1 = intent.getStringExtra(Bidding.NAME_MESSAGE);
        name_team2 = intent.getStringExtra(Bidding.NAME_MESSAGE1);
        bid_winner = intent.getStringExtra(Bidding.BID_WINNER);
        trump = intent.getStringExtra(Bidding.TRUMP);
    }

    /**
     * If team shot moon then calculate if game is over
     * @param view
     */
    public void shoot_success(View view){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(bid_winner.matches("0"))
        {
            if(score_team1 >= 0)
            {
                Intent intent = new Intent(this, Victory.class);
                intent.putExtra(Bidding.NAME_MESSAGE, name_team1);
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
            }
            else
            {
                editor.putInt(teamOneScore, 0);
                editor.apply();

                Intent intent = new Intent(this, ScorePad.class);
                intent.putExtra(Bidding.NAME_MESSAGE, name_team1);
                intent.putExtra(Bidding.NAME_MESSAGE1, name_team2);
                intent.putExtra(Bidding.SHOOT_MOON, "1");
                intent.putExtra(Bidding.TRUMP, trump);
                intent.putExtra(Bidding.GO_SET, "0");

                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
            }
        }
        else
        {
            if(score_team2 >= 0)
            {
                Intent intent = new Intent(this, Victory.class);
                intent.putExtra(Bidding.NAME_MESSAGE, name_team2);
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
            }
            else
            {
                editor.putInt(teamTwoScore, 0);
                editor.apply();

                Intent intent = new Intent(this, ScorePad.class);
                intent.putExtra(Bidding.NAME_MESSAGE, name_team1);
                intent.putExtra(Bidding.NAME_MESSAGE1, name_team2);
                intent.putExtra(Bidding.SHOOT_MOON, "1");
                intent.putExtra(Bidding.TRUMP, trump);
                intent.putExtra(Bidding.GO_SET, "0");

                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
            }
        }
    }

    /**
     * If team failed then send to victory screen
     * @param view
     */
    public void shoot_failure(View view){
        if(bid_winner.matches("0"))
        {
            Intent intent = new Intent(this, Victory.class);
            intent.putExtra(Bidding.NAME_MESSAGE, name_team2);
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        }
        else
        {
            Intent intent = new Intent(this, Victory.class);
            intent.putExtra(Bidding.NAME_MESSAGE, name_team1);
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        }
    }
}
