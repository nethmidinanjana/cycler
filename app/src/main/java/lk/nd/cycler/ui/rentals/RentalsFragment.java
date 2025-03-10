package lk.nd.cycler.ui.rentals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.nd.cycler.R;
import lk.nd.cycler.adapter.OngoingRentalAdapter;
import lk.nd.cycler.adapter.PaymentHistoryAdapter;
import lk.nd.cycler.databinding.FragmentRentalsBinding;
import lk.nd.cycler.model.OngoingRental;
import lk.nd.cycler.model.PaymentHistoryCard;

public class RentalsFragment extends Fragment {

    private FragmentRentalsBinding binding;
    private RecyclerView ongoingRentalRecyclerView;
    private OngoingRentalAdapter ongoingRentalAdapter;
    private List<OngoingRental> ongoingRentals;

    private RecyclerView paymentHistoryRecyclerView;
    private PaymentHistoryAdapter paymentHistoryAdapter;
    private List<PaymentHistoryCard> paymentHistoryList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RentalsViewModel rentalsViewModel =
                new ViewModelProvider(this).get(RentalsViewModel.class);

        binding = FragmentRentalsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ongoingRentalRecyclerView = root.findViewById(R.id.ongoingRentalRecyclerView);
        ongoingRentalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ongoingRentals = new ArrayList<>();
        loadOngoingRentalData();

        ongoingRentalAdapter = new OngoingRentalAdapter(getContext(), ongoingRentals);
        ongoingRentalRecyclerView.setAdapter(ongoingRentalAdapter);


        paymentHistoryRecyclerView = root.findViewById(R.id.paymentHistoryRecyclerView);
        paymentHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the list and add sample data
        paymentHistoryList = new ArrayList<>();
        loadPaymentHistoryData();

        // Set up the adapter
        paymentHistoryAdapter = new PaymentHistoryAdapter(getContext(), paymentHistoryList);
        paymentHistoryRecyclerView.setAdapter(paymentHistoryAdapter);

        return root;
    }

    private void loadOngoingRentalData() {
        // ongoing rental table should fetch according to the userid and data which's status is pending
        ongoingRentals.add(new OngoingRental(1, 101, "Kandy Pedal Power Rentals", 5, new Date(System.currentTimeMillis() - 3600000), "Ongoing", 300));
        ongoingRentals.add(new OngoingRental(2, 102, "Mountain Trail Bikes", 3, new Date(System.currentTimeMillis() - 7200000), "Ongoing", 400));
    }

    private void loadPaymentHistoryData() {
        // Add some sample payment history data
        paymentHistoryList.add(new PaymentHistoryCard("Kandy Pedal Power Rentals", 3, 1200));
        paymentHistoryList.add(new PaymentHistoryCard("Hilltop Bike Rentals", 5, 2000));
        paymentHistoryList.add(new PaymentHistoryCard("Royal Cycle Hire", 2, 1500));
        paymentHistoryList.add(new PaymentHistoryCard("City Wheels Bicycle Rental", 1, 1100));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}