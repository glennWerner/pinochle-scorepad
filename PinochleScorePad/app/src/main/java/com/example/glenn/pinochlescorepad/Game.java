package com.example.glenn.pinochlescorepad;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class Game extends Activity {

    /**
     * String key names for intent
     */
    private final static String NAME1 = "com.example.glenn.pinochlescorepad.MESSAGE";
    private final static String NAME2 = "com.example.glenn.pinochlescorepad.MESSAGE2";
    private final static String NAME3 = "com.example.glenn.pinochlescorepad.MESSAGE3";
    private final static String NAME4 = "com.example.glenn.pinochlescorepad.MESSAGE4";
    private static final String GAME_STARTED = "com.example.glenn.pinochlescorepad.GAME_STARTED";
    /**
     * Strings for storing team scores in shared preferences
     */
    public final static String teamOneScore = "team1Score";
    public final static String teamTwoScore = "team2Score";
    /**
     * Shared preferences object for storing variables
     */
    private SharedPreferences sharedpreferences;
    /**
     * String to access my shared preferences folder
     */
    private static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume(){
        super.onResume();

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**Called when user clicks the new game button*/

    public void go_to_bid(View view){
        Intent intent = new Intent(this, Bidding.class);

        /**
         * Sets score to zero to start the game. Saves in shared preferences.
         */
        setScore();

        /**Get values from user inputs*/

        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();

        EditText editText2 = (EditText) findViewById(R.id.editText2);
        String message2 = editText2.getText().toString();

        EditText editText3 = (EditText) findViewById(R.id.editText3);
        String message3 = editText3.getText().toString();

        EditText editText4 = (EditText) findViewById(R.id.editText4);
        String message4 = editText4.getText().toString();

        /**Put strings in shared preferences if names are filled out*/

        if(message.matches("") || message2.matches("") || message3.matches("") || message4.matches(""))
        {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, "Please fill out all of the names", duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else
        {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(NAME1, message);
            editor.putString(NAME2, message2);
            editor.putString(NAME3, message3);
            editor.putString(NAME4, message4);

            editor.apply();

            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        }

    }

    /**
     * Sets beginning score of each team to 0 in shared preferences
     */
    private void setScore(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        int the_score = 0;
        editor.putInt(teamOneScore, the_score);
        editor.putInt(teamTwoScore, the_score);
        editor.putString(GAME_STARTED, "1");
        editor.apply();
    }

}
