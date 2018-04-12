package jimmysharp.kanaclogger.app.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import jimmysharp.kanaclogger.R
import jimmysharp.kanaclogger.app.util.replaceFragment
import kotlinx.android.synthetic.main.activity_setting.*

class SettingsActivity : AppCompatActivity() {
    companion object {
        val TAG = SettingsActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setting)

        setSupportActionBar(toolbar_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        replaceFragment(findOrCreateFragment(), R.id.container_settings)
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

    private fun findOrCreateFragment() =
            supportFragmentManager.findFragmentById(R.id.container_settings) ?:
                    SettingsFragment.newInstance()
}
