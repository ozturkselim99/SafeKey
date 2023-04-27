package com.selimozturk.safekey.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.selimozturk.safekey.databinding.ActivitySplashBinding
import com.selimozturk.safekey.domain.util.IntroManager
import com.selimozturk.safekey.presentation.onboarding.IntroActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var introManager: IntroManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            layoutSplash.alpha = 0f
            layoutSplash.animate().setDuration(750).alpha(1f).withEndAction {
                val intent = if (!introManager.isIntroSkip()) {
                    Intent(this@SplashActivity, IntroActivity::class.java)
                } else {
                    Intent(this@SplashActivity, MainActivity::class.java)
                }
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
    }

}