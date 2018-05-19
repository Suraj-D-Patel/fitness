package patel.d.suraj.fitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by suraj.
 */

public class GainActivity extends AppCompatActivity {

    Button btnday1, btnday2, btnday3, btnday4, btnday5, btnday6, btnday7;
    ArrayAdapter<String> arrayAdapter;
    String arr[] = {"Breakfast", "Lunch", "Dinner"};
    ListView lview;
    int choice = 0;

    @Override
    protected void onResume() {
        super.onResume();
        btnday1.setEnabled(true);
        btnday2.setEnabled(true);
        btnday3.setEnabled(true);
        btnday4.setEnabled(true);
        btnday5.setEnabled(true);
        btnday6.setEnabled(true);
        btnday7.setEnabled(true);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gain);
        btnday1 = (Button) (findViewById(R.id.btnday1));
        btnday2 = (Button) (findViewById(R.id.btnday2));
        btnday3 = (Button) (findViewById(R.id.btnday3));
        btnday4 = (Button) (findViewById(R.id.btnday4));
        btnday5 = (Button) (findViewById(R.id.btnday5));
        btnday6 = (Button) (findViewById(R.id.btnday6));
        btnday7 = (Button) (findViewById(R.id.btnday7));

        lview = (ListView) (findViewById(R.id.listview));
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arr);
        btnday1.setOnClickListener(v -> {
            choice = 1;
            btnday2.setEnabled(false);
            btnday3.setEnabled(false);
            btnday4.setEnabled(false);
            btnday5.setEnabled(false);
            btnday6.setEnabled(false);
            btnday7.setEnabled(false);
        });

        btnday2.setOnClickListener(v -> {
            choice = 2;
            btnday1.setEnabled(false);
            btnday3.setEnabled(false);
            btnday4.setEnabled(false);
            btnday5.setEnabled(false);
            btnday6.setEnabled(false);
            btnday7.setEnabled(false);
        });
        btnday3.setOnClickListener(v -> {
            choice = 3;
            btnday2.setEnabled(false);
            btnday1.setEnabled(false);
            btnday4.setEnabled(false);
            btnday5.setEnabled(false);
            btnday6.setEnabled(false);
            btnday7.setEnabled(false);
        });
        btnday4.setOnClickListener(v -> {
            choice = 4;
            btnday2.setEnabled(false);
            btnday3.setEnabled(false);
            btnday1.setEnabled(false);
            btnday5.setEnabled(false);
            btnday6.setEnabled(false);
            btnday7.setEnabled(false);
        });
        btnday5.setOnClickListener(v -> {
            choice = 5;
            btnday2.setEnabled(false);
            btnday3.setEnabled(false);
            btnday4.setEnabled(false);
            btnday1.setEnabled(false);
            btnday6.setEnabled(false);
            btnday7.setEnabled(false);
        });
        btnday6.setOnClickListener(v -> {
            choice = 6;
            btnday2.setEnabled(false);
            btnday3.setEnabled(false);
            btnday4.setEnabled(false);
            btnday5.setEnabled(false);
            btnday1.setEnabled(false);
            btnday7.setEnabled(false);
        });
        btnday7.setOnClickListener(v -> {
            choice = 7;
            btnday2.setEnabled(false);
            btnday3.setEnabled(false);
            btnday4.setEnabled(false);
            btnday5.setEnabled(false);
            btnday6.setEnabled(false);
            btnday1.setEnabled(false);
        });

        lview.setAdapter(arrayAdapter);

        lview.setOnItemClickListener((parent, view, position, id) -> {
            if (choice == 0) {
                Toast.makeText(GainActivity.this, "Select Any Day", Toast.LENGTH_SHORT).show();
            } else {
                Intent in = new Intent(GainActivity.this, WeightActivity.class);
                in.putExtra("button", choice);
                in.putExtra("time", arr[position]);
                in.putExtra("option1", "Weight Gain");
                startActivity(in);
            }
        });
    }
}