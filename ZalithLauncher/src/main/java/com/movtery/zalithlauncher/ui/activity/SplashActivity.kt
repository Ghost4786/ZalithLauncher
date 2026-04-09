package com.movtery.zalithlauncher.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.movtery.zalithlauncher.InfoCenter
import com.movtery.zalithlauncher.InfoDistributor
import com.movtery.zalithlauncher.R
import com.movtery.zalithlauncher.databinding.ActivitySplashBinding
import com.movtery.zalithlauncher.feature.unpack.Components
import com.movtery.zalithlauncher.feature.unpack.Jre
import com.movtery.zalithlauncher.feature.unpack.UnpackComponentsTask
import com.movtery.zalithlauncher.feature.unpack.UnpackJreTask
import com.movtery.zalithlauncher.feature.unpack.UnpackSingleFilesTask
import com.movtery.zalithlauncher.task.Task
import com.movtery.zalithlauncher.ui.animation.AnimationUtils as UIAnimUtils
import com.movtery.zalithlauncher.ui.dialog.TipDialog
import com.movtery.zalithlauncher.ui.theme.ThemeManager
import com.movtery.zalithlauncher.utils.StoragePermissionsUtils
import net.kdt.pojavlaunch.LauncherActivity
import net.kdt.pojavlaunch.MissingStorageActivity
import net.kdt.pojavlaunch.Tools

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private var isStarted: Boolean = false
    private lateinit var binding: ActivitySplashBinding
    private lateinit var installableAdapter: InstallableAdapter
    private val items: MutableList<InstallableItem> = ArrayList()
    private lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeManager = ThemeManager.getInstance(this)
        themeManager.initialize()

        initItems()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        startEntranceAnimations()

        if (!Tools.checkStorageRoot()) {
            startActivity(Intent(this, MissingStorageActivity::class.java))
            finish()
            return
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P && !StoragePermissionsUtils.hasStoragePermissions(this)) {
            TipDialog.Builder(this)
                .setTitle(R.string.generic_warning)
                .setMessage(InfoCenter.replaceName(this, R.string.permissions_write_external_storage))
                .setWarning()
                .setConfirmClickListener { requestStoragePermissions() }
                .setCancelClickListener { checkEnd() }
                .showDialog()
        } else {
            checkEnd()
        }
    }

    private fun setupUI() {
        binding.toolbar.title = InfoDistributor.APP_NAME
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SplashActivity)
            adapter = installableAdapter
        }

        binding.startButton.apply {
            setOnClickListener {
                if (isStarted) return@setOnClickListener
                isStarted = true
                binding.splashText.text = getString(R.string.splash_screen_installing)
                UIAnimUtils.fadeIn(binding.splashText)
                installableAdapter.startAllTasks()
            }
            isClickable = false
            alpha = 0f
        }
    }

    private fun startEntranceAnimations() {
        binding.toolbar.apply {
            alpha = 0f
            translationY = -height.toFloat()
        }
        
        binding.recyclerView.apply {
            alpha = 0f
            translationX = -width.toFloat()
        }
        
        binding.operateLayout?.let { layout ->
            layout.apply {
                alpha = 0f
                translationX = width.toFloat()
            }
        }

        binding.toolbar.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(400)
            .setStartDelay(200)
            .withEndAction {
                binding.recyclerView.animate()
                    .alpha(1f)
                    .translationX(0f)
                    .setDuration(350)
                    .start()
            }
            .start()

        binding.operateLayout?.let { layout ->
            layout.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(350)
                .setStartDelay(100)
                .start()
        }

        binding.startButton.animate()
            .alpha(1f)
            .setDuration(300)
            .setStartDelay(600)
            .start()
    }

    private fun requestStoragePermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            checkEnd()
        }
    }

    private fun initItems() {
        Components.entries.forEach {
            val unpackComponentsTask = UnpackComponentsTask(this, it)
            if (!unpackComponentsTask.isCheckFailed()) {
                items.add(
                    InstallableItem(
                        it.displayName,
                        it.summary?.let { it1 -> getString(it1) },
                        unpackComponentsTask
                    )
                )
            }
        }
        Jre.entries.forEach {
            val unpackJreTask = UnpackJreTask(this, it)
            if (!unpackJreTask.isCheckFailed()) {
                items.add(
                    InstallableItem(
                        it.jreName,
                        getString(it.summary),
                        unpackJreTask
                    )
                )
            }
        }
        items.sort()
        installableAdapter = InstallableAdapter(items) {
            toMain()
        }
    }
    
    private fun checkEnd() {
        installableAdapter.checkAllTask()
        Task.runTask {
            UnpackSingleFilesTask(this).run()
        }.execute()

        binding.startButton.apply {
            isClickable = true
            animate()
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setDuration(100)
                .withEndAction {
                    animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()
        }
    }

    private fun toMain() {
        val intent = Intent(this, LauncherActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    companion object {
        private const val STORAGE_PERMISSION_REQUEST_CODE: Int = 100
    }
}