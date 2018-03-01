package jimmysharp.kanaclogger.view.components

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import jimmysharp.kanaclogger.R
import jimmysharp.kanaclogger.view.fragment.BulkRegisterFragment
import jimmysharp.kanaclogger.view.fragment.ConstructionsFragment
import jimmysharp.kanaclogger.view.fragment.DropsFragment
import jimmysharp.kanaclogger.view.fragment.ShipListFragment

class SectionsPagerAdapter(fm: FragmentManager, resources: Resources) : FragmentPagerAdapter(fm) {
    private val title_bulk: String
    private val title_construction: String
    private val title_drop: String
    private val title_list: String

    init {
        title_bulk = resources.getString(R.string.tab_bulk)
        title_construction = resources.getString(R.string.tab_construction)
        title_drop = resources.getString(R.string.tab_drop)
        title_list = resources.getString(R.string.tab_list)
    }

    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = BulkRegisterFragment.newInstance()
            1 -> fragment = ConstructionsFragment.newInstance()
            2 -> fragment = DropsFragment.newInstance()
            3 -> fragment = ShipListFragment.newInstance()
        }
        return fragment
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return title_bulk
            1 -> return title_construction
            2 -> return title_drop
            3 -> return title_list
        }
        return null
    }
}