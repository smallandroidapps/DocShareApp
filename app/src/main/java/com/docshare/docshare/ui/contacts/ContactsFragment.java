package com.docshare.docshare.ui.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.docshare.docshare.R;

public class ContactsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View btn = view.findViewById(R.id.btnSampleContact);
        if (btn != null) {
            btn.setOnClickListener(v -> NavHostFragment.findNavController(ContactsFragment.this)
                    .navigate(R.id.action_contactsFragment_to_contactDetailsFragment));
        }
    }
}

