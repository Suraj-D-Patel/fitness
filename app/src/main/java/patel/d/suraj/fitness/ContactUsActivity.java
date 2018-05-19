package patel.d.suraj.fitness;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by suraj.
 */

public class ContactUsActivity extends AppCompatActivity {
    TextView tv_phone,tv_phone1,tv_email,visit;
    Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        tv_phone=findViewById(R.id.tv_phone);
        tv_phone1=findViewById(R.id.tv_phone1);
        tv_email=findViewById(R.id.tv_email);
        visit=findViewById(R.id.visit);
        btn_ok=findViewById(R.id.btnok);
        tv_phone.setPaintFlags(tv_phone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_phone1.setPaintFlags(tv_phone1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_email.setPaintFlags(tv_email.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        visit.setPaintFlags(visit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tv_phone.setOnClickListener(view -> {
            Intent in=new Intent(Intent.ACTION_DIAL);
            in.setData(Uri.parse("tel:7508972605"));
            startActivity(in);
        });

        tv_phone1.setOnClickListener(view -> {
            Intent in=new Intent(Intent.ACTION_DIAL);
            in.setData(Uri.parse("tel:7986180139"));
            startActivity(in);
        });

        btn_ok.setOnClickListener(view -> {
            startActivity(new Intent(ContactUsActivity.this,MainActivity.class));
        });
    }
}