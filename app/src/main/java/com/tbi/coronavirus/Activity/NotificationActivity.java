package com.tbi.coronavirus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tbi.coronavirus.Adapter.NotificationAdapter;
import com.tbi.coronavirus.Const.urlStroe;
import com.tbi.coronavirus.Model.Notification;
import com.tbi.coronavirus.Pref.sharedprefrance;
import com.tbi.coronavirus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.rv_notification)
    RecyclerView rv_notification;
    @BindView(R.id.tv_not_found)
    TextView tv_not_found;
    @BindView(R.id.img_back)
    ImageView img_back;

    private NotificationAdapter notificationAdapter;
    ArrayList<Notification> notifications = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ButterKnife.bind(NotificationActivity.this);
        AndroidNetworking.initialize(NotificationActivity.this);

        rv_notification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this,RecyclerView.VERTICAL,false));
        getNotification();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getNotification(){
        ArrayList<Notification> tempList = new ArrayList<>();
        AndroidNetworking.post(urlStroe.GETNOTIFICATION_URL)
                .addBodyParameter("user_id", sharedprefrance.getUserID(NotificationActivity.this))
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
                                    Notification notification = new Notification();
                                    notification.setId(jsonObject.getString("id"));
                                    notification.setUserId(jsonObject.getString("user_id"));
                                    notification.setTitle(jsonObject.getString("title"));
                                    notification.setDescription(jsonObject.getString("description"));
                                    notification.setSeen(jsonObject.getString("seen"));
                                    notification.setCreatedAt(jsonObject.getString("created_at"));
                                    notification.setTimeAgo(jsonObject.getString("time_ago"));

                                    tempList.add(notification);
                                }

                                if (notifications.isEmpty()){
                                    notifications.addAll(tempList);
                                } else {
                                    notifications.clear();
                                    notifications.addAll(tempList);
                                }

                                if (tempList.isEmpty()){
                                    if (rv_notification.getVisibility() == View.VISIBLE)
                                        rv_notification.setVisibility(View.GONE);
                                    tv_not_found.setVisibility(View.VISIBLE);
                                } else {
                                    if (tv_not_found.getVisibility() == View.VISIBLE)
                                        tv_not_found.setVisibility(View.GONE);
                                    rv_notification.setVisibility(View.VISIBLE);
                                    notificationAdapter = new NotificationAdapter(notifications,NotificationActivity.this);
                                    rv_notification.setAdapter(notificationAdapter);
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("LLLL_Notif_Error: ",e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NotificationActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
