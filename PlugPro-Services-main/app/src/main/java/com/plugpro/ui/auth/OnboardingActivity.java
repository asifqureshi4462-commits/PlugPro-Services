package com.plugpro.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.plugpro.R;
import com.plugpro.ui.adapters.OnboardingAdapter;
import com.plugpro.utils.PreferenceHelper;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button btnNext;
    private TextView btnSkip;
    private LinearLayout layoutDots;
    private PreferenceHelper prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        prefs = new PreferenceHelper(this);

        viewPager = findViewById(R.id.viewPagerOnboarding);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);
        layoutDots = findViewById(R.id.layoutDots);

        OnboardingAdapter adapter = new OnboardingAdapter();
        viewPager.setAdapter(adapter);

        setupIndicators(adapter.getItemCount());
        setCurrentIndicator(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
                if (position == adapter.getItemCount() - 1) {
                    btnNext.setText(R.string.get_started);
                    btnSkip.setVisibility(View.GONE);
                } else {
                    btnNext.setText(R.string.next);
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(current + 1);
            } else {
                finishOnboarding();
            }
        });

        btnSkip.setOnClickListener(v -> finishOnboarding());
    }

    private void setupIndicators(int count) {
        layoutDots.removeAllViews();
        for (int i = 0; i < count; i++) {
            View dot = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(24, 8);
            params.setMargins(6, 0, 6, 0);
            dot.setLayoutParams(params);
            dot.setBackgroundResource(R.drawable.bg_chip_unselected);
            layoutDots.addView(dot);
        }
    }

    private void setCurrentIndicator(int index) {
        int childCount = layoutDots.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View dot = layoutDots.getChildAt(i);
            if (i == index) {
                dot.setBackgroundResource(R.drawable.bg_chip_selected);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(36, 8);
                params.setMargins(6, 0, 6, 0);
                dot.setLayoutParams(params);
            } else {
                dot.setBackgroundResource(R.drawable.bg_chip_unselected);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 8);
                params.setMargins(6, 0, 6, 0);
                dot.setLayoutParams(params);
            }
        }
    }

    private void finishOnboarding() {
        prefs.setOnboardingCompleted(true);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
