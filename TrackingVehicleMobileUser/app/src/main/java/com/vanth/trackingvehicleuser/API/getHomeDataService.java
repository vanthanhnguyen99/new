package com.vanth.trackingvehicleuser.API;

import com.vanth.trackingvehicleuser.Activities.MainActivity;
import com.vanth.trackingvehicleuser.Adapter.VehicleAdapterHome;
import com.vanth.trackingvehicleuser.Request.VehicleRequestHome;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Comparator;

public class getHomeDataService extends Thread{
    public static List<VehicleRequestHome> vehicleRequestHomeList = new ArrayList<>();
    ApiService apiService;

    void getVehicleHome()
    {
        apiService = ApiUtils.getApiService();
        while (true)
        {
            apiService.getUserVehicle(1).enqueue(new Callback<List<VehicleRequestHome>>() {
                @Override
                public void onResponse(Call<List<VehicleRequestHome>> call, Response<List<VehicleRequestHome>> response) {
                    if (response.isSuccessful())
                    {

                        getHomeDataService.vehicleRequestHomeList = response.body();

                        vehicleRequestHomeList.sort(Comparator.comparingInt((VehicleRequestHome temp)->temp.getisOnline()?0:1));
                    }

                }

                @Override
                public void onFailure(Call<List<VehicleRequestHome>> call, Throwable t) {

                }
            });

            try {
                Thread.sleep(3000);
            }
            catch (InterruptedException e)
            {

            }


        }

    }
    @Override
    public void run() {
        getVehicleHome();
    }
}
