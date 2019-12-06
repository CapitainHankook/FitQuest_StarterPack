package ru.tzhack.facegame.facetraking.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Метод для анимирования View.
 *
 * @param actionAfterFadeOut действие, которое необходимо выполнить после анимации.
 * */
fun View.fadeInOutAnim(actionAfterFadeOut: () -> Unit) {
    val animDuration = 400L
    val fadeInAnim = ValueAnimator.ofFloat(0F, 1F).apply {
        duration = animDuration
        addUpdateListener { value -> alpha = value.animatedValue as Float }
        interpolator = LinearInterpolator()

        repeatCount = 1
        repeatMode = ValueAnimator.REVERSE
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                actionAfterFadeOut()
            }
        })
    }

    fadeInAnim.start()
}

/**
 * Метод для анимирования View.
 * */
fun View.fadeOutInAnim() {
    val animDuration = 400L
    val fadeInAnim = ValueAnimator.ofFloat(1F, 0F).apply {
        duration = animDuration
        addUpdateListener { value -> alpha = value.animatedValue as Float }
        interpolator = LinearInterpolator()

        repeatCount = 1
        repeatMode = ValueAnimator.REVERSE
    }

    fadeInAnim.start()
}