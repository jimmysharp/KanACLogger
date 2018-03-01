package jimmysharp.kanaclogger.view.activity

import android.os.Bundle
import android.preference.PreferenceActivity
import android.support.v7.app.AppCompatDelegate
import android.view.MenuItem

import jimmysharp.kanaclogger.view.fragment.SettingsFragment

class SettingsActivity : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val delegate: AppCompatDelegate
        delegate = AppCompatDelegate.create(this, null)

        val actionBar = delegate.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment(), FRAGMENT_TAG)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val FRAGMENT_TAG = "setting_fragment"
    }
}
