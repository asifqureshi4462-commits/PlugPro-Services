package com.plugpro.ui.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.plugpro.R;
import com.plugpro.data.model.ServiceProvider;
import com.plugpro.data.repository.ProviderRepository;
import com.plugpro.ui.adapters.ProviderAdapter;
import com.plugpro.ui.booking.BookingScheduleActivity;
import com.plugpro.ui.providers.ProviderDetailActivity;
import com.plugpro.utils.FirebaseUtil;
import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private ImageView btnBack;
    private ProgressBar progressBar;
    private View layoutEmpty;
    private RecyclerView rvFavorites;
    private ProviderAdapter adapter;
    private ProviderRepository providerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        providerRepository = new ProviderRepository();

        btnBack = findViewById(R.id.btnFavBack);
        progressBar = findViewById(R.id.progressBarFav);
        layoutEmpty = findViewById(R.id.layoutFavEmpty);
        rvFavorites = findViewById(R.id.rvFavorites);

        btnBack.setOnClickListener(v -> finish());

        rvFavorites.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProviderAdapter(new ProviderAdapter.OnProviderInteractionListener() {
            @Override
            public void onProviderClick(ServiceProvider provider) {
                Intent intent = new Intent(FavoritesActivity.this, ProviderDetailActivity.class);
                intent.putExtra("provider", provider);
                startActivity(intent);
            }

            @Override
            public void onBookClick(ServiceProvider provider) {
                Intent intent = new Intent(FavoritesActivity.this, BookingScheduleActivity.class);
                intent.putExtra("provider", provider);
                startActivity(intent);
            }

            @Override
            public void onFavoriteClick(ServiceProvider provider, ImageView favoriteIcon) {
                String uid = FirebaseUtil.getCurrentUserId();
                providerRepository.toggleFavorite(uid, provider.getId(), isFav -> {
                    loadFavorites();
                });
            }
        });
        rvFavorites.setAdapter(adapter);

        loadFavorites();
    }

    private void loadFavorites() {
        progressBar.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);

        String uid = FirebaseUtil.getCurrentUserId();
        FirebaseUtil.getFavoritesRef().whereEqualTo("customerId", uid).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        layoutEmpty.setVisibility(View.VISIBLE);
                        adapter.setProviders(new ArrayList<>());
                        return;
                    }

                    List<String> proIds = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String proId = doc.getString("providerId");
                        if (proId != null) proIds.add(proId);
                    }

                    List<ServiceProvider> list = new ArrayList<>();
                    for (String proId : proIds) {
                        FirebaseUtil.getProvidersRef().document(proId).get()
                                .addOnSuccessListener(proDoc -> {
                                    if (proDoc.exists()) {
                                        ServiceProvider p = proDoc.toObject(ServiceProvider.class);
                                        if (p != null) {
                                            p.setId(proDoc.getId());
                                            list.add(p);
                                            adapter.setProviders(new ArrayList<>(list));
                                        }
                                    }
                                    progressBar.setVisibility(View.GONE);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    layoutEmpty.setVisibility(View.VISIBLE);
                });
    }
}
