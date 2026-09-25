package com.plugpro.ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.plugpro.R;
import com.plugpro.data.model.Booking;
import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    public interface OnBookingClickListener {
        void onBookingClick(Booking booking);
    }

    private List<Booking> bookings = new ArrayList<>();
    private final OnBookingClickListener listener;
    private final boolean isProviderView;

    public BookingAdapter(boolean isProviderView, OnBookingClickListener listener) {
        this.isProviderView = isProviderView;
        this.listener = listener;
    }

    public void setBookings(List<Booking> list) {
        this.bookings = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookings.get(position);

        holder.tvServiceName.setText(booking.getServiceName());
        if (isProviderView) {
            holder.tvProviderOrCustomer.setText("Customer: " + booking.getCustomerName());
        } else {
            holder.tvProviderOrCustomer.setText("Provider: " + booking.getProviderName());
        }

        holder.tvDate.setText(booking.getDate());
        holder.tvTime.setText(booking.getTimeSlot());
        holder.tvAmount.setText("₹" + (int) booking.getTotalAmount());
        holder.tvStatus.setText(booking.getStatus());

        // Dynamic badge color
        switch (booking.getStatus()) {
            case Booking.STATUS_COMPLETED:
                holder.tvStatus.setBackgroundResource(R.drawable.bg_chip_selected);
                holder.tvStatus.setBackgroundColor(Color.parseColor("#059669")); // Green
                break;
            case Booking.STATUS_CANCELLED:
            case Booking.STATUS_REJECTED:
                holder.tvStatus.setBackgroundResource(R.drawable.bg_chip_selected);
                holder.tvStatus.setBackgroundColor(Color.parseColor("#DC2626")); // Red
                break;
            case Booking.STATUS_ACCEPTED:
            case Booking.STATUS_ON_THE_WAY:
            case Booking.STATUS_STARTED:
                holder.tvStatus.setBackgroundResource(R.drawable.bg_chip_selected);
                holder.tvStatus.setBackgroundColor(Color.parseColor("#2563EB")); // Blue
                break;
            case Booking.STATUS_PENDING:
            default:
                holder.tvStatus.setBackgroundResource(R.drawable.bg_chip_selected);
                holder.tvStatus.setBackgroundColor(Color.parseColor("#D97706")); // Amber
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onBookingClick(booking);
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName, tvProviderOrCustomer, tvDate, tvTime, tvAmount, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvBookingServiceName);
            tvProviderOrCustomer = itemView.findViewById(R.id.tvBookingProviderName);
            tvDate = itemView.findViewById(R.id.tvBookingDate);
            tvTime = itemView.findViewById(R.id.tvBookingTime);
            tvAmount = itemView.findViewById(R.id.tvBookingAmount);
            tvStatus = itemView.findViewById(R.id.tvBookingStatus);
        }
    }
}
