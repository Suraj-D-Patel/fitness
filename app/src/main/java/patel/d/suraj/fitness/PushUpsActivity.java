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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by suraj.
 */

public class PushUpsActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private TextView tvCounter;
    private int count = 0;
    private boolean justStarted = true;
    private Calendar dateStarted;
    private long timeElapsed = 0;
    Button btn_start_pushups,btn_stop_pushups;
    TextToSpeech tts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_ups);

        tvCounter = (TextView) findViewById(R.id.tv_pushups_counter);
        btn_start_pushups=(Button) (findViewById(R.id.btn_start_pushups));
        btn_stop_pushups=(Button)(findViewById(R.id.btn_stop_pushups));
        tts = new TextToSpeech(getApplicationContext(), i -> tts.setLanguage(Locale.ENGLISH));
        /** Get system service to interact with sensors */
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        /** Find default proximity sensor */
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //let the screen donot sleep when this activity is opened
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tvCounter.setOnClickListener((v)->{
            count += 1;
            tvCounter.setText(String.valueOf(count));
        });

        btn_start_pushups.setOnClickListener((c)->{
            tvCounter.setText("0");
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                    SensorManager.SENSOR_DELAY_UI
            );
            btn_start_pushups.setEnabled(false);
        });

        btn_stop_pushups.setOnClickListener((d)->{
            PushUpData data = new PushUpData(getApplicationContext());
            Calendar currentDate = Calendar.getInstance();
            timeElapsed = currentDate.getTimeInMillis() - dateStarted.getTimeInMillis();

            try {
                data.writeData(count, timeElapsed);
            } catch (IOException e) {
                e.printStackTrace();
            }

            btn_start_pushups.setEnabled(true);
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}