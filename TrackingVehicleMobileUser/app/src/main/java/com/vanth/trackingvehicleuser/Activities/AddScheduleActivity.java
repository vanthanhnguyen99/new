package com.vanth.trackingvehicleuser.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vanth.trackingvehicleuser.API.ApiService;
import com.vanth.trackingvehicleuser.API.ApiUtils;
import com.vanth.trackingvehicleuser.R;
import com.vanth.trackingvehicleuser.Request.MyAddress;
import com.vanth.trackingvehicleuser.Request.districts;
import com.vanth.trackingvehicleuser.Request.province;
import com.vanth.trackingvehicleuser.Request.wards;
import com.vanth.trackingvehicleuser.Request.Coord;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddScheduleActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextInputEditText id;
    TextInputEditText startdate;
    TextInputEditText starttime;
    TextInputEditText finishdate;
    TextInputEditText finishime;
    TextInputEditText startlocation;
    TextInputEditText finishlocation;
    AppCompatButton add;
    FrameLayout view_startlocation;
    FrameLayout view_finishlocaion;
    ConstraintLayout view_main;
    SupportMapFragment map_startlocaion;
    SupportMapFragment map_finishlocation;
    GoogleMap googleMap_start = null;
    GoogleMap googleMap_finish = null;
    ImageView startlocation_close;
    ImageView finishlocation_close;
    SearchableSpinner startProvince;
    SearchableSpinner startDistrict;
    SearchableSpinner startWard;
    TextInputEditText startAddress;

    boolean handleStartDate = false;
    boolean handleStartTime = false;

    LatLng startLocationValue;
    LatLng fnishLocationValue;

    MarkerOptions startMarker;
    MarkerOptions finishMarker;

    List<province> listProvince;
    List<districts> listDistrict;
    List<wards> listWard;

    boolean isDragging = false;

    ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_schedule_layout);

        mapInstance();
        getProvince();
        setEvent();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        System.out.println("Xin chào các bạn");

        if (view_startlocation.getVisibility() == View.VISIBLE)
        {
            if (googleMap_start == null)
            {
                googleMap_start = googleMap;
            }

        }
        if (view_finishlocaion.getVisibility() == View.VISIBLE)
        {
            if (googleMap_finish == null)
                googleMap_finish = googleMap;

        }

    }

    void mapInstance()
    {
        id = findViewById(R.id.add_schedule_id_vehicle);
        starttime = findViewById(R.id.add_schedule_starttime);
        startdate = findViewById(R.id.add_schedule_startdate);
        finishdate = findViewById(R.id.add_schedule_finishdate);
        finishime = findViewById(R.id.add_schedule_finishtime);
        startlocation = findViewById(R.id.add_schedule_startlocation);
        finishlocation = findViewById(R.id.add_schedule_finishlocation);
        add = findViewById(R.id.add_schedule_add);
        view_startlocation = findViewById(R.id.add_schedule_view_startlocation);
        view_finishlocaion = findViewById(R.id.add_schedule_view_finishlocation);

        map_startlocaion = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.add_schedule_map_startlocaion);
        map_finishlocation = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.add_schedule_finishlocation);
        startlocation_close = findViewById(R.id.add_schedule_startlocation_close);
        finishlocation_close = findViewById(R.id.add_schedule_finishlocation_close);

        startProvince = findViewById(R.id.add_schedule_startprovince);
        startDistrict = findViewById(R.id.add_schedule_startdisrict);
        startWard = findViewById(R.id.add_schedule_startward);
        startAddress = findViewById(R.id.add_schedule_startaddress);


    }

    void setEvent()
    {
        startlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_startlocation.setVisibility(View.VISIBLE);

                id.setEnabled(false);
                starttime.setEnabled(false);
                startdate.setEnabled(false);
                finishdate.setEnabled(false);
                finishime.setEnabled(false);
                startlocation.setEnabled(false);
                finishime.setEnabled(false);
                finishlocation.setEnabled(false);
                add.setEnabled(false);

                map_startlocaion.getMapAsync(AddScheduleActivity.this);
            }
        });

        finishlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_finishlocaion.setVisibility(View.VISIBLE);

                id.setEnabled(false);
                starttime.setEnabled(false);
                startdate.setEnabled(false);
                finishdate.setEnabled(false);
                finishime.setEnabled(false);
                startlocation.setEnabled(false);
                finishime.setEnabled(false);
                finishlocation.setEnabled(false);
                add.setEnabled(false);

                map_finishlocation.getMapAsync(AddScheduleActivity.this);
            }
        });

        startlocation_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_startlocation.setVisibility(View.GONE);

                id.setEnabled(true);
                starttime.setEnabled(true);
                startdate.setEnabled(true);
                finishdate.setEnabled(true);
                finishime.setEnabled(true);
                startlocation.setEnabled(true);
                finishime.setEnabled(true);
                finishlocation.setEnabled(true);
                add.setEnabled(true);
            }
        });

        finishlocation_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_finishlocaion.setVisibility(View.GONE);

                id.setEnabled(true);
                starttime.setEnabled(true);
                startdate.setEnabled(true);
                finishdate.setEnabled(true);
                finishime.setEnabled(true);
                startlocation.setEnabled(true);
                finishime.setEnabled(true);
                finishlocation.setEnabled(true);
                add.setEnabled(true);
            }
        });

        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleStartDate = true;
                handleDatePicker();
            }
        });

        finishdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleStartDate = false;
                handleDatePicker();
            }
        });

        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                handleStartTime = true;
                hanleTimePicker();
            }
        });

        finishime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleStartTime = false;
                hanleTimePicker();
            }
        });

        startProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isDragging) return;
                listDistrict = listProvince.get(i).getDistricts();
                setDistrictAdapter(listDistrict,0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        startDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isDragging) return;
                listWard = listDistrict.get(i).getWards();
                setWardAdapter(listWard,0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        startWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                startAddress.setEnabled(true);
                if (!isDragging)
                    startAddress.setText("");
                else
                {
                    if (startAddress.getText().toString().length() > 0)
                    {
                        isDragging = false;
                    }
                }

                System.out.println("Value " + isDragging);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        startAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (startAddress.length() == 0) return;

                if (isDragging) return;

                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                String fullAddress = startAddress.getText() + " " + startWard.getSelectedItem().toString() + " " + startDistrict.getSelectedItem().toString() + " " + startProvince.getSelectedItem().toString();
                MyAddress myAddress = new MyAddress();
                myAddress.setAddress(fullAddress);

                System.out.println(fullAddress);

                apiService = ApiUtils.getApiService("map");
                apiService.getCoordFromLocaion(myAddress).enqueue(new Callback<Coord>() {
                    @Override
                    public void onResponse(Call<Coord> call, Response<Coord> response) {
                        if (response.isSuccessful())
                        {
                            Coord coord = response.body();

                            startLocationValue = new LatLng(coord.getLat(), coord.getLng());
                            googleMap_start.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocationValue,13));
                            googleMap_start.clear();
                            startMarker = new MarkerOptions()
                                    .position(startLocationValue).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                    .title("Địa điểm bắt đầu")
                                    .draggable(true);
                            googleMap_start.addMarker(startMarker);
                            setMapEvent();


                        }
                    }

                    @Override
                    public void onFailure(Call<Coord> call, Throwable t) {

                    }
                });

            }
        });


    }

    void setMapEvent()
    {
        googleMap_start.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                if (marker.getTitle().equalsIgnoreCase("Địa điểm bắt đầu"))
                {
                    double lat = marker.getPosition().latitude;
                    double lng = marker.getPosition().longitude;
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> address = null;
                    startAddress.setText("");
                    try
                    {
                        address = geocoder.getFromLocation(lat,lng,1);
                        String[] splitAddress = address.get(0).getAddressLine(0).split(",");
                        System.out.println(address.get(0).getAddressLine(0));
                        String province = splitAddress[splitAddress.length-2].trim();
                        String district = splitAddress[splitAddress.length-3].trim();
                        String ward = splitAddress[splitAddress.length-4].trim();


                        if (district.equalsIgnoreCase("Quận 2") || district.equalsIgnoreCase("Quận 9") || district.equalsIgnoreCase("Quận Thủ Đức"))
                            district = "Thủ Đức";

                        isDragging = true;

                        for (int i = 0; i < listProvince.size(); i++)
                        {
                            if (listProvince.get(i).getName().contains(province))
                            {
                                startProvince.setSelection(i);
                                listDistrict = listProvince.get(i).getDistricts();
                                setDistrictAdapter(listDistrict,0);
                                break;
                            }
                        }



                        System.out.println("Province: " + startProvince.getSelectedItem().toString());

                        for (int i = 0; i < listDistrict.size(); i++)
                        {
                            System.out.println("District: " + listDistrict.get(i).getName());
                            if (listDistrict.get(i).getName().contains(district))
                            {
                                startDistrict.setSelection(i);
                                listWard = listDistrict.get(i).getWards();
                                setWardAdapter(listWard,0);
                                break;
                            }
                        }


                        for (int i = 0; i < listWard.size(); i++)
                        {
                            if (listWard.get(i).getName().contains(ward))
                            {
                                startWard.setSelection(i);
                                break;

                            }
                        }



                        String finalAddress = splitAddress[0];
                        for (int i = 1; i < splitAddress.length-4; i++)
                        {
                            finalAddress = finalAddress + " " + splitAddress[i];
                        }
                        startAddress.setText(finalAddress);
                        System.out.println(finalAddress);
                        try
                        {
                            Thread.sleep(300);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        while (startAddress.getText().toString().length() == 0)
                        {
                            startAddress.setText(finalAddress);

                            try
                            {
                                Thread.sleep(300);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }


                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }
        });
    }

    void handleDatePicker()
    {
        String result;

        LocalDate date = LocalDate.now();

        int year = date.getYear();
        int month = date.getMonthValue()-1;
        int day = date.getDayOfMonth();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
               LocalDate getDate = LocalDate.of(year,month,day);

               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
               if (handleStartDate)
               {
                   startdate.setText(getDate.format(formatter));
                   handleStartDate = false;
               }
               else
               {
                   finishdate.setText(getDate.format(formatter));
               }

            }
        },year,month,day);

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());

        datePickerDialog.show();
    }
    void hanleTimePicker()
    {
        LocalTime time = LocalTime.now();
        int hour = time.getHour();
        int min = time.getMinute();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                if (handleStartTime)
                {
                    LocalTime time = LocalTime.of(hour,min);
                    starttime.setText(time.toString());
                    handleStartTime = false;
                }
                else
                {
                    LocalTime time = LocalTime.of(hour,min);
                    finishime.setText(time.toString());
                    handleStartTime = false;
                }
            }
        },hour,min,true);

        timePickerDialog.show();
    }

    void setProvinceAdapter(List<province> listItem)
    {
        ArrayList<String> item = new ArrayList<>();
        for (province province :listItem)
        {
            item.add(province.getName());
        }

        startProvince.setTitle("Chọn tỉnh thành");
        startProvince.setAdapter(new ArrayAdapter<>(AddScheduleActivity.this, android.R.layout.simple_spinner_dropdown_item,item));
    }

    void setDistrictAdapter(List<districts> listItem, int cases)
    {
        switch (cases)
        {
            case 0: // start
            {
                ArrayList<String> item = new ArrayList<>();
                for (districts districts :listItem)
                {
                    item.add(districts.getName());
                }
                startDistrict.setTitle("Chọn quận huyện");
                startDistrict.setAdapter(new ArrayAdapter<>(AddScheduleActivity.this, android.R.layout.simple_spinner_dropdown_item,item));
                break;
            }
            case 1: // finish
            {
                break;
            }
        }


    }

    void setWardAdapter(List<wards> listItem, int cases)
    {
        switch (cases)
        {
            case 0: // start
            {
                ArrayList<String> item = new ArrayList<>();
                for (wards wards :listItem)
                {
                    item.add(wards.getName());
                }
                startWard.setTitle("Chọn phường xã");
                startWard.setAdapter(new ArrayAdapter<>(AddScheduleActivity.this, android.R.layout.simple_spinner_dropdown_item,item));
                break;
            }
            case 1: // finish
            {
                break;
            }
        }


    }

    void getProvince()
    {
        apiService = ApiUtils.getApiService("province");

        apiService.getListProvince().enqueue(new Callback<List<province>>() {
            @Override
            public void onResponse(Call<List<province>> call, Response<List<province>> response) {
                if (response.isSuccessful())
                {
                   listProvince = response.body();
                    System.out.println(listProvince.get(0).getDistricts().get(0).getName());
                   setProvinceAdapter(listProvince);
                }
            }

            @Override
            public void onFailure(Call<List<province>> call, Throwable t) {

            }
        });
    }
}
