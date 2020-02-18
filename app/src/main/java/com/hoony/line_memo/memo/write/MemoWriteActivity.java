package com.hoony.line_memo.memo.write;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.ActivityNoteEditBinding;

public class MemoWriteActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityNoteEditBinding binding;
    MemoWriteViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MemoWriteActivity.this, R.layout.activity_note_edit);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MemoWriteViewModel.class);

        setObserve();
        setListener();
    }

    private void setObserve() {
        viewModel.getIsCompleteMutableDate().observe(MemoWriteActivity.this, isComplete -> {
            Toast.makeText(this, "Save complete", Toast.LENGTH_SHORT).show();
        });
    }

    private void setListener() {
        binding.btSave.setOnClickListener(MemoWriteActivity.this);
        binding.clAddImageLayout.setOnClickListener(MemoWriteActivity.this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_save) {
            String title = binding.etTitle.getText().toString();
            String content = binding.etContent.getText().toString();
            viewModel.saveMemo(title, content);
        }
    }
}
