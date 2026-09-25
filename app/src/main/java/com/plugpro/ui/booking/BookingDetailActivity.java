package com.plugpro.ui.booking;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.plugpro.R;
import com.plugpro.data.model.Booking;
import com.plugpro.data.model.ChatChannel;
import com.plugpro.data.model.Review;
import com.plugpro.data.repository.BookingRepository;
import com.plugpro.data.repository.ChatRepository;
import com.plugpro.ui.chat.ChatActivity;
import com.plugpro.utils.FirebaseUtil;
import com.plugpro.utils.PreferenceHelper;

public class BookingDetailActivity extends AppCompatActivity {

    private ImageView btnBack, btnChat, btnCall;
    private TextView tvBookingId, tvStatus, tvServiceName, tvDateTime;
    private TextView tvProviderName, tvProviderProfession, tvAddress, tvProblem, tvTotal;
    private Button btnRateReview, btnCancelBooking, btnOpenLiveMap;
    private View cardLiveMapTracking;

    private Booking booking;
    private BookingRepository bookingRepository;
    private ChatRepository chatRepository;
    private PreferenceHelper prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        bookingRepository = new BookingRepository();
        chatRepository = new ChatRepository();
        prefs = new PreferenceHelper(this);

        booking = (Booking) getIntent().getSerializableExtra("booking");

        initViews();
        bindData();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBookingDetailBack);
        btnChat = findViewById(R.id.btnDetailChat);
        btnCall = findViewById(R.id.btnDetailCall);
        tvBookingId = findViewById(R.id.tvDetailBookingId);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tvServiceName = findViewById(R.id.tvDetailServiceName);
        tvDateTime = findViewById(R.id.tvDetailDateTime);
        tvProviderName = findViewById(R.id.tvDetailProviderName);
        tvProviderProfession = findViewById(R.id.tvDetailProviderProfession);
        tvAddress = findViewById(R.id.tvDetailAddress);
        tvProblem = findViewById(R.id.tvDetailProblem);
        tvTotal = findViewById(R.id.tvDetailTotal);
        btnRateReview = findViewById(R.id.btnRateReview);
        btnCancelBooking = findViewById(R.id.btnCancelBooking);
        btnOpenLiveMap = findViewById(R.id.btnOpenLiveMap);
        cardLiveMapTracking = findViewById(R.id.cardLiveMapTracking);
    }

    private void bindData() {
        if (booking == null) return;

        tvBookingId.setText("Booking #" + (booking.getId() != null ? booking.getId().substring(0, Math.min(8, booking.getId().length())).toUpperCase() : "BK1001"));
        tvStatus.setText(booking.getStatus());
        tvServiceName.setText(booking.getServiceName());
        tvDateTime.setText(booking.getDate() + " at " + booking.getTimeSlot());
        tvProviderName.setText(booking.getProviderName());
        tvProviderProfession.setText(booking.getProviderProfession());
        tvAddress.setText(booking.getAddress());
        tvProblem.setText(booking.getProblemDescription());
        tvTotal.setText("₹" + (int) booking.getTotalAmount());

        // Status badge color & available actions
        String status = booking.getStatus();
        if (Booking.STATUS_COMPLETED.equalsIgnoreCase(status)) {
            tvStatus.setBackgroundColor(Color.parseColor("#059669"));
            btnRateReview.setVisibility(View.VISIBLE);
            btnCancelBooking.setVisibility(View.GONE);
        } else if (Booking.STATUS_PENDING.equalsIgnoreCase(status)) {
            tvStatus.setBackgroundColor(Color.parseColor("#D97706"));
            btnRateReview.setVisibility(View.GONE);
            btnCancelBooking.setVisibility(View.VISIBLE);
        } else if (Booking.STATUS_CANCELLED.equalsIgnoreCase(status) || Booking.STATUS_REJECTED.equalsIgnoreCase(status)) {
            tvStatus.setBackgroundColor(Color.parseColor("#DC2626"));
            btnRateReview.setVisibility(View.GONE);
            btnCancelBooking.setVisibility(View.GONE);
        } else {
            tvStatus.setBackgroundColor(Color.parseColor("#2563EB"));
            btnRateReview.setVisibility(View.GONE);
            btnCancelBooking.setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnCall.setOnClickListener(v -> {
            if (booking != null && booking.getProviderPhone() != null && !booking.getProviderPhone().isEmpty()) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + booking.getProviderPhone())));
            } else {
                Toast.makeText(this, "Provider contact number not available", Toast.LENGTH_SHORT).show();
            }
        });

        btnChat.setOnClickListener(v -> {
            if (booking == null) return;
            chatRepository.getOrCreateChannel(booking.getCustomerId(), booking.getCustomerName(),
                    booking.getProviderId(), booking.getProviderName(),
                    new ChatRepository.ChannelCallback() {
                        @Override
                        public void onSuccess(ChatChannel channel) {
                            Intent intent = new Intent(BookingDetailActivity.this, ChatActivity.class);
                            intent.putExtra("channel", channel);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(BookingDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnCancelBooking.setOnClickListener(v -> cancelBooking());
        btnRateReview.setOnClickListener(v -> showReviewDialog());

        View.OnClickListener mapClickListener = v -> {
            if (booking != null) {
                Intent intent = new Intent(BookingDetailActivity.this, LiveTrackingMapActivity.class);
                intent.putExtra("booking", booking);
                startActivity(intent);
            }
        };

        if (btnOpenLiveMap != null) btnOpenLiveMap.setOnClickListener(mapClickListener);
        if (cardLiveMapTracking != null) cardLiveMapTracking.setOnClickListener(mapClickListener);
    }

    private void cancelBooking() {
        if (booking == null || booking.getId() == null) return;

        new AlertDialog.Builder(this)
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes, Cancel", (dialog, which) -> {
                    bookingRepository.updateBookingStatus(booking.getId(), Booking.STATUS_CANCELLED, new BookingRepository.SimpleCallback() {
                        @Override
                        public void onSuccess() {
                            booking.setStatus(Booking.STATUS_CANCELLED);
                            bindData();
                            Toast.makeText(BookingDetailActivity.this, "Booking cancelled", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(BookingDetailActivity.this, "Failed to cancel: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showReviewDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_review, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.dialogRatingBar);
        EditText etComment = dialogView.findViewById(R.id.etDialogReviewComment);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmitReviewDialog);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        btnSubmit.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = etComment.getText().toString().trim();

            if (comment.isEmpty()) {
                etComment.setError("Please write a short review");
                return;
            }

            Review review = new Review(
                    booking.getId(),
                    booking.getCustomerId(),
                    booking.getCustomerName(),
                    "",
                    booking.getProviderId(),
                    rating,
                    comment
            );

            bookingRepository.addReview(review, new BookingRepository.SimpleCallback() {
                @Override
                public void onSuccess() {
                    dialog.dismiss();
                    btnRateReview.setVisibility(View.GONE);
                    Toast.makeText(BookingDetailActivity.this, "Thank you for your review!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(BookingDetailActivity.this, "Failed: " + message, Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }
}
