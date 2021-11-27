package com.vanth.trackingvehicleuser.Request;

import java.util.List;

public class province {
    String name;

    List<com.vanth.trackingvehicleuser.Request.districts> districts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<com.vanth.trackingvehicleuser.Request.districts> getDistricts() {
        return districts;
    }

    public void setDistricts(List<com.vanth.trackingvehicleuser.Request.districts> districts) {
        this.districts = districts;
    }
}
