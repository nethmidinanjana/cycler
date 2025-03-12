package lk.nd.cycler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewActivity extends AppCompatActivity {

    MapView shopMapView;
    GoogleMap gMap;
    LatLng shopLatLng;
    String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        shopMapView = findViewById(R.id.shopMapView);

        Intent intent = getIntent();

        shopName = intent.getStringExtra("shopName");

        String latStr = intent.getStringExtra("latitude");
        String lngStr = intent.getStringExtra("longitude");

        if (latStr != null && lngStr != null) {
            try {
                double latitude = Double.parseDouble(latStr);
                double longitude = Double.parseDouble(lngStr);
                shopLatLng = new LatLng(latitude, longitude);

                Log.d("MapViewActivity", "Received Latitude: " + latitude + ", Longitude: " + longitude);
                // Now use shopLatLng to display on the map

            } catch (NumberFormatException e) {
                Log.e("MapViewActivity", "Invalid Latitude/Longitude format", e);
            }
        } else {
            Log.e("MapViewActivity", "Latitude or Longitude data missing");
        }

        // Only proceed if shopLatLng is valid
        if (shopLatLng != null) {
            SupportMapFragment supportMapFragment = new SupportMapFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.shopMapView, supportMapFragment);
            fragmentTransaction.commit();

            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    Log.i("Map", "Map is ready.");

                    if (shopLatLng != null) {  // Ensure it's not null before using it
                        googleMap.addMarker(
                                new MarkerOptions()
                                        .position(shopLatLng)
                                        .title(shopName)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                        );
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shopLatLng, 20));
                    } else {
                        Log.e("Map", "shopLatLng is null. Cannot place marker.");
                    }

                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                }
            });
        } else {
            Toast.makeText(this, "Invalid location data received", Toast.LENGTH_SHORT).show();
        }

    }
}