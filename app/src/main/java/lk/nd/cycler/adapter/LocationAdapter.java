package lk.nd.cycler.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import lk.nd.cycler.R;
import lk.nd.cycler.MapViewActivity;
import lk.nd.cycler.SingleShopView;
import lk.nd.cycler.model.LocationCard;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private Context context;
    private List<LocationCard> locationList;

    public LocationAdapter(Context context, List<LocationCard> locationList) {
        this.context = context;
        this.locationList = locationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationCard location = locationList.get(position);

        holder.shopName.setText(location.getShopName());
        holder.location.setText(location.getLocation());
        holder.rating.setText(location.getRating());
        holder.bicycleCount.setText(String.valueOf(location.getBicycleCount()));

        Glide.with(context)
                .load(location.getImageUrl()) // URL or local drawable
                .placeholder(R.mipmap.rental_place1) // Fallback image
                .into(holder.image);

        //go to map view
        holder.showMapLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(context, MapViewActivity.class);
                context.startActivity(i);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(context, MapViewActivity.class);
                context.startActivity(i);
            }
        });

        //rent now function
        holder.rentNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, SingleShopView.class);
                i.putExtra("location_data", location);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void updateList(List<LocationCard> newList) {
        locationList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName, location, rating, bicycleCount;
        ImageView image, showMapLocationBtn;
        Button rentNowBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.shopName);
            location = itemView.findViewById(R.id.shopLocation);
            rating = itemView.findViewById(R.id.shopRating);
            bicycleCount = itemView.findViewById(R.id.bicycleCount);
            image = itemView.findViewById(R.id.shopImage);
            rentNowBtn = itemView.findViewById(R.id.rentNowBtn);
            showMapLocationBtn = itemView.findViewById(R.id.showMapLocationBtn);
        }
    }
}
