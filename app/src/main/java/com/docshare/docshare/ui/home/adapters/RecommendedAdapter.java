package com.docshare.docshare.ui.home.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.docshare.docshare.databinding.ItemRecommendedBinding;
import com.docshare.docshare.ui.home.model.DocumentItem;
import com.docshare.docshare.utils.ViewExtensions;

public class RecommendedAdapter extends ListAdapter<DocumentItem, RecommendedAdapter.RecommendedVH> {

    public interface OnRecommendedClickListener { void onRecommendedClick(DocumentItem item); }
    private final OnRecommendedClickListener listener;

    public RecommendedAdapter(OnRecommendedClickListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<DocumentItem> DIFF = new DiffUtil.ItemCallback<DocumentItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull DocumentItem oldItem, @NonNull DocumentItem newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull DocumentItem oldItem, @NonNull DocumentItem newItem) {
            return oldItem.name.equals(newItem.name) && oldItem.lastOpened == newItem.lastOpened;
        }
    };

    @NonNull
    @Override
    public RecommendedVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecommendedBinding binding = ItemRecommendedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecommendedVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedVH holder, int position) {
        DocumentItem item = getItem(position);
        holder.bind(item);
    }

    class RecommendedVH extends RecyclerView.ViewHolder {
        final ItemRecommendedBinding binding;
        RecommendedVH(ItemRecommendedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        void bind(DocumentItem item) {
            binding.tvName.setText(item.name);
            binding.tvType.setText(item.extension.toUpperCase());
            binding.tvTimestamp.setText(ViewExtensions.formatTimestamp(item.lastOpened));
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { if (listener != null) listener.onRecommendedClick(item); }
            });
        }
    }
}

