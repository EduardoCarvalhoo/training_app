package com.example.training.presentation.ui.splashScreen;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.example.training.presentation.ui.login.LoginActivity;
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
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(InitialPresentationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}