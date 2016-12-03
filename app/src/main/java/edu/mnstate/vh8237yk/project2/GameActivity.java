package edu.mnstate.vh8237yk.project2;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

import static android.view.Menu.NONE;

public class GameActivity extends BaseActivity
        implements GameFragment.OnDicesRolledListener {

    RollsFragment rFrag;
    GameFragment gFrag;

    SharedPreferences prefs;
    boolean allowSkips;
    boolean showRolls;
    boolean showToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        gFrag = (GameFragment) getFragmentManager().findFragmentById(R.id.fragGame);
        rFrag = (RollsFragment) getFragmentManager().findFragmentById(R.id.fragRolls);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        allowSkips = prefs.getBoolean("pref_skip_turn", true);
        showRolls = prefs.getBoolean("pref_display_rolls", true);
        showToolbar = prefs.getBoolean("pref_display_dbtoolbar", false);

        if (allowSkips)
            gFrag.getView().findViewById(R.id.btnSkip).setVisibility(View.VISIBLE);
        else
            gFrag.getView().findViewById(R.id.btnSkip).setVisibility(View.INVISIBLE);

        if (showRolls)
            getFragmentManager().beginTransaction().show(rFrag).commit();
        else
            getFragmentManager().beginTransaction().hide(rFrag).commit();

        if (showToolbar)
            rFrag.getView().findViewById(R.id.dbToolbar).setVisibility(View.VISIBLE);
        else
            rFrag.getView().findViewById(R.id.dbToolbar).setVisibility(View.GONE);
    }



    @Override
    public void OnDicesRolled(Roll r) {
        ((RollsFragment) getFragmentManager().findFragmentById(R.id.fragRolls)).newRoll(r);
    }

    public boolean onCreateOptionsMenu(Menu m)
    {
        getMenuInflater().inflate(R.menu.app_menu, m);
        m.add(NONE, 100, NONE, "New Game");
        m.add(NONE, 200, NONE, "Settings");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch(menuItem.getItemId())
        {
            case R.id.about:
                displayAbout();
                return true;
            case 100:
                newGame();
                return true;
            case 200:
                showSettings();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void showSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void displayAbout() {
        LayoutInflater li = LayoutInflater.from(this);
        View aboutView = li.inflate(R.layout.about_layout, null);
        AlertDialog.Builder buil = new AlertDialog.Builder(this);
        buil.setView(aboutView);
        buil.setCancelable(false);
        buil.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        buil.create().show();
    }

    private void newGame() {
        LayoutInflater li = LayoutInflater.from(this);
        View newGameView = li.inflate(R.layout.new_game_layout, null);
        AlertDialog.Builder buil = new AlertDialog.Builder(this);
        buil.setView(newGameView);
        buil.setCancelable(false);
        buil.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        buil.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        buil.create().show();
    }
}
