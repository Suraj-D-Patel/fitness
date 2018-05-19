package patel.d.suraj.fitness;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by suraj.
 */

public class MyService extends Service implements LocationListener, StepListener, SensorEventListener {

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    LocationManager lm;
    ArrayList<String> lat, lng;
    Long start, stop;
    String date;
    String StartTime;
    String endTime;
    double latitude = 0, longitude = 0;
    String session;
    double meter = 0;
    LocalBroadcastManager lbm;
    long startTime = 0;
    Handler timeHandler;
    int cal = 0;
    long millis;

    Intent in1;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw new UnsupportedOperationException("Not yet implemented");


    }

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {

            millis = millis + 1000;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            //Intent in2=new Intent("com.fitness.app");
            in1 = new Intent("com.fitness.app");

//            //in2.putExtra("calorie",c);
            in1.putExtra("minutes", minutes + ":" + seconds);
            in1.putExtra("meters", "na");


            lbm.sendBroadcast(in1);
            //            lbm.sendBroadcast(in2);

            timeHandler.postDelayed(this, 1000);
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        lat = new ArrayList<>();

        numSteps = 0;


        lng = new ArrayList<>();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);

//        Toast.makeText(MyService.this, date+","+formattedTime, Toast.LENGTH_SHORT).show();
        boolean gpsStatus = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);


        //---check if NETWORK_PROVIDER is enabled---

        boolean networkStatus = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        // check which provider is enabled

        if (gpsStatus == false && networkStatus == false)

        {

            Toast.makeText(MyService.this, "Both GPS and Newtork are disabled", Toast.LENGTH_LONG).show();


            //---display the "Location services" settings page---

            Intent in = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);

            startActivity(in);

        }


        if (networkStatus == true)

        {

            Toast.makeText(MyService.this, "Network Location is Enabled, using it", Toast.LENGTH_LONG).show();

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this);

        }
        if (gpsStatus == true)

        {

            Toast.makeText(MyService.this, "GPS is Enabled, using it", Toast.LENGTH_LONG).show();

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);

        }


        startTime = System.currentTimeMillis();

        timeHandler = new Handler();


        timeHandler.postDelayed(timerRunnable, 0);

        lbm = LocalBroadcastManager.getInstance(this);
        session = intent.getStringExtra("session");
        android.text.format.Time time = new android.text.format.Time();
        time.setToNow();
        start = time.toMillis(false);


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");

        date = df.format(c.getTime());

        StartTime = df1.format(c.getTime());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        sensorManager.unregisterListener(this);

        if (meter < 1000) {
            Toast.makeText(this, "Total distance run:" + meter + "m", Toast.LENGTH_SHORT).show();
        } else if (meter >= 1000) {
            Toast.makeText(this, "Total distance run:" + (meter + 0.000) / 1000 + "Km", Toast.LENGTH_SHORT).show();
        }
        lm.removeUpdates(this);

        android.text.format.Time time = new android.text.format.Time();
        time.setToNow();
        stop = time.toMillis(false);


        double diff = (double) (stop - start) / 360000;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = df1.format(c.getTime());
        endTime = formattedTime.toString();
        Toast.makeText(this, formattedTime + " Total Time=" + diff + "hrs", Toast.LENGTH_SHORT).show();


        final SharedPreferences mypref = getSharedPreferences("data.txt", MODE_PRIVATE);

        final String username = mypref.getString("username", null);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference db = firebaseDatabase.getReference("Session");
        final DatabaseReference myRef = db.child(session + username + date);
        final Session obj = new Session(session, StartTime, endTime, date, username, lat, lng, meter, millis, cal);

        myRef.setValue(obj);
        Toast.makeText(getApplicationContext(), "Record Added Successfully", Toast.LENGTH_SHORT).show();
        timeHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat, lon;


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
        Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            lat = loc.getLatitude();
            lon = loc.getLongitude();
            if(latitude==0&&longitude==0)
            {
                latitude=lat;
                longitude=lon;
                this.lat.add(latitude+"");
                this.lng.add(longitude+"");
                Toast.makeText(MyService.this, "Current: " + latitude + "," + longitude, Toast.LENGTH_SHORT).show();

            }
            else{
                if(!(latitude == lat || longitude== lon))
                {

                    double km=distance(latitude,longitude,lat,lon);

                    latitude=lat;
                    longitude=lon;
                    this.lat.add(latitude+"");
                    this.lng.add(longitude+"");

                    Toast.makeText(MyService.this, "Current: " + latitude + "," + longitude, Toast.LENGTH_SHORT).show();
                    meter=meter+km;
                    String result = String.format("%.2f",meter);
                    Intent iin=new Intent("com.fitness.app");
                    iin.putExtra("meters",result);
                    iin.putExtra("minutes","na");
                    iin.putExtra("calorie","na");

                    lbm.sendBroadcast(iin);

                }
            }


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
        private double distance(double lat1, double lon1, double lat2, double lon2) {
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1))
                    * Math.sin(deg2rad(lat2))
                    + Math.cos(deg2rad(lat1))
                    * Math.cos(deg2rad(lat2))
                    * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            return (dist);
        }

        private double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        private double rad2deg(double rad) {
            return (rad * 180.0 / Math.PI);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                simpleStepDetector.updateAccel(
                        event.timestamp, event.values[0], event.values[1], event.values[2]);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void step(long timeNs) {
            numSteps++;


            if(numSteps==40)
            {
                cal++;
                in1.putExtra("calorie",cal+"");
            }
            else
            {
                in1.putExtra("calorie","na");
            }

        }
    }