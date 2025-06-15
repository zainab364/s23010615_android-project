package com.s23010615.zainab;

import android.content.Intent;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;



public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor tempSensor;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Handler handler = new Handler();
    private boolean hasPlayed = false;

    private View txt;

    private final float TEMP_THRESHOLD = 15f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        seekBar = findViewById(R.id.seekBar);
        mediaPlayer = MediaPlayer.create(this, R.raw.bgmusic);
        txt = findViewById(R.id.textViewLogout);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SensorActivity.this, LoginActivity.class));

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
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
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float currentTemp = event.values[0];

        if (currentTemp > TEMP_THRESHOLD && !hasPlayed) {
            mediaPlayer.start();
            hasPlayed = true;
            updateSeekBar();
        } else if (currentTemp <= TEMP_THRESHOLD) {
            hasPlayed = false;
        }
    }

    private void updateSeekBar() {
        seekBar.setMax(mediaPlayer.getDuration());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
