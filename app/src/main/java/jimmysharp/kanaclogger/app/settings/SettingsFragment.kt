package jimmysharp.kanaclogger.app.settings

import android.os.Bundle
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat

import jimmysharp.kanaclogger.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)
    }
}
