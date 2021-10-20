package com.tbi.coronavirus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tbi.coronavirus.Activity.MainActivity;
import com.tbi.coronavirus.Const.urlStroe;
import com.tbi.coronavirus.Pref.sharedprefrance;
import com.tbi.coronavirus.Splash.Splash2Activity;
import com.tbi.coronavirus.UserPages.SignUpActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileActivity extends AppCompatActivity {


    @BindView(R.id.et_full_name)
    EditText et_full_name;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_dob)
    EditText et_dob;
    @BindView(R.id.tv_signup)
    TextView tv_signup;
    @BindView(R.id.spinner_country)
    Spinner spinner_country;

    Calendar myCalendar;
    String gender = "";
    int countryCode;
    private String android_id;


    String[] country_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ButterKnife.bind(EditProfileActivity.this);
        AndroidNetworking.initialize(EditProfileActivity.this);

        getData();

        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        tv_signup.setOnClickListener(v -> {
            if (!et_full_name.getText().toString().trim().isEmpty()&&
                    !et_dob.getText().toString().trim().isEmpty()&&
                    !et_email.getText().toString().trim().isEmpty())
            {
                         signUP();
            } else {
                Toast.makeText(EditProfileActivity.this, "Please enter all details...", Toast.LENGTH_SHORT).show();
            }
        });

        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        country_code = getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_country.setAdapter(adapter);
        spinner_country.setSelection(98);

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_dob.setText(sdf.format(myCalendar.getTime()));
    }

    private void signUP() {

            AndroidNetworking.post(urlStroe.EDIT_PROFILE_URL)
                    .addBodyParameter("full_name", et_full_name.getText().toString().trim())
                    .addBodyParameter("email", et_email.getText().toString().trim())
                    .addBodyParameter("dob", et_dob.getText().toString().trim())
                    .addBodyParameter("country", country_code[spinner_country.getSelectedItemPosition()])
                    .setPriority(Priority.IMMEDIATE)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("LLLL_Response:", response.toString());
                            try {
                                if (response.getBoolean("success"))
                                {
                                    Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(EditProfileActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e("LLLL_Error: ", anError.getMessage());
                        }
                    });
    }

    private void getData() {

        AndroidNetworking.post(urlStroe.GET_PROFILE_URL)
                .addBodyParameter("user_id", sharedprefrance.getUserID(EditProfileActivity.this))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("LLLL_Response:", response.toString());
                        try {
                            if (response.getBoolean("success"))
                            {
                                JSONArray jsonArray = response.getJSONArray("data");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                et_full_name.setText(jsonObject.getString("full_name"));
                                et_email.setText(jsonObject.getString("email"));
                                et_dob.setText(jsonObject.getString("dob"));

                            } else {
                                Toast.makeText(EditProfileActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLL_Error: ", anError.getMessage());
                    }
                });
    }

}
