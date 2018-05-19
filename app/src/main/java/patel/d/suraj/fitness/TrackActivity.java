package patel.d.suraj.fitness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by suraj.
 */

public class TrackActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView tv1, tv2, tv3;
    double lat1, lon1;
    Button bt1;
    LocationManager lm;
    Intent intent;
    LocalBroadcastManager lbm;
    long startTime = 0;
    mylocationlistener mylocationlistenerobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        tv2 = (TextView) (findViewById(R.id.tv2));
        tv1 = (TextView) (findViewById(R.id.tv1));
        tv3 = (TextView) (findViewById(R.id.tv3));
        bt1 = (Button) (findViewById(R.id.bt1));
        bt1.setOnClickListener(v -> {
            if (bt1.getText().toString().toUpperCase().equals("START")) {
                startActivityForResult(new Intent(TrackActivity.this, StartActivityForResult.class), 80);
            } else if (bt1.getText().toString().toUpperCase().equals("STOP")) {
                Intent in3 = new Intent();
                in3.putExtra("name", bt1.getText().toString().toUpperCase());
                bt1.setText("Start");

                Toast.makeText(getApplicationContext(), "stopped", Toast.LENGTH_SHORT).show();
                stopService(intent);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setPositiveButton("OK", (dialog, which) ->
                {
                    new SweetAlertDialog(TrackActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Save Confirmed")
                            .setContentText("Your Session was Saved!!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(sDialog -> {sDialog.dismissWithAnimation();})
                            .show();
                    startActivity(new Intent(TrackActivity.this,MainActivity.class));
                    finish();
                });
                alertDialog.setNegativeButton("Cancel", (DialogInterface dialog, int which) -> {
                            new SweetAlertDialog(TrackActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Save Cancelled")
                                    .setContentText("Your Session was not Saved!!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(sDialog -> {sDialog.dismissWithAnimation();})
                                    .show();
                        }).setMessage("Do you want to save the Session");

                AlertDialog dialog=alertDialog.create();
                dialog.show();
            }
        });
//         Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        /////////////////   GET LAST GPS AND NW LOCTIONS if Available  ///////////
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastlcgps = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lastlcnw = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (lastlcgps == null) {        } else {
            lat1 = lastlcgps.getLatitude();
            lon1 = lastlcgps.getLongitude();
        }

        if (lastlcnw == null) { } else {
            lat1 = lastlcnw.getLatitude();
            lon1 = lastlcnw.getLongitude();
        }
        ////////   Logic to get CURRENT LOCATIONS /////////////////
        //---check if GPS_PROVIDER is enabled---
        boolean gpsStatus = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //---check if NETWORK_PROVIDER is enabled---
        boolean networkStatus = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        mylocationlistenerobj = new mylocationlistener();
        // check which provider is enabled
        if (gpsStatus == false && networkStatus == false) {
            Toast.makeText(this, "Both GPS and Newtork are disabled", Toast.LENGTH_LONG).show();
            //---display the "Location services" settings page---
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        if (gpsStatus == true) {
            Toast.makeText(this, "GPS is Enabled, using it", Toast.LENGTH_LONG).show();
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mylocationlistenerobj);
        }

        if (networkStatus == true) {
            Toast.makeText(this, "Network Location is Enabled, using it", Toast.LENGTH_LONG).show();
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mylocationlistenerobj);
        }
        lbm = LocalBroadcastManager.getInstance(this);
        myreciever obj = new myreciever();
        lbm.registerReceiver(obj, new IntentFilter("com.fitness.app"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 80) {
            if (resultCode == RESULT_OK) {
                startTime = System.currentTimeMillis();
                String session = data.getStringExtra("session");
                Toast.makeText(this, session, Toast.LENGTH_SHORT).show();
                bt1.setText("Stop");
                intent = new Intent(TrackActivity.this, MyService.class);
                intent.putExtra("session", session);
                startService(intent);
            }
        }
    }

    ///// Inner Class  //////////////////
    class myreciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("com.fitness.app")) {
                if (!intent.getStringExtra("meters").equals("na")) {
                    String m = intent.getStringExtra("meters");
                    tv3.setText(m);
                }
                if (!intent.getStringExtra("minutes").equals("na")) {
                    String t = intent.getStringExtra("minutes");
                    tv1.setText(t);
                }
            }
        }
    }

    class mylocationlistener implements LocationListener {

        public void onLocationChanged(Location location) {
            double lat, lon;
            String name1;
            List<Address> name;
            Geocoder geocoder;
            geocoder = new Geocoder(TrackActivity.this, Locale.getDefault());
            lat = location.getLatitude();
            lon = location.getLongitude();
            try {
                name = geocoder.getFromLocation(lat, lon, 1);
                name1 = name.get(0).getAddressLine(0);
                LatLng mylocation = new LatLng(lat, lon);
                mMap.clear();
                MarkerOptions markerOptions = new MarkerOptions().position(mylocation).title(name1);
                mMap.addMarker(markerOptions);
                lm.removeUpdates(mylocationlistenerobj);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        public void onProviderDisabled(String provider){}

        public void onProviderEnabled(String provider){}

        public void onStatusChanged(String provider, int status, Bundle extras){}
    }

    @Override
    public void onBackPressed() {
        if (bt1.getText().toString().toUpperCase().equals("START")) {
            super.onBackPressed();
        } else if (bt1.getText().toString().toUpperCase().equals("STOP")) {
            Toast.makeText(this, "Stop Service first", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng mylocation = new LatLng(lat1, lon1);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(18);
        mMap.animateCamera(cameraUpdate);
    }
}