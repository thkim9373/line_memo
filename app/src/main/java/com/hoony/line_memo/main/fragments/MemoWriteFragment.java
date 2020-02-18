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
import com.hoony.line_memo.databinding.FragmentMemoEditBinding;
import com.hoony.line_memo.main.MainViewModel;

public class MemoWriteFragment extends Fragment implements View.OnClickListener {

    private FragmentMemoEditBinding binding;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo_edit, container, false);
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
        viewModel = ViewModelProviders.of(MemoWriteFragment.this).get(MainViewModel.class);
        setObserve();
    }

    private void setObserve() {

    }

    private void setListener() {
        binding.btSave.setOnClickListener(MemoWriteFragment.this);
        binding.clAddImageLayout.setOnClickListener(MemoWriteFragment.this);
    }

    @Override
    public void onClick(View view) {

    }
}
