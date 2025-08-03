package com.example.training.presentation.ui.splashScreen;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.example.training.presentation.ui.login.LoginActivity;
import com.example.treinoacademia.R;
import com.example.treinoacademia.databinding.ActivityInitialPresentationBinding;

public class InitialPresentationActivity extends AppCompatActivity {
    private ActivityInitialPresentationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInitialPresentationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupSplashScreen();
    }

    public void setupSplashScreen() {
        // Carrega a animação de rotação
        Animation rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_rotation);
        
        // Define um listener para quando a animação terminar
        rotationAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animação iniciou
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Quando a animação terminar, aguarda um pouco e navega para o login
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    Intent intent = new Intent(InitialPresentationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }, 500); // Aguarda 500ms após a animação terminar
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animação repetiu
            }
        });
        
        // Inicia a animação no logo
        binding.splashLogoImageView.startAnimation(rotationAnimation);
    }
}