package com.plugpro.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.plugpro.R;
import com.plugpro.data.model.ServiceProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.ViewHolder> {

    public interface OnProviderInteractionListener {
        void onProviderClick(ServiceProvider provider);
        void onBookClick(ServiceProvider provider);
        void onFavoriteClick(ServiceProvider provider, ImageView favoriteIcon);
    }

    private List<ServiceProvider> providers = new ArrayList<>();
    private final OnProviderInteractionListener listener;

    public ProviderAdapter(OnProviderInteractionListener listener) {
        this.listener = listener;
    }

    public void setProviders(List<ServiceProvider> list) {
        this.providers = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_provider_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceProvider provider = providers.get(position);

        holder.tvName.setText(provider.getName());
        holder.tvProfession.setText(provider.getProfession());
        holder.tvRating.setText(String.format(Locale.getDefault(), "%.1f", provider.getRating()));
        holder.tvReviewCount.setText(String.format(Locale.getDefault(), "(%d)", provider.getReviewCount()));
        holder.tvExperience.setText(provider.getExperienceYears() + " yrs exp");
        holder.tvHourlyPrice.setText("₹" + (int) provider.getHourlyRate() + "/hr");

        holder.ivVerifiedBadge.setVisibility(provider.isVerified() ? View.VISIBLE : View.GONE);

        if (provider.getProfileImageUrl() != null && !provider.getProfileImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(provider.getProfileImageUrl())
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(holder.ivAvatar);
        } else {
            holder.ivAvatar.setImageResource(R.drawable.ic_profile);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onProviderClick(provider);
        });

        holder.btnBookNow.setOnClickListener(v -> {
            if (listener != null) listener.onBookClick(provider);
        });

        holder.ivFavorite.setOnClickListener(v -> {
            if (listener != null) listener.onFavoriteClick(provider, holder.ivFavorite);
        });
    }

    @Override
    public int getItemCount() {
        return providers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar, ivVerifiedBadge, ivFavorite;
        TextView tvName, tvProfession, tvRating, tvReviewCount, tvExperience, tvHourlyPrice;
        Button btnBookNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivProviderAvatar);
            ivVerifiedBadge = itemView.findViewById(R.id.ivVerifiedBadge);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            tvName = itemView.findViewById(R.id.tvProviderName);
            tvProfession = itemView.findViewById(R.id.tvProviderProfession);
            tvRating = itemView.findViewById(R.id.tvProviderRating);
            tvReviewCount = itemView.findViewById(R.id.tvProviderReviewCount);
            tvExperience = itemView.findViewById(R.id.tvProviderExperience);
            tvHourlyPrice = itemView.findViewById(R.id.tvHourlyPrice);
            btnBookNow = itemView.findViewById(R.id.btnBookNow);
        }
    }
}
