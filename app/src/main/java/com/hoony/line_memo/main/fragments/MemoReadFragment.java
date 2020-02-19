package com.hoony.line_memo.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.FragmentMemoReadBinding;
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
        viewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(MainViewModel.class);
        setObserve();
    }

    private void setObserve() {
        viewModel.getCurrentMemoMutableData().observe(getViewLifecycleOwner(), memo -> {
            binding.tvTitle.setText(memo.getTitle());
            binding.tvContent.setText(memo.getContent());
        });
    }

    private void setListener() {
        binding.ibEdit.setOnClickListener(MemoReadFragment.this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ib_edit) {
            viewModel.setFragmentIndex(MainViewModel.FRAGMENT_WRITE);
        }
//        ((MainActivity) requireActivity()).replaceFragment(MainActivity.FRAGMENT_WRITE);
    }
}
