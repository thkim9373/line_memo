package com.hoony.line_memo.main.fragments.editor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MemoEditorFragment extends Fragment
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
        binding.ibAddPhoto.setOnClickListener(MemoEditorFragment.this);
        binding.ibSave.setOnClickListener(MemoEditorFragment.this);
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
        viewModel.getEditMemoMutableData().observe(getViewLifecycleOwner(), memo -> {
            if (memo != null) {
                binding.etTitle.setText(memo.getTitle());
                binding.etContent.setText(memo.getContent());

                MemoImageAdapter adapter = new MemoImageAdapter(memo.getImageDataList(), MemoEditorFragment.this);
                binding.rvImage.setAdapter(adapter);
            }

            setTextWatcher();
        });
    }

    private void setTextWatcher() {
        binding.etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (viewModel != null) viewModel.setMemoTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (viewModel != null) viewModel.setMemoContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ib_save) {
            viewModel.saveMemo();
            viewModel.setFragmentIndex(MainViewModel.FRAGMENT_LIST);
        } else if (view.getId() == R.id.ib_add_photo) {
            AddPhotoBottomSheetDialog dialog = new AddPhotoBottomSheetDialog(requireContext(), MemoEditorFragment.this);
            dialog.show();
        }
    }

    @Override
    public void onCameraClick() {
        sendTakePhotoIntent();
    }

    private Uri photoUri;

    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName(), photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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
                if (resultCode == AppCompatActivity.RESULT_OK) {

                    viewModel.addImages(new ImageData(ImageData.CAMERA, photoUri.toString()));

                    MemoImageAdapter memoImageAdapter = (MemoImageAdapter) binding.rvImage.getAdapter();
                    if (memoImageAdapter != null) {
                        int itemCount = memoImageAdapter.getItemCount();
                        memoImageAdapter.notifyItemInserted(itemCount - 1);
                    }
                }

                break;
            case REQUEST_GALLERY:
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    if (data == null) return;

                    List<ImageData> imageDataList = data.getParcelableArrayListExtra("image_data_list");
                    if (imageDataList == null) return;

                    viewModel.addImages(imageDataList);

                    MemoImageAdapter memoImageAdapter = (MemoImageAdapter) binding.rvImage.getAdapter();
                    if (memoImageAdapter != null) {
                        int itemCount = memoImageAdapter.getItemCount();
                        memoImageAdapter.notifyItemRangeInserted(itemCount, itemCount + imageDataList.size() - 1);
                    }
                }
                break;
            case REQUEST_URL:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.rvImage.setAdapter(null);
    }
}
