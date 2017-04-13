package com.example.glenn.pinochlescorepad;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MenuScreen extends Activity {

    /**
     * Shared preferences object for storing variables
     */
    private SharedPreferences sharedpreferences;
    /**
     * String to access my shared preferences folder
     */
    private static final String MyPREFERENCES = "MyPrefs" ;
    /**
     * String for variable checking if game has been started
     */
    private static final String GAME_STARTED = "com.example.glenn.pinochlescorepad.GAME_STARTED";
    /**
     * Resume button object. Used if needed to change background and disable
     */
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onResume(){
        super.onResume();

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        mButton = (Button) findViewById(R.id.button2);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        checkResume(this);
    }

    /**Called when user clicks the new game button*/
    public void newGame(View view){

        Intent intent = new Intent(this, Game.class);
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    public void resumeGame(View view){


        Intent intent = new Intent(this, Bidding.class);
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    public void goToRules(View view){
        Intent intent = new Intent(this, Rules.class);
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    private void checkResume(Context context){
        String checkGameStarted = sharedpreferences.getString(GAME_STARTED, "0");
        if(checkGameStarted.matches("0"))
        {
            mButton.setBackground(ContextCompat.getDrawable(context, R.drawable.disabled_button));
            mButton.setEnabled(false);
        }
    }
}
