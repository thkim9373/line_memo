package com.hoony.line_memo.gallery;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.ItemPhotoGridBinding;
import com.hoony.line_memo.gallery.pojo.CheckableImageData;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter {

    private final List<CheckableImageData> mList;
    private Context mContext;

    GalleryAdapter(List<CheckableImageData> checkableImageDataList) {
        this.mList = checkableImageDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemPhotoGridBinding binding = ((ItemHolder) holder).getBinding();
        CheckableImageData imageData = mList.get(position);

        Uri uri = Uri.parse(imageData.getUriPath());

        Glide.with(mContext)
                .load(uri)
                .thumbnail(0.3f)
                .centerCrop()
                .into(binding.ivPhoto);

        if (imageData.isChecked()) {
            binding.viewCheckedFilter.setVisibility(View.VISIBLE);
        } else {
            binding.viewCheckedFilter.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private ItemPhotoGridBinding binding;

        ItemHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.ivPhoto.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mList.get(position).toggle();
                        notifyItemChanged(position);
                    }
                });
            }
        }

        public ItemPhotoGridBinding getBinding() {
            return binding;
        }
    }
}
