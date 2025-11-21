package com.docshare.docshare.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.docshare.docshare.R;
import com.docshare.docshare.databinding.FragmentHomeBinding;
import com.docshare.docshare.ui.home.adapters.RecommendedAdapter;
import com.docshare.docshare.ui.home.adapters.RecentFilesAdapter;
import com.docshare.docshare.ui.home.adapters.TagAdapter;
import com.google.android.material.transition.MaterialFadeThrough;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private TagAdapter tagAdapter;
    private RecentFilesAdapter recentAdapter;
    private RecommendedAdapter recommendedAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setEnterTransition(new MaterialFadeThrough());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        setupRecyclerViews();
        setupClickListeners();
        observeViewModel();
        viewModel.loadInitialHomeData();
    }

    private void setupRecyclerViews() {
        tagAdapter = new TagAdapter(tag -> {
            viewModel.applyTagFilter(tag.name);
        });
        binding.rvTags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvTags.setAdapter(tagAdapter);

        recentAdapter = new RecentFilesAdapter(item -> {
            com.docshare.docshare.utils.ViewExtensions.showToast(requireContext(), "Open " + item.name);
        });
        binding.rvRecentFiles.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvRecentFiles.setAdapter(recentAdapter);

        recommendedAdapter = new RecommendedAdapter(item -> {
            com.docshare.docshare.utils.ViewExtensions.showToast(requireContext(), "Preview " + item.name);
        });
        binding.rvRecommended.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvRecommended.setAdapter(recommendedAdapter);
    }

    private void setupClickListeners() {
        binding.cardSearch.setOnClickListener(v -> NavHostFragment.findNavController(this)
                .navigate(R.id.action_homeFragment_to_searchFragment));
        binding.btnUpload.setOnClickListener(v -> NavHostFragment.findNavController(this)
                .navigate(R.id.action_homeFragment_to_uploadFragment));
    }

    private void observeViewModel() {
        viewModel.getTags().observe(getViewLifecycleOwner(), tagAdapter::submitList);
        viewModel.getRecent().observe(getViewLifecycleOwner(), list -> {
            recentAdapter.submitList(list);
            animateItemsOnLoad(binding.rvRecentFiles);
        });
        viewModel.getRecommended().observe(getViewLifecycleOwner(), list -> {
            recommendedAdapter.submitList(list);
            animateItemsOnLoad(binding.rvRecommended);
        });
    }

    private void animateItemsOnLoad(View target) {
        target.setAlpha(0f);
        target.animate().alpha(1f).setDuration(250).setStartDelay(100).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
