package com.movtery.zalithlauncher.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.movtery.anim.AnimPlayer
import com.movtery.anim.animations.Animations
import com.movtery.zalithlauncher.R
import com.movtery.zalithlauncher.databinding.FragmentAboutBinding
import com.movtery.zalithlauncher.ui.fragment.about.AboutInfoPageFragment
import com.movtery.zalithlauncher.utils.ZHTools
import com.movtery.zalithlauncher.utils.stringutils.StringUtils

class AboutFragment : FragmentWithAnim(R.layout.fragment_about) {
    companion object {
        const val TAG: String = "AboutFragment"
    }

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initInfoDisplay()

        binding.returnButton.setOnClickListener { ZHTools.onBackPressed(requireActivity()) }
    }

    private fun initInfoDisplay() {
        binding.apply {
            appInfo.text = StringUtils.insertNewline(StringUtils.insertSpace(getString(R.string.about_version_name), ZHTools.getVersionName()),
                StringUtils.insertSpace(getString(R.string.about_version_code), ZHTools.getVersionCode()),
                StringUtils.insertSpace(getString(R.string.about_last_update_time), ZHTools.getLastUpdateTime(requireContext())),
                StringUtils.insertSpace(getString(R.string.about_version_status), ZHTools.getVersionStatus(requireContext())))
            appInfo.setOnClickListener{ StringUtils.copyText("text", appInfo.text.toString(), requireContext()) }
            
            supportDevelopment.visibility = View.GONE
        }
    }

    override fun slideIn(animPlayer: AnimPlayer) {
        animPlayer.apply(AnimPlayer.Entry(binding.appInfo, Animations.BounceInDown))
            .apply(AnimPlayer.Entry(binding.returnButton, Animations.BounceInLeft))
    }

    override fun slideOut(animPlayer: AnimPlayer) {
        animPlayer.apply(AnimPlayer.Entry(binding.appInfo, Animations.FadeOutUp))
        animPlayer.apply(AnimPlayer.Entry(binding.returnButton, Animations.FadeOutRight))
    }
}

