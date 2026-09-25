package com.plugpro.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.plugpro.R;
import com.plugpro.data.model.User;
import com.plugpro.data.repository.AuthRepository;
import com.plugpro.ui.customer.MainActivity;
import com.plugpro.ui.provider.ProviderRegistrationActivity;
import com.plugpro.utils.PreferenceHelper;
import com.plugpro.utils.ValidationUtil;

public class SignupActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPhone, etPassword, etConfirmPassword;
    private RadioGroup rgRole;
    private Button btnSignUp;
    private TextView tvBackToLogin, tvSignupError;
    private ProgressBar progressBar;
    private AuthRepository authRepository;
    private PreferenceHelper prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authRepository = new AuthRepository();
        prefs = new PreferenceHelper(this);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etSignupEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etSignupPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        rgRole = findViewById(R.id.rgRole);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        tvSignupError = findViewById(R.id.tvSignupError);
        progressBar = findViewById(R.id.progressBarSignup);

        btnSignUp.setOnClickListener(v -> attemptSignup());
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void attemptSignup() {
        String name = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        tvSignupError.setVisibility(View.GONE);

        if (!ValidationUtil.isValidName(name)) {
            etFullName.setError("Please enter your full name");
            etFullName.requestFocus();
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
            return;
        }

        if (!ValidationUtil.isValidPhone(phone)) {
            etPhone.setError("Please enter a valid phone number");
            etPhone.requestFocus();
            return;
        }

        if (!ValidationUtil.isValidPassword(password)) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        String role = (rgRole.getCheckedRadioButtonId() == R.id.rbProvider) ? "provider" : "customer";

        setLoading(true);

        authRepository.signup(name, email, phone, password, role, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                setLoading(false);
                prefs.setUserRole(user.getRole());
                prefs.setUserName(user.getName());

                if (user.isProvider()) {
                    // Navigate to provider profile setup
                    startActivity(new Intent(SignupActivity.this, ProviderRegistrationActivity.class));
                } else {
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                }
                finishAffinity();
            }

            @Override
            public void onError(String message) {
                setLoading(false);
                tvSignupError.setText(message);
                tvSignupError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSignUp.setText(loading ? "" : getString(R.string.sign_up));
        btnSignUp.setEnabled(!loading);
    }
}
