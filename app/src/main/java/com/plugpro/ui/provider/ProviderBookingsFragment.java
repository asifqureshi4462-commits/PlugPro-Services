package com.plugpro.ui.provider;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.plugpro.R;
import com.plugpro.data.model.Booking;
import com.plugpro.data.repository.BookingRepository;
import com.plugpro.ui.adapters.BookingAdapter;
import com.plugpro.utils.FirebaseUtil;
import java.util.List;

public class ProviderBookingsFragment extends Fragment {

    private RecyclerView rvBookings;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private BookingAdapter adapter;
    private BookingRepository bookingRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_bookings, container, false);

        bookingRepository = new BookingRepository();

        rvBookings = view.findViewById(R.id.rvProviderAllBookings);
        progressBar = view.findViewById(R.id.progressBarProviderBookings);
        tvEmpty = view.findViewById(R.id.tvEmptyProviderBookings);

        rvBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingAdapter(true, this::showStatusUpdateDialog);
        rvBookings.setAdapter(adapter);

        loadBookings();

        return view;
    }

    private void loadBookings() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);

        String providerId = FirebaseUtil.getCurrentUserId();
        bookingRepository.getProviderBookings(providerId, new BookingRepository.BookingsCallback() {
            @Override
            public void onSuccess(List<Booking> bookings) {
                progressBar.setVisibility(View.GONE);
                adapter.setBookings(bookings);
                tvEmpty.setVisibility(bookings.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showStatusUpdateDialog(Booking booking) {
        String currentStatus = booking.getStatus();
        String[] options;

        if (Booking.STATUS_PENDING.equalsIgnoreCase(currentStatus)) {
            options = new String[]{"Accept Booking", "Reject Booking"};
        } else if (Booking.STATUS_ACCEPTED.equalsIgnoreCase(currentStatus)) {
            options = new String[]{"Mark 'On The Way'"};
        } else if (Booking.STATUS_ON_THE_WAY.equalsIgnoreCase(currentStatus)) {
            options = new String[]{"Mark 'Job Started'"};
        } else if (Booking.STATUS_STARTED.equalsIgnoreCase(currentStatus)) {
            options = new String[]{"Mark 'Completed'"};
        } else {
            Toast.makeText(getContext(), "Booking is already " + currentStatus, Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Update Status for " + booking.getCustomerName())
                .setItems(options, (dialog, which) -> {
                    String newStatus = currentStatus;
                    if (Booking.STATUS_PENDING.equalsIgnoreCase(currentStatus)) {
                        newStatus = (which == 0) ? Booking.STATUS_ACCEPTED : Booking.STATUS_REJECTED;
                    } else if (Booking.STATUS_ACCEPTED.equalsIgnoreCase(currentStatus)) {
                        newStatus = Booking.STATUS_ON_THE_WAY;
                    } else if (Booking.STATUS_ON_THE_WAY.equalsIgnoreCase(currentStatus)) {
                        newStatus = Booking.STATUS_STARTED;
                    } else if (Booking.STATUS_STARTED.equalsIgnoreCase(currentStatus)) {
                        newStatus = Booking.STATUS_COMPLETED;
                    }

                    bookingRepository.updateBookingStatus(booking.getId(), newStatus, new BookingRepository.SimpleCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Status updated!", Toast.LENGTH_SHORT).show();
                            loadBookings();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .show();
    }
}
