package com.docshare.docshare.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.docshare.docshare.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View contactsBtn = view.findViewById(R.id.btnContacts);
        if (contactsBtn != null) {
            contactsBtn.setOnClickListener(v -> NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_homeFragment_to_contactsFragment));
        }
        View premiumBtn = view.findViewById(R.id.btnPremium);
        if (premiumBtn != null) {
            premiumBtn.setOnClickListener(v -> NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_homeFragment_to_premiumPlansFragment));
        }
    }
}

