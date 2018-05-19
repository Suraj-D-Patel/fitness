package patel.d.suraj.fitness;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by suraj.
 */

public class HistoryActivity extends AppCompatActivity {

    ArrayList<Session> arrayList;
    ListView listv;
    MYADAPTERz adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        arrayList=new ArrayList<>();
        listv=(ListView) (findViewById(R.id.listv));
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myref=firebaseDatabase.getReference("Session");

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot dsp:dataSnapshot.getChildren()) {
                    Session session=dsp.getValue(Session.class);
                    arrayList.add(session);
                    Log.d("MYMSG",dsp.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        adapter=new MYADAPTERz();
        listv.setAdapter(adapter);
    }

    class MYADAPTERz extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position*10;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=LayoutInflater.from(HistoryActivity.this);
            convertView=inflater.inflate(R.layout.custom_layout_history,parent,false);
            final Session obj=arrayList.get(position);

            SharedPreferences sharedPreferences= HistoryActivity.this.getSharedPreferences("USERNAME",MODE_PRIVATE);
            String zuser=sharedPreferences.getString("userz",null);

            String sessionNAme="Session Name: "+obj.sessionname;
            TextView tv1,tv2,tv3;
            Button bt1,bt2;
            String DurationinHISTORY=obj.StartTime+" -- "+obj.EndTime;
            bt1=(Button)(convertView.findViewById(R.id.bt1));
            bt2=(Button)(convertView.findViewById(R.id.bt2));
            tv1=(TextView) (convertView.findViewById(R.id.tv1));
            tv2=(TextView) (convertView.findViewById(R.id.tv2));
            tv3=(TextView) (convertView.findViewById(R.id.tv3));

            tv1.setText(sessionNAme);
            tv2.setText(zuser);
            tv3.setText(DurationinHISTORY);


            bt1.setOnClickListener(v -> {
                String Key=obj.getSessionname()+obj.getUsername()+obj.getDate();

                Intent in =new Intent(HistoryActivity.this,ViewHistoryActivity.class);
                in.putExtra("Key",Key);
                startActivity(in);
            });

            bt2.setOnClickListener(v -> {
                AlertDialog.Builder builder=new AlertDialog.Builder(HistoryActivity.this);
                builder.setTitle("Delete Message");
                builder.setMessage("Are You Sure You Want to delete this?");
                builder.setIcon(R.drawable.ic_delete_forever_black_24dp);
                builder.setPositiveButton("OK", (dialog, which) -> {
                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                    DatabaseReference mainref=firebaseDatabase.getReference("Session");

                    DatabaseReference myref=mainref.child(obj.getSessionname()+obj.getUsername()+obj.getDate());
                    myref.removeValue();

                    Toast.makeText(HistoryActivity.this, obj.getSessionname()+obj.getUsername()+obj.getDate()+" Record Deleted", Toast.LENGTH_SHORT).show();
                }).setNegativeButton("Cancel", (dialog, which) ->
                        Toast.makeText(HistoryActivity.this, "Nothing is Deleted", Toast.LENGTH_SHORT).show());

                AlertDialog alertDialog= builder.create();
                alertDialog.show();
            });
            return convertView;
        }
    }
}