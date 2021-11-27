package com.vanth.trackingvehicleuser.Activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vanth.trackingvehicleuser.API.ApiService;
import com.vanth.trackingvehicleuser.API.ApiUtils;
import com.vanth.trackingvehicleuser.R;
import com.vanth.trackingvehicleuser.Request.VehicleInfoRequest;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment map;
    GoogleMap googleMap;
    LatLng location;
    ApiService apiService;
    String id_vehicle;
    VehicleInfoRequest vehicleInfoRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_layout);

        if (savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                id_vehicle = extras.getString("id_vehicle");
            }
        }
        else
        {
            id_vehicle = (String) savedInstanceState.getSerializable("id_vehicle");
        }

        mapInstane();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        LatLng Brisbane = new LatLng(-27.470125, 153.021072);
//        double distance = SphericalUtil.computeDistanceBetween(sydney, Brisbane);
//        Toast.makeText(this, "Distance between Sydney and Brisbane is \n " + String.format("%.2f", distance / 1000) + "km", Toast.LENGTH_SHORT).show();
//        LatLng sydney = new LatLng(-33.852, 151.211);
//        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,13));
//
//        this.googleMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .title("Marker in Sydney"));
        Tracking();
    }

    void mapInstane()
    {
        map = (SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.tracking_mymap);
        map.getMapAsync(this);

    }

    void Tracking()
    {
        apiService = ApiUtils.getApiService();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) // not exit
                {
                    apiService.getVehicleInfo(id_vehicle).enqueue(new Callback<VehicleInfoRequest>() {
                        @Override
                        public void onResponse(Call<VehicleInfoRequest> call, Response<VehicleInfoRequest> response)
                        {
                            if (response.isSuccessful())
                            {
                                vehicleInfoRequest = response.body();
                                if (location != null && location.latitude == vehicleInfoRequest.getLastX() && location.longitude == vehicleInfoRequest.getLastY())
                                    return;
                                location = new LatLng(vehicleInfoRequest.getLastX(),vehicleInfoRequest.getLastY());
                                googleMap.clear();
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,13));

                                googleMap.addMarker(new MarkerOptions()
                                        .position(location)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                        .title(vehicleInfoRequest.getTitle()));
                            }
                            else
                            {
                                try
                                {
                                    String result = response.errorBody().string();
                                    System.out.println(result);
                                    if (result.compareToIgnoreCase("102") == 0)
                                        Toast.makeText(TrackingActivity.this, "Biển số không tồn tại trong hệ thống", Toast.LENGTH_SHORT).show();
                                    else if (result.compareToIgnoreCase("101") == 0)
                                        Toast.makeText(TrackingActivity.this, "Lỗi hệ thống, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e){

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<VehicleInfoRequest> call, Throwable t)
                        {

                        }
                    });

                    try
                    {
                        Thread.sleep(3000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }


}
