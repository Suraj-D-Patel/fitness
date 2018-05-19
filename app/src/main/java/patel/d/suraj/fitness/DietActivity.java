package patel.d.suraj.fitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * Created by suraj.
 */

public class DietActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    Button btn_view;
    ListView listView;
    Myadapter adapter;
    String arr[]={"Week 1","Week 2","Week 3","Week 4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        imageView=(ImageView) (findViewById(R.id.imageview_diet));
        listView=(ListView) (findViewById(R.id.listview_diet));
        btn_view=(Button)(findViewById(R.id.btn_view_diet));
        textView=(TextView) (findViewById(R.id.tv_diet));

        adapter=new Myadapter();

        final String option=getIntent().getStringExtra("option");

        if(option.equals("Weight Loss")) {
            imageView.setImageResource(R.drawable.apple);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent=new Intent(DietActivity.this,WeightActivity.class);
                intent.putExtra("choice",arr[position]);
                intent.putExtra("option1",option);
                startActivity(intent);
            });
        }
        else {
            Picasso.with(DietActivity.this).load(R.drawable.weight_gain_pic_one).resize(290,215).into(imageView);
            btn_view.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }

        btn_view.setOnClickListener(v -> {
            startActivity(new Intent(DietActivity.this,GainActivity.class));
        });
    }

    class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arr.length;
        }

        @Override
        public Object getItem(int i) {
            return arr[i];
        }

        @Override
        public long getItemId(int i) {
            return i * 10;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(R.layout.row_layout_listview, viewGroup, false);

            ImageView imv;
            final TextView textview_in_listview, textview2_in_listview;
            imv=(ImageView) (view.findViewById(R.id.imageview_in_listview));
            textview_in_listview = (TextView) (view.findViewById(R.id.textview_in_listview));
            textview2_in_listview = (TextView) (view.findViewById(R.id.textview2_in_listview));

            textview_in_listview.setTextSize(20);
            textview2_in_listview.setVisibility(View.INVISIBLE);

            if(i==0) {
                Picasso.with(DietActivity.this).load(R.drawable.week1).resize(40,40).into(imv);
            }else if(i==1){
                Picasso.with(DietActivity.this).load(R.drawable.week2).resize(40,40).into(imv);
            }else if(i==2){
                Picasso.with(DietActivity.this).load(R.drawable.week3).resize(40,40).into(imv);
            }else if(i==3){
                Picasso.with(DietActivity.this).load(R.drawable.week4).resize(40,40).into(imv);
            }
            textview_in_listview.setText(arr[i]);

            return view;
        }
    }
}