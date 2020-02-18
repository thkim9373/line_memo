package com.hoony.line_memo.main.fragments.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.FragmentMemoListBinding;
import com.hoony.line_memo.main.MainActivity;
import com.hoony.line_memo.main.MainViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

public class MemoListFragment extends Fragment implements View.OnClickListener, MemoAdapter.onItemClickListener {

    private FragmentMemoListBinding binding;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo_list, container, false);
        binding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView();
        setListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        setObserve();
    }

    private void setRecyclerView() {
        binding.svMemo.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void setObserve() {
        viewModel.getMemoListMutableData().observe(getViewLifecycleOwner(), memoList -> {
            MemoAdapter adapter = (MemoAdapter) binding.svMemo.getAdapter();
            if (adapter == null) {
                adapter = new MemoAdapter(memoList);
                adapter.setItemClickListener(MemoListFragment.this);
                binding.svMemo.setAdapter(adapter);
            } else {
                adapter.setMemoList(memoList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setListener() {
        binding.fabCreateMemo.setOnClickListener(MemoListFragment.this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_create_memo) {
            viewModel.getCurrentMemoMutableData().setValue(null);
            ((MainActivity) requireActivity()).replaceFragment(MainActivity.FRAGMENT_WRITE);
        }
    }

    @Override
    public void onItemClick(int position) {
        viewModel.setCurrentMemoMutableData(position);
        ((MainActivity) requireActivity()).replaceFragment(MainActivity.FRAGMENT_READ);
    }
}
