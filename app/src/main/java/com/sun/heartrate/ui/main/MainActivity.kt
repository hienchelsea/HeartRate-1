package com.sun.heartrate.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sun.heartrate.R
import com.sun.heartrate.ui.guideline.GuidelineFragment
import com.sun.heartrate.ui.heartbeat.HeartbeatFragment
import com.sun.heartrate.ui.history.HistoryFragment
import com.sun.heartrate.utils.animator.AlphaAnimator
import com.sun.heartrate.utils.animator.CountDownAnimation
import kotlinx.android.synthetic.main.partial_splash.*
import kotlinx.android.synthetic.main.partial_tab_pager.*

class MainActivity : AppCompatActivity(),
    MainPagerAdapter.OnLoadSaveHeartFragment {
    
    private val _adapter: MainPagerAdapter by lazy {
        MainPagerAdapter(supportFragmentManager, this).apply {
            addFragment(GuidelineFragment.newInstance(), getString(R.string.label_help))
            addFragment(HeartbeatFragment.newInstance(this), getString(R.string.label_measure))
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
        }.start()
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
    
    override fun nextSaveHeartFragment(fragment: Fragment) {
        nextFragment(fragment, R.id.relativeMain)
    }
    
    override fun backFragment() {
        onBackPressed()
    }
    
    private fun nextFragment(fragment: Fragment, id: Int) {
        
        val backStateName = MainActivity::class.java.canonicalName as String
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(0, 0, 0, 0)
        transaction.replace(id, fragment)
        transaction.addToBackStack(backStateName)
        transaction.commit()
    }
    
    companion object {
        const val COUNT_DOWN_INTERVAL = 2000L
        const val TIME_ANIMATION_DURATION = 600L
        const val HEART_SCREEN_INDEX = 1
    }
}
