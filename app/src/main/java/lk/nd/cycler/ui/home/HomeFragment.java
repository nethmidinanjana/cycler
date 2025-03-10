package lk.nd.cycler.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lk.nd.cycler.R;
import lk.nd.cycler.adapter.LocationAdapter;
import lk.nd.cycler.databinding.FragmentHomeBinding;
import lk.nd.cycler.model.LocationCard;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView locationRecyclerView;
    private LocationAdapter locationAdapter;
    private List<LocationCard> locationsList;
    private List<LocationCard> filteredList; // To hold the filtered list
    EditText searchEditText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        locationRecyclerView = root.findViewById(R.id.locationRecyclerView);
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        locationsList = new ArrayList<>();
        loadLocationData();

        // Initially set filtered list to the entire locationsList
        filteredList = new ArrayList<>(locationsList);

        locationAdapter = new LocationAdapter(getContext(), filteredList);
        locationRecyclerView.setAdapter(locationAdapter);

        searchEditText = root.findViewById(R.id.searchEditText);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterLocations(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return root;
    }

    private void loadLocationData(){
        locationsList.add(new LocationCard(1, "Kandy Pedal Power Rentals", "Peradeniya Road, Kandy", "4.9", 10, "https://i.ibb.co/1Gsqp7Yd/rental-place1.webp"));
        locationsList.add(new LocationCard(2, "Hilltop Bike Rentals", "Udawattakele, Kandy", "4.8", 8, "https://i.ibb.co/7JFP3PxF/30-Bicycle-rental-Kyoto-3.webp"));
        locationsList.add(new LocationCard(3, "Royal Cycle Hire", "Ampitiya Road, Kandy", "4.7", 12, "https://i.ibb.co/wFqjq803/images.jpg"));
        locationsList.add(new LocationCard(4, "City Wheels Bicycle Rental", "Near Royal Botanical Gardens, Peradeniya", "4.6", 6, "https://i.ibb.co/WNc17qcr/ODT-Ediz-Hook-credit-lynnette-braillard-1024x777.jpg"));
        locationsList.add(new LocationCard(5, "Mountain Trail Bikes Kandy", "Kandy City Center Area", "4.9", 9, "https://i.ibb.co/rfbwr649/images-1.jpg"));
        locationsList.add(new LocationCard(6, "Ride & Explore Kandy", "Hanthana Road, Kandy", "4.9", 9, "https://i.ibb.co/n8NgQSFV/bike-rentals-768x1152.webp"));
        locationsList.add(new LocationCard(7, "Pedal Path Bicycle Rentals", "Kandy Lake Round", "4.9", 19, "https://i.ibb.co/jPrYRYnG/40.jpg"));
        locationsList.add(new LocationCard(8, "Golden Gear Bike Rentals", "Katugastota Road, Kandy", "4.9", 4, "https://i.ibb.co/yF2HrwJ3/33.jpg"));
        locationsList.add(new LocationCard(9, "EcoCycle Rent Kandy", "Senanayake Mawatha, Kandy", "4.9", 18, "https://i.ibb.co/7t7H5N88/Nuwara-Eliya-to-Kandy-Cycling-Tour-4-a931073367.jpg"));
        locationsList.add(new LocationCard(10, "Green Ride Kandy", "Tennekumbura, Kandy", "4.9", 20, "https://i.ibb.co/Jj8B6fGg/ce.jpg"));
    }

    private void filterLocations(String query) {

        String lowerCaseQuery = query.toLowerCase().trim();

        filteredList = locationsList.stream()
                .filter(location -> location.getShopName().toLowerCase().contains(lowerCaseQuery) ||
                        location.getLocation().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());

        locationAdapter.updateList(filteredList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
