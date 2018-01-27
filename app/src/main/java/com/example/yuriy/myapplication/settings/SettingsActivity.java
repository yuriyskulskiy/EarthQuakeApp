package com.example.yuriy.myapplication.settings;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yuriy.myapplication.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference prefMagnitude = findPreference(getString(R.string.settings_min_magnitude_key));
            bindPreferenceSummaryToValue(prefMagnitude);

            Preference orderByPreference = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderByPreference);
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String prefStr = sharedPreferences.getString((preference.getKey()), "");
            onPreferenceChange(preference, prefStr);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            String valueStr = newValue.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int indexOfValue = listPreference.findIndexOfValue(valueStr);
                if (indexOfValue >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    CharSequence entryForSummary = labels[indexOfValue];
                    listPreference.setSummary(entryForSummary);
                }
            } else {
                preference.setSummary(valueStr);
            }

            return true;

        }
    }
}
