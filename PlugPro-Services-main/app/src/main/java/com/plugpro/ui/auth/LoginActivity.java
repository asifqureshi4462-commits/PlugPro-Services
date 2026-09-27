package com.plugpro.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.plugpro.R;
import com.plugpro.data.model.User;
import com.plugpro.data.repository.AuthRepository;
import com.plugpro.ui.customer.MainActivity;
import com.plugpro.ui.provider.ProviderMainActivity;
import com.plugpro.utils.PreferenceHelper;
import com.plugpro.utils.ValidationUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignIn;
    private TextView tvSignUp, tvForgotPassword, tvErrorMessage;
    private ProgressBar progressBar;
    private AuthRepository authRepository;
    private PreferenceHelper prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authRepository = new AuthRepository();
        prefs = new PreferenceHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        progressBar = findViewById(R.id.progressBar);

        btnSignIn.setOnClickListener(v -> attemptLogin());
        tvSignUp.setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
        tvForgotPassword.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        tvErrorMessage.setVisibility(View.GONE);

        if (!ValidationUtil.isValidEmail(email)) {
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
            return;
        }

        if (!ValidationUtil.isValidPassword(password)) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        setLoading(true);

        authRepository.login(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                setLoading(false);
                prefs.setUserRole(user.getRole());
                prefs.setUserName(user.getName());

                if (user.isProvider()) {
                    startActivity(new Intent(LoginActivity.this, ProviderMainActivity.class));
                } else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                finishAffinity();
            }

            @Override
            public void onError(String message) {
                setLoading(false);
                tvErrorMessage.setText(message);
                tvErrorMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSignIn.setText(loading ? "" : getString(R.string.sign_in));
        btnSignIn.setEnabled(!loading);
    }
}
