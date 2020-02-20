package com.hoony.line_memo.main.fragments.reader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class MemoReaderFragment extends Fragment implements View.OnClickListener {

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
                    adapter = new MemoImageAdapter(memo.getImageDataList());
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
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
