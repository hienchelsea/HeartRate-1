package com.sun.heartrate.utils.animator

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import com.sun.heartrate.utils.gone

class AlphaAnimator(
    private val view: View
) : BaseCreative() {
    
    fun hide(duration: Long) {
        addAnimator(ObjectAnimator.ofFloat(view, ALPHA, START_ALPHA, END_ALPHA))
        setDuration(duration)
        addListener(AlphaAnimatorListener {
            view.gone()
        })
        startAnimationTogether()
    }
    
    companion object {
        private const val ALPHA = "alpha"
        private const val START_ALPHA = 1F
        private const val END_ALPHA = 0F
    }
}

class AlphaAnimatorListener(
    private val onAnimationFinished: () -> Unit
) : Animator.AnimatorListener {
    override fun onAnimationRepeat(p0: Animator?) {
    }
    
    override fun onAnimationEnd(p0: Animator?) {
        onAnimationFinished()
    }
    
    override fun onAnimationCancel(p0: Animator?) {
    }
    
    override fun onAnimationStart(p0: Animator?) {
    }
}
