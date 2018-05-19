package patel.d.suraj.fitness;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by suraj.
 */
public class SplashActivity extends AppCompatActivity {

    ImageView imv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imv1=(ImageView) (findViewById(R.id.imageView2));
        new Handler().postDelayed(() -> imv1.setImageResource(R.drawable.dumbbell),2000);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            finish();
        },4000);
    }
}