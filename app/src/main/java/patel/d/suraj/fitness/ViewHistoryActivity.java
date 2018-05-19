package patel.d.suraj.fitness;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
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

public class ViewHistoryActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView tv1, tv2, tv3,tv4,tv5,tv6,tv7,tv8;
    ArrayList<String> lat=null, lng=null;
    private GoogleMap mMap;
    PolylineOptions polyoption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        lat = new ArrayList<>();
        lng = new ArrayList<>();

        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_map));
        mapFragment.getMapAsync(this);

        tv1 = (TextView) (findViewById(R.id.tv1));
        tv2 = (TextView) (findViewById(R.id.tv2));
        tv3 = (TextView) (findViewById(R.id.tv3));
        tv4 = (TextView) (findViewById(R.id.tv4));
        tv5 = (TextView) (findViewById(R.id.tv5));
        tv6 = (TextView) (findViewById(R.id.tv6));
        tv7 = (TextView) (findViewById(R.id.tv7));
        tv8 = (TextView) (findViewById(R.id.tv8));
        String key = getIntent().getStringExtra("Key");

        SharedPreferences sharedPreferences= ViewHistoryActivity.this.getSharedPreferences("USERNAME",MODE_PRIVATE);
        String username=sharedPreferences.getString("userz",null);


        ////////////   Code to Fetch Data from Firebase  //////////
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mainref = firebaseDatabase.getReference("Session");
        DatabaseReference myref = mainref.child(key);
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Session obj = dataSnapshot.getValue(Session.class);
                assert obj != null;
                tv1.setText(obj.getSessionname());
                tv2.setText(obj.getStartTime());
                tv3.setText(obj.getEndTime());
                tv4.setText(obj.getDate());








                tv5.setText(username);







                lat = obj.getLat();
                lng = obj.getLng();
                tv6.setText(obj.getMillis()/1000 + " seconds");
                //Log.d("<><>", "onDataChange: "+obj.getMillis()/1000);
                tv7.setText(obj.getMeters() + " meters");
                tv8.setText(obj.getCalorie() + " KCal");

                polyoption = new PolylineOptions();
                int i = 0;
                if (lat != null || lng != null) {
                    while (i < lat.size()) {
                        double lat1 = Double.parseDouble(lat.get(i));
                        double lng1 = Double.parseDouble(lng.get(i));
                        polyoption.add(new LatLng(lat1, lng1)).width(10).color(Color.RED);
                        if (i == 0) {
                            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat1, lng1)).title("Starting Point");
                            mMap.addMarker(markerOptions);
                        }
                        if (i == (lat.size() - 1)) {
                            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat1, lng1)).title("Ending Point");
                            mMap.addMarker(markerOptions);
                        }
                        i++;
                    }
                }else{
                    Toast.makeText(ViewHistoryActivity.this, "Insufficient Data to be viewed!!", Toast.LENGTH_SHORT).show();
                }

               
//        MarkerOptions markerOptions = new MarkerOptions().position(mylocation).title("Welcome to Amritsar");
//        mMap.addMarker(markerOptions);

                mMap.addPolyline(polyoption);
                double lat1 = Double.parseDouble(lat.get(0));
                double lng1 = Double.parseDouble(lng.get(0));
                LatLng mylocation = new LatLng(lat1, lng1);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
                CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(16);
                mMap.animateCamera(cameraUpdate);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
    public void onMapReady(GoogleMap googleMap) { mMap = googleMap; }
}