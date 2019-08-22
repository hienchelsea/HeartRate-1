package com.sun.heartrate.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sun.heartrate.R
import com.sun.heartrate.ui.guideline.GuidelineFragment
import com.sun.heartrate.ui.heartbeat.HeartbeatFragment
import com.sun.heartrate.ui.history.HistoryFragment
import com.sun.heartrate.utils.animator.AlphaAnimator
import com.sun.heartrate.utils.animator.CountDownAnimation
import kotlinx.android.synthetic.main.partial_splash.*
import kotlinx.android.synthetic.main.partial_tab_pager.*

class MainActivity : AppCompatActivity() {
    private val _adapter: MainPagerAdapter by lazy {
        MainPagerAdapter(supportFragmentManager).apply {
            addFragment(GuidelineFragment.newInstance(), getString(R.string.label_help))
            addFragment(HeartbeatFragment.newInstance(), getString(R.string.label_measure))
            addFragment(HistoryFragment.newInstance(), getString(R.string.label_history))
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSplashView()
        initViewPager()
    }
    
    private fun initSplashView() {
        relativeSplashView?.visibility = View.VISIBLE
        CountDownAnimation(COUNT_DOWN_INTERVAL) {
            hideSplashView(relativeSplashView)
        }
    }
    
    private fun hideSplashView(view: View) {
        AlphaAnimator(view).hide(TIME_ANIMATION_DURATION)
    }
    
    private fun initViewPager() {
        viewPagerMain?.apply {
            this.adapter = _adapter
            setCurrentItem(HEART_SCREEN_INDEX, true)
            tabLayoutMain?.setupWithViewPager(this)
        }
    }
    
    companion object {
        const val COUNT_DOWN_INTERVAL = 2000L
        const val TIME_ANIMATION_DURATION = 600L
        const val HEART_SCREEN_INDEX = 1
    }
}
