package speedmeter.fly.speed;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.widgets.Dialog;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements LocationListener, GpsStatus.Listener {


    private SharedPreferences sharedPreferences;//get the context from getsharedprefereses

    private LocationManager mLocationManager;

    private static Data data;


    private Toolbar toolbar;//top toolbar

    private FloatingActionButton fab; // floating button which pause

    private FloatingActionButton refresh;//floating button which stop and refresh

    private ProgressBarCircularIndeterminate progressBarCircularIndeterminate;//circular progress

    private TextView satellite;

    private TextView status;

    private TextView accuracy;

    private TextView currentSpeed;

    private TextView maxSpeed;

    private TextView averageSpeed;

    private TextView distance;

    private Chronometer time;//time will show up as chronometer

    private Data.onGpsServiceUpdate onGpsServiceUpdate;


    private boolean firstfix;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        data = new Data(onGpsServiceUpdate);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //setTitle("");

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setVisibility(View.INVISIBLE);


        refresh = (FloatingActionButton) findViewById(R.id.refresh);

        refresh.setVisibility(View.INVISIBLE);


        onGpsServiceUpdate = new Data.onGpsServiceUpdate() {

            @Override

            public void update() {

                double maxSpeedTemp = data.getMaxSpeed();//if its in the first then its zero

                double distanceTemp = data.getDistance();
                double calories = 0;

                double averageTemp;

                if (sharedPreferences.getBoolean("auto_average", false)) {

                    averageTemp = data.getAverageSpeedMotion();

                } else {

                    averageTemp = data.getAverageSpeed();

                }


                String speedUnits;

                String distanceUnits;

                if (sharedPreferences.getBoolean("miles_per_hour", false)) {

                    maxSpeedTemp *= 0.62137119;//to mile

                    distanceTemp = distanceTemp / 1000.0 * 0.62137119;//distance in miles

                    averageTemp *= 0.62137119;//speed to miles

                    speedUnits = "mi/h";

                    distanceUnits = "mi";

                } else {

                    speedUnits = "km/h";

                    if (distanceTemp <= 1000.0) {//distance below 1000 then its displayed like met

                        distanceUnits = "m";

                    } else {

                        distanceTemp /= 1000.0;

                        distanceUnits = "km";

                    }

                }


                SpannableString s = new SpannableString(String.format("%.0f", maxSpeedTemp) + speedUnits);

                s.setSpan(new RelativeSizeSpan(0.5f), s.length() - 4, s.length(), 0);
                // string span will add max speed of temp of float with speed units of milli or km

                maxSpeed.setText(s);


                s = new SpannableString(String.format("%.0f", averageTemp) + speedUnits);

                s.setSpan(new RelativeSizeSpan(0.5f), s.length() - 4, s.length(), 0);

                averageSpeed.setText(s);


                s = new SpannableString(String.format("%.3f", distanceTemp) + distanceUnits);

                s.setSpan(new RelativeSizeSpan(0.5f), s.length() - 2, s.length(), 0);

                distance.setText(s);
                ///////////////////////*************////////////////////////
                calories = getCalorires(distanceTemp);
                accuracy.setError(calories+"");


            }

        };


        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        satellite = (TextView) findViewById(R.id.satellite);

        status = (TextView) findViewById(R.id.status);

        accuracy = (TextView) findViewById(R.id.accuracy);

        maxSpeed = (TextView) findViewById(R.id.maxSpeed);

        averageSpeed = (TextView) findViewById(R.id.averageSpeed);

        distance = (TextView) findViewById(R.id.distance);

        time = (Chronometer) findViewById(R.id.time);

        currentSpeed = (TextView) findViewById(R.id.currentSpeed);

        progressBarCircularIndeterminate = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndeterminate);


        time.setText("00:00:00");

        time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            boolean isPair = true;

            @Override

            public void onChronometerTick(Chronometer chrono) {//each tick will inc time

                long time;

                if (data.isRunning()) {//if the user running and its return true

                    time = SystemClock.elapsedRealtime() - chrono.getBase();
                    //after sitting basetime in the onfabclick we calc the current time

                    data.setTime(time);

                } else {

                    time = data.getTime();

                }


                int h = (int) (time / 3600000);

                int m = (int) (time - h * 3600000) / 60000;

                int s = (int) (time - h * 3600000 - m * 60000) / 1000;

                String hh = h < 10 ? "0" + h : h + "";

                String mm = m < 10 ? "0" + m : m + "";

                String ss = s < 10 ? "0" + s : s + "";

                chrono.setText(hh + ":" + mm + ":" + ss);


                if (data.isRunning()) {

                    chrono.setText(hh + ":" + mm + ":" + ss);

                } else {

                    if (isPair) {

                        isPair = false;

                        chrono.setText(hh + ":" + mm + ":" + ss);

                    } else {

                        isPair = true;

                        chrono.setText("");//he's not ruuning,not start then no need to time units

                    }

                }


            }

        });

    }


    public void onFabClick(View v) {

        if (!data.isRunning()) {

            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));

            data.setRunning(true);

            time.setBase(SystemClock.elapsedRealtime() - data.getTime());

            time.start();

            data.setFirstTime(true);

            startService(new Intent(getBaseContext(), GpsServices.class));
            //start the gps services class after start running

            refresh.setVisibility(View.INVISIBLE);

        } else {

            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));

            data.setRunning(false);

            status.setText("");

            stopService(new Intent(getBaseContext(), GpsServices.class));

            refresh.setVisibility(View.VISIBLE);

        }

    }


    public void onRefreshClick(View v) {

        resetData();

        stopService(new Intent(getBaseContext(), GpsServices.class));

    }


    @Override

    protected void onResume() {

        super.onResume();

        firstfix = true;

        if (!data.isRunning()) {

            Gson gson = new Gson();

            String json = sharedPreferences.getString("data", "");

            data = gson.fromJson(json, Data.class);//get all the date which stoped and run it again

        }

        if (data == null) {

            data = new Data(onGpsServiceUpdate);

        } else {

            data.setOnGpsServiceUpdate(onGpsServiceUpdate);

        }


        if (mLocationManager.getAllProviders().indexOf(LocationManager.GPS_PROVIDER) >= 0) {

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);

        } else {

            Log.w("MainActivity", "No GPS location provider found. GPS data display will not be available.");

        }


        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            showGpsDisabledDialog();

        }


        mLocationManager.addGpsStatusListener(this);

    }


    @Override

    protected void onPause() {

        super.onPause();

        mLocationManager.removeUpdates(this);

        mLocationManager.removeGpsStatusListener(this);
        //remove all gps statuses to stop the resume it if resume clicked

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(data);

        prefsEditor.putString("data", json);

        prefsEditor.commit();

    }


    @Override

    public void onDestroy() {

        super.onDestroy();

        stopService(new Intent(getBaseContext(), GpsServices.class));

    }


    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;

    }


    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will

        // automatically handle clicks on the Home or Up, button

        // as you specify a parent activity in AndroidManifest XML FILE.

        int id = item.getItemId();


        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {//if the id is setting then new intent of setting

            Intent intent = new Intent(this, Settings.class);

            startActivity(intent);

            return true;

        }


        return super.onOptionsItemSelected(item);

    }


    ////////////////////////////////////// down here is the location setting
    @Override

    public void onLocationChanged(Location location) {

        if (location.hasAccuracy()) {

            SpannableString s = new SpannableString(String.format("%.0f", location.getAccuracy()) + "m");

            s.setSpan(new RelativeSizeSpan(0.75f), s.length() - 1, s.length(), 0);

            accuracy.setText(s);


            if (firstfix) {

                status.setText("");

                fab.setVisibility(View.VISIBLE);

                if (!data.isRunning() && !maxSpeed.getText().equals("")) {

                    refresh.setVisibility(View.VISIBLE);

                }

                firstfix = false;

            }

        } else {

            firstfix = true;

        }


        if (location.hasSpeed()) {

            progressBarCircularIndeterminate.setVisibility(View.GONE);

            String speed = String.format(Locale.ENGLISH, "%.0f", location.getSpeed() * 3.6) + "km/h";


            if (sharedPreferences.getBoolean("miles_per_hour", false)) { // Convert to MPH

                speed = String.format(Locale.ENGLISH, "%.0f", location.getSpeed() * 3.6 * 0.62137119) + "mi/h";

            }

            SpannableString s = new SpannableString(speed);

            s.setSpan(new RelativeSizeSpan(0.25f), s.length() - 4, s.length(), 0);

            currentSpeed.setText(s);
            //put speed from the location get speed and put it in spannable string

        }


    }


    public void onGpsStatusChanged(int event) {
        //this method provides the satellite status from the gps

        switch (event) {

            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                //in this case we handle the gps and satellite method

                GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);

                int satsInView = 0;

                int satsUsed = 0;

                Iterable<GpsSatellite> sats = gpsStatus.getSatellites();
                //using Iterable object to add the sattaliies view

                for (GpsSatellite sat : sats) {

                    satsInView++;

                    if (sat.usedInFix()) {
                        // if the gps engine is get fixed

                        satsUsed++;
                        //inc and add to sats

                    }

                }

                satellite.setText(String.valueOf(satsUsed) + "/" + String.valueOf(satsInView));
                //set the values of the sats used and sats in view to satellit label

                if (satsUsed == 0) {
                    //if there is no use of gps then restet the play button
                    //reset all data

                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));

                    data.setRunning(false);

                    status.setText("");

                    stopService(new Intent(getBaseContext(), GpsServices.class));

                    fab.setVisibility(View.INVISIBLE);

                    refresh.setVisibility(View.INVISIBLE);

                    accuracy.setText("");

                    status.setText(getResources().getString(R.string.waiting_for_fix));

                    firstfix = true;

                }

                break;


            case GpsStatus.GPS_EVENT_STOPPED:
                //no gps

                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    showGpsDisabledDialog();

                }

                break;

            case GpsStatus.GPS_EVENT_FIRST_FIX:

                break;

        }

    }


    public void showGpsDisabledDialog() {
        //if there is no gps then we start new activity to enable gps after pressing accept dialog

        Dialog dialog = new Dialog(this, getResources().getString(R.string.gps_disabled), getResources().getString(R.string.please_enable_gps));


        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));

            }

        });

        dialog.show();

    }


    public void resetData() {//reset time,speed, max speed to the default

        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));

        refresh.setVisibility(View.INVISIBLE);

        time.stop();

        maxSpeed.setText("");

        averageSpeed.setText("");

        distance.setText("");

        time.setText("00:00:00");

        data = new Data(onGpsServiceUpdate);

    }


    public static Data getData() {

        return data;

    }


    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);

        a.addCategory(Intent.CATEGORY_HOME);

        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(a);

    }


    @Override

    public void onStatusChanged(String s, int i, Bundle bundle) {
    }


    @Override

    public void onProviderEnabled(String s) {
    }


    @Override

    public void onProviderDisabled(String s) {
    }

    public double getCalorires(double distance1) {
        double weight = 66; // kg

        double height = 178; // cm

        double stepsCount;

        double walkingFactor = 0.57;

        double CaloriesBurnedPerMile;

        double strip;

        double stepCountMile; // step/mile

        double conversationFactor;

        double CaloriesBurned;

        NumberFormat formatter = new DecimalFormat("#0.00");

        double distance = distance1;
        stepsCount = CaloriesBurnedPerMile = walkingFactor * (weight * 2.2);

        strip = height * 0.415;

        stepCountMile = 160934.4 / strip;

        conversationFactor = CaloriesBurnedPerMile / stepCountMile;

        CaloriesBurned = stepsCount * conversationFactor;

        System.out.println("Calories burned: "
                + formatter.format(CaloriesBurned) + " cal");

        distance = (stepsCount * strip) / 100000;
        return distance;

    }

    public void onClicktry(View view) {
        startActivity(new Intent(MainActivity.this, Login.class));
    }

}