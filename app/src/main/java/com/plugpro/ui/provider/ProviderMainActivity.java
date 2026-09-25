package com.plugpro.ui.provider;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.plugpro.R;
import com.plugpro.ui.customer.ChatListFragment;
import com.plugpro.ui.customer.CustomerProfileFragment;

public class ProviderMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_main);

        bottomNav = findViewById(R.id.providerBottomNav);

        if (savedInstanceState == null) {
            loadFragment(new ProviderDashboardFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment selectedFragment = null;

            if (id == R.id.nav_provider_dashboard) {
                selectedFragment = new ProviderDashboardFragment();
            } else if (id == R.id.nav_provider_bookings) {
                selectedFragment = new ProviderBookingsFragment();
            } else if (id == R.id.nav_provider_chat) {
                selectedFragment = new ChatListFragment();
            } else if (id == R.id.nav_provider_profile) {
                selectedFragment = new CustomerProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.providerFragmentContainer, fragment)
                .commit();
    }
}
