package com.hoony.line_memo.main.fragments.write;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.ItemPhotoMemoBinding;
import com.hoony.line_memo.db.pojo.ImageData;

import java.util.List;

public class MemoImageAdapter extends RecyclerView.Adapter {

    private List<ImageData> mList;
    private MemoImageAdapterListener mListener;
    private Context mContext;

    public MemoImageAdapter(List<ImageData> mList, MemoImageAdapterListener listener) {
        this.mList = mList;
        this.mListener = listener;
    }

    public void setImageDataList(List<ImageData> imageDataList) {
        this.mList = imageDataList;
    }

    interface MemoImageAdapterListener {
        void onItemClick(int position);
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
                Glide.with(mContext)
                        .load(imageData.getUri())
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
