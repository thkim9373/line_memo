package com.hoony.line_memo.main.fragments.viewer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.ItemPhotoMemoBinding;
import com.hoony.line_memo.db.pojo.ImageData;

import java.util.List;

public class ViewerImageAdapter extends RecyclerView.Adapter {

    private List<ImageData> mList;
    private final MemoImageAdapterListener mListener;
    private Context mContext;

    ViewerImageAdapter(List<ImageData> mList, MemoImageAdapterListener listener) {
        this.mList = mList;
        this.mListener = listener;
    }

    void setImageDataList(List<ImageData> imageDataList) {
        this.mList = imageDataList;
    }

    interface MemoImageAdapterListener {
        void onItemClick(int position);

        void onLoadFail(ImageData imageData);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) mContext = parent.getContext();
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_memo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemPhotoMemoBinding binding = ((ItemHolder) holder).getBinding();

        ImageData imageData = mList.get(position);

        Uri uri = Uri.parse(imageData.getUriPath());
        Glide.with(mContext)
                .load(uri)
                .thumbnail(0.3f)
                .fitCenter()
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (mListener != null) {
                            mListener.onLoadFail(imageData);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(binding.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private ItemPhotoMemoBinding binding;

        ItemHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.ivPhoto.setOnClickListener(view -> {
                    int position = getAdapterPosition();
                    if (mListener != null && position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position);
                    }
                });
            }
        }

        ItemPhotoMemoBinding getBinding() {
            return binding;
        }
    }
}