package jimmysharp.kanaclogger.view.fragment

import android.os.Bundle

import android.preference.PreferenceFragment

import jimmysharp.kanaclogger.R

class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)
    }
}
