package com.sun.heartrate.utils.animator

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator

open class BaseCreative {
    
    private val animatorSet by lazyOf(AnimatorSet())
    private val animators by lazyOf(mutableListOf<Animator>())
    
    fun startAnimationTogether() {
        animatorSet.apply {
            playTogether(animators)
            start()
        }
    }
    
    fun addListener(animatorListener: Animator.AnimatorListener) {
        animatorSet.addListener(animatorListener)
    }
    
    fun setDuration(time: Long) {
        animatorSet.duration = time
    }
    
    fun addAnimator(animator: ObjectAnimator) {
        animators.add(animator)
    }
}
