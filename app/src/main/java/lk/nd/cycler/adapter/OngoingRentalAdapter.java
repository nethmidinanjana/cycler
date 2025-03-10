package lk.nd.cycler.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import lk.nd.cycler.R;
import lk.nd.cycler.model.OngoingRental;

public class OngoingRentalAdapter extends RecyclerView.Adapter<OngoingRentalAdapter.ViewHolder> {

    private Context context;
    private List<OngoingRental> ongoingRentals;
    private Handler handler = new Handler();

    public OngoingRentalAdapter(Context context, List<OngoingRental> ongoingRentals) {
        this.context = context;
        this.ongoingRentals = ongoingRentals;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ongoing_rental_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OngoingRental rental = ongoingRentals.get(position);
        holder.shopName.setText(rental.getShopName());
        holder.rentedCycleCount.setText(String.valueOf(rental.getRentedCycleCount()));

        holder.timeSpent.setText(rental.getTimeSpentInHours() + (rental.getTimeSpentInHours() == 1 ? " hour" : " hours"));
        holder.payment.setText("Rs. "+ rental.getPaymentPerHour() * rental.getTimeSpentInHours()+ ".00");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long timeElapsed = currentTime - rental.getRentedDateTime().getTime();

                if (timeElapsed >= 60 * 60 * 1000) {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        notifyItemChanged(currentPosition);
                    }
                }
            }
        }, 60 * 60 * 1000);


    }

    @Override
    public int getItemCount() {
        return ongoingRentals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView shopName, rentedCycleCount, timeSpent, payment;

        public ViewHolder(View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.shop_name);
            rentedCycleCount = itemView.findViewById(R.id.rentedCycleCount);
            timeSpent = itemView.findViewById(R.id.timeSpent);
            payment = itemView.findViewById(R.id.payment);
        }
    }
}
