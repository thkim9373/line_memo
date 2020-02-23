package com.hoony.line_memo.main.fragments.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.FragmentMemoListBinding;
import com.hoony.line_memo.main.MainViewModel;
import com.hoony.line_memo.util.ToastPrinter;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class ListFragment extends Fragment implements View.OnClickListener, MemoAdapter.onItemClickListener {

    private FragmentMemoListBinding binding;
    private MainViewModel viewModel;

    private long backKeyPressedTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (viewModel.getListFragmentMode() == MainViewModel.LIST_MODE_SELECT) {
                    setModeDefault();
                } else {
                    if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                        backKeyPressedTime = System.currentTimeMillis();
                        ToastPrinter.show("한 번 더 누르면 종료됩니다.", requireContext());
                    } else {
                        requireActivity().finish();
                    }
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(ListFragment.this, callback);
    }

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

    private void setRecyclerView() {
        binding.svMemo.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.svMemo.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
    }

    private void setListener() {
        binding.fabCreateMemo.setOnClickListener(ListFragment.this);
        binding.ibDelete.setOnClickListener(ListFragment.this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(MainViewModel.class);
        setObserve();
    }

    private void setObserve() {
        viewModel.getMemoListMutableData().observe(getViewLifecycleOwner(), memoList -> {
            MemoAdapter adapter = (MemoAdapter) binding.svMemo.getAdapter();
            if (adapter == null) {
                adapter = new MemoAdapter(memoList, viewModel.getListFragmentMode());
                adapter.setListener(ListFragment.this);
                binding.svMemo.setAdapter(adapter);
            } else {
                adapter.setList(memoList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_create_memo) {
            viewModel.createEditMemoMutableData();
            viewModel.setFragmentIndex(MainViewModel.FRAGMENT_EDITOR);
        } else if (view.getId() == R.id.ib_delete) {

            new AlertDialog.Builder(requireContext())
                    .setMessage(getString(R.string.do_you_want_to_delete_the_note))
                    .setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                        deleteMemoList();
                        setModeDefault();
                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        }
    }

    private void deleteMemoList() {
        MemoAdapter memoAdapter = (MemoAdapter) binding.svMemo.getAdapter();
        if (memoAdapter != null) {
            viewModel.deleteMemoList(memoAdapter.getCheckedMemoList());
        }
    }

    @Override
    public void onItemClick(int position) {
        viewModel.setReadMemoMutableData(position);
        viewModel.setFragmentIndex(MainViewModel.FRAGMENT_VIEWER);
    }

    @Override
    public void onItemLongClick() {
        binding.ibDelete.setVisibility(View.VISIBLE);
        binding.fabCreateMemo.setVisibility(View.GONE);
        viewModel.setListFragmentModeMutableData(MainViewModel.LIST_MODE_SELECT);
    }

    private void setModeDefault() {
        MemoAdapter memoAdapter = (MemoAdapter) binding.svMemo.getAdapter();
        if (memoAdapter != null) {
            memoAdapter.setMode(MainViewModel.LIST_MODE_DEFAULT);
        }
        viewModel.setListFragmentModeMutableData(MainViewModel.LIST_MODE_DEFAULT);
        binding.ibDelete.setVisibility(View.GONE);
        binding.fabCreateMemo.setVisibility(View.VISIBLE);
    }
}
