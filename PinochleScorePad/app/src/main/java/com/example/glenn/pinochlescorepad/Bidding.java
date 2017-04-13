package com.example.glenn.pinochlescorepad;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Bidding extends Activity {

    /**
     * Key strings for putExtras
     */
    public final static String NAME_MESSAGE = "com.example.glenn.pinochlescorepad.NAME1";
    public final static String NAME_MESSAGE1 = "com.example.glenn.pinochlescorepad.NAME2";
    public final static String BID_AMOUNT = "com.example.glenn.pinochlescorepad.BID";
    public final static String WINNER_MELD = "com.example.glenn.pinochlescorepad.WINNERMELD";
    public final static String OPP_MELD = "com.example.glenn.pinochlescorepad.OPPMELD";
    public final static String BID_WINNER = "com.example.glenn.pinochlescorepad.BIDWINNER";
    public final static String TRUMP = "com.example.glenn.pinochlescorepad.TRUMP";
    public final static String GO_SET = "com.example.glenn.pinochlescorepad.GOSET";
    public final static String SHOOT_MOON = "com.example.glenn.pinochlescorepad.SHOOTMOON";
    /**
     * Value strings for putExtras
     */
    private String names1 = null;
    private String names2 = null;
    private String the_team = null;
    private String trump = null;
    /**
     * Strings for shared preferences
     */
    private final static String NAME1 = "com.example.glenn.pinochlescorepad.MESSAGE";
    private final static String NAME2 = "com.example.glenn.pinochlescorepad.MESSAGE2";
    private final static String NAME3 = "com.example.glenn.pinochlescorepad.MESSAGE3";
    private final static String NAME4 = "com.example.glenn.pinochlescorepad.MESSAGE4";
    /**
     * For determining when to show toast to user telling how many points they need to take
     */
    private EditText edit_text;
    private EditText edit_text2;
    /**
     * Boolean for winning bid
     */
    private boolean textChanged = false;
    /**
     * Value for shared preferences
     */
    private static final String MyPREFERENCES = "MyPrefs" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bidding);
        /**
         * Initialize edit_text to second textedit widget
         */
        edit_text = (EditText) findViewById(R.id.winnerMeld);
        edit_text2 = (EditText) findViewById(R.id.bidPoints);

        /**
         * Call function to hide keyboard after touching outside keyboard
         */
        getRidOfKeyboard(findViewById(R.id.parentPanel));

        /**
         * Set screen to fullscreen
         */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /**
         * Listener calls pointsNeeded() when user presses done button on meld EditText
         */
        edit_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (textChanged) {
                        pointsNeeded();
                    }
                }
                return false;
            }
        });
        /**
         * Listener calls pointsNeeded() when meld EditText loses focus
         */
        edit_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(textChanged) {
                        pointsNeeded();
                    }
                }
            }
        });
        edit_text2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        /**
         * Display names of the teams
         */
        setTeamText();
    }

    /**
     * Set text to names of players
     */
    private void setTeamText(){

        /**Get values stored in shared preferences*/

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String name1 = sharedPreferences.getString(NAME1, "Name1 Error");
        String name2 = sharedPreferences.getString(NAME2, "Name2 Error");
        String name3 = sharedPreferences.getString(NAME3, "Name3 Error");
        String name4 = sharedPreferences.getString(NAME4, "Name4 Error");

        TextView textView = (TextView) findViewById(R.id.names1);
        TextView textView2 = (TextView) findViewById(R.id.names2);

        names1 = name1 + " & " + name2;
        names2 = name3 + " & " + name4;

        /**Set text view text to retrieved values*/

        textView.setText(names1);
        textView2.setText(names2);
    }

    /**
     *Function to hide softkeyboard
     */
    private void getRidOfKeyboard(View view) {

        /**Set up touch listener for non-textbox views to hide keyboard.*/
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Bidding.this);
                    return false;
                }

            });
        }

        /**If a layout container, iterate over children and seed recursion.*/
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                getRidOfKeyboard(innerView);
            }
        }
    }

    /**
     * Function that determines which team won the bid
     */
    public void onRadioButtonTeamClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.names1:
                if (checked)
                    the_team = "0";
                    break;
            case R.id.names2:
                if (checked)
                    the_team = "1";
                    break;
        }
    }

    /**
     * Checks what trump suit is selected
     */
    public void onRadioButtonTrumpClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.spades:
                if (checked)
                    trump = "spades";
                break;
            case R.id.hearts:
                if (checked)
                    trump = "hearts";
                break;
            case R.id.clubs:
                if (checked)
                    trump = "clubs";
                break;
            case R.id.diamonds:
                if (checked)
                    trump = "diamonds";
                break;
        }
    }

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    /**
     * Sends user to home menu
     */
    public void goHome(View view){
        Intent intent = new Intent(this, MenuScreen.class);
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    /**
     * Go to the score pad screen. Put variables in intents. If bid winner or trump has not been picked
     * or winning bid, meld and opponent's meld are empty, show toast prompt
     */
    public void go_to_scorepad(View view){
        if(the_team != null) {
            if(trump != null) {
                Intent intent = new Intent(this, ScorePad.class);

                EditText editText = (EditText) findViewById(R.id.bidPoints);
                String bid = editText.getText().toString();

                EditText editText1 = (EditText) findViewById(R.id.winnerMeld);
                String winnerMeld = editText1.getText().toString();

                EditText editText2 = (EditText) findViewById(R.id.oppMeldEdit);
                String oppMeld = editText2.getText().toString();

                if(!bid.matches(""))
                {
                    int bid_check = Integer.parseInt(bid);
                    if(bid_check >= 250) {
                        if (!winnerMeld.matches("")) {
                            if (!oppMeld.matches("")) {
                                intent.putExtra(NAME_MESSAGE, names1);
                                intent.putExtra(NAME_MESSAGE1, names2);
                                intent.putExtra(BID_WINNER, the_team);
                                intent.putExtra(TRUMP, trump);
                                intent.putExtra(BID_AMOUNT, bid);
                                intent.putExtra(WINNER_MELD, winnerMeld);
                                intent.putExtra(OPP_MELD, oppMeld);
                                intent.putExtra(GO_SET, "0");
                                intent.putExtra(Bidding.SHOOT_MOON, "0");

                                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                            }
                            else
                                makeToast("Please enter the opponent's meld points");
                        }
                        else
                            makeToast("Please enter the meld points");
                    }
                    else
                        makeToast("Bid must be at least 250");
                }
                else
                    makeToast("Please enter the bid amount");
            }
            else
                makeToast("Please choose what trump is");
        }
        else
            makeToast("Please choose the winner of the bid");
    }

    /**
     * Calculate points needed to make the board. Make toast to tell user what that number is.
     */
    private void pointsNeeded(){
        int points;
        EditText editText = (EditText) findViewById(R.id.winnerMeld);
        int meld = Integer.parseInt(editText.getText().toString().trim());
        EditText editText2 = (EditText) findViewById(R.id.bidPoints);
        int bid = Integer.parseInt(editText2.getText().toString().trim());

        if(bid >= 250)
        {
            points = bid - meld;
            points = points/10;

            if(points < 1) {
                makeToast("You need to take 1 point");
            }
            else if(points > 25)
            {
                makeToast("You need to take more than 25 points!");
            }
            else
            {
                makeToast("You need to take " + points + " points");
            }
        }
    }

    /**
     * If team won't be able to make bid then they forfeit the round.
     * Bid points subtracted from total score.
     * Opponents get their meld points added.
     */
    public void goSet(View view){
        Intent intent = new Intent(this, ScorePad.class);
        EditText editText = (EditText) findViewById(R.id.bidPoints);
        String theBid = editText.getText().toString();
        EditText editText1 = (EditText) findViewById(R.id.oppMeldEdit);
        String oppMeld = editText1.getText().toString();
        int bid = 0;

        if(the_team != null)
        {
            if(!theBid.matches(""))
            {
                bid = Integer.parseInt(editText.getText().toString());
                if(bid >= 250) {
                    if (!oppMeld.matches("")) {
                        int meld = Integer.parseInt(editText1.getText().toString());
                        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        int score_team1 = sharedPreferences.getInt("team1Score", 0);
                        int score_team2 = sharedPreferences.getInt("team2Score", 0);

                        if (the_team.matches("0")) {
                            score_team1 -= bid;
                            score_team2 += meld;
                        } else {
                            score_team2 -= bid;
                            score_team1 += meld;
                        }

                        if (Math.abs(score_team1 - score_team2) >= 1500) {
                            Intent mIntent = new Intent(this, Victory.class);
                            if (score_team1 > score_team2) {
                                mIntent.putExtra(NAME_MESSAGE, names1);
                            } else {
                                mIntent.putExtra(NAME_MESSAGE, names2);
                            }

                            startActivity(mIntent);
                        }

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(Game.teamOneScore, score_team1);
                        editor.putInt(Game.teamTwoScore, score_team2);
                        editor.apply();

                        intent.putExtra(NAME_MESSAGE, names1);
                        intent.putExtra(NAME_MESSAGE1, names2);
                        intent.putExtra(GO_SET, "1");
                        intent.putExtra(Bidding.SHOOT_MOON, "0");

                        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                    }
                    else
                        makeToast("Please enter opponent's meld points");
                }
                else
                    makeToast("Bid must be at least 250");
            }
            else
                makeToast("Please enter the bid amount");
        }
        else
            makeToast("Please choose the winner of the bid");
    }

    /**
     * Sends user to score pad with intent to shoot the moon
     */
    public void shoot_the_moon(View view){
        Intent intent = new Intent(this, shoot_moon.class);

        if(the_team != null)
        {
            if(trump != null)
            {
                intent.putExtra(BID_WINNER, the_team);
                intent.putExtra(TRUMP, trump);
                intent.putExtra(NAME_MESSAGE, names1);
                intent.putExtra(NAME_MESSAGE1, names2);

                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
            }
            else
                makeToast("PLEASE CHOOSE WHAT TRUMP IS");
        }
        else
            makeToast("PLEASE CHOOSE THE WINNER OF THE BID");
    }

    /**
     * Take focus away from meld EditText when user clicks away
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (edit_text.isFocused()) {
                Rect outRect = new Rect();
                edit_text.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    edit_text.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Method to put toast message on screen
     * @param charSequence the message to be displayed
     */
    private void makeToast(CharSequence charSequence){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, charSequence, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
