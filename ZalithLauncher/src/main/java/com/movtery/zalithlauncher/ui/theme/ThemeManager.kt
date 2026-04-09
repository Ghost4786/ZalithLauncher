package com.movtery.zalithlauncher.ui.theme

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.movtery.zalithlauncher.setting.AllSettings
import com.movtery.zalithlauncher.setting.Settings
import net.kdt.pojavlaunch.Tools

class ThemeManager(private val context: Context) : LifecycleEventObserver {

    enum class ThemeMode {
        LIGHT, DARK, SYSTEM
    }

    private var currentMode: ThemeMode = ThemeMode.SYSTEM
    private var isAnimating = false

    fun initialize() {
        val nightMode = AllSettings.launcher_nightMode.get()
        currentMode = when (nightMode) {
            0 -> ThemeMode.SYSTEM
            1 -> ThemeMode.LIGHT
            2 -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
        applyTheme(currentMode)
    }

    fun setThemeMode(mode: ThemeMode, animate: Boolean = true) {
        if (currentMode == mode && !animate) return

        if (animate && !isAnimating) {
            animateThemeChange(mode)
        } else {
            applyTheme(mode)
        }
    }

    fun toggleTheme(animate: Boolean = true) {
        val newMode = when (currentMode) {
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.LIGHT
            ThemeMode.SYSTEM -> if (isDarkMode()) ThemeMode.LIGHT else ThemeMode.DARK
        }
        setThemeMode(newMode, animate)
    }

    private fun applyTheme(mode: ThemeMode) {
        currentMode = mode
        val nightMode = when (mode) {
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
        saveThemePreference(mode)
    }

    private fun animateThemeChange(targetMode: ThemeMode) {
        isAnimating = true
        val window = getWindow() ?: run {
            applyTheme(targetMode)
            return
        }

        val view = window.decorView
        val currentColor = getBackgroundColor(view)
        val targetColor = getTargetColor(targetMode)

        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = THEME_ANIMATION_DURATION
            addUpdateListener { animation ->
                val fraction = animation.animatedValue as Float
                val interpolatedColor = ColorUtils.blendARGB(currentColor, targetColor, fraction)
                view.setBackgroundColor(interpolatedColor)
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    isAnimating = false
                    applyTheme(targetMode)
                }
            })
        }
        animator.start()
    }

    private fun getWindow(): Window? {
        return try {
            val activity = context as? android.app.Activity
            activity?.window
        } catch (e: Exception) {
            null
        }
    }

    private fun getBackgroundColor(view: View): Int {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                view.background?.TintList?.defaultColor ?: 0xFFF5FBFA.toInt()
            } else {
                0xFFF5FBFA.toInt()
            }
        } catch (e: Exception) {
            0xFFF5FBFA.toInt()
        }
    }

    private fun getTargetColor(mode: ThemeMode): Int {
        return when (mode) {
            ThemeMode.LIGHT -> 0xFFF5FBFA.toInt()
            ThemeMode.DARK -> 0xFF0D1413.toInt()
            ThemeMode.SYSTEM -> if (isDarkMode()) 0xFF0D1413.toInt() else 0xFFF5FBFA.toInt()
        }
    }

    fun isDarkMode(): Boolean {
        return when (currentMode) {
            ThemeMode.DARK -> true
            ThemeMode.LIGHT -> false
            ThemeMode.SYSTEM -> {
                val nightModeFlags = context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
                nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES
            }
        }
    }

    private fun saveThemePreference(mode: ThemeMode) {
        val nightModeValue = when (mode) {
            ThemeMode.LIGHT -> 1
            ThemeMode.DARK -> 2
            ThemeMode.SYSTEM -> 0
        }
        try {
            AllSettings.launcher_nightMode.set(nightModeValue)
        } catch (e: Exception) {
            Tools.showToast(context, "Failed to save theme preference")
        }
    }

    override fun onStateChanged(source: androidx.lifecycle.LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                // Check for system theme changes when resuming
                if (currentMode == ThemeMode.SYSTEM) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
            else -> {}
        }
    }

    companion object {
        private const val THEME_ANIMATION_DURATION = 300L

        @Volatile
        private var instance: ThemeManager? = null

        fun getInstance(context: Context): ThemeManager {
            return instance ?: synchronized(this) {
                instance ?: ThemeManager(context.applicationContext).also { instance = it }
            }
        }
    }
}