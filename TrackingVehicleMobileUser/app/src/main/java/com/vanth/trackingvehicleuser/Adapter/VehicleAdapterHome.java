package com.vanth.trackingvehicleuser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vanth.trackingvehicleuser.Activities.MainActivity;
import com.vanth.trackingvehicleuser.Activities.VehicleInfoActivity;
import com.vanth.trackingvehicleuser.Activities.WelcomeActivity;
import com.vanth.trackingvehicleuser.R;
import com.vanth.trackingvehicleuser.Request.VehicleRequestHome;

import java.util.List;

public class VehicleAdapterHome extends RecyclerView.Adapter<VehicleAdapterHome.itemViewHolder> {
    Context context;
    List<VehicleRequestHome> vehicleRequestHomeList;

    public VehicleAdapterHome(Context context, List<VehicleRequestHome> vehicleRequestHomes)
    {
        this.context = context;
        this.vehicleRequestHomeList = vehicleRequestHomes;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_layout_item,null);
        itemViewHolder itemViewHolder = new itemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position)
    {
        VehicleRequestHome vehicleRequestHome = vehicleRequestHomeList.get(position);
        holder.id.setText(vehicleRequestHome.getId());
        holder.title.setText(vehicleRequestHome.getTitle());
        System.out.println(vehicleRequestHome.getisOnline() + " name: " + vehicleRequestHome.getId());
        if (vehicleRequestHome.getisOnline())
        {
            holder.status.setImageResource(R.drawable.online_status);
        }
        else holder.status.setImageResource(R.drawable.offline_status);
    }

    @Override
    public int getItemCount() {
        return vehicleRequestHomeList.size();
    }

    public  class itemViewHolder extends RecyclerView.ViewHolder
    {
        TextView id;
        TextView title;
        ImageView status;

        public itemViewHolder(View itemView)
        {
            super(itemView);

            // Mapping
            id = itemView.findViewById(R.id.home_item_vehicleID);
            title = itemView.findViewById(R.id.home_item_title);
            status = itemView.findViewById(R.id.home_item_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // View Vehicle's detail
                    Intent intent = new Intent(context,VehicleInfoActivity.class);
                    intent.putExtra("id_vehicle",id.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
