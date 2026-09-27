package com.plugpro.ui.providers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.plugpro.R;
import com.plugpro.data.model.Review;
import com.plugpro.data.model.ServiceProvider;
import com.plugpro.data.repository.ChatRepository;
import com.plugpro.data.repository.ProviderRepository;
import com.plugpro.ui.adapters.ReviewAdapter;
import com.plugpro.ui.booking.BookingScheduleActivity;
import com.plugpro.ui.chat.ChatActivity;
import com.plugpro.utils.FirebaseUtil;
import com.plugpro.utils.PreferenceHelper;
import java.util.List;
import java.util.Locale;

public class ProviderDetailActivity extends AppCompatActivity {

    private ImageView btnBack, btnFavorite, ivAvatar, ivVerified;
    private TextView tvName, tvProfession, tvArea, tvRating, tvExp, tvJobs, tvAbout, tvDays, tvSlots;
    private View btnChat, btnCall;
    private Button btnSchedule;
    private RecyclerView rvReviews;

    private ServiceProvider provider;
    private ProviderRepository providerRepository;
    private ChatRepository chatRepository;
    private ReviewAdapter reviewAdapter;
    private PreferenceHelper prefs;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_detail);

        providerRepository = new ProviderRepository();
        chatRepository = new ChatRepository();
        prefs = new PreferenceHelper(this);

        provider = (ServiceProvider) getIntent().getSerializableExtra("provider");

        initViews();
        bindData();
        setupListeners();
        checkFavoriteStatus();
        loadReviews();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnProBack);
        btnFavorite = findViewById(R.id.btnProFavorite);
        ivAvatar = findViewById(R.id.ivProHeroAvatar);
        ivVerified = findViewById(R.id.ivProHeroVerified);
        tvName = findViewById(R.id.tvProHeroName);
        tvProfession = findViewById(R.id.tvProHeroProfession);
        tvArea = findViewById(R.id.tvProHeroArea);
        tvRating = findViewById(R.id.tvStatRating);
        tvExp = findViewById(R.id.tvStatExp);
        tvJobs = findViewById(R.id.tvStatJobs);
        tvAbout = findViewById(R.id.tvProAbout);
        tvDays = findViewById(R.id.tvProAvailableDays);
        tvSlots = findViewById(R.id.tvProAvailableSlots);
        btnChat = findViewById(R.id.btnProChat);
        btnCall = findViewById(R.id.btnProCall);
        btnSchedule = findViewById(R.id.btnProSchedule);
        rvReviews = findViewById(R.id.rvProReviews);

        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter();
        rvReviews.setAdapter(reviewAdapter);
    }

    private void bindData() {
        if (provider == null) return;

        tvName.setText(provider.getName());
        tvProfession.setText(provider.getProfession());
        tvArea.setText("Serving " + provider.getServiceArea());
        tvRating.setText(String.format(Locale.getDefault(), "%.1f ★", provider.getRating()));
        tvExp.setText(provider.getExperienceYears() + " Yrs");
        tvJobs.setText(provider.getCompletedJobs() + "+");
        tvAbout.setText(provider.getAbout());

        ivVerified.setVisibility(provider.isVerified() ? View.VISIBLE : View.GONE);

        if (provider.getAvailableDays() != null && !provider.getAvailableDays().isEmpty()) {
            tvDays.setText("Working Days: " + String.join(", ", provider.getAvailableDays()));
        }

        if (provider.getAvailableTimeSlots() != null && !provider.getAvailableTimeSlots().isEmpty()) {
            tvSlots.setText("Time Slots: " + String.join(", ", provider.getAvailableTimeSlots()));
        }

        btnSchedule.setText(getString(R.string.btn_schedule) + " • ₹" + (int) provider.getHourlyRate() + "/hr");

        if (provider.getProfileImageUrl() != null && !provider.getProfileImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(provider.getProfileImageUrl())
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(ivAvatar);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnFavorite.setOnClickListener(v -> {
            String uid = FirebaseUtil.getCurrentUserId();
            providerRepository.toggleFavorite(uid, provider.getId(), isFav -> {
                isFavorite = isFav;
                btnFavorite.setImageResource(isFav ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
                Toast.makeText(this, isFav ? "Saved to favorites" : "Removed from favorites", Toast.LENGTH_SHORT).show();
            });
        });

        btnCall.setOnClickListener(v -> {
            if (provider.getPhone() != null && !provider.getPhone().isEmpty()) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + provider.getPhone()));
                startActivity(dialIntent);
            } else {
                Toast.makeText(this, "Phone number not available", Toast.LENGTH_SHORT).show();
            }
        });

        btnChat.setOnClickListener(v -> {
            String customerId = FirebaseUtil.getCurrentUserId();
            String customerName = prefs.getUserName();
            chatRepository.getOrCreateChannel(customerId, customerName, provider.getId(), provider.getName(),
                    new ChatRepository.ChannelCallback() {
                        @Override
                        public void onSuccess(com.plugpro.data.model.ChatChannel channel) {
                            Intent intent = new Intent(ProviderDetailActivity.this, ChatActivity.class);
                            intent.putExtra("channel", channel);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(ProviderDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookingScheduleActivity.class);
            intent.putExtra("provider", provider);
            startActivity(intent);
        });
    }

    private void checkFavoriteStatus() {
        String uid = FirebaseUtil.getCurrentUserId();
        if (provider == null || uid.isEmpty()) return;

        providerRepository.isFavorite(uid, provider.getId(), isFav -> {
            isFavorite = isFav;
            btnFavorite.setImageResource(isFav ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
        });
    }

    private void loadReviews() {
        if (provider == null) return;
        FirebaseUtil.getReviewsRef().whereEqualTo("providerId", provider.getId()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Review> list = queryDocumentSnapshots.toObjects(Review.class);
                    reviewAdapter.setReviews(list);
                });
    }
}
