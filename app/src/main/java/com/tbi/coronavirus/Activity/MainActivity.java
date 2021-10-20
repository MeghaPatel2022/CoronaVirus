package com.tbi.coronavirus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.GoogleMap;
import com.tbi.coronavirus.Adapter.DrawerListAdapter;
import com.tbi.coronavirus.Const.urlStroe;
import com.tbi.coronavirus.EditProfileActivity;
import com.tbi.coronavirus.MapFragment.MapViewFragment;
import com.tbi.coronavirus.Pref.sharedprefrance;
import com.tbi.coronavirus.R;
import com.tbi.coronavirus.Service.MyService;
import com.tbi.coronavirus.Splash.Splash2Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.rl_profile)
    RelativeLayout rl_profile;
    @BindView(R.id.map)
    RelativeLayout mapView;
    @BindView(R.id.img_nav)
    ImageView img_nav;
    @BindView(R.id.lv_drawer)
    ListView lv_drawer;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.tv_profile)
    TextView tv_profile;
    @BindView(R.id.spinner_status)
    Spinner spinner_status;
    @BindView(R.id.tv_change_status)
            TextView tv_change_status;
    @BindView(R.id.tv_my_status)
            TextView tv_my_status;

    GoogleMap map;
    Bitmap myBitmap;

    public LocationManager mLocManager;
    private DrawerListAdapter drawerListAdapter;

    ArrayList<String> navigation_items;

    // Drawer
    public static int[] drawer_icons = {R.drawable.home,
            R.drawable.notification,
            R.drawable.contact_history,
            R.drawable.info,
            R.drawable.logout};

    String[] corona_status;
    String status="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(MainActivity.this);
        startService(new Intent(getApplicationContext(), MyService.class));
        navigation_items = new ArrayList<>();

        //adding menu items for naviations
        navigation_items.add("Home");
        navigation_items.add("Notifications");
        navigation_items.add("My Contact History");
        navigation_items.add("About App");
        navigation_items.add("Logout");

        setDrawer();

        img_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.END);
                } else {
                    drawer_layout.openDrawer(GravityCompat.START);
                }
            }
        });

        if (!sharedprefrance.getFullName(MainActivity.this).equalsIgnoreCase(""))
            tv_profile.setText(sharedprefrance.getFullName(MainActivity.this));

        FragmentManager fragmentManager1 = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        MapViewFragment mapViewFragment = new MapViewFragment();
        fragmentTransaction1.add(R.id.map, mapViewFragment, "map");
        fragmentTransaction1.commit();

        corona_status = getResources().getStringArray(R.array.corona_status);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.corona_status, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_status.setAdapter(adapter);
        spinner_status.setSelection(1);

        tv_change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner_status.getVisibility()==View.GONE)
                    spinner_status.setVisibility(View.VISIBLE);
            }
        });

        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setDrawer() {
        drawerListAdapter = new DrawerListAdapter(MainActivity.this, drawer_icons,navigation_items);
        lv_drawer.setAdapter(drawerListAdapter);
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        }
        lv_drawer.setOnItemClickListener((parent, view, position, id) -> {

            if (navigation_items.get(position).equalsIgnoreCase("Home")) {
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START);
                } else {
                    drawer_layout.openDrawer(GravityCompat.START);
                }
            } else if (navigation_items.get(position).equalsIgnoreCase("Notifications")) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
                finish();

            } else if (navigation_items.get(position).equalsIgnoreCase("My Contact History")) {
                Intent intent = new Intent(MainActivity.this, ContacthistoryActivity.class);
                startActivity(intent);
                finish();
            } else if (navigation_items.get(position).equalsIgnoreCase("About App")) {

            } else if (navigation_items.get(position).equalsIgnoreCase("Logout")) {
                String token = sharedprefrance.getfirebasetoken(MainActivity.this);
               sharedprefrance.clearAll(MainActivity.this);
               sharedprefrance.setfirebasetoken(MainActivity.this,token);
               Intent intent = new Intent(MainActivity.this,Splash2Activity.class);
               startActivity(intent);
               finish();
            }

        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinner_status.setSelection(position);
        String status1=corona_status[position];

        if (status1.equals("I have COVID-19")) {
            status = "1";
            tv_my_status.setText("I have COVID-19");
            if (!status.equalsIgnoreCase(sharedprefrance.getCorona_status(MainActivity.this))){
                changeStatus();
            }
        }else if (status1.equals("I may have COVI_19 symptoms")) {
            status = "2";
            tv_my_status.setText("I may have COVI_19 symptoms");
            if (!status.equalsIgnoreCase(sharedprefrance.getCorona_status(MainActivity.this))){
                changeStatus();
            }
        }else if (status1.equals("I am fine")) {
            status = "3";
            tv_my_status.setText("I am fine");
            if (!status.equalsIgnoreCase(sharedprefrance.getCorona_status(MainActivity.this))){
                changeStatus();
            }
        }else
            Toast.makeText(this, "Please Select a Status..", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void changeStatus(){
        AndroidNetworking.post(urlStroe.EDIT_CORONA_STATUS_URL)
                .addBodyParameter("user_id",sharedprefrance.getUserID(MainActivity.this))
                .addBodyParameter("corona_status",status)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                sharedprefrance.setCorona_status(MainActivity.this,jsonObject.getString("corona_status"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("LLL_Error: ",e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLL_Error :",anError.getMessage());
                    }
                });
    }
}
