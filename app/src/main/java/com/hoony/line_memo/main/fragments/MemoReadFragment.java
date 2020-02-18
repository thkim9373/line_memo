package com.hoony.line_memo.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.FragmentMemoReadBinding;
import com.hoony.line_memo.main.MainActivity;
import com.hoony.line_memo.main.MainViewModel;

public class MemoReadFragment extends Fragment implements View.OnClickListener {
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(MemoReadFragment.this).get(MainViewModel.class);
        setObserve();
    }

    private void setObserve() {
        viewModel.getTargetMemoMutableData().observe(MemoReadFragment.this, memo -> {
            binding.tvTitle.setText(memo.getTitle());
            binding.tvContent.setText(memo.getContent());
        });
    }

    private void setListener() {
        binding.ibEdit.setOnClickListener(MemoReadFragment.this);
    }

    @Override
    public void onClick(View view) {
        ((MainActivity) requireActivity()).replaceFragment(MainActivity.FRAGMENT_WRITE);
    }
}
