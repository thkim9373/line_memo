package com.hoony.line_memo.main.fragments.editor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.ItemPhotoEditorBinding;
import com.hoony.line_memo.db.pojo.ImageData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link EditorFragment} 의 이미지 리스트 어탭터.
 */
public class EditorImageAdapter extends RecyclerView.Adapter {

    private List<ImageData> mList;
    private MemoImageAdapterListener mListener;
    private Context mContext;

    EditorImageAdapter(List<ImageData> mList, MemoImageAdapterListener listener) {
        this.mList = mList;
        this.mListener = listener;
    }

    interface MemoImageAdapterListener {
        void onItemClick(ImageData imageData);

        void onLoadFail();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) mContext = parent.getContext();
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_editor, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemPhotoEditorBinding binding = ((ItemHolder) holder).getBinding();

        ImageData imageData = mList.get(position);

        Uri uri = Uri.parse(imageData.getUriPath());
        Glide.with(mContext)
                .load(uri)
                .thumbnail(0.3f)
                .fitCenter()
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (mListener != null) mListener.onLoadFail();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .error(R.drawable.ic_error_red_a200_24dp)
                .into(binding.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private ItemPhotoEditorBinding binding;

        ItemHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.ibDelete.setOnClickListener(view -> {
                    int position = getAdapterPosition();
                    if (mListener != null && position != RecyclerView.NO_POSITION) {
                        ImageData imageData = mList.get(position);
                        mListener.onItemClick(imageData);

                        mList.remove(position);
                        notifyItemRemoved(position);
                    }
                });
            }
        }

        ItemPhotoEditorBinding getBinding() {
            return binding;
        }
    }
}
