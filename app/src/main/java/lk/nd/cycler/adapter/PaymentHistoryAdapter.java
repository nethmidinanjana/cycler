package lk.nd.cycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lk.nd.cycler.R;
import lk.nd.cycler.model.PaymentHistoryCard;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder> {

    private Context context;
    private List<PaymentHistoryCard> paymentHistoryList;

    public PaymentHistoryAdapter(Context context, List<PaymentHistoryCard> paymentHistoryList) {
        this.context = context;
        this.paymentHistoryList = paymentHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.renting_history_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentHistoryCard paymentHistory = paymentHistoryList.get(position);

        // Set the values from the data to the views
        holder.shopName.setText(paymentHistory.getShopName());
        holder.cycleCount.setText(paymentHistory.getCycleCount() + (paymentHistory.getCycleCount() == 1 ? "  Bicycle" : "  Bicycles"));
        holder.total.setText("Rs." + paymentHistory.getTotal()+".00");
    }

    @Override
    public int getItemCount() {
        return paymentHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName, cycleCount, total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.shopname);
            cycleCount = itemView.findViewById(R.id.cycleCount);
            total = itemView.findViewById(R.id.total);
        }
    }
}
