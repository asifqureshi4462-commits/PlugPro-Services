package com.plugpro.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.plugpro.R;
import com.plugpro.data.model.ServiceProvider;
import com.plugpro.data.repository.ProviderRepository;
import com.plugpro.ui.adapters.ProviderAdapter;
import com.plugpro.ui.booking.BookingScheduleActivity;
import com.plugpro.ui.providers.ProviderDetailActivity;
import com.plugpro.utils.FirebaseUtil;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText etSearch;
    private ImageView btnBack;
    private ProgressBar progressBar;
    private View layoutEmpty;
    private RecyclerView rvResults;
    private ProviderAdapter adapter;
    private ProviderRepository providerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        providerRepository = new ProviderRepository();

        etSearch = findViewById(R.id.etSearchQuery);
        btnBack = findViewById(R.id.btnSearchBack);
        progressBar = findViewById(R.id.progressBarSearch);
        layoutEmpty = findViewById(R.id.layoutSearchEmpty);
        rvResults = findViewById(R.id.rvSearchResults);

        btnBack.setOnClickListener(v -> finish());

        rvResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProviderAdapter(new ProviderAdapter.OnProviderInteractionListener() {
            @Override
            public void onProviderClick(ServiceProvider provider) {
                Intent intent = new Intent(SearchActivity.this, ProviderDetailActivity.class);
                intent.putExtra("provider", provider);
                startActivity(intent);
            }

            @Override
            public void onBookClick(ServiceProvider provider) {
                Intent intent = new Intent(SearchActivity.this, BookingScheduleActivity.class);
                intent.putExtra("provider", provider);
                startActivity(intent);
            }

            @Override
            public void onFavoriteClick(ServiceProvider provider, ImageView favoriteIcon) {
                String uid = FirebaseUtil.getCurrentUserId();
                providerRepository.toggleFavorite(uid, provider.getId(), isFav -> {
                    favoriteIcon.setImageResource(isFav ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
                    Toast.makeText(SearchActivity.this, isFav ? "Added to favorites" : "Removed from favorites", Toast.LENGTH_SHORT).show();
                });
            }
        });
        rvResults.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.length() >= 2) {
                    performSearch(query);
                } else if (query.isEmpty()) {
                    loadInitial();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadInitial();
    }

    private void loadInitial() {
        progressBar.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);

        providerRepository.getRecommendedProviders(new ProviderRepository.ProvidersCallback() {
            @Override
            public void onSuccess(List<ServiceProvider> providers) {
                progressBar.setVisibility(View.GONE);
                adapter.setProviders(providers);
                layoutEmpty.setVisibility(providers.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                layoutEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void performSearch(String query) {
        progressBar.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);

        providerRepository.searchProviders(query, new ProviderRepository.ProvidersCallback() {
            @Override
            public void onSuccess(List<ServiceProvider> providers) {
                progressBar.setVisibility(View.GONE);
                adapter.setProviders(providers);
                layoutEmpty.setVisibility(providers.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                layoutEmpty.setVisibility(View.VISIBLE);
            }
        });
    }
}
