package com.tbi.coronavirus.UserPages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tbi.coronavirus.Activity.MainActivity;
import com.tbi.coronavirus.Const.urlStroe;
import com.tbi.coronavirus.Pref.sharedprefrance;
import com.tbi.coronavirus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.tv_login)
    TextView tv_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(LoginActivity.this);
        AndroidNetworking.initialize(LoginActivity.this);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_password.getText().toString().trim().isEmpty()&&
                !et_email.getText().toString().trim().isEmpty()) {
                    sendOTP();
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter username and password...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_password.getText().toString().trim().isEmpty()&&
                        !et_email.getText().toString().trim().isEmpty()) {
                    sendOTP();
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter username and password...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendOTP(){
        Log.e("LLL_token: ",sharedprefrance.getfirebasetoken(LoginActivity.this));

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("LLL_new_token", newToken);
            sharedprefrance.setfirebasetoken(LoginActivity.this,newToken);
        });

        AndroidNetworking.post(urlStroe.LOGIN_URL)
                .addBodyParameter("email",et_email.getText().toString().trim())
                .addBodyParameter("password",et_password.getText().toString().trim())
                .addBodyParameter("token", sharedprefrance.getfirebasetoken(LoginActivity.this))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")){
                                JSONArray jsonArray = response.getJSONArray("data");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                sharedprefrance.setUserID(LoginActivity.this,jsonObject.getString("id"));
                                sharedprefrance.setFullName(LoginActivity.this,jsonObject.getString("full_name"));
                                sharedprefrance.setEmail(LoginActivity.this,jsonObject.getString("email"));
                                sharedprefrance.setContact_no(LoginActivity.this,jsonObject.getString("contact_no"));
                                sharedprefrance.setDob(LoginActivity.this,jsonObject.getString("dob"));
                                sharedprefrance.setCorona_status(LoginActivity.this,jsonObject.getString("corona_status"));
                                sharedprefrance.setCountry(LoginActivity.this,jsonObject.getString("country"));
                                sharedprefrance.setLatitude(LoginActivity.this,jsonObject.getString("latitude"));
                                sharedprefrance.setLongitude(LoginActivity.this,jsonObject.getString("longitude"));
                                sharedprefrance.setIsLogin(LoginActivity.this,true);

                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
