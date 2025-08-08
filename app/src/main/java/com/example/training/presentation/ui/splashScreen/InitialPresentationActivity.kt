package com.example.training.presentation.ui.splashScreen;

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler
import android.os.Looper
import com.example.training.presentation.ui.login.LoginActivity;
import com.example.treinoacademia.databinding.ActivityInitialPresentationBinding;
import androidx.core.animation.doOnEnd

class InitialPresentationActivity : AppCompatActivity() {
    private val binding by lazy { ActivityInitialPresentationBinding.inflate(layoutInflater) };

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(binding.root);

        setupSplashScreen();
    }

    private fun setupSplashScreen() {
        val rotationAnimation = ObjectAnimator.ofFloat(binding.splashLogoImageView, "rotation", 0f, 360f).apply {
            duration = 1000
        }

        rotationAnimation.doOnEnd {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@InitialPresentationActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 500)
        }
        rotationAnimation.start()
    }
}