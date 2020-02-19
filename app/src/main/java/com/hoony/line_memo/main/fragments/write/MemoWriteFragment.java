package com.hoony.line_memo.main.fragments.write;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.FragmentMemoEditBinding;
import com.hoony.line_memo.db.pojo.ImageData;
import com.hoony.line_memo.gallery.GalleryActivity;
import com.hoony.line_memo.main.MainViewModel;

import java.util.List;

public class MemoWriteFragment extends Fragment
        implements View.OnClickListener,
        AddPhotoBottomSheetDialog.AddPhotoBottomSheetDialogListener,
        MemoImageAdapter.MemoImageAdapterListener {

    private final int REQUEST_CAMERA = 0;
    private final int REQUEST_GALLERY = 1;
    private final int REQUEST_URL = 2;

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
        setRecyclerView();
    }

    private void setListener() {
        binding.ibAddPhoto.setOnClickListener(MemoWriteFragment.this);
        binding.ibSave.setOnClickListener(MemoWriteFragment.this);
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
        viewModel.getCurrentMemoMutableData().observe(getViewLifecycleOwner(), memo -> {
            if (memo != null) {
                binding.etTitle.setText(memo.getTitle());
                binding.etContent.setText(memo.getContent());

                if (memo.getImageDataList() != null) {
                    MemoImageAdapter adapter = (MemoImageAdapter) binding.rvImage.getAdapter();
                    if (adapter == null) {
                        adapter = new MemoImageAdapter(memo.getImageDataList(), MemoWriteFragment.this);
                        binding.rvImage.setAdapter(adapter);
                    } else {
                        adapter.setImageDataList(memo.getImageDataList());
                        adapter.notifyDataSetChanged();
                    }
                }
            } else {
                binding.etTitle.setText("");
                binding.etContent.setText("");
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ib_save) {
            viewModel.saveMemo(binding.etTitle.getText().toString(), binding.etContent.getText().toString());
            viewModel.setFragmentIndex(MainViewModel.FRAGMENT_READ);
        } else if (view.getId() == R.id.ib_add_photo) {
            AddPhotoBottomSheetDialog dialog = new AddPhotoBottomSheetDialog(requireContext(), MemoWriteFragment.this);
            dialog.show();
        }
    }

    @Override
    public void onCameraClick() {

    }

    @Override
    public void onGalleryClick() {
        Intent intent = new Intent(requireActivity(), GalleryActivity.class);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    public void onUrlClick() {

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA:
                break;
            case REQUEST_GALLERY:
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    if (data == null) return;
                    List<ImageData> imageDataList = data.getParcelableArrayListExtra("image_data_list");
                    viewModel.addImages(imageDataList);
                }
                break;
            case REQUEST_URL:
                break;
        }
    }
}
