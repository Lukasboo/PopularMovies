package com.nanodegree.udacity.lucas.popularmovies.app;

import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    DialogPreference dialogPreference;
    EditTextPreference editTextPreference;
    String pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        pref = intent.getStringExtra("pref");

        addPreferencesFromResource(R.xml.pref_general);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        //dialogPreference = (DialogPreference)getPreferenceScreen().findPreference(getString(R.string.pref_movies_key));
        //dialogPreference = (DialogPreference)getPreferenceScreen().findPreference(pref);
        editTextPreference = (EditTextPreference)getPreferenceScreen().findPreference(getString(R.string.pref_movies_key));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_movies_key)));
        //bindPreferenceSummaryToValue(findPreference(pref));
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        //int pos = findPreference("sort").getOrder();
        //preferenceScreen.onItemClick(null, null, pos, 0);

        //onPreferenceChange(findPreference(getString(R.string.pref_movies_key)), pref);
        onPreferenceChange(findPreference("sort"), pref);

        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
        //finish();
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
                        //.getString(pref, ""));
                        //.getString(preference.getKey(), ""));

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();
        preference.setSummary(stringValue);
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
                //preference.setSummary(stringValue);
            }
        } else {
            preference.setSummary(stringValue);
            editTextPreference.setText(pref);
        }
        return true;
    }



}
