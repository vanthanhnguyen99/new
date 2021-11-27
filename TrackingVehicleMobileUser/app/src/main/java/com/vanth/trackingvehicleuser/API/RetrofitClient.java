package com.vanth.trackingvehicleuser.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String baseURL = "http://192.168.31.84:8080/";
    public static final String provinceAPI = "https://provinces.open-api.vn/api/";
    public static final String bingMapAPI = "https://bing-map-api.herokuapp.com/bingmap/";
    public static Retrofit retrofit = null;
    public static Retrofit retrofitProvince = null;
    public static Retrofit retrofitMap = null;

    public static Retrofit getClient(String cases)
    {
        switch (cases)
        {
            case "trackking":
            {
                Gson gson = new GsonBuilder().setLenient().create();
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                return retrofit;
            }
            case "province":
            {
                Gson gson = new GsonBuilder().setLenient().create();
                retrofitProvince = new Retrofit.Builder()
                        .baseUrl(provinceAPI)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                return retrofitProvince;
            }
            case "map":
            {
                Gson gson = new GsonBuilder().setLenient().create();
                retrofitMap = new Retrofit.Builder()
                        .baseUrl(bingMapAPI)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                return retrofitMap;
            }

        }

        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;

    }
}
