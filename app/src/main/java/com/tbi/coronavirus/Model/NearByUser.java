package com.tbi.coronavirus.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearByUser {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("corona_status")
    @Expose
    private String coronaStatus;
    @SerializedName("distance_in_km")
    @Expose
    private String distanceInKm;
    @SerializedName("distance_in_feet")
    @Expose
    private String distanceInFeet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCoronaStatus() {
        return coronaStatus;
    }

    public void setCoronaStatus(String coronaStatus) {
        this.coronaStatus = coronaStatus;
    }

    public String getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(String distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    public String getDistanceInFeet() {
        return distanceInFeet;
    }

    public void setDistanceInFeet(String distanceInFeet) {
        this.distanceInFeet = distanceInFeet;
    }
}
