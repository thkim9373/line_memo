package com.hoony.line_memo.main.fragments.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.FragmentMemoListBinding;
import com.hoony.line_memo.main.MainViewModel;

import static com.hoony.line_memo.main.MainViewModel.FRAGMENT_LIST;

public class MemoListFragment extends Fragment implements View.OnClickListener, MemoAdapter.onItemClickListener {

    private FragmentMemoListBinding binding;
    private MainViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(requireContext(), "onBackpressed!!!", Toast.LENGTH_SHORT).show();
//                if (viewModel.getListFragmentMode() == MainViewModel.LIST_MODE_SELECT) {
//                    MemoListFragment memoListFragment = (MemoListFragment) this.fragmentList.get(FRAGMENT_LIST);
//                    memoListFragment.setModeDefault();
//                } else {
//                    if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
//                        backKeyPressedTime = System.currentTimeMillis();
//                        showToast("한 번 더 누르면 종료됩니다.");
//                    } else {
//                        finish();
//                    }
//                }
            }
        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(MemoListFragment.this, callback);
        requireActivity().getOnBackPressedDispatcher().addCallback(callback);
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
        binding.fabCreateMemo.setOnClickListener(MemoListFragment.this);
        binding.ibDelete.setOnClickListener(MemoListFragment.this);
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
                adapter.setListener(MemoListFragment.this);
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
                    .setTitle("메모 삭제")
                    .setMessage("메모를 삭제하시겠습니까?")
                    .setPositiveButton("확인", (dialog, which) -> {
                        deleteMemoList();
                        setModeDefault();
                    })
                    .setNegativeButton("취소", null)
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
        viewModel.setFragmentIndex(MainViewModel.FRAGMENT_READER);
    }

    @Override
    public void onItemLongClick() {
        binding.ibDelete.setVisibility(View.VISIBLE);
        binding.fabCreateMemo.setVisibility(View.GONE);
        viewModel.setListFragmentModeMutableData(MainViewModel.LIST_MODE_SELECT);
    }

    public void setModeDefault() {
        MemoAdapter memoAdapter = (MemoAdapter) binding.svMemo.getAdapter();
        if (memoAdapter != null) {
            memoAdapter.setMode(MainViewModel.LIST_MODE_DEFAULT);
        }
        viewModel.setListFragmentModeMutableData(MainViewModel.LIST_MODE_DEFAULT);
        binding.ibDelete.setVisibility(View.GONE);
        binding.fabCreateMemo.setVisibility(View.VISIBLE);
    }
}
