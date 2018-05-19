package patel.d.suraj.fitness;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by suraj.
 */

public class AboutApp_Activity extends AppCompatActivity {

    Button btn_google_login,btn_start_tracking,btn_exercise,btn_music_player,btn_history;
    TextView tv_phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        btn_google_login=(Button)findViewById(R.id.btn_google_login);
        btn_start_tracking=(Button)findViewById(R.id.btn_start_tracking);
        btn_exercise=(Button)findViewById(R.id.btn_exercise);
        btn_music_player=(Button)findViewById(R.id.btn_music_player);
        btn_history=(Button)findViewById(R.id.btn_history);

        tv_phoneno=(TextView)findViewById(R.id.tv_phoneno);
        tv_phoneno.setPaintFlags(tv_phoneno.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btn_google_login.setOnClickListener(view -> {
            startActivity(new Intent(AboutApp_Activity.this,LoginActivity.class));
            Toast.makeText(AboutApp_Activity.this, "Welcome to Login Page", Toast.LENGTH_SHORT).show();
        });

        btn_start_tracking.setOnClickListener(view -> {
            startActivity(new Intent(AboutApp_Activity.this,TrackActivity.class));
        });

        btn_exercise.setOnClickListener(view -> {
            startActivity(new Intent(AboutApp_Activity.this,ChinUpsActivity.class));
        });

        btn_music_player.setOnClickListener(view -> {
            startActivity(new Intent(AboutApp_Activity.this,MusicPlayerActivity.class));
        });

        btn_history.setOnClickListener(view -> {
            startActivity(new Intent(AboutApp_Activity.this,HistoryActivity.class));
        });

        tv_phoneno.setOnClickListener(view -> {
            Intent intent=new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:7508972605"));
            startActivity(intent);
        });
    }
}