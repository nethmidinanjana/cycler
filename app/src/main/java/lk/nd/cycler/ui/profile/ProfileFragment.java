package lk.nd.cycler.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import lk.nd.cycler.R;
import lk.nd.cycler.SignInActivity;
import lk.nd.cycler.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    EditText usernameEditText, emailEditText, mobileEditText;
    long userId;
    Button logoutBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        usernameEditText = root.findViewById(R.id.usernameEditText);
        emailEditText = root.findViewById(R.id.emailEditText);
        mobileEditText = root.findViewById(R.id.mobileEditText);
        logoutBtn = root.findViewById(R.id.logoutBtn);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        userId = sharedPreferences.getLong("user_id", -1); // Default -1 if not found
        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");
        String mobile = sharedPreferences.getString("mobile", "");

        usernameEditText.setText(username);
        emailEditText.setText(email);
        mobileEditText.setText(mobile);


        logoutBtn.setOnClickListener(v -> {
            // Clear SharedPreferences
            SharedPreferences.Editor editor = requireActivity()
                    .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    .edit();

            editor.clear();
            editor.apply();

            // Redirect to SignInActivity
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}