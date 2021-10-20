package com.tbi.coronavirus.MapFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.CellLocation;
import android.util.ArraySet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tbi.coronavirus.Const.urlStroe;
import com.tbi.coronavirus.MapDraw.MapAreaManager;
import com.tbi.coronavirus.MapDraw.MapAreaMeasure;
import com.tbi.coronavirus.MapDraw.MapAreaWrapper;
import com.tbi.coronavirus.Model.NearByUser;
import com.tbi.coronavirus.Pref.sharedprefrance;
import com.tbi.coronavirus.R;
import com.tbi.coronavirus.Service.MyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapViewFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    @BindView(R.id.mapviews)
    MapView mapView;

    GoogleMap map;
    public LocationManager mLocManager;

    MarkerOptions markerOptions = new MarkerOptions();
    ArrayList<LatLng> arrayList = new ArrayList<>();

    ArrayList<NearByUser> nearByUsers = new ArrayList<>();

    private static final double EARTH_RADIUS = 6378100.0;
    private int offset;
    private String address = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        ButterKnife.bind(this, view);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mLocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
            }
        }
        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                this);

        mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0, this);


        locationUpdate();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(false);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (!nearByUsers.isEmpty()) {
            for (int i = 0; i < nearByUsers.size(); i++) {
                if (!nearByUsers.get(i).getLatitude().isEmpty()) {
                    addCustomMarkerFromURL(new LatLng(Double.parseDouble(nearByUsers.get(i).getLatitude()), Double.parseDouble(nearByUsers.get(i).getLongitude())), nearByUsers.get(i).getFullName(), nearByUsers.get(i).getCoronaStatus());
                } else {

                }
            }
        }

    }

    private void locationUpdate() {
        CellLocation.requestLocationUpdate();
    }


    private void addCustomMarkerFromURL(LatLng point, String ImageUrl,String feet) {

        if (map == null) {
            return;
        }

//        MarkerOptions options = new MarkerOptions();
//        options.position(point);
//        options.icon(BitmapDescriptorFactory.fromBitmap(getBitmap(point)));

        if (feet.equalsIgnoreCase("")){
            map.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(ImageUrl));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 10f));
        } else {
            int feet1 = Integer.parseInt(feet);

            if (feet1 == 1) {
                map.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(ImageUrl));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 10f));
            } else if (feet1 == 2) {
                map.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).title(ImageUrl));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 10f));
            } else if (feet1 == 3) {
                map.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(ImageUrl));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 10f));
            } else {
                map.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(ImageUrl));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 10f));
            }
        }


//            drawCircle(point);

//            mapView.onResume();
    }
    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(20);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        map.addCircle(circleOptions);

    }

    private int convertMetersToPixels(double lat, double lng, double radiusInMeters) {

        double lat1 = radiusInMeters / EARTH_RADIUS;
        double lng1 = radiusInMeters / (EARTH_RADIUS * Math.cos((Math.PI * lat / 180)));

        double lat2 = lat + lat1 * 180 / Math.PI;
        double lng2 = lng + lng1 * 180 / Math.PI;

        Point p1 = map.getProjection().toScreenLocation(new LatLng(lat, lng));
        Point p2 = map.getProjection().toScreenLocation(new LatLng(lat2, lng2));

        return Math.abs(p1.x - p2.x);
    }

    private Bitmap getBitmap(LatLng point) {

        // fill color
        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setColor(0x110000FF);
        paint1.setStyle(Paint.Style.FILL);

        // stroke color
        Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2.setColor(0xFF0000FF);
        paint2.setStyle(Paint.Style.STROKE);

        // icon
        Bitmap icon = BitmapFactory.decodeResource(Objects.requireNonNull(getActivity()).getResources(), R.drawable.red_border);

        // circle radius - 200 meters
        int radius = offset = convertMetersToPixels(point.latitude, point.longitude, 200);

        // if zoom too small
        if (radius < icon.getWidth() / 2) {

            radius = icon.getWidth() / 2;
        }

        // create empty bitmap
        Bitmap b = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        // draw blue area if area > icon size
        if (radius != icon.getWidth() / 2) {

            c.drawCircle(radius, radius, radius, paint1);
            c.drawCircle(radius, radius, radius, paint2);
        }

        // draw icon
        c.drawBitmap(icon, radius - icon.getWidth() / 2, radius - icon.getHeight() / 2, new Paint());

        return b;
    }

// 4. calculate image offset:

    private LatLng getCoords(LatLng point) {

        LatLng latLng = point;

        Projection proj = map.getProjection();
        Point p = proj.toScreenLocation(latLng);
        p.set(p.x, p.y + offset);

        return proj.fromScreenLocation(p);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("LLL_current: ", location.getLatitude() + "      " + location.getLongitude());
        getAddress(location.getLatitude(),location.getLongitude());
        addCustomMarkerFromURL(new LatLng(location.getLatitude(), location.getLongitude()), "","");
        if (location != null) {
            mLocManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (status ==
                LocationProvider.TEMPORARILY_UNAVAILABLE) {
            Toast.makeText(getActivity(),
                    "LocationProvider.TEMPORARILY_UNAVAILABLE",
                    Toast.LENGTH_SHORT).show();
        } else if (status == LocationProvider.OUT_OF_SERVICE) {
            Toast.makeText(getActivity(),
                    "LocationProvider.OUT_OF_SERVICE", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    private void getNearByUser(double lat,double lang){
        ArrayList<NearByUser> tempList = new ArrayList<>();
        AndroidNetworking.post(urlStroe.NEAR_BY_USER_URL)
                .addBodyParameter("user_id", sharedprefrance.getUserID(getActivity()))
                .addBodyParameter("latitude", String.valueOf(lat))
                .addBodyParameter("longitude", String.valueOf(lang))
                .addBodyParameter("location",address)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")){
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    NearByUser nearByUser = new NearByUser();
                                    nearByUser.setId(jsonObject.getString("id"));
                                    nearByUser.setFullName(jsonObject.getString("full_name"));
                                    nearByUser.setLatitude(jsonObject.getString("latitude"));
                                    nearByUser.setLongitude(jsonObject.getString("longitude"));
                                    nearByUser.setCoronaStatus(jsonObject.getString("corona_status"));
                                    nearByUser.setDistanceInKm(jsonObject.getString("distance_in_km"));
                                    nearByUser.setDistanceInFeet(jsonObject.getString("distance_in_feet"));

                                    tempList.add(nearByUser);
                                }

                                if (nearByUsers.isEmpty())
                                    nearByUsers.addAll(tempList);
                                else {
                                    nearByUsers.clear();
                                    nearByUsers.addAll(tempList);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLL_Error; ",anError.getMessage());
                    }
                });
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            address = obj.getAddressLine(0);
            getNearByUser(lat,lng);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
