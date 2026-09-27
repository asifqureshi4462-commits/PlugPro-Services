package com.plugpro.ui.provider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.ListenerRegistration;
import com.plugpro.R;
import com.plugpro.data.model.Booking;
import com.plugpro.data.model.ServiceProvider;
import com.plugpro.data.repository.BookingRepository;
import com.plugpro.data.repository.ProviderRepository;
import com.plugpro.ui.adapters.BookingAdapter;
import com.plugpro.ui.booking.BookingDetailActivity;
import com.plugpro.utils.FirebaseUtil;
import com.plugpro.utils.PreferenceHelper;
import java.util.ArrayList;
import java.util.List;

public class ProviderDashboardFragment extends Fragment {

    private TextView tvStatusBadge, tvTotalEarnings, tvActiveJobsCount, tvNoPending;
    private Button btnSetAvailability, btnSimulateNotification;
    private SwitchCompat switchNotifications;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvPending;

    private BookingAdapter adapter;
    private BookingRepository bookingRepository;
    private ProviderRepository providerRepository;
    private PreferenceHelper prefs;
    private ListenerRegistration pendingBookingsListener;
    private boolean isInitialSnapshot = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_dashboard, container, false);

        bookingRepository = new BookingRepository();
        providerRepository = new ProviderRepository();
        prefs = new PreferenceHelper(requireContext());

        tvStatusBadge = view.findViewById(R.id.tvProviderStatusBadge);
        tvTotalEarnings = view.findViewById(R.id.tvTotalEarnings);
        tvActiveJobsCount = view.findViewById(R.id.tvActiveJobsCount);
        tvNoPending = view.findViewById(R.id.tvNoPendingRequests);
        btnSetAvailability = view.findViewById(R.id.btnSetAvailability);
        btnSimulateNotification = view.findViewById(R.id.btnSimulateNotification);
        switchNotifications = view.findViewById(R.id.switchNotifications);
        progressBar = view.findViewById(R.id.progressBarProviderDashboard);
        swipeRefresh = view.findViewById(R.id.swipeRefreshDashboard);
        rvPending = view.findViewById(R.id.rvProviderPendingBookings);

        rvPending.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingAdapter(true, booking -> {
            Intent intent = new Intent(getContext(), BookingDetailActivity.class);
            intent.putExtra("booking", booking);
            startActivity(intent);
        });
        rvPending.setAdapter(adapter);

        // Load saved notification preference
        boolean notificationsEnabled = requireContext().getSharedPreferences("plugpro_prefs", 0)
                .getBoolean("provider_notifications_enabled", true);
        switchNotifications.setChecked(notificationsEnabled);

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            requireContext().getSharedPreferences("plugpro_prefs", 0)
                    .edit()
                    .putBoolean("provider_notifications_enabled", isChecked)
                    .apply();

            if (isChecked) {
                Toast.makeText(getContext(), "🔔 Booking push alerts enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "🔕 Booking push alerts muted", Toast.LENGTH_SHORT).show();
            }
        });

        btnSimulateNotification.setOnClickListener(v -> {
            if (switchNotifications.isChecked()) {
                Toast.makeText(getContext(), "🔔 New Booking Request from Alex Johnson! Service: Electrical Repair (₹499)", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Notifications are currently toggled OFF. Turn switch ON to see alerts.", Toast.LENGTH_SHORT).show();
            }
        });

        btnSetAvailability.setOnClickListener(v -> startActivity(new Intent(getContext(), ProviderAvailabilityActivity.class)));
        swipeRefresh.setOnRefreshListener(this::loadDashboardData);

        loadDashboardData();
        setupRealtimeBookingAlerts();

        return view;
    }

    private void setupRealtimeBookingAlerts() {
        String providerId = FirebaseUtil.getCurrentUserId();
        if (providerId.isEmpty()) return;

        isInitialSnapshot = true;
        pendingBookingsListener = FirebaseUtil.getBookingsRef()
                .whereEqualTo("providerId", providerId)
                .whereEqualTo("status", Booking.STATUS_PENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null || snapshots == null) return;

                    if (isInitialSnapshot) {
                        isInitialSnapshot = false;
                        return;
                    }

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            Booking newBooking = dc.getDocument().toObject(Booking.class);
                            if (newBooking != null && switchNotifications.isChecked()) {
                                Toast.makeText(getContext(),
                                        "🔔 New Booking Alert: " + newBooking.getCustomerName() + " requested " + newBooking.getServiceName() + "!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    loadDashboardData();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (pendingBookingsListener != null) {
            pendingBookingsListener.remove();
        }
    }

    private void loadDashboardData() {
        progressBar.setVisibility(View.VISIBLE);
        String providerId = FirebaseUtil.getCurrentUserId();

        // Load provider profile
        providerRepository.getProviderById(providerId, new ProviderRepository.ProviderCallback() {
            @Override
            public void onSuccess(ServiceProvider provider) {
                if (isAdded()) {
                    if (provider.isVerified()) {
                        tvStatusBadge.setText("Status: Verified Professional ✓");
                        tvStatusBadge.setTextColor(Color.parseColor("#059669"));
                    } else if ("rejected".equalsIgnoreCase(provider.getVerifiedStatus())) {
                        tvStatusBadge.setText("Status: Verification Rejected");
                        tvStatusBadge.setTextColor(Color.parseColor("#DC2626"));
                    } else {
                        tvStatusBadge.setText("Status: Pending Verification");
                        tvStatusBadge.setTextColor(Color.parseColor("#D97706"));
                    }
                }
            }

            @Override
            public void onError(String message) {}
        });

        // Load provider bookings
        bookingRepository.getProviderBookings(providerId, new BookingRepository.BookingsCallback() {
            @Override
            public void onSuccess(List<Booking> bookings) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);

                double earnings = 0;
                int activeJobs = 0;
                List<Booking> pendingList = new ArrayList<>();

                for (Booking b : bookings) {
                    if (Booking.STATUS_COMPLETED.equalsIgnoreCase(b.getStatus())) {
                        earnings += b.getServiceFee();
                    } else if (Booking.STATUS_ON_THE_WAY.equalsIgnoreCase(b.getStatus()) ||
                               Booking.STATUS_STARTED.equalsIgnoreCase(b.getStatus()) ||
                               Booking.STATUS_ACCEPTED.equalsIgnoreCase(b.getStatus())) {
                        activeJobs++;
                    } else if (Booking.STATUS_PENDING.equalsIgnoreCase(b.getStatus())) {
                        pendingList.add(b);
                    }
                }

                tvTotalEarnings.setText("₹" + (int) earnings);
                tvActiveJobsCount.setText(String.valueOf(activeJobs));

                adapter.setBookings(pendingList);
                tvNoPending.setVisibility(pendingList.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }
        });
    }
}
