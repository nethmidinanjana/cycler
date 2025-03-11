package lk.nd.cycler.ui.rentals;

import android.content.Context;
import android.content.SharedPreferences;
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
import lk.nd.cycler.database.DatabaseHelper;
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
    private int userId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RentalsViewModel rentalsViewModel =
                new ViewModelProvider(this).get(RentalsViewModel.class);

        binding = FragmentRentalsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = (int) sharedPreferences.getLong("user_id", -1);

        // Setup RecyclerView for Ongoing Rentals
        ongoingRentalRecyclerView = root.findViewById(R.id.ongoingRentalRecyclerView);
        ongoingRentalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ongoingRentals = new ArrayList<>();
        ongoingRentalAdapter = new OngoingRentalAdapter(getContext(), ongoingRentals); // Initialize Adapter
        ongoingRentalRecyclerView.setAdapter(ongoingRentalAdapter);

        // Now load the data
        loadOngoingRentalData();

        // Setup RecyclerView for Payment History
        paymentHistoryRecyclerView = root.findViewById(R.id.paymentHistoryRecyclerView);
        paymentHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        paymentHistoryList = new ArrayList<>();
        paymentHistoryAdapter = new PaymentHistoryAdapter(getContext(), paymentHistoryList);
        paymentHistoryRecyclerView.setAdapter(paymentHistoryAdapter);

        loadPaymentHistoryData();

        return root;
    }

    private void loadOngoingRentalData() {
        if (ongoingRentalAdapter == null) return;

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        ongoingRentals.clear();
        ongoingRentals.addAll(dbHelper.getOngoingRentalsByUserId(userId));

        ongoingRentalAdapter.notifyDataSetChanged();
    }


    private void loadPaymentHistoryData() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        paymentHistoryList.clear();
        paymentHistoryList.addAll(dbHelper.getPaymentHistoryByUserId(userId));

        paymentHistoryAdapter.notifyDataSetChanged();
    }



    @Override
    public void onResume() {
        super.onResume();
        loadOngoingRentalData();
        loadPaymentHistoryData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}