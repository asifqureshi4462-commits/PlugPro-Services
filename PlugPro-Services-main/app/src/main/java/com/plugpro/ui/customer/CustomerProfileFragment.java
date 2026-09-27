package com.plugpro.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.plugpro.R;
import com.plugpro.data.model.User;
import com.plugpro.data.repository.AuthRepository;
import com.plugpro.ui.auth.LoginActivity;
import com.plugpro.ui.favorites.FavoritesActivity;
import com.plugpro.ui.notifications.NotificationsActivity;
import com.plugpro.ui.provider.ProviderMainActivity;
import com.plugpro.ui.provider.ProviderRegistrationActivity;
import com.plugpro.utils.FirebaseUtil;
import com.plugpro.utils.PreferenceHelper;

public class CustomerProfileFragment extends Fragment {

    private TextView tvName, tvEmail, tvPhone;
    private ImageView ivAvatar;
    private View menuFavorites, menuNotifications, menuSwitchProvider;
    private Button btnLogout;

    private AuthRepository authRepository;
    private PreferenceHelper prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_profile, container, false);

        authRepository = new AuthRepository();
        prefs = new PreferenceHelper(requireContext());

        tvName = view.findViewById(R.id.tvProfileName);
        tvEmail = view.findViewById(R.id.tvProfileEmail);
        tvPhone = view.findViewById(R.id.tvProfilePhone);
        ivAvatar = view.findViewById(R.id.ivProfileAvatar);
        menuFavorites = view.findViewById(R.id.menuFavorites);
        menuNotifications = view.findViewById(R.id.menuNotifications);
        menuSwitchProvider = view.findViewById(R.id.menuSwitchProvider);
        btnLogout = view.findViewById(R.id.btnLogout);

        loadUserData();

        menuFavorites.setOnClickListener(v -> startActivity(new Intent(getContext(), FavoritesActivity.class)));
        menuNotifications.setOnClickListener(v -> startActivity(new Intent(getContext(), NotificationsActivity.class)));

        menuSwitchProvider.setOnClickListener(v -> {
            String role = prefs.getUserRole();
            if ("provider".equalsIgnoreCase(role)) {
                startActivity(new Intent(getContext(), ProviderMainActivity.class));
            } else {
                startActivity(new Intent(getContext(), ProviderRegistrationActivity.class));
            }
        });

        btnLogout.setOnClickListener(v -> {
            authRepository.logout();
            prefs.clear();
            prefs.setOnboardingCompleted(true);
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    private void loadUserData() {
        String uid = FirebaseUtil.getCurrentUserId();
        if (uid.isEmpty()) return;

        authRepository.fetchUserProfile(uid, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                if (isAdded()) {
                    tvName.setText(user.getName());
                    tvEmail.setText(user.getEmail());
                    tvPhone.setText(user.getPhone().isEmpty() ? "No phone added" : user.getPhone());
                    prefs.setUserName(user.getName());
                    prefs.setUserRole(user.getRole());
                }
            }

            @Override
            public void onError(String message) {
                // Keep default placeholder
            }
        });
    }
}
