package com.sun.heartrate.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sun.heartrate.R
import com.sun.heartrate.data.database.HeartDatabase
import com.sun.heartrate.data.database.SharedPreferencesUtils
import com.sun.heartrate.data.repository.HeartRepository
import com.sun.heartrate.data.source.HeartLocalDataSource
import com.sun.heartrate.ui.guideline.GuidelineFragment
import com.sun.heartrate.ui.heartbeat.HeartbeatFragment
import com.sun.heartrate.ui.history.HistoryFragment
import com.sun.heartrate.utils.*
import com.sun.heartrate.utils.animator.AlphaAnimator
import com.sun.heartrate.utils.animator.CountDownAnimation
import kotlinx.android.synthetic.main.partial_main.*
import kotlinx.android.synthetic.main.partial_splash.*
import kotlinx.android.synthetic.main.partial_tab_pager.*
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(),
    MainPagerAdapter.OnLoadCallback,
    View.OnClickListener,
    OptionalHistoryMenu.MenuOptionCallback,
    LanguageDialog.LanguageDialogCallback,
    TimePicker.TimePickerDialogCallback,
    MainContract.View {
    
    private val _adapter: MainPagerAdapter by lazy {
        MainPagerAdapter(supportFragmentManager, imageViewOptionHistory, this).apply {
            addFragment(GuidelineFragment.newInstance(), getString(R.string.label_help))
            addFragment(HeartbeatFragment.newInstance(this), getString(R.string.label_measure))
            addFragment(HistoryFragment.newInstance(), getString(R.string.label_history))
        }
    }
    
    private val heartDatabase: HeartDatabase by lazy {
        HeartDatabase(applicationContext)
    }
    
    private val heartLocalDataSource: HeartLocalDataSource by lazy {
        HeartLocalDataSource(heartDatabase)
    }
    
    private val heartRepository: HeartRepository by lazy {
        HeartRepository(heartLocalDataSource)
    }
    private val sharedPreferencesUtils: SharedPreferencesUtils by lazy {
        SharedPreferencesUtils(applicationContext)
    }
    private val mainPresenter: MainContract.Presenter by lazy {
        MainPresenter(heartRepository, sharedPreferencesUtils, this)
    }
    private var onMenuOptionCallBack: OnMenuOptionCallBack? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setLanguage(mainPresenter.getLanguage(Constant.LANGUAGE))
        initSplashView()
        setTimeNotification()
        initCheckBoxView()
        initViewPager()
        initListener()
    }
    
    private var hour = 0
    private var minute = 0
    
    @SuppressLint("ResourceAsColor")
    private fun initCheckBoxView() {
        checkBoxNotification.isChecked = mainPresenter.getNotification(
            Constant.CHECK_NOTIFICATION
        ) == true
    }
    
    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is OnMenuOptionCallBack) {
            onMenuOptionCallBack = fragment
        }
    }
    
    private fun initListener() {
        assignViews(
            imageViewOptionHistory,
            imageViewOption,
            textViewLanguage,
            imageViewSelectTime,
            checkBoxNotification
        )
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
    
    @SuppressLint("ObsoleteSdkInt")
    override fun onChangeLanguage(language: String) {
        mainPresenter.setLanguage(Constant.LANGUAGE, language)
        resetActivity()
        
    }
    
    override fun onChangeTimePicker(hourOfDay: Int, minute: Int) {
        mainPresenter.setLanguage(Constant.HOUR_NOTIFICATION, hourOfDay.toString())
        mainPresenter.setLanguage(Constant.MINUTE_NOTIFICATION, minute.toString())
        createNotification()
    }
    
    private fun resetActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun setLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
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
            R.id.imageViewOption -> drawerLayout.openDrawer(Gravity.LEFT)
            R.id.textViewLanguage -> LanguageDialog(
                this
            ).languageDialog(this)
            R.id.imageViewSelectTime -> TimePicker(
                this
            ).clickTimePicker(this)
            R.id.checkBoxNotification -> {
                mainPresenter.setNotification(
                    Constant.CHECK_NOTIFICATION,
                    checkBoxNotification.isChecked
                )
                createNotification()
            }
        }
    }
    
    private fun setTimeNotification() {
        if (mainPresenter.getLanguage(Constant.HOUR_NOTIFICATION) == "en") {
            hour = 8
            minute = 0
        } else {
            hour = mainPresenter.getLanguage(Constant.HOUR_NOTIFICATION).toInt()
            minute = mainPresenter.getLanguage(Constant.MINUTE_NOTIFICATION).toInt()
        }
        textViewTimeSelect.text = hour.formatDecimal()+" : "+minute.formatDecimal()
    }
    private fun createNotification(){
        setTimeNotification()
        if (checkBoxNotification.isChecked) {
            val calendarCurrent = Calendar.getInstance()
            calendarCurrent.apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
            AlarmUtils.cancelAlarm(this, Constant.REQUEST_CODE)
            AlarmUtils.setTurnOnRepeat(this, Constant.REQUEST_CODE, calendarCurrent.timeInMillis)
        } else {
            AlarmUtils.cancelAlarm(this, Constant.REQUEST_CODE)
        }
    }
    
    companion object {
        const val COUNT_DOWN_INTERVAL = 2000L
        const val TIME_ANIMATION_DURATION = 600L
        const val HEART_SCREEN_INDEX = 1
        const val HISTORY_SCREEN_INDEX = 2
        
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
    
    interface OnMenuOptionCallBack {
        fun menuOptionCallBack(value: String)
    }
}
