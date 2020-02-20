package com.hoony.line_memo.main.fragments.reader;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.FragmentMemoReadBinding;
import com.hoony.line_memo.main.MainViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MemoReaderFragment extends Fragment implements View.OnClickListener, MemoImageAdapter.MemoImageAdapterListener {

    private FragmentMemoReadBinding binding;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo_read, container, false);
        binding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListener();
        setRecyclerView();
    }

    private void setListener() {
        binding.ibEdit.setOnClickListener(MemoReaderFragment.this);
        binding.ibImageClose.setOnClickListener(MemoReaderFragment.this);
    }

    private void setRecyclerView() {
        binding.rvImage.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(MainViewModel.class);
        setObserve();
    }

    private void setObserve() {
        viewModel.getReadMemoMutableData().observe(getViewLifecycleOwner(), memo -> {

            binding.tvTitle.setText(memo.getTitle());
            binding.tvContent.setText(memo.getContent());

            if (memo.getImageDataList() != null) {
                MemoImageAdapter adapter = (MemoImageAdapter) binding.rvImage.getAdapter();
                if (adapter == null) {
                    adapter = new MemoImageAdapter(memo.getImageDataList(), MemoReaderFragment.this);
                    binding.rvImage.setAdapter(adapter);
                } else {
                    adapter.setImageDataList(memo.getImageDataList());
                    adapter.notifyDataSetChanged();
                }
            }

        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ib_edit) {
            viewModel.initEditMemoMutableData();
            viewModel.setFragmentIndex(MainViewModel.FRAGMENT_WRITE);
        } else if (view.getId() == R.id.ib_image_close) {
            binding.clSelectedImageContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemClick(int position) {
        String UriPath = viewModel.getSelectedImageUri(position);

        binding.clSelectedImageContainer.setVisibility(View.VISIBLE);

        Uri uri = Uri.parse(UriPath);

        Glide.with(MemoReaderFragment.this)
                .load(uri)
                .thumbnail(0.3f)
                .fitCenter()
                .into(binding.ivSelectedImage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.clSelectedImageContainer.setVisibility(View.INVISIBLE);
    }
}
