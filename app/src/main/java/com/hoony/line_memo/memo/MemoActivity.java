package com.hoony.line_memo.memo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.ActivityNoteEditBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

public class MemoActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityNoteEditBinding binding;
    MemoViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MemoActivity.this, R.layout.activity_note_edit);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MemoViewModel.class);

        setObserve();
        setListener();
    }

    private void setObserve() {
        viewModel.getIsCompleteMutableDate().observe(MemoActivity.this, isComplete -> {
            Toast.makeText(this, "Save complete", Toast.LENGTH_SHORT).show();
        });
    }

    private void setListener() {
        binding.btSave.setOnClickListener(MemoActivity.this);
        binding.clAddImageLayout.setOnClickListener(MemoActivity.this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_save) {
            String title = binding.etTitle.getText().toString();
            String content = binding.etMemo.getText().toString();
            viewModel.saveMemo(title, content);
        }
    }
}
