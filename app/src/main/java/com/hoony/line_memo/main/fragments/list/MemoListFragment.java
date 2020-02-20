package com.hoony.line_memo.main.fragments.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.FragmentMemoListBinding;
import com.hoony.line_memo.main.MainViewModel;

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
        viewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(MainViewModel.class);
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
            viewModel.createEditMemoMutableData();
            viewModel.setFragmentIndex(MainViewModel.FRAGMENT_WRITE);
        }
    }

    @Override
    public void onItemClick(int position) {
        viewModel.setReadMemoMutableData(position);
        viewModel.setFragmentIndex(MainViewModel.FRAGMENT_READ);
    }

    @Override
    public void onItemLongClick(int position) {
        new AlertDialog.Builder(requireContext())
                .setTitle("메모 삭제")
                .setMessage("메모를 삭제하시겠습니까?")
                .setPositiveButton("확인", (dialog, which) -> {
                    viewModel.deleteMemo(position);
                })
                .setNegativeButton("취소", null)
                .show();
    }
}
