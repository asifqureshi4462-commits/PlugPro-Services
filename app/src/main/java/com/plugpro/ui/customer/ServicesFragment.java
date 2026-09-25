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
import com.plugpro.R;
import com.plugpro.data.model.ServiceCategory;
import com.plugpro.ui.adapters.CategoryGridAdapter;
import com.plugpro.ui.services.CategoryDetailActivity;
import com.plugpro.utils.FirebaseUtil;
import java.util.List;

public class ServicesFragment extends Fragment {

    private RecyclerView rvServicesGrid;
    private ProgressBar progressBar;
    private CategoryGridAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        rvServicesGrid = view.findViewById(R.id.rvServicesGrid);
        progressBar = view.findViewById(R.id.progressBarServices);

        rvServicesGrid.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CategoryGridAdapter(category -> {
            Intent intent = new Intent(getContext(), CategoryDetailActivity.class);
            intent.putExtra("category", category);
            startActivity(intent);
        });
        rvServicesGrid.setAdapter(adapter);

        loadServices();

        return view;
    }

    private void loadServices() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUtil.getServicesRef().whereEqualTo("active", true).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);
                    List<ServiceCategory> categories = queryDocumentSnapshots.toObjects(ServiceCategory.class);
                    adapter.setCategories(categories);
                })
                .addOnFailureListener(e -> progressBar.setVisibility(View.GONE));
    }
}
