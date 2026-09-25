package com.plugpro.ui.notifications;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.plugpro.R;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ImageView btnBack = findViewById(R.id.btnNotifBack);
        btnBack.setOnClickListener(v -> finish());
    }
}
