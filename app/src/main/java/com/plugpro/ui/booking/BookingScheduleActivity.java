package com.plugpro.ui.booking;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.plugpro.R;
import com.plugpro.data.model.Booking;
import com.plugpro.data.model.ServiceProvider;
import com.plugpro.data.repository.BookingRepository;
import com.plugpro.utils.DateTimeUtil;
import com.plugpro.utils.FirebaseUtil;
import com.plugpro.utils.PreferenceHelper;
import java.util.Calendar;
import java.util.Locale;

public class BookingScheduleActivity extends AppCompatActivity {

    private ImageView btnBack, ivAvatar;
    private TextView tvProName, tvProProfession, tvHourlyRate;
    private EditText etService, etDate, etTime, etAddress, etProblem;
    private TextView tvServiceFee, tvCharges, tvTotal;
    private Button btnConfirm;
    private ProgressBar progressBar;

    private ServiceProvider provider;
    private BookingRepository bookingRepository;
    private PreferenceHelper prefs;
    private double serviceFee = 450.0;
    private double additionalCharges = 49.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_schedule);

        bookingRepository = new BookingRepository();
        prefs = new PreferenceHelper(this);

        provider = (ServiceProvider) getIntent().getSerializableExtra("provider");

        initViews();
        bindData();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnScheduleBack);
        ivAvatar = findViewById(R.id.ivScheduleProAvatar);
        tvProName = findViewById(R.id.tvScheduleProName);
        tvProProfession = findViewById(R.id.tvScheduleProProfession);
        tvHourlyRate = findViewById(R.id.tvScheduleHourlyRate);
        etService = findViewById(R.id.etBookingService);
        etDate = findViewById(R.id.etBookingDate);
        etTime = findViewById(R.id.etBookingTime);
        etAddress = findViewById(R.id.etBookingAddress);
        etProblem = findViewById(R.id.etBookingProblem);
        tvServiceFee = findViewById(R.id.tvSummaryServiceFee);
        tvCharges = findViewById(R.id.tvSummaryCharges);
        tvTotal = findViewById(R.id.tvSummaryTotal);
        btnConfirm = findViewById(R.id.btnConfirmBooking);
        progressBar = findViewById(R.id.progressBarBooking);

        etDate.setText(DateTimeUtil.getTodayDateFormatted());
    }

    private void bindData() {
        if (provider != null) {
            tvProName.setText(provider.getName());
            tvProProfession.setText(provider.getProfession());
            tvHourlyRate.setText("₹" + (int) provider.getHourlyRate() + "/hr");
            serviceFee = provider.getHourlyRate() > 0 ? provider.getHourlyRate() : 450.0;
            etService.setText(provider.getProfession() + " Service");

            if (provider.getProfileImageUrl() != null && !provider.getProfileImageUrl().isEmpty()) {
                Glide.with(this)
                        .load(provider.getProfileImageUrl())
                        .placeholder(R.drawable.ic_profile)
                        .into(ivAvatar);
            }
        }

        tvServiceFee.setText("₹" + (int) serviceFee);
        tvCharges.setText("₹" + (int) additionalCharges);
        tvTotal.setText("₹" + (int) (serviceFee + additionalCharges));
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        etDate.setOnClickListener(v -> showDatePicker());

        btnConfirm.setOnClickListener(v -> attemptBooking());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth);
                    etDate.setText(DateTimeUtil.formatDate(selected.getTime()));
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void attemptBooking() {
        String service = etService.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String problem = etProblem.getText().toString().trim();

        if (address.isEmpty()) {
            etAddress.setError("Please enter the service address");
            etAddress.requestFocus();
            return;
        }

        if (problem.isEmpty()) {
            etProblem.setError("Please briefly describe the repair or service required");
            etProblem.requestFocus();
            return;
        }

        String customerId = FirebaseUtil.getCurrentUserId();
        String customerName = prefs.getUserName();
        String customerPhone = "";

        String proId = provider != null ? provider.getId() : "pro_sample";
        String proName = provider != null ? provider.getName() : "Professional";
        String proProfession = provider != null ? provider.getProfession() : "Service Pro";
        String proPhone = provider != null ? provider.getPhone() : "";

        Booking booking = new Booking(
                customerId, customerName, customerPhone,
                proId, proName, proProfession, proPhone,
                provider != null ? provider.getCategoryId() : "cat_general",
                service, date, time, address, problem, serviceFee
        );

        setLoading(true);

        bookingRepository.createBooking(booking, new BookingRepository.BookingCallback() {
            @Override
            public void onSuccess(Booking createdBooking) {
                setLoading(false);
                Toast.makeText(BookingScheduleActivity.this, R.string.booking_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BookingScheduleActivity.this, BookingDetailActivity.class);
                intent.putExtra("booking", createdBooking);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                setLoading(false);
                Toast.makeText(BookingScheduleActivity.this, "Booking failed: " + message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnConfirm.setText(loading ? "" : getString(R.string.confirm_booking));
        btnConfirm.setEnabled(!loading);
    }
}
