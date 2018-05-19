package patel.d.suraj.fitness;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * Created by suraj.
 */

public class WeightActivity extends AppCompatActivity {
    ImageView imv;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.90);
        setContentView(R.layout.activity_weight);
        getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        imv=(ImageView) (findViewById(R.id.image1));
        tv=(TextView) (findViewById(R.id.tv));

        String option=getIntent().getStringExtra("option1");
        String choice=getIntent().getStringExtra("choice");

        int button=getIntent().getIntExtra("button",0);
        String time=getIntent().getStringExtra("time");
        if(option.equals("Weight Loss")) {

            if(choice.equals("Week 1")) {
                Picasso.with(WeightActivity.this).load(R.drawable.weight_loss_day_1).resize(870,875).into(imv);
            }else if(choice.equals("Week 2")) {
                Picasso.with(WeightActivity.this).load(R.drawable.weight_loss_day_2).resize(870,875).into(imv);
            }else if(choice.equals("Week 3")) {
                Picasso.with(WeightActivity.this).load(R.drawable.weight_loss_day_3).resize(870,875).into(imv);
            }else if(choice.equals("Week 4")) {
                Picasso.with(WeightActivity.this).load(R.drawable.weight_loss_day_4).resize(870,875).into(imv);
            }
        }
        else{
            if(option.equals("Weight Gain")) {
                tv.setVisibility(View.VISIBLE);
                if(button==1||button==6) {
                    tv.setText("Day "+button);
                    if(time.equals("Breakfast")) {
                        imv.setImageResource(R.drawable.weight_gain_bf_day_1_6);
                    }else if(time.equals("Lunch")) {
                        imv.setImageResource(R.drawable.weight_gain_l_day_1_6);
                    }else if(time.equals("Dinner")) {
                        imv.setImageResource(R.drawable.weight_gain_d_day_1_6);
                    }
                }

                if(button==2||button==7) {
                    tv.setText("Day "+button);
                    if(time.equals("Breakfast")) {
                        imv.setImageResource(R.drawable.weight_gain_bf_day_2_7);
                    }else if(time.equals("Lunch")) {
                        imv.setImageResource(R.drawable.weight_gain_l_day_2_7);
                    }else if(time.equals("Dinner")) {
                        imv.setImageResource(R.drawable.weight_gain_d_day_2_7);
                    }
                }
                if(button==3){
                    tv.setText("Day "+button);
                    if(time.equals("Breakfast")) {
                        imv.setImageResource(R.drawable.weight_gain_bf_day_3);
                    }else if(time.equals("Lunch")) {
                        imv.setImageResource(R.drawable.weight_gain_l_day_3);
                    }else if(time.equals("Dinner")) {
                        imv.setImageResource(R.drawable.weight_gain_d_day_3);
                    }
                }
                if(button==4) {
                    tv.setText("Day "+button);
                    if(time.equals("Breakfast")) {
                        imv.setImageResource(R.drawable.weight_gain_bf_day_4);
                    }else if(time.equals("Lunch")){
                        imv.setImageResource(R.drawable.weight_gain_l_day_4);
                    }else if(time.equals("Dinner")){
                        imv.setImageResource(R.drawable.weight_gain_d_day_4);
                    }
                }
                if(button==5) {
                    tv.setText("Day "+button);
                    if(time.equals("Breakfast")) {
                        imv.setImageResource(R.drawable.weight_gain_bf_day_5);
                    }else if(time.equals("Lunch")) {
                        imv.setImageResource(R.drawable.weight_gain_l_day_5);
                    }else if(time.equals("Dinner")) {
                        imv.setImageResource(R.drawable.weight_gain_d_day_5);
                    }
                }
            }
        }
    }
}