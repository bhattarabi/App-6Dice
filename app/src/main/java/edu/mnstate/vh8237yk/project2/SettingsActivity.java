package edu.mnstate.vh8237yk.project2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(android.R.style.Theme_Dialog);
        getFragmentManager().beginTransaction().
                replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
