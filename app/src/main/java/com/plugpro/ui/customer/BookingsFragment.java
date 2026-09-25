package com.plugpro.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.chip.ChipGroup;
import com.plugpro.R;
import com.plugpro.data.model.Booking;
import com.plugpro.data.repository.BookingRepository;
import com.plugpro.ui.adapters.BookingAdapter;
import com.plugpro.ui.booking.BookingDetailActivity;
import com.plugpro.utils.FirebaseUtil;
import java.util.ArrayList;
import java.util.List;

public class BookingsFragment extends Fragment {

    private RecyclerView rvBookings;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private View layoutEmpty;
    private ChipGroup chipGroup;
    private BookingAdapter adapter;
    private BookingRepository bookingRepository;
    private List<Booking> allBookings = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);

        bookingRepository = new BookingRepository();

        rvBookings = view.findViewById(R.id.rvBookings);
        swipeRefresh = view.findViewById(R.id.swipeRefreshBookings);
        progressBar = view.findViewById(R.id.progressBarBookings);
        layoutEmpty = view.findViewById(R.id.layoutEmptyBookings);
        chipGroup = view.findViewById(R.id.chipGroupBookings);

        rvBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingAdapter(false, booking -> {
            Intent intent = new Intent(getContext(), BookingDetailActivity.class);
            intent.putExtra("booking", booking);
            startActivity(intent);
        });
        rvBookings.setAdapter(adapter);

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> filterBookings());
        swipeRefresh.setOnRefreshListener(this::loadBookings);

        loadBookings();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBookings();
    }

    private void loadBookings() {
        progressBar.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);

        String customerId = FirebaseUtil.getCurrentUserId();
        bookingRepository.getCustomerBookings(customerId, new BookingRepository.BookingsCallback() {
            @Override
            public void onSuccess(List<Booking> bookings) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                allBookings = bookings;
                filterBookings();
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                layoutEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void filterBookings() {
        int checkedId = chipGroup.getCheckedChipId();
        List<Booking> filtered = new ArrayList<>();

        for (Booking b : allBookings) {
            String status = b.getStatus();
            if (checkedId == R.id.chipUpcoming) {
                if (Booking.STATUS_PENDING.equalsIgnoreCase(status) ||
                    Booking.STATUS_CONFIRMED.equalsIgnoreCase(status) ||
                    Booking.STATUS_ACCEPTED.equalsIgnoreCase(status)) {
                    filtered.add(b);
                }
            } else if (checkedId == R.id.chipActive) {
                if (Booking.STATUS_ON_THE_WAY.equalsIgnoreCase(status) ||
                    Booking.STATUS_STARTED.equalsIgnoreCase(status)) {
                    filtered.add(b);
                }
            } else if (checkedId == R.id.chipCompleted) {
                if (Booking.STATUS_COMPLETED.equalsIgnoreCase(status)) {
                    filtered.add(b);
                }
            } else if (checkedId == R.id.chipCancelled) {
                if (Booking.STATUS_CANCELLED.equalsIgnoreCase(status) ||
                    Booking.STATUS_REJECTED.equalsIgnoreCase(status)) {
                    filtered.add(b);
                }
            }
        }

        adapter.setBookings(filtered);
        layoutEmpty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
