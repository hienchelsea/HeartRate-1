package com.sun.heartrate.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class MainPagerAdapter(
    fragmentManager: FragmentManager?
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    
    private val fragments = mutableListOf<Fragment>()
    private val titles = mutableListOf<String>()
    
    override fun getItem(position: Int) = fragments[position]
    
    override fun getCount() = fragments.size
    
    override fun getPageTitle(position: Int) = titles[position]
    
    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }
    
}
