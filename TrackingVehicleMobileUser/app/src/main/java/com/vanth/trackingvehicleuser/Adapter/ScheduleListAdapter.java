package com.vanth.trackingvehicleuser.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vanth.trackingvehicleuser.Entity.Schedule;
import com.vanth.trackingvehicleuser.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.itemViewHolder>{
    Context context;
    List<Schedule> listSchedule;

    public ScheduleListAdapter(Context context, List<Schedule> data)
    {
        this.context = context;
        listSchedule = data;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item_layout,parent,false);
        itemViewHolder itemViewHolder = new itemViewHolder(view);

        return itemViewHolder;
    }

    @Override
    public int getItemCount() {
        return listSchedule.size();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position)
    {
        Schedule schedule = listSchedule.get(position);

        holder.vehicle_id.setText(schedule.getVehicle_id());
        switch (schedule.getStatus())
        {
            case -1:
            {
                holder.status.setText("Đã hủy");
                holder.status.setTextColor(R.color.red);
                break;
            }
            case 0:
            {
                holder.status.setText("Đang chờ");
                holder.status.setTextColor(R.color.orange);
                break;
            }
            case 1:
            {
                holder.status.setText("Đang thực hiện");
                holder.status.setTextColor(R.color.green);
                break;
            }
            case 2:
            {
                holder.status.setText("Hoàn thành");
                holder.status.setTextColor(R.color.stronggreen);
                break;
            }
        }
        holder.startTime.setText(schedule.getStart_time().toString());
        holder.endTime.setText(schedule.getFinish_time().toString());

        Geocoder geocoder;
        geocoder = new Geocoder(context, Locale.getDefault());
        try
        {
            List<Address> address = geocoder.getFromLocation(schedule.getStart_x(),schedule.getStart_y(),1);
            holder.startLocation.setText("Địa điểm khởi hành: " + address.get(0).getAddressLine(0));

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            List<Address> address = geocoder.getFromLocation(schedule.getFinish_x(),schedule.getFinish_y(),1);
            holder.endLocation.setText("Địa điểm đích: " + address.get(0).getAddressLine(0));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    class itemViewHolder extends RecyclerView.ViewHolder
    {
        TextView vehicle_id;
        TextView status;
        TextView startTime;
        TextView endTime;
        TextView startLocation;
        TextView endLocation;

        public itemViewHolder(View itemView)
        {
            super(itemView);

            // Mapping
            vehicle_id = itemView.findViewById(R.id.schedule_item_vehicleID);
            status = itemView.findViewById(R.id.schedule_item_status);
            startTime = itemView.findViewById(R.id.schedule_item_starttime);
            endTime = itemView.findViewById(R.id.schedule_item_endtimeschedule);
            startLocation = itemView.findViewById(R.id.schedule_item_startlocation);
            endLocation = itemView.findViewById(R.id.schedule_item_endlocation);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
