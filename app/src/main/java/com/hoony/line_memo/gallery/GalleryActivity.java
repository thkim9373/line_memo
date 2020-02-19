package com.hoony.line_memo.gallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.View;

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.ActivityPhotoGridViewBinding;
import com.hoony.line_memo.db.pojo.ImageData;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener, GalleryAdapter.onItemClickListener {

    private final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};

    private ActivityPhotoGridViewBinding binding;
    private GalleryViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isPermissionsDenied()) return;

        createActivity();
    }

    private void createActivity() {
        binding = DataBindingUtil.setContentView(GalleryActivity.this, R.layout.activity_photo_grid_view);
        viewModel = new ViewModelProvider(GalleryActivity.this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(GalleryViewModel.class);

        setListener();
        setRecyclerView();
        setObserve();
    }

    private void setListener() {
        binding.ibDone.setOnClickListener(GalleryActivity.this);
    }

    private void setRecyclerView() {
        binding.rvGrid.setLayoutManager(new GridLayoutManager(GalleryActivity.this, 3));
    }

    private void setObserve() {
        viewModel.getImageDateListMutableData().observe(
                GalleryActivity.this,
                imageDataList -> binding.rvGrid.setAdapter(new GalleryAdapter(imageDataList, GalleryActivity.this))
        );
    }

    private boolean isPermissionsDenied() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(PERMISSIONS[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(PERMISSIONS, 0);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemClick(int position) {
        viewModel.addSelectedItem(position);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_done) {
            List<ImageData> imageDataList = viewModel.getSelectedImageDataMutableData().getValue();

            if (imageDataList != null && imageDataList.size() != 0) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("image_data_list", (ArrayList<? extends Parcelable>) imageDataList);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.rvGrid.setAdapter(null);
            binding = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(GalleryActivity.this);
                dialog.setTitle("Need permissions");
                dialog.setMessage("Please allow the permissions.");
                dialog.setPositiveButton("Setting", (dialogInterface, i) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
                dialog.setNegativeButton("Close", (dialogInterface, i) -> finish());
                dialog.show();
                return;
            }
        }

        createActivity();
    }
}
