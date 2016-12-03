package edu.mnstate.vh8237yk.project2;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by aemuxu on 11/27/2016.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
