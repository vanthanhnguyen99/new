package com.vanth.trackingvehicleuser.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.vanth.trackingvehicleuser.API.ApiService;
import com.vanth.trackingvehicleuser.API.ApiUtils;
import com.vanth.trackingvehicleuser.API.getHomeDataService;
import com.vanth.trackingvehicleuser.Adapter.VehicleAdapterHome;
import com.vanth.trackingvehicleuser.R;
import com.vanth.trackingvehicleuser.Request.VehicleRequestHome;

import java.util.ArrayList;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity{

    androidx.appcompat.widget.Toolbar toolbar;
    ViewFlipper viewFlipper;
    NavigationView navigationView;
    ListView listViewHomePage;
    DrawerLayout drawerLayoutHomePage;
    SwipeRefreshLayout pullToRefresh;
    List<VehicleRequestHome> vehicleRequestHomeList;

    RecyclerView recyclerView;
    VehicleAdapterHome vehicleAdapterHome;

    ApiService apiService;

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mapInstance();
        initActionBar();
        initActionViewFlipper();

        getVehicleHome();
        setEvent();
    }

    void mapInstance()
    {
        toolbar = findViewById(R.id.main_toolbarHomePage);
        viewFlipper = findViewById(R.id.main_viewFlipper);
        recyclerView = findViewById(R.id.main_recyclerView);
        pullToRefresh = findViewById(R.id.main_pullToRefresh);
        navigationView = findViewById(R.id.main_navigationView);
        listViewHomePage = findViewById(R.id.main_listViewHomePage);
        drawerLayoutHomePage = findViewById(R.id.drawerLayoutHomePage);

        textView = navigationView.getHeaderView(0).findViewById(R.id.tvHeaderNavLogiNname);
        textView.setText("Xin chào");
    }

    void setEvent()
    {
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setAdapterVehicleHome(vehicleRequestHomeList);
                pullToRefresh.setRefreshing(false);
            }
        });


    }
    void initActionBar()
    {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayoutHomePage.openDrawer(GravityCompat.START);
            }
        });

    }

    void initActionViewFlipper()
    {
        ArrayList<Integer> bannerList = new ArrayList<>();
        bannerList.add(R.drawable.banner1);
        bannerList.add(R.drawable.banner2);
        bannerList.add(R.drawable.banner3);
        bannerList.add(R.drawable.banner4);

        for(int i = 0; i < bannerList.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
//            Picasso.get().load(bannerList.get(i)).into(imageView);
            imageView.setImageResource(bannerList.get(i));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
    }

    void setAdapterVehicleHome(List<VehicleRequestHome> vehicleRequestHomes)
    {


        vehicleAdapterHome = new VehicleAdapterHome(getApplicationContext(),vehicleRequestHomes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.setAdapter(vehicleAdapterHome);

        vehicleAdapterHome.notifyDataSetChanged();

    }

    void getVehicleHome()
    {
        apiService = ApiUtils.getApiService();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run(){
                while (true)
                {
                    apiService.getUserVehicle(1).enqueue(new Callback<List<VehicleRequestHome>>() {
                        @Override
                        public void onResponse(Call<List<VehicleRequestHome>> call, Response<List<VehicleRequestHome>> response) {
                            if (response.isSuccessful())
                            {
                                List<VehicleRequestHome> list = response.body();
                                list.sort(Comparator.comparingInt((VehicleRequestHome temp)->temp.getisOnline()?0:1));
                                if (vehicleRequestHomeList == null || list.size() != vehicleRequestHomeList.size())
                                {
                                    vehicleRequestHomeList = list;
                                    setAdapterVehicleHome(vehicleRequestHomeList);
                                }
                                else
                                {
                                    for (int i = 0; i < list.size(); i++)
                                    {
                                        if (!vehicleRequestHomeList.get(i).getId().equalsIgnoreCase(list.get(i).getId()) || vehicleRequestHomeList.get(i).getisOnline() != list.get(i).getisOnline())
                                        {
                                            vehicleRequestHomeList = list;
                                            setAdapterVehicleHome(vehicleRequestHomeList);
                                        }
                                    }
                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<List<VehicleRequestHome>> call, Throwable t) {

                        }
                    });
                    try {
                        Thread.sleep(3000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();





    }

}
