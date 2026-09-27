package com.plugpro.ui.provider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.plugpro.R;
import com.plugpro.data.model.ServiceProvider;
import com.plugpro.data.repository.ProviderRepository;
import com.plugpro.utils.FirebaseUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProviderAvailabilityActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etDays, etSlots;
    private Button btnSave;
    private ProviderRepository providerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_availability);

        providerRepository = new ProviderRepository();

        btnBack = findViewById(R.id.btnAvailBack);
        etDays = findViewById(R.id.etAvailDays);
        etSlots = findViewById(R.id.etAvailSlots);
        btnSave = findViewById(R.id.btnSaveAvailability);

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveAvailability());

        loadCurrentAvailability();
    }

    private void loadCurrentAvailability() {
        String uid = FirebaseUtil.getCurrentUserId();
        providerRepository.getProviderById(uid, new ProviderRepository.ProviderCallback() {
            @Override
            public void onSuccess(ServiceProvider provider) {
                if (provider.getAvailableDays() != null && !provider.getAvailableDays().isEmpty()) {
                    etDays.setText(String.join(", ", provider.getAvailableDays()));
                }
                if (provider.getAvailableTimeSlots() != null && !provider.getAvailableTimeSlots().isEmpty()) {
                    etSlots.setText(String.join(", ", provider.getAvailableTimeSlots()));
                }
            }

            @Override
            public void onError(String message) {}
        });
    }

    private void saveAvailability() {
        String daysStr = etDays.getText().toString().trim();
        String slotsStr = etSlots.getText().toString().trim();

        List<String> days = new ArrayList<>();
        for (String d : daysStr.split(",")) {
            if (!d.trim().isEmpty()) days.add(d.trim());
        }

        List<String> slots = new ArrayList<>();
        for (String s : slotsStr.split(",")) {
            if (!s.trim().isEmpty()) slots.add(s.trim());
        }

        String uid = FirebaseUtil.getCurrentUserId();
        FirebaseUtil.getProvidersRef().document(uid)
                .update("availableDays", days, "availableTimeSlots", slots)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Availability saved successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
