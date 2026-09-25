package com.plugpro.ui.booking;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.MapsApiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.plugpro.R;
import com.plugpro.data.model.Booking;
import com.plugpro.data.model.ChatChannel;
import com.plugpro.data.repository.ChatRepository;
import com.plugpro.ui.chat.ChatActivity;
import java.util.ArrayList;
import java.util.List;

public class LiveTrackingMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Booking booking;
    private TextView tvEta, tvStatus, tvSpeed, tvProName, tvProProfession, tvAddress;
    private ImageView ivAvatar;
    private View btnBack, btnCall, btnChat;
    private FloatingActionButton fabRecenter;

    private Marker technicianMarker;
    private Marker destinationMarker;
    private Polyline routePolyline;
    private Handler simulationHandler = new Handler(Looper.getMainLooper());
    private int currentRouteIndex = 0;
    private List<LatLng> routePoints = new ArrayList<>();
    private ChatRepository chatRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Required Google Maps Platform tracking attribution
        MapsApiSettings.addInternalUsageAttributionId(this, "gmp_mcp_codeassist_v1_aistudio");

        setContentView(R.layout.activity_live_tracking_map);

        chatRepository = new ChatRepository();
        booking = (Booking) getIntent().getSerializableExtra("booking");

        initViews();
        bindData();
        setupListeners();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnMapBack);
        btnCall = findViewById(R.id.btnTrackingCall);
        btnChat = findViewById(R.id.btnTrackingChat);
        fabRecenter = findViewById(R.id.fabRecenter);

        tvEta = findViewById(R.id.tvTrackingEta);
        tvStatus = findViewById(R.id.tvTrackingStatus);
        tvSpeed = findViewById(R.id.tvTrackingSpeed);
        tvProName = findViewById(R.id.tvTrackingProName);
        tvProProfession = findViewById(R.id.tvTrackingProProfession);
        tvAddress = findViewById(R.id.tvTrackingAddress);
        ivAvatar = findViewById(R.id.ivTrackingProAvatar);
    }

    private void bindData() {
        if (booking == null) return;

        tvProName.setText(booking.getProviderName());
        tvProProfession.setText(booking.getProviderProfession());
        tvAddress.setText(booking.getAddress());
        tvStatus.setText("Technician is On The Way");
        tvEta.setText("Arriving in ~12 mins (3.2 km)");
        tvSpeed.setText("Speed: 32 km/h");
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnCall.setOnClickListener(v -> {
            if (booking != null && booking.getProviderPhone() != null && !booking.getProviderPhone().isEmpty()) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + booking.getProviderPhone())));
            } else {
                Toast.makeText(this, "Provider phone number unavailable", Toast.LENGTH_SHORT).show();
            }
        });

        btnChat.setOnClickListener(v -> {
            if (booking == null) return;
            chatRepository.getOrCreateChannel(booking.getCustomerId(), booking.getCustomerName(),
                    booking.getProviderId(), booking.getProviderName(),
                    new ChatRepository.ChannelCallback() {
                        @Override
                        public void onSuccess(ChatChannel channel) {
                            Intent intent = new Intent(LiveTrackingMapActivity.this, ChatActivity.class);
                            intent.putExtra("channel", channel);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(LiveTrackingMapActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        fabRecenter.setOnClickListener(v -> zoomToRoute());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        // Center around base city coordinates (e.g., Central Tech District)
        LatLng destination = new LatLng(28.6139, 77.2090); // Customer address coordinates
        LatLng techStart = new LatLng(28.6328, 77.2195);    // Initial technician location

        buildSampleRoute(techStart, destination);

        // Destination Marker (Customer Home)
        destinationMarker = mMap.addMarker(new MarkerOptions()
                .position(destination)
                .title("Service Address")
                .snippet(booking != null ? booking.getAddress() : "Customer Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // Technician Marker
        technicianMarker = mMap.addMarker(new MarkerOptions()
                .position(techStart)
                .title(booking != null ? booking.getProviderName() : "Technician")
                .snippet("On The Way - 32 km/h")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        // Polyline connecting route
        routePolyline = mMap.addPolyline(new PolylineOptions()
                .addAll(routePoints)
                .width(12f)
                .color(Color.parseColor("#F59E0B"))
                .geodesic(true));

        zoomToRoute();
        startMovementSimulation();
    }

    private void buildSampleRoute(LatLng start, LatLng end) {
        routePoints.clear();
        routePoints.add(start);
        routePoints.add(new LatLng(28.6290, 77.2180));
        routePoints.add(new LatLng(28.6250, 77.2150));
        routePoints.add(new LatLng(28.6200, 77.2120));
        routePoints.add(new LatLng(28.6160, 77.2105));
        routePoints.add(end);
    }

    private void zoomToRoute() {
        if (mMap == null || routePoints.isEmpty()) return;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : routePoints) {
            builder.include(point);
        }
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 180));
    }

    private void startMovementSimulation() {
        simulationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentRouteIndex < routePoints.size() - 1) {
                    currentRouteIndex++;
                    LatLng nextPos = routePoints.get(currentRouteIndex);
                    if (technicianMarker != null) {
                        technicianMarker.setPosition(nextPos);
                    }

                    int remainingMinutes = Math.max(2, (routePoints.size() - currentRouteIndex) * 2);
                    double remainingKm = Math.max(0.4, (routePoints.size() - currentRouteIndex) * 0.6);
                    tvEta.setText("Arriving in ~" + remainingMinutes + " mins (" + String.format("%.1f", remainingKm) + " km)");

                    if (currentRouteIndex == routePoints.size() - 1) {
                        tvStatus.setText("Technician has arrived at location!");
                        tvEta.setText("Arrived • Please open the door");
                        tvSpeed.setText("Stopped");
                        Toast.makeText(LiveTrackingMapActivity.this, "Technician has arrived at your address!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    simulationHandler.postDelayed(this, 3000);
                }
            }
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simulationHandler.removeCallbacksAndMessages(null);
    }
}
