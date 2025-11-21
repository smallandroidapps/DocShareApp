package com.docshare.docshare.ui.home.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.docshare.docshare.databinding.ItemTagBinding;
import com.docshare.docshare.ui.home.model.TagItem;

public class TagAdapter extends ListAdapter<TagItem, TagAdapter.TagVH> {

    public interface OnTagSelectedListener { void onTagSelected(TagItem tag); }
    private final OnTagSelectedListener listener;

    public TagAdapter(OnTagSelectedListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<TagItem> DIFF = new DiffUtil.ItemCallback<TagItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull TagItem oldItem, @NonNull TagItem newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull TagItem oldItem, @NonNull TagItem newItem) {
            return oldItem.name.equals(newItem.name) && oldItem.selected == newItem.selected;
        }
    };

    @NonNull
    @Override
    public TagVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTagBinding binding = ItemTagBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TagVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TagVH holder, int position) {
        TagItem item = getItem(position);
        holder.bind(item);
    }

    class TagVH extends RecyclerView.ViewHolder {
        final ItemTagBinding binding;
        TagVH(ItemTagBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        void bind(TagItem tag) {
            binding.chipTag.setText(tag.name);
            binding.chipTag.setChecked(tag.selected);
            binding.chipTag.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { if (listener != null) listener.onTagSelected(tag); }
            });
        }
    }
}

