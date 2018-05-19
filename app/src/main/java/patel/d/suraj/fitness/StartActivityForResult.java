package patel.d.suraj.fitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by suraj.
 */
public class StartActivityForResult extends AppCompatActivity {

    EditText et1;
    Button bt1,bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_for_result);

        et1=(EditText) (findViewById(R.id.et1));
        bt1=(Button) (findViewById(R.id.bt1));
        bt2=(Button) (findViewById(R.id.bt2));

        bt1.setOnClickListener(v -> {
            if(et1.getText().toString().equals("")) {
                Toast.makeText(StartActivityForResult.this,"Enter Session name",Toast.LENGTH_SHORT).show();
            }
            else {
                String session=et1.getText().toString();
                Intent in=new Intent();
                in.putExtra("session",session);
                setResult(RESULT_OK,in);
                finish();
            }
        });
        bt2.setOnClickListener(v -> finish());
    }
}