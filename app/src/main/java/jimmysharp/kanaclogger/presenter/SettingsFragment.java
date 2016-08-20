package jimmysharp.kanaclogger.presenter;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import jimmysharp.kanaclogger.R;

public class SettingsFragment extends PreferenceFragment{
    public SettingsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
