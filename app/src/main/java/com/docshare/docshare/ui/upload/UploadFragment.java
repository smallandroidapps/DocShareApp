package com.docshare.docshare.ui.upload;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.docshare.docshare.databinding.UploadFragmentBinding;
import com.google.android.material.chip.Chip;

import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;

import com.docshare.docshare.R;

import java.util.ArrayList;

public class UploadFragment extends Fragment {

    private UploadFragmentBinding binding;
    private ActivityResultLauncher<String> fileChooserLauncher;

    private boolean fileChosen = false;
    private String fileName = null;
    private String fileSize = null;
    private String fileType = null;
    private final ArrayList<String> tags = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = UploadFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupFileChooser();
        setupTopBar();
        setupInputs();
        setupTagAdd();
        setupNext();
    }

    private void setupTopBar() {
        binding.btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
    }

    private void setupFileChooser() {
        fileChooserLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            // Mock behavior: ignore actual uri and set dummy metadata
            onMockFileSelected(Uri.parse("content://com.docshare/mock"));
        });

        View.OnClickListener openChooser = v -> fileChooserLauncher.launch("*/*");
        binding.cardFile.setOnClickListener(openChooser);
        binding.btnChangeFile.setOnClickListener(openChooser);
    }

    private void onMockFileSelected(Uri dummyUri) {
        fileChosen = true;
        fileName = "sample_document.pdf";
        fileSize = "325 KB";
        fileType = "PDF";

        binding.groupChoose.setVisibility(View.GONE);
        binding.groupFileInfo.setVisibility(View.VISIBLE);
        binding.tvFileName.setText(fileName);
        binding.tvFileMeta.setText(fileType + " • " + fileSize);

        updateNextEnabled();
    }

    private void setupInputs() {
        binding.editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                updateNextEnabled();
            }
        });
    }

    private void setupTagAdd() {
        binding.chipAddTag.setOnClickListener(v -> {
            EditText input = new EditText(requireContext());
            input.setHint("Tag name");
            new AlertDialog.Builder(requireContext())
                    .setTitle("Add Tag")
                    .setView(input)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String tagText = input.getText() != null ? input.getText().toString().trim() : "";
                        if (!tagText.isEmpty()) {
                            addTagChip(tagText);
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    private void addTagChip(String text) {
        Chip chip = new Chip(requireContext());
        chip.setText(text);
        chip.setCloseIconVisible(true);
        chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.chip_light_gray)));
        chip.setChipCornerRadius(50f);
        chip.setOnCloseIconClickListener(v -> {
            binding.chipGroupTags.removeView(chip);
            tags.remove(text);
        });
        binding.chipGroupTags.addView(chip, binding.chipGroupTags.getChildCount() - 1);
        tags.add(text);
    }

    private void setupNext() {
        binding.btnNext.setOnClickListener(v -> {
            // Build bundle for navigation to review
            Bundle args = new Bundle();
            args.putString("fileName", fileName);
            args.putString("fileSize", fileSize);
            args.putString("fileType", fileType);
            args.putString("title", binding.editTitle.getText() != null ? binding.editTitle.getText().toString().trim() : "");
            args.putString("desc", binding.editDescription.getText() != null ? binding.editDescription.getText().toString().trim() : "");
            args.putStringArrayList("tags", new ArrayList<>(tags));

            NavHostFragment.findNavController(this).navigate(R.id.action_uploadFragment_to_reviewFragment, args);
        });
    }

    private void updateNextEnabled() {
        boolean titleProvided = binding.editTitle.getText() != null && !binding.editTitle.getText().toString().trim().isEmpty();
        boolean enabled = fileChosen && titleProvided;
        binding.btnNext.setEnabled(enabled);
        binding.btnNext.setAlpha(enabled ? 1f : 0.4f);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

