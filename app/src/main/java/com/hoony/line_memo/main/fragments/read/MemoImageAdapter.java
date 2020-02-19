package com.hoony.line_memo.main.fragments.read;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.ItemPhotoMemoBinding;
import com.hoony.line_memo.db.pojo.ImageData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MemoImageAdapter extends RecyclerView.Adapter {

    private List<ImageData> mList;
    private Context mContext;

    MemoImageAdapter(List<ImageData> mList) {
        this.mList = mList;
    }

    void setImageDataList(List<ImageData> imageDataList) {
        this.mList = imageDataList;
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

        switch (imageData.getKind()) {
            case ImageData.CAMERA:
                break;
            case ImageData.GALLERY:
                Uri uri = Uri.parse(imageData.getUriPath());
                Glide.with(mContext)
                        .load(uri)
                        .thumbnail(0.3f)
                        .fitCenter()
                        .into(binding.ivPhoto);
                break;
            case ImageData.URL:
                break;
        }
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
        }

        ItemPhotoMemoBinding getBinding() {
            return binding;
        }
    }
}
