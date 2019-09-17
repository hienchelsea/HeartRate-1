package com.sun.heartrate.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.sun.heartrate.R
import com.sun.heartrate.ui.guideline.GuidelineFragment
import com.sun.heartrate.ui.heartbeat.HeartbeatFragment
import com.sun.heartrate.ui.history.HistoryFragment
import com.sun.heartrate.utils.animator.AlphaAnimator
import com.sun.heartrate.utils.animator.CountDownAnimation
import com.sun.heartrate.utils.assignViews
import com.sun.heartrate.utils.gone
import com.sun.heartrate.utils.show
import kotlinx.android.synthetic.main.partial_main.*
import kotlinx.android.synthetic.main.partial_splash.*
import kotlinx.android.synthetic.main.partial_tab_pager.*

class MainActivity : AppCompatActivity(),
    MainPagerAdapter.OnLoadCallback,
    View.OnClickListener,
    OptionalHistoryMenu.MenuOptionCallback {
    
    private val _adapter: MainPagerAdapter by lazy {
        MainPagerAdapter(supportFragmentManager, imageViewOptionHistory, this).apply {
            addFragment(GuidelineFragment.newInstance(), getString(R.string.label_help))
            addFragment(HeartbeatFragment.newInstance(this), getString(R.string.label_measure))
            addFragment(HistoryFragment.newInstance(), getString(R.string.label_history))
        }
    }
    
    private var onMenuOptionCallBack: OnMenuOptionCallBack? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSplashView()
        initViewPager()
        initListener()
    }
    
    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is OnMenuOptionCallBack) {
            onMenuOptionCallBack = fragment
        }
    }
    
    private fun initListener() {
        assignViews(imageViewOptionHistory,imageViewOption,textViewLanguage)
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
        viewPagerMain?.addOnPageChangeListener(OnPageChangeListener {
            displayImageViewOption(imageViewOptionHistory)
        })
    }
    
    override fun nextSaveHeartFragment(fragment: Fragment) {
        nextFragment(fragment, R.id.relativeMain)
    }
    
    override fun backFragment() {
        onBackPressed()
    }
    
    private fun displayImageViewOption(view: View) {
        if (viewPagerMain.currentItem == HISTORY_SCREEN_INDEX) view.show()
        else view.gone()
    }
    
    private fun nextFragment(fragment: Fragment, id: Int) {
        val backStateName = MainActivity::class.java.canonicalName as String
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(0, 0, 0, 0)
        transaction.replace(id, fragment)
        transaction.addToBackStack(backStateName)
        transaction.commit()
    }
    
    override fun loadMenuOptionCallback(value: String) {
        onMenuOptionCallBack?.menuOptionCallBack(value)
    }
    
    @SuppressLint("RtlHardcoded")
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imageViewOptionHistory -> OptionalHistoryMenu(
                this
            ).optionalHistoryMenu(this, imageViewOptionHistory)
            R.id.imageViewOption -> {
                drawerLayout.openDrawer(Gravity.LEFT)
            }
            R.id.textViewLanguage-> LanguageDialog().languageDialog(this)
        }
    }
    
    companion object {
        const val COUNT_DOWN_INTERVAL = 2000L
        const val TIME_ANIMATION_DURATION = 600L
        const val HEART_SCREEN_INDEX = 1
        const val HISTORY_SCREEN_INDEX = 2
    }
    
    interface OnMenuOptionCallBack {
        fun menuOptionCallBack(value: String)
    }
}
