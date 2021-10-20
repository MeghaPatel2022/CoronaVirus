package com.tbi.coronavirus.Service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.CellLocation;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tbi.coronavirus.Activity.MainActivity;
import com.tbi.coronavirus.Const.urlStroe;
import com.tbi.coronavirus.Pref.sharedprefrance;
import com.tbi.coronavirus.R;
import com.tbi.coronavirus.UserPages.SignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MyService extends Service implements LocationListener {

    private LocationManager mLocManager;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    double latitude, longitude;
    String address = "";

    Handler handler;
    Runnable test;
    public MyService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidNetworking.initialize(MyService.this);
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {

                return;
            } else {
                getLocation();
            }
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();

        onTaskRemoved(intent);
        handler = new Handler();
        Toast.makeText(getApplicationContext(),"This is a Service running in Background",
                Toast.LENGTH_SHORT).show();
        test = () -> {
            Log.d("foo", "bar");
            getLocation();
            handler.postDelayed(test, 10000); //100 ms you should do it 4000
        };

        handler.postDelayed(test, 0);

        return START_NOT_STICKY;


    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }

    private void sendLocation(){

        if (!sharedprefrance.getUserID(MyService.this).equals("")) {
            AndroidNetworking.post(urlStroe.USER_LOCATION_URL)
                    .addBodyParameter("user_id",sharedprefrance.getUserID(MyService.this))
                    .addBodyParameter("latitude", String.valueOf(latitude))
                    .addBodyParameter("longitude", String.valueOf(longitude))
                    .addBodyParameter("location",address)
                    .setPriority(Priority.IMMEDIATE)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Toast.makeText(MyService.this, response.getString("success")+""+response.getString("msg"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(MyService.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(MyService.this, anError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                            Log.e("LLLL_Error; ", Objects.requireNonNull(anError.getMessage()));
                        }
                    });
        }

        if (!sharedprefrance.getUserID(MyService.this).equals("")) {
            AndroidNetworking.post(urlStroe.NEAR_BY_USER_URL)
                    .addBodyParameter("user_id",sharedprefrance.getUserID(MyService.this))
                    .addBodyParameter("latitude", String.valueOf(latitude))
                    .addBodyParameter("longitude", String.valueOf(longitude))
                    .addBodyParameter("location",address)
                    .setPriority(Priority.IMMEDIATE)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
//                                Log.d("foo_LLL_Send_Location,",response.getString("success"));
                                Toast.makeText(MyService.this, response.getString("success")+""+response.getString("msg"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(MyService.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(MyService.this, anError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                            Log.e("LLLL_Error; ", Objects.requireNonNull(anError.getMessage()));
                        }
                    });
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        getAddress(latitude, longitude);
        if (location != null) {
            mLocManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (status ==
                LocationProvider.TEMPORARILY_UNAVAILABLE) {
            Toast.makeText(MyService.this,
                    "LocationProvider.TEMPORARILY_UNAVAILABLE",
                    Toast.LENGTH_SHORT).show();
        } else if (status == LocationProvider.OUT_OF_SERVICE) {
            Toast.makeText(MyService.this,
                    "LocationProvider.OUT_OF_SERVICE", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        Toast.makeText(MyService.this, "Gps Disabled", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    //Get location
    public void getLocation() {
        mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location myLocation = mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (myLocation == null)
        {
            locationUpdate();
        }
    }
    private void locationUpdate() {
        CellLocation.requestLocationUpdate();
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(MyService.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
           address = obj.getAddressLine(0);

            sendLocation();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(MyService.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            serviceChannel.enableLights(false);
            serviceChannel.enableVibration(false);
            serviceChannel.setShowBadge(false);
            serviceChannel.setSound(null,null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}
