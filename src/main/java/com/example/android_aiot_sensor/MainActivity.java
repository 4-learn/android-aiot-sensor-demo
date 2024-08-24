package com.example.android_aiot_sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor rotationVectorSensor;
    private ImageView compassImageView;
    private ProgressBar progressBarPitch;
    private TextView textViewWarning;
    private Button buttonStartStop;
    private boolean isMonitoring = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 UI 元件
        compassImageView = findViewById(R.id.compassImageView);
        progressBarPitch = findViewById(R.id.progressBarPitch);
        textViewWarning = findViewById(R.id.textViewWarning);
        buttonStartStop = findViewById(R.id.buttonStartStop);

        // 獲取 SensorManager 實例
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // 設置按鈕點擊事件
        buttonStartStop.setOnClickListener(v -> {
            if (isMonitoring) {
                stopMonitoring();
            } else {
                startMonitoring();
            }
        });
    }

    private void startMonitoring() {
        // TODO
    }

    private void stopMonitoring() {
        // TODO
        progressBarPitch.setProgress(0);
        // TODO
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // TODO

            // 更新指南針圖標的旋轉
            compassImageView.setRotation(-azimuth);

            // 更新進度條來顯示俯仰角
            progressBarPitch.setProgress(Math.abs((int) pitch));

            // TODO: 當俯仰角超過 30 度時顯示警告
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 當感測器的精度改變時，可以在這裡處理（本示例中不需要處理）
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMonitoring) {
            sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isMonitoring) {
            sensorManager.unregisterListener(this);
        }
    }
}