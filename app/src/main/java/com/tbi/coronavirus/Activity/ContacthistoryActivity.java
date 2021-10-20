package com.tbi.coronavirus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tbi.coronavirus.Adapter.ContactHistoryAdapter;
import com.tbi.coronavirus.Const.urlStroe;
import com.tbi.coronavirus.Model.ContactHistory;
import com.tbi.coronavirus.Pref.sharedprefrance;
import com.tbi.coronavirus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContacthistoryActivity extends AppCompatActivity {

    @BindView(R.id.cv_not_Found)
    CardView cv_not_Found;
    @BindView(R.id.ll_list)
    LinearLayout ll_list;
    @BindView(R.id.rv_contact_history)
    RecyclerView rv_contact_history;
    @BindView(R.id.img_back)
    ImageView img_back;

    private ContactHistoryAdapter contactHistoryAdapter;
    ArrayList<ContactHistory> contactHistories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacthistory);

        ButterKnife.bind(ContacthistoryActivity.this);
        AndroidNetworking.initialize(ContacthistoryActivity.this);

        rv_contact_history.setLayoutManager(new LinearLayoutManager(ContacthistoryActivity.this,RecyclerView.VERTICAL,false));
        getContcatHistory();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getContcatHistory(){
        ArrayList<ContactHistory> tempList = new ArrayList<>();
        AndroidNetworking.post(urlStroe.HISTORIES_URL)
                .addBodyParameter("user_id", sharedprefrance.getUserID(ContacthistoryActivity.this))
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
                                    ContactHistory contactHistory = new ContactHistory();
                                    contactHistory.setId(jsonObject.getString("id"));
                                    contactHistory.setUserId(jsonObject.getString("user_id"));
                                    contactHistory.setLatitude(jsonObject.getString("latitude"));
                                    contactHistory.setLongitude(jsonObject.getString("longitude"));
                                    contactHistory.setLocation(jsonObject.getString("location"));
                                    contactHistory.setCoronaStatus(jsonObject.getString("corona_status"));
                                    contactHistory.setCreatedAt(jsonObject.getString("created_at"));
                                    contactHistory.setTimeAgo(jsonObject.getString("time_ago"));

                                    tempList.add(contactHistory);
                                }

                                if (contactHistories.isEmpty()){
                                    contactHistories.addAll(tempList);
                                } else {
                                    contactHistories.clear();
                                    contactHistories.addAll(tempList);
                                }

                                if (tempList.isEmpty()){
                                    if (ll_list.getVisibility() == View.VISIBLE)
                                        ll_list.setVisibility(View.GONE);
                                    cv_not_Found.setVisibility(View.VISIBLE);
                                } else {
                                    if (cv_not_Found.getVisibility() == View.VISIBLE)
                                        cv_not_Found.setVisibility(View.GONE);
                                    ll_list.setVisibility(View.VISIBLE);
                                    contactHistoryAdapter = new ContactHistoryAdapter(contactHistories,ContacthistoryActivity.this);
                                    rv_contact_history.setAdapter(contactHistoryAdapter);
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLL_History_Error: ",anError.getMessage());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ContacthistoryActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}
