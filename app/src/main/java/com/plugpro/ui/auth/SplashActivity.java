package com.plugpro.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.plugpro.R;
import com.plugpro.data.repository.AuthRepository;
import com.plugpro.ui.customer.MainActivity;
import com.plugpro.ui.provider.ProviderMainActivity;
import com.plugpro.utils.FirebaseUtil;
import com.plugpro.utils.PreferenceHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(this::checkNavigation, 1500);
    }

    private void checkNavigation() {
        PreferenceHelper prefs = new PreferenceHelper(this);

        if (!prefs.isOnboardingCompleted()) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
            return;
        }

        if (FirebaseUtil.isLoggedIn()) {
            String uid = FirebaseUtil.getCurrentUserId();
            new AuthRepository().fetchUserProfile(uid, new AuthRepository.AuthCallback() {
                @Override
                public void onSuccess(com.plugpro.data.model.User user) {
                    prefs.setUserRole(user.getRole());
                    prefs.setUserName(user.getName());
                    if (user.isProvider()) {
                        startActivity(new Intent(SplashActivity.this, ProviderMainActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                    finish();
                }

                @Override
                public void onError(String message) {
                    // Fallback to customer home if profile fetch fails
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            });
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
