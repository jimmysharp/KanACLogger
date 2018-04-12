package jimmysharp.kanaclogger.app.util

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.replaceFragment(fragment : Fragment, frameId : Int) {
    supportFragmentManager.beginTransaction()
            .replace(frameId, fragment)
            .commit()
}