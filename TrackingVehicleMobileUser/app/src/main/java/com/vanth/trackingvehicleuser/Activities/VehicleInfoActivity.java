package com.vanth.trackingvehicleuser.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.vanth.trackingvehicleuser.API.ApiService;
import com.vanth.trackingvehicleuser.API.ApiUtils;
import com.vanth.trackingvehicleuser.R;
import com.vanth.trackingvehicleuser.Request.VehicleInfoRequest;
import com.vanth.trackingvehicleuser.Request.VehicleRequestHome;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleInfoActivity  extends AppCompatActivity {

    TextView id_vehicle;
    TextView title;
    TextView status;
    TextView lastConect;
    AppCompatButton tracking;
    AppCompatButton edit;
    AppCompatButton schedule;
    String vehicle_id = "";

    ApiService apiService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                vehicle_id = extras.getString("id_vehicle");
            }
        }
        else
        {
            vehicle_id = (String) savedInstanceState.getSerializable("id_vehicle");
        }
        this.setContentView(R.layout.vehilcle_info_layout);
        mapInstance();
        getVehicleInfo(vehicle_id);
        setEvent();
    }


    void mapInstance()
    {
        id_vehicle = findViewById(R.id.vehicle_info_id);
        title = findViewById(R.id.vehicle_info_title);
        status = findViewById(R.id.vehicle_info_status);
        lastConect = findViewById(R.id.vehicle_info_lastconnect);
        tracking = findViewById(R.id.vehicle_info_tracking);
        edit = findViewById(R.id.vehicle_info_edit);
        schedule = findViewById(R.id.vehicle_info_schedule);

    }

    void setEvent()
    {
        tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehicleInfoActivity.this, TrackingActivity.class);
                intent.putExtra("id_vehicle",vehicle_id);
                startActivity(intent);
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehicleInfoActivity.this,ScheduleListActivity.class);
                intent.putExtra("vehicle_id",vehicle_id);
                startActivity(intent);
            }
        });
    }

    void getVehicleInfo(String id)
    {
        apiService = ApiUtils.getApiService();
        apiService.getVehicleInfo(id).enqueue(new Callback<VehicleInfoRequest>() {
            @Override
            public void onResponse(Call<VehicleInfoRequest> call, Response<VehicleInfoRequest> response) {
                if (response.isSuccessful())
                {
                    VehicleInfoRequest vehicleInfoRequest = response.body();

                    id_vehicle.setText(vehicleInfoRequest.getId());
                    title.setText(vehicleInfoRequest.getTitle());
                    if (vehicleInfoRequest.isState()) status.setText("Trạng thái: Đang hoạt động");
                    else status.setText("Trạng thái: Không kết nối");
                    lastConect.setText("Lần cuối kết nối: " + vehicleInfoRequest.getLastConnectTime());

                    //  long,lad
                }
                else
                {
                    try
                    {
                        String result = response.errorBody().string();
                        System.out.println(result);
                        if (result.compareToIgnoreCase("102") == 0)
                            Toast.makeText(VehicleInfoActivity.this, "Biển số không tồn tại trong hệ thống", Toast.LENGTH_SHORT).show();
                        else if (result.compareToIgnoreCase("101") == 0)
                            Toast.makeText(VehicleInfoActivity.this, "Lỗi hệ thống, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){

                    }
                }
            }

            @Override
            public void onFailure(Call<VehicleInfoRequest> call, Throwable t) {

            }
        });
    }
}
