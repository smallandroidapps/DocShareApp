package com.docshare.docshare.ui.review;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.docshare.docshare.R;
import com.docshare.docshare.databinding.ReviewFragmentBinding;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {

    private ReviewFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ReviewFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String fileName = args != null ? args.getString("fileName", "") : "";
        String fileSize = args != null ? args.getString("fileSize", "") : "";
        String fileType = args != null ? args.getString("fileType", "") : "";
        String title = args != null ? args.getString("title", "") : "";
        String desc = args != null ? args.getString("desc", "") : "";
        ArrayList<String> tags = args != null ? args.getStringArrayList("tags") : new ArrayList<>();

        binding.tvReviewFileName.setText(fileName);
        binding.tvReviewFileMeta.setText(fileType + " • " + fileSize);
        binding.tvReviewTitle.setText(title);
        binding.tvReviewDesc.setText(desc.isEmpty() ? "—" : desc);

        for (String tag : tags) {
            Chip chip = new Chip(requireContext());
            chip.setText(tag);
            chip.setChipBackgroundColor(ContextCompat.getColorStateList(requireContext(), R.color.chip_light_gray));
            chip.setChipCornerRadius(50f);
            binding.chipGroupReviewTags.addView(chip);
        }

        binding.btnSubmit.setOnClickListener(v -> {
            binding.btnSubmit.setEnabled(false);
            binding.btnSubmit.setAlpha(0.4f);
            binding.progressSubmit.setVisibility(View.VISIBLE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                binding.progressSubmit.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Document submitted for admin review", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack(R.id.homeFragment, false);
            }, 1000);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

