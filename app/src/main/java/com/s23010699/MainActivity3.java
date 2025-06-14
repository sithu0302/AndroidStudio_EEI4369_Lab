package com.s23010699;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor tempSensor;
    private TextView txtTemperature, statusText;
    private MediaPlayer mediaPlayer;
    private boolean hasPlayed = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        txtTemperature = findViewById(R.id.txtTemperature);
        statusText = findViewById(R.id.statusText);

        mediaPlayer = MediaPlayer.create(this, R.raw.sound); // Add sound.mp3 under res/raw

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }

        if (tempSensor == null) {
            txtTemperature.setText("Temperature sensor not available.");
            statusText.setText("Status: Sensor not supported.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tempSensor != null) {
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        float temperature = event.values[0];
        txtTemperature.setText("Current Temperature: " + temperature + "°C");

        // use last 2 digits of your SID
        float THRESHOLD = 99f;
        if (temperature > THRESHOLD && !hasPlayed) {
            mediaPlayer.start();
            hasPlayed = true;
            statusText.setText(" High Temp Detected! Audio played.");
        } else if (temperature <= THRESHOLD && hasPlayed) {
            hasPlayed = false;
            statusText.setText("Temp back to normal.");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
