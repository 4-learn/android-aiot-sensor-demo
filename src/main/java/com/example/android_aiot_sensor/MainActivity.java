package com.example.android_aiot_sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor rotationVectorSensor;
    private TextView textViewAzimuth, textViewPitch, textViewRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 TextView 來顯示數據
        textViewAzimuth = findViewById(R.id.textViewAzimuth);
        textViewPitch = findViewById(R.id.textViewPitch);
        textViewRoll = findViewById(R.id.textViewRoll);

        // 獲取 SensorManager 實例
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // 獲取旋轉向量感測器
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // 註冊感測器監聽器
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // 創建旋轉矩陣
            float[] rotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            // 轉換旋轉矩陣為方向數據（俯仰角、滾動角、方位角）
            float[] orientationValues = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientationValues);

            // 方向數據的角度變為度數（從弧度轉換為度數）
            float azimuth = (float) Math.toDegrees(orientationValues[0]); // 方位角
            float pitch = (float) Math.toDegrees(orientationValues[1]);   // 俯仰角
            float roll = (float) Math.toDegrees(orientationValues[2]);    // 滾動角

            // 更新 UI 顯示
            textViewAzimuth.setText("Azimuth: " + azimuth);
            textViewPitch.setText("Pitch: " + pitch);
            textViewRoll.setText("Roll: " + roll);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 當感測器的精度改變時，可以在這裡處理（本示例中不需要處理）
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 當 Activity 重新啟動時，重新註冊感測器監聽器
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 當 Activity 暫停時，解除感測器監聽器的註冊
        sensorManager.unregisterListener(this);
    }
}
