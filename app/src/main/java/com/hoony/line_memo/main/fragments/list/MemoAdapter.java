package com.hoony.line_memo.main.fragments.list;

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
import com.hoony.line_memo.databinding.ItemMemoListBinding;
import com.hoony.line_memo.db.pojo.ImageData;
import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.main.MainViewModel;
import com.hoony.line_memo.main.fragments.list.pojo.CheckableMemo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MemoAdapter extends RecyclerView.Adapter {

    private int mode;

    private List<CheckableMemo> mList;
    private onItemClickListener mListener = null;
    private Context mContext;

    MemoAdapter(List<CheckableMemo> memoList, int mode) {
        this.mList = memoList;
        this.mode = mode;
    }

    void setList(List<CheckableMemo> mList) {
        this.mList = mList;
    }

    void setMode(int mode) {
        if (mode == MainViewModel.LIST_MODE_DEFAULT) {
            List<Integer> checkedPositionList = new ArrayList<>();
            for (CheckableMemo memo : mList) {
                if (memo.isChecked()) {
                    checkedPositionList.add(mList.indexOf(memo));
                    memo.setChecked(false);
                }
            }
            for (int i : checkedPositionList) {
                notifyItemChanged(i);
            }
        }
        this.mode = mode;
    }

    List<Memo> getCheckedMemoList() {
        List<Memo> memoList = new ArrayList<>();
        for (CheckableMemo checkableMemo : mList) {
            if (checkableMemo.isChecked()) memoList.add(checkableMemo);
        }
        return memoList;
    }

    interface onItemClickListener {
        void onItemClick(int position);

        void onItemLongClick();
    }

    void setListener(onItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) mContext = parent.getContext();
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemMemoListBinding binding = ((ItemHolder) holder).getBinding();
        CheckableMemo memo = mList.get(position);

        binding.tvTitle.setText(memo.getTitle());
        binding.tvContent.setText(memo.getContent());
        binding.tvDate.setText(memo.getDate());

        showThumbnail(memo.getImageDataList(), binding);

        if (memo.isChecked()) {
            binding.clCancelLayout.setVisibility(View.VISIBLE);
        } else {
            binding.clCancelLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void showThumbnail(List<ImageData> imageDataList, ItemMemoListBinding binding) {
        if (imageDataList != null && imageDataList.size() > 0) {
            ImageData imageData = imageDataList.get(0);

            Uri uri = Uri.parse(imageData.getUriPath());

            Glide.with(mContext)
                    .load(uri)
                    .fitCenter()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            binding.ivAlbumArt.setVisibility(View.GONE);
                            binding.tvContent.setSingleLine(false);
                            binding.tvContent.setMaxLines(10);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(binding.ivAlbumArt);

            binding.ivAlbumArt.setVisibility(View.VISIBLE);
            binding.tvContent.setSingleLine(true);
            binding.tvContent.setMaxLines(1);
        } else {
            binding.ivAlbumArt.setVisibility(View.GONE);
            binding.tvContent.setSingleLine(false);
            binding.tvContent.setMaxLines(10);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private ItemMemoListBinding binding;

        ItemHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.cvItem.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (mListener != null && position != RecyclerView.NO_POSITION) {
                        if (mode == MainViewModel.LIST_MODE_DEFAULT) {
                            mListener.onItemClick(position);
                        } else if (mode == MainViewModel.LIST_MODE_SELECT) {
                            mList.get(position).toggle();
                            notifyItemChanged(position);
                        }
                    }
                });
                binding.cvItem.setOnLongClickListener(v -> {
                    int position = getAdapterPosition();
                    if (mListener != null && position != RecyclerView.NO_POSITION) {

                        mListener.onItemLongClick();

                        setMode(MainViewModel.LIST_MODE_SELECT);
                        mList.get(position).toggle();
                        notifyItemChanged(position);
                    }
                    return true;
                });
            }
        }

        ItemMemoListBinding getBinding() {
            return binding;
        }
    }
}
