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
import java.util.List;

import lk.nd.cycler.R;
import lk.nd.cycler.SingleShopViewActivity;
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
                Intent i =  new Intent(context, SingleShopViewActivity.class);
                context.startActivity(i);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(context, SingleShopViewActivity.class);
                context.startActivity(i);
            }
        });

        holder.rentNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.rent_now_card);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);

                TextView shopNameTextView = dialog.findViewById(R.id.shopNameDialog);
                Button placeOrderButton = dialog.findViewById(R.id.placeOrderBtn);
                ImageView closeButton = dialog.findViewById(R.id.closeIcon);
                TextView errorText = dialog.findViewById(R.id.errorText);

                errorText.setVisibility(View.INVISIBLE);

                shopNameTextView.setText(location.getShopName());

                closeButton.setOnClickListener(v -> dialog.dismiss());

                placeOrderButton.setOnClickListener(v -> {
                    Toast.makeText(context, "Order placed for " + location.getShopName(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });

                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return locationList.size();
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
