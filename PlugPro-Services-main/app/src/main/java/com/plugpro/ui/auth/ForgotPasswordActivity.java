package com.plugpro.ui.auth;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.plugpro.R;
import com.plugpro.data.repository.AuthRepository;
import com.plugpro.utils.ValidationUtil;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etResetEmail;
    private Button btnSendReset;
    private TextView tvResetStatus;
    private ImageView btnBack;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        authRepository = new AuthRepository();

        etResetEmail = findViewById(R.id.etResetEmail);
        btnSendReset = findViewById(R.id.btnSendReset);
        tvResetStatus = findViewById(R.id.tvResetStatus);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
        btnSendReset.setOnClickListener(v -> sendResetLink());
    }

    private void sendResetLink() {
        String email = etResetEmail.getText().toString().trim();

        if (!ValidationUtil.isValidEmail(email)) {
            etResetEmail.setError("Please enter a valid email address");
            etResetEmail.requestFocus();
            return;
        }

        btnSendReset.setEnabled(false);
        tvResetStatus.setVisibility(View.GONE);

        authRepository.resetPassword(email, new AuthRepository.SimpleCallback() {
            @Override
            public void onSuccess() {
                btnSendReset.setEnabled(true);
                tvResetStatus.setText(R.string.reset_link_sent);
                tvResetStatus.setTextColor(Color.parseColor("#059669"));
                tvResetStatus.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                btnSendReset.setEnabled(true);
                tvResetStatus.setText(message);
                tvResetStatus.setTextColor(Color.parseColor("#DC2626"));
                tvResetStatus.setVisibility(View.VISIBLE);
            }
        });
    }
}
