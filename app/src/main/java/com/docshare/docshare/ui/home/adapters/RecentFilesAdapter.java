package com.docshare.docshare.ui.home.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.docshare.docshare.databinding.ItemRecentFileBinding;
import com.docshare.docshare.ui.home.model.DocumentItem;
import com.docshare.docshare.utils.ViewExtensions;

public class RecentFilesAdapter extends ListAdapter<DocumentItem, RecentFilesAdapter.RecentVH> {

    public interface OnRecentClickListener { void onRecentClick(DocumentItem item); }
    private final OnRecentClickListener listener;

    public RecentFilesAdapter(OnRecentClickListener listener) {
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
    public RecentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecentFileBinding binding = ItemRecentFileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecentVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentVH holder, int position) {
        DocumentItem item = getItem(position);
        holder.bind(item);
    }

    class RecentVH extends RecyclerView.ViewHolder {
        final ItemRecentFileBinding binding;
        RecentVH(ItemRecentFileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        void bind(DocumentItem item) {
            binding.tvName.setText(item.name);
            binding.tvType.setText(item.extension.toUpperCase());
            binding.tvTimestamp.setText(ViewExtensions.formatTimestamp(item.lastOpened));
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { if (listener != null) listener.onRecentClick(item); }
            });
        }
    }
}

