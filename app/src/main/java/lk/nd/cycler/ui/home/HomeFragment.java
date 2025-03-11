package lk.nd.cycler.ui.home;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lk.nd.cycler.R;
import lk.nd.cycler.adapter.LocationAdapter;
import lk.nd.cycler.database.DatabaseHelper;
import lk.nd.cycler.databinding.FragmentHomeBinding;
import lk.nd.cycler.model.LocationCard;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView locationRecyclerView;
    private LocationAdapter locationAdapter;
    private List<LocationCard> locationsList;
    private List<LocationCard> filteredList;
    private EditText searchEditText;
    private TextView welcomeText;

    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_ID = "user_id";
    private long userId;
    private String userName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        welcomeText = root.findViewById(R.id.welcomeText);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Retrieve stored data
        userName = sharedPreferences.getString(KEY_USER_NAME, "Guest");
        userId = sharedPreferences.getLong(KEY_USER_ID, -1); // Default value -1 if not found
        welcomeText.setText("Welcome, " + userName + "! Ready to Ride Through Kandy?");

        locationRecyclerView = root.findViewById(R.id.locationRecyclerView);
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        locationsList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Initialize the adapter
        locationAdapter = new LocationAdapter(getContext(), filteredList);
        locationRecyclerView.setAdapter(locationAdapter);

        searchEditText = root.findViewById(R.id.searchEditText);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterLocations(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No implementation needed
            }
        });

        return root;
    }

    // Method to load location data from the database
    private void loadLocationData() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        locationsList = dbHelper.getAllLocations(); // Fetch updated data from the database
        // Ensure the adapter is not null and update it with new data
        if (locationAdapter != null) {
            locationAdapter.updateList(locationsList); // Update the adapter with the new data
        }
    }

    // Method to filter the list of locations based on search input
    private void filterLocations(String query) {
        String lowerCaseQuery = query.toLowerCase().trim();

        filteredList = locationsList.stream()
                .filter(location -> location.getShopName().toLowerCase().contains(lowerCaseQuery) ||
                        location.getLocation().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());

        // Update the adapter with the filtered list
        if (locationAdapter != null) {
            locationAdapter.updateList(filteredList);
        }
    }

    // Reload location data when the fragment is resumed
    @Override
    public void onResume() {
        super.onResume();
        loadLocationData();
        if (locationAdapter != null) {
            locationAdapter.notifyDataSetChanged();
        }
    }

    // Handle activity results (e.g., after placing an order)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Refresh the data after order placement
            loadLocationData();
            if (locationAdapter != null) {
                locationAdapter.notifyDataSetChanged();
            }
        }
    }

    // Clean up binding when the fragment's view is destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
