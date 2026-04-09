package com.movtery.zalithlauncher.ui.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

object AnimationUtils {
    private const val DEFAULT_DURATION = 300L
    private const val FAST_DURATION = 150L
    private const val SLOW_DURATION = 500L

    fun fadeIn(view: View, duration: Long = DEFAULT_DURATION, startDelay: Long = 0) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f)
            .setDuration(duration)
            .setStartDelay(startDelay)
            .setInterpolator(DecelerateInterpolatorCompat())
            .start()
    }

    fun fadeOut(view: View, duration: Long = DEFAULT_DURATION, startDelay: Long = 0, onEnd: (() -> Unit)? = null) {
        view.animate()
            .alpha(0f)
            .setDuration(duration)
            .setStartDelay(startDelay)
            .setInterpolator(AccelerateInterpolatorCompat())
            .withEndAction {
                view.visibility = View.GONE
                onEnd?.invoke()
            }
            .start()
    }

    fun fadeInOut(view: View, duration: Long = DEFAULT_DURATION) {
        fadeIn(view, duration)
        view.postDelayed({ fadeOut(view, duration) }, duration * 3)
    }

    fun slideInFromBottom(view: View, duration: Long = DEFAULT_DURATION, startDelay: Long = 0) {
        view.translationY = view.height.toFloat()
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(duration)
            .setStartDelay(startDelay)
            .setInterpolator(DecelerateInterpolatorCompat())
            .start()
    }

    fun slideOutToBottom(view: View, duration: Long = DEFAULT_DURATION, startDelay: Long = 0, onEnd: (() -> Unit)? = null) {
        view.animate()
            .translationY(view.height.toFloat())
            .alpha(0f)
            .setDuration(duration)
            .setStartDelay(startDelay)
            .setInterpolator(AccelerateInterpolatorCompat())
            .withEndAction {
                view.visibility = View.GONE
                onEnd?.invoke()
            }
            .start()
    }

    fun slideInFromTop(view: View, duration: Long = DEFAULT_DURATION, startDelay: Long = 0) {
        view.translationY = -view.height.toFloat()
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(duration)
            .setStartDelay(startDelay)
            .setInterpolator(DecelerateInterpolatorCompat())
            .start()
    }

    fun slideOutToTop(view: View, duration: Long = DEFAULT_DURATION, startDelay: Long = 0, onEnd: (() -> Unit)? = null) {
        view.animate()
            .translationY(-view.height.toFloat())
            .alpha(0f)
            .setDuration(duration)
            .setStartDelay(startDelay)
            .setInterpolator(AccelerateInterpolatorCompat())
            .withEndAction {
                view.visibility = View.GONE
                onEnd?.invoke()
            }
            .start()
    }

    fun slideInFromLeft(view: View, duration: Long = DEFAULT_DURATION, startDelay: Long = 0) {
        view.translationX = -view.width.toFloat()
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .translationX(0f)
            .alpha(1f)
            .setDuration(duration)
            .setStartDelay(startDelay)
            .setInterpolator(DecelerateInterpolatorCompat())
            .start()
    }

    fun slideOutToRight(view: View, duration: Long = DEFAULT_DURATION, startDelay: Long = 0, onEnd: (() -> Unit)? = null) {
        view.animate()
            .translationX(view.width.toFloat())
            .alpha(0f)
            .setDuration(duration)
            .setStartDelay(startDelay)
            .setInterpolator(AccelerateInterpolatorCompat())
            .withEndAction {
                view.visibility = View.GONE
                onEnd?.invoke()
            }
            .start()
    }

    fun scaleIn(view: View, duration: Long = DEFAULT_DURATION, startDelay: Long = 0) {
        view.scaleX = 0.8f
        view.scaleY = 0.8f
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(duration)
            .setStartDelay(startDelay)
            .setInterpolator(DecelerateInterpolatorCompat())
            .start()
    }

    fun scaleOut(view: View, duration: Long = DEFAULT_DURATION, startDelay: Long = 0, onEnd: (() -> Unit)? = null) {
        view.animate()
            .scaleX(0.8f)
            .scaleY(0.8f)
            .alpha(0f)
            .setDuration(duration)
            .setStartDelay(startDelay)
            .setInterpolator(AccelerateInterpolatorCompat())
            .withEndAction {
                view.visibility = View.GONE
                onEnd?.invoke()
            }
            .start()
    }

    fun pulse(view: View, duration: Long = 200L) {
        view.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(duration / 2)
            .setInterpolator(AccelerateDecelerateInterpolatorCompat())
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(duration / 2)
                    .setInterpolator(AccelerateDecelerateInterpolatorCompat())
                    .start()
            }
            .start()
    }

    fun shake(view: View, duration: Long = 100L) {
        val shakeAmount = 10f
        view.animate()
            .translationX(shakeAmount)
            .setDuration(duration)
            .withEndAction {
                view.animate()
                    .translationX(-shakeAmount)
                    .setDuration(duration)
                    .withEndAction {
                        view.animate()
                            .translationX(shakeAmount / 2)
                            .setDuration(duration)
                            .withEndAction {
                                view.animate()
                                    .translationX(0f)
                                    .setDuration(duration)
                                    .start()
                            }
                            .start()
                    }
                    .start()
            }
            .start()
    }

    fun staggerIn(views: List<View>, staggerDelay: Long = 50L, duration: Long = DEFAULT_DURATION) {
        views.forEachIndexed { index, view ->
            view.alpha = 0f
            view.translationY = 50f
            view.visibility = View.VISIBLE
            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(duration)
                .setStartDelay(index * staggerDelay)
                .setInterpolator(DecelerateInterpolatorCompat())
                .start()
        }
    }

    fun staggerOut(views: List<View>, staggerDelay: Long = 30L, duration: Long = FAST_DURATION, onEnd: (() -> Unit)? = null) {
        views.forEachIndexed { index, view ->
            view.animate()
                .alpha(0f)
                .translationY(50f)
                .setDuration(duration)
                .setStartDelay(index * staggerDelay)
                .setInterpolator(AccelerateInterpolatorCompat())
                .withEndAction {
                    view.visibility = View.GONE
                    if (index == views.lastIndex) onEnd?.invoke()
                }
                .start()
        }
    }

    fun cancelAllAnimations(view: View) {
        view.animate().cancel()
    }

    fun setEnabledAnimated(view: View, enabled: Boolean, duration: Long = FAST_DURATION) {
        view.animate()
            .alpha(if (enabled) 1f else 0.5f)
            .setDuration(duration)
            .withEndAction { view.isEnabled = enabled }
            .start()
    }
}

class SpringAnimations {
    private val stiffness = SpringForce.STIFFNESS_MEDIUM
    private val damping = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY

    fun bouncyScale(view: View, targetScale: Float = 1f) {
        SpringAnimation(view, DynamicAnimation.SCALE_X).apply {
            spring = SpringForce(targetScale).apply {
                this.stiffness = stiffness
                this.dampingRatio = damping
            }
            start()
        }
        SpringAnimation(view, DynamicAnimation.SCALE_Y).apply {
            spring = SpringForce(targetScale).apply {
                this.stiffness = stiffness
                this.dampingRatio = damping
            }
            start()
        }
    }

    fun springTranslationX(view: View, targetX: Float) {
        SpringAnimation(view, DynamicAnimation.TRANSLATION_X).apply {
            spring = SpringForce(targetX).apply {
                this.stiffness = stiffness
                this.dampingRatio = damping
            }
            start()
        }
    }

    fun springTranslationY(view: View, targetY: Float) {
        SpringAnimation(view, DynamicAnimation.TRANSLATION_Y).apply {
            spring = SpringForce(targetY).apply {
                this.stiffness = stiffness
                this.dampingRatio = damping
            }
            start()
        }
    }

    fun springAlpha(view: View, targetAlpha: Float) {
        SpringAnimation(view, DynamicAnimation.ALPHA).apply {
            spring = SpringForce(targetAlpha).apply {
                this.stiffness = SpringForce.STIFFNESS_LOW
                this.dampingRatio = damping
            }
            start()
        }
    }
}

private class DecelerateInterpolatorCompat : android.view.animation.DecelerateInterpolator()
private class AccelerateInterpolatorCompat : android.view.animation.AccelerateInterpolator()
private class AccelerateDecelerateInterpolatorCompat : android.view.animation.AccelerateDecelerateInterpolator()