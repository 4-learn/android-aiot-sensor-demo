package com.example.android_aiot_sensor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private TextView textViewLatitude, textViewLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 TextView 用於顯示緯度和經度
        textViewLatitude = findViewById(R.id.textViewLatitude);
        textViewLongitude = findViewById(R.id.textViewLongitude);

        // 獲取 LocationManager 實例
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 檢查位置權限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // 如果未授予權限，請求權限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // 如果已經有權限，開始位置更新
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        // 請求位置更新
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            // 當位置改變時更新緯度和經度
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            textViewLatitude.setText("Latitude: " + latitude);
            textViewLongitude.setText("Longitude: " + longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            // 當 GPS 提供者啟用時，顯示一條消息或處理狀態
            textViewLatitude.setText("GPS Enabled");
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            // 當 GPS 提供者禁用時，顯示一條消息或處理狀態
            textViewLatitude.setText("GPS Disabled");
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 權限授予，開始位置更新
                startLocationUpdates();
            } else {
                // 權限被拒絕，顯示一條消息或處理拒絕情況
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在活動暫停時停止位置更新以節省電量
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在活動恢復時重新啟動位置更新
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
    }
}
