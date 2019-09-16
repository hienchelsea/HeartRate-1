package com.sun.heartrate.ui.main

import androidx.viewpager.widget.ViewPager

class OnPageChangeListener(
    private val onPageSelected: () -> Unit
) : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun onPageSelected(position: Int) {
        onPageSelected()
    }
}
