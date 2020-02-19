package com.hoony.line_memo.main.fragments.write;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.BottomSheetDialogPhotoBinding;

public class AddPhotoBottomSheetDialog extends BottomSheetDialog implements View.OnClickListener {

    private final AddPhotoBottomSheetDialogListener mListener;

    AddPhotoBottomSheetDialog(@NonNull Context context, AddPhotoBottomSheetDialogListener listener) {
        super(context);
        this.mListener = listener;
        create();
    }

    interface AddPhotoBottomSheetDialogListener {
        void onCameraClick();

        void onGalleryClick();

        void onUrlClick();
    }

    public void create() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog_photo, null, false);
        BottomSheetDialogPhotoBinding binding = DataBindingUtil.bind(view);
        if (binding != null) {
            binding.btCamera.setOnClickListener(AddPhotoBottomSheetDialog.this);
            binding.btGallery.setOnClickListener(AddPhotoBottomSheetDialog.this);
            binding.btUrl.setOnClickListener(AddPhotoBottomSheetDialog.this);
        }
        setContentView(view);

        this.setOnShowListener(dialog -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_camera:
                this.mListener.onCameraClick();
                dismiss();
                break;
            case R.id.bt_gallery:
                this.mListener.onGalleryClick();
                dismiss();
                break;
            case R.id.bt_url:
                this.mListener.onUrlClick();
                dismiss();
                break;
        }
    }
}