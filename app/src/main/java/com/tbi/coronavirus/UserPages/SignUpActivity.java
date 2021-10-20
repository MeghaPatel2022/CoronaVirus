package com.tbi.coronavirus.UserPages;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.provider.Settings;
import android.telephony.CellLocation;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tbi.coronavirus.Activity.MainActivity;
import com.tbi.coronavirus.Const.urlStroe;
import com.tbi.coronavirus.R;
import com.tbi.coronavirus.Splash.Splash2Activity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @BindView(R.id.et_full_name)
    EditText et_full_name;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_confiorm_pass)
    EditText et_confiorm_pass;
    @BindView(R.id.et_dob)
    EditText et_dob;
    @BindView(R.id.rb_positive)
    RadioButton rb_positive;
    @BindView(R.id.rb_negetive)
    RadioButton rb_negetive;
    @BindView(R.id.rb_may_be)
    RadioButton rb_may_be;
    @BindView(R.id.tv_signup)
    TextView tv_signup;
    @BindView(R.id.radio_grp)
    RadioGroup radio_grp;
    @BindView(R.id.spinner_country)
            Spinner spinner_country;

    Calendar myCalendar;
    String gender = "";
    int countryCode;
    private String android_id;


    String[] country_code;
    private RadioButton radioSexButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(SignUpActivity.this);
        AndroidNetworking.initialize(SignUpActivity.this);

        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
                return;
            }
        }

        et_confiorm_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!et_confiorm_pass.getText().toString().trim().equalsIgnoreCase(et_password.getText().toString().trim())){
                    et_confiorm_pass.setError("Passoword didn't match");
                }
            }
        });

        tv_signup.setOnClickListener(v -> {
            if (!et_full_name.getText().toString().trim().isEmpty()&&
                    !et_confiorm_pass.getText().toString().trim().isEmpty()&&
                    !et_dob.getText().toString().trim().isEmpty()&&
                    !et_email.getText().toString().trim().isEmpty()&&
                    !et_password.getText().toString().trim().isEmpty()) {
                if (et_password.getText().toString().trim().equalsIgnoreCase(et_confiorm_pass.getText().toString().trim())) {
                    signUP();
                } else {
                    Toast.makeText(SignUpActivity.this, "Password didn't match...", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignUpActivity.this, "Please enter all details...", Toast.LENGTH_SHORT).show();
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
                new DatePickerDialog(SignUpActivity.this, date, myCalendar
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

    private void locationUpdate() {
        CellLocation.requestLocationUpdate();
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(SignUpActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void signUP() {

        // get selected radio button from radioGroup
        int selectedId = radio_grp.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioSexButton = (RadioButton) findViewById(selectedId);
        String status = "";

        if (radioSexButton.getText().equals("I have COVID-19"))
            status = "1";
        else if (radioSexButton.getText().equals("I may have COVI_19 symptoms"))
            status = "2";
        else if (radioSexButton.getText().equals("I am fine"))
            status = "3";
        else
            Toast.makeText(this, "Please Select a Status..", Toast.LENGTH_SHORT).show();

        if (status != "") {
            AndroidNetworking.post(urlStroe.REG_URL)
                    .addBodyParameter("full_name", et_full_name.getText().toString().trim())
                    .addBodyParameter("email", et_email.getText().toString().trim())
                    .addBodyParameter("password", et_confiorm_pass.getText().toString().trim())
                    .addBodyParameter("dob", et_dob.getText().toString().trim())
                    .addBodyParameter("corona_status", status)
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
                                    Intent intent = new Intent(SignUpActivity.this, Splash2Activity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // Permission Denied
                    Toast.makeText(this, "your message", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_signup.setOnClickListener(v -> {
            if (!et_full_name.getText().toString().trim().isEmpty()&&
                    !et_confiorm_pass.getText().toString().trim().isEmpty()&&
                    !et_dob.getText().toString().trim().isEmpty()&&
                    !et_email.getText().toString().trim().isEmpty()&&
                    !et_password.getText().toString().trim().isEmpty()) {
                if (et_password.getText().toString().trim().equalsIgnoreCase(et_confiorm_pass.getText().toString().trim())) {
                    signUP();
                } else {
                    Toast.makeText(SignUpActivity.this, "Password didn't match...", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignUpActivity.this, "Please enter all details...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinner_country.setSelection(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
