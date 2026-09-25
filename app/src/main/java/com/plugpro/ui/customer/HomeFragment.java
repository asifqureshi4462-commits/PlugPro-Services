package com.plugpro.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.plugpro.R;
import com.plugpro.data.model.ServiceCategory;
import com.plugpro.data.model.ServiceProvider;
import com.plugpro.data.repository.ProviderRepository;
import com.plugpro.ui.adapters.CategoryHorizontalAdapter;
import com.plugpro.ui.adapters.ProviderAdapter;
import com.plugpro.ui.booking.BookingScheduleActivity;
import com.plugpro.ui.notifications.NotificationsActivity;
import com.plugpro.ui.providers.ProviderDetailActivity;
import com.plugpro.ui.search.SearchActivity;
import com.plugpro.ui.services.CategoryDetailActivity;
import com.plugpro.utils.FirebaseUtil;
import com.plugpro.utils.PreferenceHelper;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvCategories, rvRecommendedPros;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBarPros;
    private TextView tvEmptyPros, tvUserGreeting, btnSeeAllCategories;
    private View layoutSearchBar, btnBannerBookNow;
    private ImageView btnNotification;

    private CategoryHorizontalAdapter categoryAdapter;
    private ProviderAdapter providerAdapter;
    private ProviderRepository providerRepository;
    private PreferenceHelper prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        providerRepository = new ProviderRepository();
        prefs = new PreferenceHelper(requireContext());

        rvCategories = view.findViewById(R.id.rvCategoriesHorizontal);
        rvRecommendedPros = view.findViewById(R.id.rvRecommendedPros);
        swipeRefresh = view.findViewById(R.id.swipeRefreshHome);
        progressBarPros = view.findViewById(R.id.progressBarPros);
        tvEmptyPros = view.findViewById(R.id.tvEmptyPros);
        tvUserGreeting = view.findViewById(R.id.tvUserGreeting);
        layoutSearchBar = view.findViewById(R.id.layoutSearchBar);
        btnBannerBookNow = view.findViewById(R.id.btnBannerBookNow);
        btnSeeAllCategories = view.findViewById(R.id.btnSeeAllCategories);
        btnNotification = view.findViewById(R.id.btnNotification);

        setupGreeting();
        setupRecyclerViews();
        setupListeners();

        loadData();

        return view;
    }

    private void setupGreeting() {
        String name = prefs.getUserName();
        if (name != null && !name.isEmpty()) {
            tvUserGreeting.setText("Welcome, " + name + "!");
        }
    }

    private void setupRecyclerViews() {
        // Categories Horizontal
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryHorizontalAdapter(category -> {
            Intent intent = new Intent(getContext(), CategoryDetailActivity.class);
            intent.putExtra("category", category);
            startActivity(intent);
        });
        rvCategories.setAdapter(categoryAdapter);

        // Providers Vertical
        rvRecommendedPros.setLayoutManager(new LinearLayoutManager(getContext()));
        providerAdapter = new ProviderAdapter(new ProviderAdapter.OnProviderInteractionListener() {
            @Override
            public void onProviderClick(ServiceProvider provider) {
                Intent intent = new Intent(getContext(), ProviderDetailActivity.class);
                intent.putExtra("provider", provider);
                startActivity(intent);
            }

            @Override
            public void onBookClick(ServiceProvider provider) {
                Intent intent = new Intent(getContext(), BookingScheduleActivity.class);
                intent.putExtra("provider", provider);
                startActivity(intent);
            }

            @Override
            public void onFavoriteClick(ServiceProvider provider, ImageView favoriteIcon) {
                String uid = FirebaseUtil.getCurrentUserId();
                providerRepository.toggleFavorite(uid, provider.getId(), isFav -> {
                    favoriteIcon.setImageResource(isFav ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
                    Toast.makeText(getContext(), isFav ? "Added to favorites" : "Removed from favorites", Toast.LENGTH_SHORT).show();
                });
            }
        });
        rvRecommendedPros.setAdapter(providerAdapter);
    }

    private void setupListeners() {
        layoutSearchBar.setOnClickListener(v -> startActivity(new Intent(getContext(), SearchActivity.class)));

        btnBannerBookNow.setOnClickListener(v -> startActivity(new Intent(getContext(), SearchActivity.class)));

        btnSeeAllCategories.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToServicesTab();
            }
        });

        btnNotification.setOnClickListener(v -> startActivity(new Intent(getContext(), NotificationsActivity.class)));

        swipeRefresh.setOnRefreshListener(this::loadData);
    }

    private void loadData() {
        // Load categories
        FirebaseUtil.getServicesRef().whereEqualTo("active", true).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ServiceCategory> categories = queryDocumentSnapshots.toObjects(ServiceCategory.class);
                    categoryAdapter.setCategories(categories);
                });

        // Load recommended providers
        progressBarPros.setVisibility(View.VISIBLE);
        tvEmptyPros.setVisibility(View.GONE);

        providerRepository.getRecommendedProviders(new ProviderRepository.ProvidersCallback() {
            @Override
            public void onSuccess(List<ServiceProvider> providers) {
                progressBarPros.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                if (providers.isEmpty()) {
                    tvEmptyPros.setVisibility(View.VISIBLE);
                } else {
                    tvEmptyPros.setVisibility(View.GONE);
                    providerAdapter.setProviders(providers);
                }
            }

            @Override
            public void onError(String message) {
                progressBarPros.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                tvEmptyPros.setText(message);
                tvEmptyPros.setVisibility(View.VISIBLE);
            }
        });
    }
}
