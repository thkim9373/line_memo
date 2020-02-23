package com.hoony.line_memo.main.fragments.viewer;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.FragmentMemoViewerBinding;
import com.hoony.line_memo.main.MainViewModel;
import com.hoony.line_memo.util.ToastPrinter;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.hoony.line_memo.main.MainViewModel.FRAGMENT_LIST;

public class ViewerFragment extends Fragment implements View.OnClickListener, ViewerImageAdapter.MemoImageAdapterListener {

    private FragmentMemoViewerBinding binding;
    private MainViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewModel.setFragmentIndex(FRAGMENT_LIST);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(ViewerFragment.this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo_viewer, container, false);
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
        binding.ibEdit.setOnClickListener(ViewerFragment.this);
        binding.ibDelete.setOnClickListener(ViewerFragment.this);
        binding.ibImageClose.setOnClickListener(ViewerFragment.this);
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
                ViewerImageAdapter adapter = (ViewerImageAdapter) binding.rvImage.getAdapter();
                if (memo.getImageDataList().size() != 0) {
                    if (adapter == null) {
                        adapter = new ViewerImageAdapter(memo.getImageDataList(), ViewerFragment.this);
                        binding.rvImage.setAdapter(adapter);
                    } else {
                        adapter.setImageDataList(memo.getImageDataList());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    binding.rvImage.setVisibility(View.GONE);
                }
            } else {
                binding.rvImage.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ib_edit) {
            viewModel.initEditMemoMutableData();
            viewModel.setFragmentIndex(MainViewModel.FRAGMENT_EDITOR);
        } else if (view.getId() == R.id.ib_delete) {

            new AlertDialog.Builder(requireContext())
                    .setMessage(getString(R.string.do_you_want_to_delete_the_note))
                    .setPositiveButton(getString(R.string.confirm), (dialog, which) -> deleteMemo())
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        } else if (view.getId() == R.id.ib_image_close) {
            binding.clSelectedImageContainer.setVisibility(View.INVISIBLE);
        }
    }

    private void deleteMemo() {
        viewModel.deleteMemo();
    }

    @Override
    public void onItemClick(int position) {
        String UriPath = viewModel.getSelectedImageUri(position);

        binding.clSelectedImageContainer.setVisibility(View.VISIBLE);

        Uri uri = Uri.parse(UriPath);

        Glide.with(ViewerFragment.this)
                .load(uri)
                .thumbnail(0.3f)
                .fitCenter()
                .into(binding.ivSelectedImage);
    }

    @Override
    public void onLoadFail() {
        ToastPrinter.show(getString(R.string.failed_to_load_image), requireContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.clSelectedImageContainer.setVisibility(View.INVISIBLE);
    }
}
