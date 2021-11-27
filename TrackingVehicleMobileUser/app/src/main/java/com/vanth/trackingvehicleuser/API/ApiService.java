package com.vanth.trackingvehicleuser.API;

import com.vanth.trackingvehicleuser.Entity.Account;
import com.vanth.trackingvehicleuser.Entity.Schedule;
import com.vanth.trackingvehicleuser.Request.Coord;
import com.vanth.trackingvehicleuser.Request.MyAddress;
import com.vanth.trackingvehicleuser.Request.province;
import com.vanth.trackingvehicleuser.Request.RegistAccountRequest;
import com.vanth.trackingvehicleuser.Request.VehicleInfoRequest;
import com.vanth.trackingvehicleuser.Request.VehicleRequestHome;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // Request for login
    @POST("login")
    Call<Boolean> sign_in(@Body Account account);

    // Request for regist
    @POST("regist")
    Call<String> registAccount(@Body RegistAccountRequest registAccountRequest);

    // Request for list user's vehicle
    @GET("vehicle-user/{id_user}")
    Call<List<VehicleRequestHome>> getUserVehicle(@Path("id_user") int id_user);

    // Request for vehicle's info
    @GET("vehicle-info/{id}")
    Call<VehicleInfoRequest> getVehicleInfo(@Path("id") String id);

    // Request for vehicle's schedule
    @GET("/schedule/{id}")
    Call<List<Schedule>> getVehicleScheduleList(@Path("id") String id);

    // Request for add new vehicle's schedule
    @POST("add-schedule")
    Call<String> addVehicleSchedule(@Body Schedule schedule);

    @GET("?depth=3")
    Call<List<province>> getListProvince();

    @POST("coordinates")
    Call<Coord> getCoordFromLocaion(@Body MyAddress address);
}
