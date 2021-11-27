package com.vanth.trackingvehicleuser.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vanth.trackingvehicleuser.API.ApiService;
import com.vanth.trackingvehicleuser.API.ApiUtils;
import com.vanth.trackingvehicleuser.Adapter.ScheduleListAdapter;
import com.vanth.trackingvehicleuser.Entity.Schedule;
import com.vanth.trackingvehicleuser.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleListActivity extends AppCompatActivity {
    ListView listView;
    SwipeRefreshLayout pullToRefresh;
    List<Schedule> scheduleList;
    ApiService apiService;
    RecyclerView recyclerView;
    String id_vehicle;
    FloatingActionButton add;

    ScheduleListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_layout);

        if (savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                id_vehicle = extras.getString("vehicle_id");
            }
        }
        else {
            id_vehicle = (String) savedInstanceState.getSerializable("vehicle_id");
        }
        mapIntance();
        getData();
        setEvent();
    }

    void setAdapter()
    {
        adapter = new ScheduleListAdapter(getApplicationContext(),scheduleList);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    void mapIntance()
    {
        listView = findViewById(R.id.schedule_list_listView);
        pullToRefresh = findViewById(R.id.schedule_list_pullToRefresh);
        recyclerView = findViewById(R.id.schedule_list_recyclerView);
        add = findViewById(R.id.schedule_list_add);
    }

    void setEvent()
    {
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                pullToRefresh.setRefreshing(false);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleListActivity.this,AddScheduleActivity.class);
                startActivity(intent);
            }
        });
    }

    void getData()
    {
        apiService = ApiUtils.getApiService();
        apiService.getVehicleScheduleList(id_vehicle).enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response)
            {
                if (response.isSuccessful())
                {
                    System.out.println(response.message());
                    scheduleList = response.body();
                    System.out.println( "size = " + scheduleList.size());
                    setAdapter();
                }
                else
                {
                    try
                    {
                        String result = response.errorBody().string();
                        System.out.println(result);
                        if (result.compareToIgnoreCase("103") == 0)
                            Toast.makeText(ScheduleListActivity.this, "Lỗi hệ thống", Toast.LENGTH_SHORT).show();

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t)
            {
                System.out.println(t.getMessage());

            }
        });
    }
}
