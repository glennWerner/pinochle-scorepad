package com.example.glenn.pinochlescorepad;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ScorePad extends Activity {

    /**
     * String to help determine what trump image to show
     */
    private String trump = null;
    /**
     * String key for stored preferences
     */
    private static final String MyPREFERENCES = "MyPrefs" ;
    /**
     * String to check if you need to hide the TextView or EditText widgets
     */
    private String hideCheck;
    /**
     * String to check if shooting the moon
     */
    private String moonCheck;
    /**
     * ImageView for showing trump image
     */
    private ImageView mImageView;
    /**
     * Indicates who won the bid. 0 for team 1, 1 for team 2
     */
    private int bid_winner;
    /**
     * The bid amount
     */
    private int the_bid;
    /**
     * The bid winner's meld points
     */
    private int winner_meld;
    /**
     * Opponent's meld points
     */
    private int opp_meld;
    /**
     * Strings for storing team scores in shared preferences
     */
    private final static String teamOneScore = "team1Score";
    private final static String teamTwoScore = "team2Score";
    /**
     * strings to hold names of teams
     */
    private String message;
    private String message2;
    /**
     * Keep track of who won last trick
     */
    private int last_trick = 0;
    /**
     * Score for team 1
     */
    private int score_team1;
    /**
     * Score for team 2
     */
    private int score_team2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_pad);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume(){
        super.onResume();

        setActivityText();
        getScore();
        setTrumpImage();
        hideViews();
    }

    /**
     * Set the names of the teams and initialize variables with values from intent
     */
    private void setActivityText(){
        Intent intent = getIntent();

        message = intent.getStringExtra(Bidding.NAME_MESSAGE);
        message2 = intent.getStringExtra(Bidding.NAME_MESSAGE1);

        trump = intent.getStringExtra(Bidding.TRUMP);
        hideCheck = intent.getStringExtra(Bidding.GO_SET);
        moonCheck = intent.getStringExtra(Bidding.SHOOT_MOON);

        /**
         * Checks if team decided to go set or shoot the moon and then assigns variables
         */
        if(!hideCheck.matches("1") && !moonCheck.matches("1"))
        {
            bid_winner = Integer.parseInt(intent.getStringExtra(Bidding.BID_WINNER));
            the_bid = Integer.parseInt(intent.getStringExtra(Bidding.BID_AMOUNT));
            winner_meld = Integer.parseInt(intent.getStringExtra(Bidding.WINNER_MELD));
            opp_meld = Integer.parseInt(intent.getStringExtra(Bidding.OPP_MELD));
        }

        TextView textView = (TextView) findViewById(R.id.team1);
        TextView textView2 = (TextView) findViewById(R.id.team2);

        /**
         * Sets team names to text views
         */
        textView.setText(message);
        textView2.setText(message2);
    }

    /**
     * Set trump image
     */
    private void setTrumpImage(){
        mImageView = (ImageView) findViewById(R.id.trumpImage);
        if(trump != null) {
            switch (trump) {
                case "spades":
                    mImageView.setImageResource(R.drawable.spadenotification);
                    break;
                case "hearts":
                    mImageView.setImageResource(R.drawable.heartnotification);
                    break;
                case "clubs":
                    mImageView.setImageResource(R.drawable.clubnotification);
                    break;
                case "diamonds":
                    mImageView.setImageResource(R.drawable.diamondnotification);
                    break;
            }
        }
    }

    /**
     * Get scores from stored preferences and set TextViews to those numbers
     */
    private void getScore(){
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        score_team1 = sharedPreferences.getInt("team1Score", 0);
        score_team2 = sharedPreferences.getInt("team2Score", 0);

        TextView textView = (TextView) findViewById(R.id.score1);
        TextView textView1 = (TextView) findViewById(R.id.score2);

        //textView.setText(Integer.toString(score_team1));
        textView.setText(String.format("%d", score_team1));
        textView1.setText(String.format("%d", score_team2));
    }

    /**
     * Hides certain views on page if winning team went set
     */
    private void hideViews(){
        TextView textView = (TextView) findViewById(R.id.enterScores);
        EditText editText = (EditText) findViewById(R.id.roundPoints1);
        EditText editText1 = (EditText) findViewById(R.id.roundPoints2);
        TextView textView1 = (TextView) findViewById(R.id.lastTrick);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.lastTrickRGroup);
        Button mButton = (Button) findViewById(R.id.scorePadButton);

        if(hideCheck.matches("1"))
        {
            textView.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
            editText1.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            mButton.setText("Next Round");
        }

        if(moonCheck.matches("1"))
        {
            textView.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
            editText1.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            mButton.setText("Next Round");
            hideCheck = "1";
        }
    }

    /**
     * Determines which radio button is pressed for last trick
     */
    public void lastTrickSelected(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.team1LastTrick:
                if (checked)
                    last_trick = 1;
                break;
            case R.id.team2LastTrick:
                if (checked)
                    last_trick = 2;
                break;
        }
    }

    /**
     * Hides views, adds scores, changes button text, redirects to bidding page
     */
    public void keyButton(View view){

        /**
         * Check if hideCheck is set (variable indicates if team went set and therefore doesn't need to calculate score)
         */
        if(hideCheck.matches("1"))
        {
            Intent intent = new Intent(this, Bidding.class);
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        }
        else
        {
            EditText editText = (EditText) findViewById(R.id.roundPoints1);
            String team1RoundPoints = editText.getText().toString();
            EditText editText1 = (EditText) findViewById(R.id.roundPoints2);
            String team2RoundPoints = editText1.getText().toString();

            if (!team1RoundPoints.matches("")) {
                if (!team2RoundPoints.matches("")) {
                    if (last_trick != 0) {
                        hideCheck = "1";
                        hideViews();

                        /**
                         * Get points scored by each team during the round
                         */
                        int score1 = 10 * (Integer.parseInt(team1RoundPoints));
                        int score2 = 10 * (Integer.parseInt(team2RoundPoints));

                        if (last_trick == 1)
                            score1 += 10;
                        else
                            score2 += 10;
                        /**
                         * Determine if the team that won the bid made enough points.
                         * If so then add points together.
                         * Else just add points for the opponent
                         */
                        if (bid_winner == 0)
                        {
                            if (score1 >= (the_bid - winner_meld))
                            {
                                score_team1 += (score1 + winner_meld);
                            } else
                            {
                                score_team1 -= the_bid;
                            }

                            if (score2 > 0)
                            {
                                score_team2 += (score2 + opp_meld);
                            }
                        }
                        else
                        {
                            if (score2 >= (the_bid - winner_meld))
                            {
                                score_team2 += (score2 + winner_meld);
                            }
                            else
                            {
                                score_team2 -= the_bid;
                            }

                            if (score1 > 0)
                            {
                                score_team1 += (score1 + opp_meld);
                            }
                        }

                        /**
                         * If there is a 1500 point spread then send team with highest score to victory screen
                         */
                        if(Math.abs(score_team1 - score_team2) >= 1500){
                            Intent mIntent = new Intent(this, Victory.class);
                            if(score_team1 > score_team2)
                            {
                                mIntent.putExtra(Bidding.NAME_MESSAGE, message);
                            }
                            else
                            {
                                mIntent.putExtra(Bidding.NAME_MESSAGE, message2);
                            }

                            startActivity(mIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                        }

                        /**
                         * If team 1 has more than 1500, send to victory screen
                         */
                        if(score_team1 >= 1500)
                        {
                            Intent mIntent = new Intent(this, Victory.class);
                            mIntent.putExtra(Bidding.NAME_MESSAGE, message);
                            startActivity(mIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                        }

                        /**
                         * If team 2 has more than 1500, send to victory screen
                         */
                        if(score_team2 >= 1500)
                        {
                            Intent mIntent = new Intent(this, Victory.class);
                            mIntent.putExtra(Bidding.NAME_MESSAGE, message2);
                            startActivity(mIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                        }

                        /**
                         * Store updated scores in shared preferences
                         */
                        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(teamOneScore, score_team1);
                        editor.putInt(teamTwoScore, score_team2);
                        editor.apply();

                        /**
                         * Update scores on the screen
                         */
                        TextView textView = (TextView) findViewById(R.id.score1);
                        TextView textView1 = (TextView) findViewById(R.id.score2);
                        textView.setText(Integer.toString(score_team1));
                        textView1.setText(Integer.toString(score_team2));
                    }
                    else
                        makeToast("Please select who won the last trick");
                }
                else
                    makeToast("Please enter valid number for point cards won for Team 1");
            }
            else
                makeToast("Please enter valid number for point cards won for Team 2");
        }
    }

    /**
     * Create a toast based off of the string provided
     * @param charSequence Message to display
     */
    private void makeToast(CharSequence charSequence){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, charSequence, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void goHomeScorePad(View view){
        Intent intent = new Intent(this, MenuScreen.class);
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }
}
