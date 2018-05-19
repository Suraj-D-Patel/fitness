package patel.d.suraj.fitness;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by suraj.
 */

public class PushUps extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private TextView tvCounter;
    private int count = 0;
    private boolean justStarted = true;
    private Calendar dateStarted;
    private long timeElapsed = 0;
    Button bt1,bt2;
    TextToSpeech tts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_ups);

        tvCounter = (TextView) findViewById(R.id.tv1);
        bt1=(Button) (findViewById(R.id.bt1));
        bt2=(Button)(findViewById(R.id.bt2));
        tts = new TextToSpeech(getApplicationContext(), i -> tts.setLanguage(Locale.ENGLISH));
        /** Get system service to interact with sensors */
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        /** Find default proximity sensor */
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    public void go(View v) {
        tvCounter.setText("0");
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_UI
        );
        bt1.setEnabled(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (justStarted) {
            justStarted = false;
            dateStarted = Calendar.getInstance();
            return;
        }

        int val = (int) event.values[0];
        int a=count;

        if (val > 1) {
            val = 1;
            tts.speak(String.valueOf(a+=1), TextToSpeech.QUEUE_FLUSH, null);
        }

        count += val;
        tvCounter.setText(String.valueOf(count));
    }

    public void onClick(View v) {
        count += 1;
        tvCounter.setText(String.valueOf(count));
    }

    public void finishCounting(View v) {

        PushUpData data = new PushUpData(getApplicationContext());
        Calendar currentDate = Calendar.getInstance();
        timeElapsed = currentDate.getTimeInMillis() - dateStarted.getTimeInMillis();

        try {
            data.writeData(count, timeElapsed);
        } catch (IOException e) {
            e.printStackTrace();
        }

        bt1.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}