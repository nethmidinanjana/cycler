package lk.nd.cycler.ui.rentals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import lk.nd.cycler.databinding.FragmentRentalsBinding;

public class RentalsFragment extends Fragment {

    private FragmentRentalsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RentalsViewModel rentalsViewModel =
                new ViewModelProvider(this).get(RentalsViewModel.class);

        binding = FragmentRentalsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}