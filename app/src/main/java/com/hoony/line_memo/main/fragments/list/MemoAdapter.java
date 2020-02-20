package com.hoony.line_memo.main.fragments.list;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.ItemMemoListBinding;
import com.hoony.line_memo.db.pojo.ImageData;
import com.hoony.line_memo.db.table.memo.Memo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MemoAdapter extends RecyclerView.Adapter {

    private List<Memo> memoList;
    private onItemClickListener itemClickListener = null;
    private Context mContext;

    MemoAdapter(List<Memo> memoList) {
        this.memoList = memoList;
    }

    void setMemoList(List<Memo> memoList) {
        this.memoList = memoList;
    }

    interface onItemClickListener {
        void onItemClick(int position);

        void onItemLongClick(int position);
    }

    void setItemClickListener(onItemClickListener listener) {
        this.itemClickListener = listener;
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
        Memo memo = memoList.get(position);

        binding.tvTitle.setText(memo.getTitle());
        binding.tvDate.setText(memo.getDate());
        binding.tvContent.setText(memo.getContent());

        if (memo.getImageDataList() != null && memo.getImageDataList().size() > 0) {
            ImageData imageData = memo.getImageDataList().get(0);

            Uri uri = Uri.parse(imageData.getUriPath());

            Glide.with(mContext)
                    .load(uri)
                    .fitCenter()
                    .into(binding.ivAlbumArt);

            binding.ivAlbumArt.setVisibility(View.VISIBLE);
        } else {
            binding.ivAlbumArt.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private final int MODE_DEFAULT = 0;
        private final int MODE_DELETE = 1;

        private ItemMemoListBinding binding;

        ItemHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.clCardContainer.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (itemClickListener != null && position != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(position);
                    }
                });
                binding.clCardContainer.setOnLongClickListener(v -> {
                    int position = getAdapterPosition();
                    if (itemClickListener != null && position != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemLongClick(position);
                    }
                    return false;
                });
            }
        }

        ItemMemoListBinding getBinding() {
            return binding;
        }
    }
}
