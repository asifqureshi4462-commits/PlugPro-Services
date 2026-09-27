package com.plugpro.ui.provider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.plugpro.R;
import com.plugpro.data.model.ServiceProvider;
import com.plugpro.data.repository.ProviderRepository;
import com.plugpro.utils.FirebaseUtil;
import com.plugpro.utils.PreferenceHelper;
import java.util.Arrays;

public class ProviderRegistrationActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etProfession, etExp, etRate, etArea, etAbout;
    private Button btnSubmit;
    private ProgressBar progressBar;

    private ProviderRepository providerRepository;
    private PreferenceHelper prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_registration);

        providerRepository = new ProviderRepository();
        prefs = new PreferenceHelper(this);

        btnBack = findViewById(R.id.btnRegBack);
        etProfession = findViewById(R.id.etRegProfession);
        etExp = findViewById(R.id.etRegExp);
        etRate = findViewById(R.id.etRegRate);
        etArea = findViewById(R.id.etRegArea);
        etAbout = findViewById(R.id.etRegAbout);
        btnSubmit = findViewById(R.id.btnSubmitProviderProfile);
        progressBar = findViewById(R.id.progressBarProviderReg);

        btnBack.setOnClickListener(v -> finish());
        btnSubmit.setOnClickListener(v -> submitProfile());
    }

    private void submitProfile() {
        String profession = etProfession.getText().toString().trim();
        String expStr = etExp.getText().toString().trim();
        String rateStr = etRate.getText().toString().trim();
        String area = etArea.getText().toString().trim();
        String about = etAbout.getText().toString().trim();

        if (profession.isEmpty()) {
            etProfession.setError("Please enter your profession");
            etProfession.requestFocus();
            return;
        }

        if (expStr.isEmpty()) {
            etExp.setError("Please enter your years of experience");
            etExp.requestFocus();
            return;
        }

        if (rateStr.isEmpty()) {
            etRate.setError("Please enter your hourly rate");
            etRate.requestFocus();
            return;
        }

        int exp = Integer.parseInt(expStr);
        double rate = Double.parseDouble(rateStr);

        String uid = FirebaseUtil.getCurrentUserId();
        String name = prefs.getUserName();
        String email = FirebaseUtil.getCurrentUser() != null ? FirebaseUtil.getCurrentUser().getEmail() : "";

        ServiceProvider provider = new ServiceProvider(
                uid, uid, name, profession, "cat_repair", exp, rate, about, area, "", email
        );
        provider.setVerifiedStatus("pending");
        provider.setAvailableDays(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
        provider.setAvailableTimeSlots(Arrays.asList("09:00 AM", "11:00 AM", "02:00 PM", "05:00 PM"));

        progressBar.setVisibility(View.VISIBLE);
        btnSubmit.setEnabled(false);

        providerRepository.registerProvider(provider, new ProviderRepository.ProviderCallback() {
            @Override
            public void onSuccess(ServiceProvider provider) {
                progressBar.setVisibility(View.GONE);
                prefs.setUserRole("provider");
                Toast.makeText(ProviderRegistrationActivity.this, "Profile created! Status: Pending Verification", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ProviderRegistrationActivity.this, ProviderMainActivity.class));
                finishAffinity();
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);
                Toast.makeText(ProviderRegistrationActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
