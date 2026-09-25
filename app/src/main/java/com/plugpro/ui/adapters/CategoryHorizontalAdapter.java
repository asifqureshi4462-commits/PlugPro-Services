package com.plugpro.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.plugpro.R;
import com.plugpro.data.model.ServiceCategory;
import java.util.ArrayList;
import java.util.List;

public class CategoryHorizontalAdapter extends RecyclerView.Adapter<CategoryHorizontalAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(ServiceCategory category);
    }

    private List<ServiceCategory> categories = new ArrayList<>();
    private final OnCategoryClickListener listener;

    public CategoryHorizontalAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<ServiceCategory> list) {
        this.categories = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceCategory category = categories.get(position);
        holder.tvName.setText(category.getName());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onCategoryClick(category);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCategoryName);
            ivIcon = itemView.findViewById(R.id.ivCategoryIcon);
        }
    }
}
