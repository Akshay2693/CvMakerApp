package com.blackbox.onepage.cvmaker.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import com.blackbox.onepage.cvmaker.ui.fragments.BasicFragment
import com.blackbox.onepage.cvmaker.ui.fragments.ContactFragment

/**
 * Created by umair on 30/05/2017.
 */

class EmptyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Fragment {

        var fragment: Fragment? = null
        when (position) {

            0 -> fragment = BasicFragment()

            1 -> fragment = ContactFragment()

            else -> fragment = BasicFragment()
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Page " + position
    }

}
