package com.sun.heartrate.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sun.heartrate.ui.heartbeat.HeartbeatFragment

class MainPagerAdapter(
    fragmentManager: FragmentManager,
    private val onLoadSaveHeartFragment: OnLoadSaveHeartFragment
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT),
    HeartbeatFragment.OnLoadFragment {
    
    private val fragments = mutableListOf<Fragment>()
    private val titles = mutableListOf<String>()
    
    override fun getItem(position: Int) = fragments[position]
    
    override fun getCount() = fragments.size
    
    override fun getPageTitle(position: Int) = titles[position]
    
    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }
    
    override fun nextFragment(fragment: Fragment) {
        onLoadSaveHeartFragment.nextSaveHeartFragment(fragment)
    }
    
    override fun backFragment() {
        onLoadSaveHeartFragment.backFragment()
    }
    
    interface OnLoadSaveHeartFragment {
        fun nextSaveHeartFragment(fragment: Fragment)
        fun backFragment()
    }
    
}
