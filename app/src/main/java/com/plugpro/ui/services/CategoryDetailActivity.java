package com.plugpro.ui.services;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.plugpro.R;
import com.plugpro.data.model.ServiceCategory;
import com.plugpro.data.model.ServiceProvider;
import com.plugpro.data.repository.ProviderRepository;
import com.plugpro.ui.adapters.ProviderAdapter;
import com.plugpro.ui.booking.BookingScheduleActivity;
import com.plugpro.ui.providers.ProviderDetailActivity;
import com.plugpro.utils.FirebaseUtil;
import java.util.List;

public class CategoryDetailActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView btnBack;
    private ProgressBar progressBar;
    private View layoutEmpty;
    private RecyclerView rvPros;
    private ProviderAdapter adapter;
    private ProviderRepository providerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        providerRepository = new ProviderRepository();

        ServiceCategory category = (ServiceCategory) getIntent().getSerializableExtra("category");

        tvTitle = findViewById(R.id.tvCatDetailTitle);
        btnBack = findViewById(R.id.btnCatDetailBack);
        progressBar = findViewById(R.id.progressBarCatPros);
        layoutEmpty = findViewById(R.id.layoutCatEmpty);
        rvPros = findViewById(R.id.rvCategoryPros);

        if (category != null) {
            tvTitle.setText(category.getName());
        }

        btnBack.setOnClickListener(v -> finish());

        rvPros.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProviderAdapter(new ProviderAdapter.OnProviderInteractionListener() {
            @Override
            public void onProviderClick(ServiceProvider provider) {
                Intent intent = new Intent(CategoryDetailActivity.this, ProviderDetailActivity.class);
                intent.putExtra("provider", provider);
                startActivity(intent);
            }

            @Override
            public void onBookClick(ServiceProvider provider) {
                Intent intent = new Intent(CategoryDetailActivity.this, BookingScheduleActivity.class);
                intent.putExtra("provider", provider);
                startActivity(intent);
            }

            @Override
            public void onFavoriteClick(ServiceProvider provider, ImageView favoriteIcon) {
                String uid = FirebaseUtil.getCurrentUserId();
                providerRepository.toggleFavorite(uid, provider.getId(), isFav -> {
                    favoriteIcon.setImageResource(isFav ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
                    Toast.makeText(CategoryDetailActivity.this, isFav ? "Added to favorites" : "Removed from favorites", Toast.LENGTH_SHORT).show();
                });
            }
        });
        rvPros.setAdapter(adapter);

        if (category != null) {
            loadProviders(category.getId());
        }
    }

    private void loadProviders(String categoryId) {
        progressBar.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);

        providerRepository.getProvidersByCategory(categoryId, new ProviderRepository.ProvidersCallback() {
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
