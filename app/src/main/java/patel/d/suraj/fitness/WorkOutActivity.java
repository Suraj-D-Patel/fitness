package patel.d.suraj.fitness;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Locale;

/**
 * Created by suraj.
 */

public class WorkOutActivity extends AppCompatActivity {

    ImageView imv;
    String a[] = {"Push Up", "Sit Up", "Chin Up"};
    String choice;
    int count = 0;
    Thread t;
    Button bt1, bt2;
    TextView tv;
    TextToSpeech tts;
    boolean flag= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out);
        count = 0;
        tts = new TextToSpeech(getApplicationContext(), i -> tts.setLanguage(Locale.ENGLISH));

        tv = (TextView) (findViewById(R.id.textView5));
        imv = (ImageView) (findViewById(R.id.img));
        bt1 = (Button) (findViewById(R.id.bt1));
        bt2 = (Button) (findViewById(R.id.bt2));
        choice = getIntent().getStringExtra("option12");

        try {
            if (choice.equals(a[0])) {
                imv.setImageResource(R.drawable.push);
            } else if (choice.equals(a[1])) {
                imv.setImageResource(R.drawable.situp_sitdown);
            } else if (choice.equals(a[2])) {
                imv.setImageResource(R.drawable.pull);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        bt1.setOnClickListener(v -> {
            if (choice.equals(a[1])) {
                imv.setImageResource(R.drawable.sitdown);
                t = new Thread(new myjob());
                t.start();
                bt1.setEnabled(false);
            }
        });

        bt2.setOnClickListener(v -> bt1.setEnabled(true));
    }

    class myjob implements Runnable {

        int no = 0;
        @Override
        public void run() {
            count = 0;
            flag=true;
            while (count < 20 && flag) {
                try {
                    Thread.sleep(2000);
                    count++;
                    Log.d("MyMSG", "" + count);
                    if (count % 2 != 0) {
                        no++;
                        runOnUiThread(() -> {
                            bt1.setText("" + no);
                            imv.setImageResource(R.drawable.situp);
                        });
                        tts.speak("sit up", TextToSpeech.QUEUE_FLUSH, null);
                    } else {
                        runOnUiThread(() -> imv.setImageResource(R.drawable.sitdown));
                        tts.speak("sit down", TextToSpeech.QUEUE_FLUSH, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(() -> {
                bt1.setText("Start");
                bt1.setEnabled(true);
            });
        }
    }

    @Override
    protected void onDestroy() {
        if(flag) {
            flag = false;
            t.interrupt();
        }
        super.onDestroy();
    }
}