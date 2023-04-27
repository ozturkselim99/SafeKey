package com.selimozturk.safekey.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.selimozturk.safekey.databinding.ActivityIntroBinding
import com.selimozturk.safekey.domain.util.IntroManager
import com.selimozturk.safekey.presentation.MainActivity
import com.selimozturk.safekey.presentation.onboarding.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    @Inject
    lateinit var introManager: IntroManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        setupViewPager()
        setupListeners()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = ViewPagerAdapter()
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ ->
        }.attach()
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == MAX_STEP - 1) {
                    binding.btnNext.text = "Get Started"
                    binding.btnNext.contentDescription = "Get Started"
                } else {
                    binding.btnNext.text = "Next"
                    binding.btnNext.contentDescription = "Next"
                }
            }
        })
    }

    private fun setupListeners() {
        binding.btnNext.setOnClickListener {
            if (binding.btnNext.text.toString() == "Get Started") {
                navigateToMainActivity()
            } else {
                val current = (binding.viewPager.currentItem) + 1
                binding.viewPager.currentItem = current

                if (current >= MAX_STEP - 1) {
                    binding.btnNext.text = "Get Started"
                    binding.btnNext.contentDescription = "Get Started"

                } else {
                    binding.btnNext.text = "Next"
                    binding.btnNext.contentDescription = "Next"
                }
            }
        }

        binding.btnSkip.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        introManager.setIntro(true)
        startActivity(
            Intent(this@IntroActivity, MainActivity::class.java)
        )
        finish()
    }

    companion object {
        const val MAX_STEP = 3
    }
}