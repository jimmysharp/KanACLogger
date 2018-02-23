package jimmysharp.kanaclogger.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import jimmysharp.kanaclogger.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_main)
        setSupportActionBar(toolbar)

//        val manager = fragmentManager
//        val transaction = manager.beginTransaction()
//        transaction.replace(R.id.container_main, ContentsFragment())
//        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
//
//        if (id == R.id.action_settings) {
//            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
//            startActivity(intent)
//            return true
//        }

        return super.onOptionsItemSelected(item)
    }
}
