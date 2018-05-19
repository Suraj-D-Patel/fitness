package patel.d.suraj.fitness;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Locale;

/**
 * Created by suraj.
 */

public class SitUpsActivity extends AppCompatActivity {

    ImageView imgview;
    String array[] = {"Push Up", "Sit Up", "Chin Up"};
    String choice;
    int count = 0;
    Thread thread;
    Button btn_start_situps, btn_stop_situps;
    TextView txtdesc;
    TextToSpeech tts;
    boolean flag= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sit_ups);
        count = 0;
        tts = new TextToSpeech(getApplicationContext(), i -> tts.setLanguage(Locale.ENGLISH));

        txtdesc = (TextView) (findViewById(R.id.txtdesc));
        imgview = (ImageView) (findViewById(R.id.imgview));
        btn_start_situps = (Button) (findViewById(R.id.btn_start_situps));
        btn_stop_situps = (Button) (findViewById(R.id.btn_stop_situps));
        choice = getIntent().getStringExtra("option12");

        try {
            if (choice.equals(array[0])) {
                imgview.setImageResource(R.drawable.push_ups);
            } else if (choice.equals(array[1])) {
                imgview.setImageResource(R.drawable.situp_sitdown);
            } else if (choice.equals(array[2])) {
                imgview.setImageResource(R.drawable.pull_ups);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btn_start_situps.setOnClickListener(v -> {
            if (choice.equals(array[1])) {
                imgview.setImageResource(R.drawable.sitdown);
                thread = new Thread(new myjob());
                thread.start();
                btn_start_situps.setEnabled(false);
            }
        });

        btn_stop_situps.setOnClickListener(v -> btn_start_situps.setEnabled(true));

        //let the screen donot sleep when this activity is opened
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
                  /*  Log.d("MyMSG", "" + count);*/
                    if (count % 2 != 0) {
                        no++;
                        runOnUiThread(() -> {
                            btn_start_situps.setText("" + no);
                            imgview.setImageResource(R.drawable.situp);
                        });
                        tts.speak("sit up", TextToSpeech.QUEUE_FLUSH, null);
                    } else {
                        runOnUiThread(() -> imgview.setImageResource(R.drawable.sitdown));
                        tts.speak("sit down", TextToSpeech.QUEUE_FLUSH, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(() -> {
                btn_start_situps.setText("Start");
                btn_start_situps.setEnabled(true);
            });
        }
    }

    @Override
    protected void onDestroy() {
        if(flag) {
            flag = false;
            thread.interrupt();
        }
        super.onDestroy();
    }
}