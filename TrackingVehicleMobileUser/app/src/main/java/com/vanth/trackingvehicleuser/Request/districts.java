package com.vanth.trackingvehicleuser.Request;

import java.util.List;

public class districts {
    String name;
    List<com.vanth.trackingvehicleuser.Request.wards> wards;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<com.vanth.trackingvehicleuser.Request.wards> getWards() {
        return wards;
    }

    public void setWards(List<com.vanth.trackingvehicleuser.Request.wards> wards) {
        this.wards = wards;
    }
}
