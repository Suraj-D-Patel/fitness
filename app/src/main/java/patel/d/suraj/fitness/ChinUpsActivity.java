package patel.d.suraj.fitness;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;

/**
 * Created by suraj.
 */

public class ChinUpsActivity extends AppCompatActivity implements SensorEventListener, StepListener {
    int pushup=0;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Chin Ups: ";
    private int numSteps;
    TextToSpeech tts;

    TextView TvSteps;
    Button BtnStart,BtnStop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chin_ups);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        tts = new TextToSpeech(getApplicationContext(), i -> tts.setLanguage(Locale.ENGLISH));

        TvSteps = (TextView) findViewById(R.id.tv_steps);
        BtnStart = (Button) findViewById(R.id.btn_start);
        BtnStop = (Button) findViewById(R.id.btn_stop);
        BtnStart.setOnClickListener(arg0 -> {
            numSteps = 0;
            sensorManager.registerListener(ChinUpsActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
            BtnStart.setEnabled(false);
        });

        BtnStop.setOnClickListener(arg0 -> {
            BtnStart.setEnabled(true);
            sensorManager.unregisterListener(ChinUpsActivity.this);
        });

        //let the screen donot sleep when this activity is opened
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        if(numSteps%2==0) {
            pushup=pushup+1;
            tts.speak(pushup+"", TextToSpeech.QUEUE_FLUSH, null);
            TvSteps.setText(TEXT_NUM_STEPS + pushup);
        }
    }
}