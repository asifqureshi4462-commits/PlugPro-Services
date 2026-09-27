package com.plugpro.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.plugpro.R;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {

    public static class PageItem {
        public final int iconRes;
        public final int titleRes;
        public final int descRes;

        public PageItem(int iconRes, int titleRes, int descRes) {
            this.iconRes = iconRes;
            this.titleRes = titleRes;
            this.descRes = descRes;
        }
    }

    private final PageItem[] pages = new PageItem[]{
            new PageItem(R.drawable.ic_search, R.string.onboarding_title_1, R.string.onboarding_desc_1),
            new PageItem(R.drawable.ic_calendar, R.string.onboarding_title_2, R.string.onboarding_desc_2),
            new PageItem(R.drawable.ic_verified, R.string.onboarding_title_3, R.string.onboarding_desc_3)
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_onboarding_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PageItem item = pages[position];
        holder.ivIcon.setImageResource(item.iconRes);
        holder.tvTitle.setText(item.titleRes);
        holder.tvDesc.setText(item.descRes);
    }

    @Override
    public int getItemCount() {
        return pages.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle, tvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivOnboardingIcon);
            tvTitle = itemView.findViewById(R.id.tvOnboardingTitle);
            tvDesc = itemView.findViewById(R.id.tvOnboardingDesc);
        }
    }
}
